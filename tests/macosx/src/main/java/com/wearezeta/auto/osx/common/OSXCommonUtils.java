package com.wearezeta.auto.osx.common;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.util.NSPoint;
import java.util.Arrays;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class OSXCommonUtils extends CommonUtils {

    private static final Logger LOG = ZetaLogger.getLog(OSXCommonUtils.class.getName());

    public static NSPoint calculateScreenResolution(ZetaOSXDriver driver) throws Exception {
        BufferedImage im = DriverUtils.takeFullScreenShot(driver).orElseThrow(IllegalStateException::new);
        return new NSPoint(im.getWidth(), im.getHeight());
    }

    public static boolean isRetinaDisplay(ZetaOSXDriver driver) throws Exception {
        NSPoint size = calculateScreenResolution(driver);
        return isRetinaDisplay(size.x(), size.y());
    }

    public static boolean isRetinaDisplay(int width, int height) {
        return width == 2560 && height == 1600;
    }

    public static int screenPixelsMultiplier(ZetaOSXDriver driver) throws Exception {
        return (isRetinaDisplay(driver)) ? OSXConstants.Common.SIZE_MULTIPLIER_RETINA : OSXConstants.Common.SIZE_MULTIPLIER_NO_RETINA;
    }

    public static BufferedImage takeElementScreenshot(WebElement element, ZetaOSXDriver driver) throws Exception {
        int multiply = screenPixelsMultiplier(driver);
        BufferedImage screenshot = DriverUtils.takeFullScreenShot((ZetaDriver) driver).orElseThrow(IllegalStateException::new);
        Point elPoint = element.getLocation();
        Dimension elSize = element.getSize();
        return screenshot.getSubimage(elPoint.x * multiply, elPoint.y * multiply, elSize.width * multiply,
                elSize.height * multiply);
    }

    public static int clearAppData() throws Exception {
        LOG.debug("Clearing Wire wrapper database");
        final String[] commands = new String[]{
            "/bin/sh",
            "-c",
            "rm -rf"
            + String.format(
            " \"%s/Library/Application Support/Wire/\"",
            OSXExecutionContext.USER_HOME)
            + String.format(
            " \"%s/Library/Application Support/WireInternal/\"",
            OSXExecutionContext.USER_HOME)
            + String.format(
            " \"%s/Library/Containers/com.wearezeta.zclient.mac/Data/Library/Application Support/Wire/\"",
            OSXExecutionContext.USER_HOME)
        };
        LOG.debug("executing command: " + Arrays.toString(commands));
        return executeOsXCommand(commands);
    }

    public static int clearAddressbookPermission() throws Exception {
        final String[] commands = new String[]{
            "/bin/sh",
            "-c",
            "tccutil reset AddressBook"};
        LOG.debug("executing command: " + Arrays.toString(commands));
        return executeOsXCommand(commands);
    }

    public static int getNumberOfWireProcesses() throws Exception {
        String[] command = new String[]{"/bin/sh", "-c", "ps aux | grep Wire.*app | wc -l"};
        LOG.debug("executing command: " + Arrays.toString(command));
        String numberString = executeOsXCommandWithOutput(command);
        int numberProcesses = Integer.parseInt(numberString.trim()) - 2;//substract grep and wc process
        return numberProcesses;
    }

    public static int killAllApps() throws Exception {
        // TODO: merge commands
        String[] command = new String[]{"/bin/sh", "-c", String.format("killall %s", "Electron")};
        LOG.debug("executing command: " + Arrays.toString(command));
        int returnCode1 = executeOsXCommand(command);

        command = new String[]{"/bin/sh", "-c", String.format("killall %s", "Wire Helper")};
        LOG.debug("executing command: " + Arrays.toString(command));
        int returnCode2 = executeOsXCommand(command);

        command = new String[]{"/bin/sh", "-c", String.format("killall %s", "chromedriver")};
        LOG.debug("executing command: " + Arrays.toString(command));
        int returnCode3 = executeOsXCommand(command);
        return returnCode1;//we ignore returnCode2 for now
    }

    public static long getSizeOfAppInMB() throws Exception {
        final String[] commands = new String[]{"/bin/sh", "-c", String.format("du -sk %s", OSXExecutionContext.WIRE_APP_PATH)};
        LOG.debug("executing command: " + Arrays.toString(commands));
        String stringResult = executeOsXCommandWithOutput(commands);
        stringResult = stringResult.replace(OSXExecutionContext.WIRE_APP_PATH, "").trim();
        long longResult = Long.parseLong(stringResult) / 1024;
        LOG.debug("result: " + longResult);
        return longResult;
    }

    public static long startAppium4Mac() throws Exception {
        final String[] commands = new String[]{"/bin/sh", "-c", String.format("open %s", OSXExecutionContext.APPIUM_MAC_PATH)};
        LOG.debug("executing command: " + Arrays.toString(commands));
        return executeOsXCommand(commands);
    }
}
