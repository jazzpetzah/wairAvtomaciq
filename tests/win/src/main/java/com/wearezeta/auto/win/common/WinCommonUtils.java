package com.wearezeta.auto.win.common;

import com.wearezeta.common.process.ProcessHandler;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.NSPoint;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import static com.wearezeta.auto.win.common.WinExecutionContext.WIRE_APP_CACHE_FOLDER;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class WinCommonUtils {

    private static final Logger LOG = ZetaLogger.getLog(WinCommonUtils.class
            .getName());

    public static NSPoint calculateScreenResolution(ZetaOSXDriver driver)
            throws Exception {
        BufferedImage im = DriverUtils.takeFullScreenShot(driver).orElseThrow(
                IllegalStateException::new);
        return new NSPoint(im.getWidth(), im.getHeight());
    }

    public static boolean isRetinaDisplay(ZetaOSXDriver driver)
            throws Exception {
        NSPoint size = calculateScreenResolution(driver);
        return isRetinaDisplay(size.x(), size.y());
    }

    public static boolean isRetinaDisplay(int width, int height) {
        if (width == 2560 && height == 1600) {
            return true;
        } else {
            return false;
        }
    }

    public static int screenPixelsMultiplier(ZetaOSXDriver driver)
            throws Exception {
        return (isRetinaDisplay(driver)) ? WinConstants.Common.SIZE_MULTIPLIER_RETINA
                : WinConstants.Common.SIZE_MULTIPLIER_NO_RETINA;
    }

    public static BufferedImage takeElementScreenshot(WebElement element,
            ZetaOSXDriver driver) throws Exception {
        int multiply = screenPixelsMultiplier(driver);

        BufferedImage screenshot = DriverUtils.takeFullScreenShot(
                (ZetaDriver) driver).orElseThrow(IllegalStateException::new);
        Point elPoint = element.getLocation();
        Dimension elSize = element.getSize();
        return screenshot.getSubimage(elPoint.x * multiply, elPoint.y
                * multiply, elSize.width * multiply, elSize.height * multiply);
    }

    public static boolean clearAppData() throws Exception {
        final String[] commands = new String[]{"cmd", "/c",
            String.format("DEL /F /S /Q /A \"%s*\"", WIRE_APP_CACHE_FOLDER)};
        return new ProcessHandler(commands).startProcess(30, TimeUnit.SECONDS).stopProcess().getExitCode() == 0;
    }

    public static int killAllApps() throws Exception {
        final String[] commands = new String[]{
            "cmd",
            "/c",
            String.format(
            "taskkill /F /im %s & taskkill /F /im %s & taskkill /F /im %s & taskkill /F /im %s",
            "Wire.exe", "WireInternal.exe", "Update.exe", "chromedriver.exe")};
        return new ProcessHandler(commands).startProcess(30, TimeUnit.SECONDS).stopProcess().getExitCode();
    }
    
    public static int killOnlyWire() throws Exception {
        final String[] commands = new String[]{
            "cmd",
            "/c",
            String.format(
            "taskkill /F /im %s",
            "Wire.exe")};
        return new ProcessHandler(commands).startProcess(30, TimeUnit.SECONDS).stopProcess().getExitCode();
    }

    // needs powershell 5 which has to be installed seperately on windows 7 by installing windows management framework 5
    public static long getSizeOfAppInMB() throws Exception {
        final String[] commands = new String[]{
            "cmd",
            "/c",
            String.format(
            "powershell -noprofile -command \"(Get-ChildItem %s -recurse | Measure-Object -property length -sum).sum / 1MB\"",
            Paths.get(WinExecutionContext.WIRE_APP_FOLDER))};
        List<String> output = new ProcessHandler(commands).startProcess(30, TimeUnit.SECONDS).stopProcess().getOutput();
        LOG.debug("stringResult: " + output);
        String sizeString = output.get(0).trim().split("\\.")[0];
        long longResult = Long.parseLong(sizeString);
        LOG.debug("result: " + longResult);
        return longResult;
    }

}
