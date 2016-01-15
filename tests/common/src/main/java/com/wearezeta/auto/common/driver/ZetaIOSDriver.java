package com.wearezeta.auto.common.driver;

import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.*;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.cristik.cocoa4java.WindowCapture;
import com.cristik.cocoa4java.WindowInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import javax.imageio.ImageIO;

public class ZetaIOSDriver extends IOSDriver<WebElement> implements ZetaDriver {
    private static final Logger log = ZetaLogger.getLog(ZetaIOSDriver.class.getSimpleName());

    private volatile boolean isSessionLost = false;

    public ZetaIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    private byte[] getTemplatePngImage() {
        final byte[] aByteArray = {0xa, 0x2, 0xf, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        final int width = 100;
        final int height = 100;

        DataBuffer buffer = new DataBufferByte(aByteArray, aByteArray.length);
        WritableRaster raster =
                Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[]{0, 1, 2}, null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(),
                false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(cm, raster, true, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            ImageIO.write(image, "png", baos);
            baos.flush();
            result = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private byte[] getScreenshotAsBytes() {
        for (WindowInfo windowInfo : WindowCapture.findWindowsForPID(-1)) {
            if (windowInfo.title.contains("iOS") && windowInfo.ownerName.toLowerCase().equals("simulator")) {
                return WindowCapture.getWindowSnapshotData(windowInfo.windowNumber, WindowCapture.IMAGE_FORMAT_PNG);
            }
        }
        return getTemplatePngImage();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        final Object result = Base64.encodeBase64(getScreenshotAsBytes());
        final String base64EncodedPng = new String((byte[]) result);
        return outputType.convertFromBase64Png(base64EncodedPng);
    }

    @Override
    public boolean isSessionLost() {
        return this.isSessionLost;
    }

    private void setSessionLost(boolean isSessionLost) {
        if (isSessionLost != this.isSessionLost) {
            log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
        }
        if (isSessionLost && !this.isSessionLost) {
            try {
                AppiumServerTools.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.isSessionLost = isSessionLost;
    }

    private ExecutorService pool;

    private synchronized ExecutorService getPool() {
        if (this.pool == null) {
            this.pool = Executors.newFixedThreadPool(1);
        }
        return this.pool;
    }

    private boolean isSessionLostBecause(Throwable e) {
        return (e instanceof UnreachableBrowserException) || (e instanceof SessionNotFoundException);
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
    }

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        if (this.isSessionLost() && !driverCommand.equals(DriverCommand.SCREENSHOT)) {
            log.warn(String.format("Appium session is dead. Skipping execution of '%s' command...", driverCommand));
            return null;
        }
        final Callable<Response> task = () -> super.execute(driverCommand, parameters);
        final Future<Response> future = getPool().submit(task);
        try {
            return future.get(MAX_COMMAND_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (isSessionLostBecause(e.getCause())) {
                    setSessionLost(true);
                }
                Throwables.propagate(e.getCause());
            } else {
                setSessionLost(true);
                Throwables.propagate(e);
            }
        }
        // This should never happen
        return super.execute(driverCommand, parameters);
    }

}
