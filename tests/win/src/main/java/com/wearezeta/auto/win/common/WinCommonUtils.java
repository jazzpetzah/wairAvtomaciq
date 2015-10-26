package com.wearezeta.auto.win.common;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.NSPoint;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.Arrays;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class WinCommonUtils extends CommonUtils {

	private static final int PREFS_DAEMON_RESTART_TIMEOUT = 1000;

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

	public static int clearAppData() throws Exception {
            
		final String[] commands = new String[] {"cmd", "/c" ,"DEL /F /S /Q /A \"C:\\Users\\Michael\\AppData\\Roaming\\Wire\\*\""};

		LOG.debug("executing command: " + Arrays.toString(commands));
		return executeOsXCommand(commands);
	}

	public static int killAllApps() throws Exception {
		final String[] commands = new String[] { "cmd", "/c",
				String.format("taskkill /F /im %s", "Wire.exe") };
		LOG.debug("executing command: " + Arrays.toString(commands));
		return executeOsXCommand(commands);
	}

	public static long getSizeOfAppInMB() throws Exception {
		final String[] commands = new String[] { "cmd", "/c",
				String.format("powershell -noprofile -command \"(Get-ChildItem %s -recurse | Measure-Object -property length -sum).sum / 1MB\"", WinExecutionContext.WIRE_APP_FOLDER) };
		String stringResult = executeOsXCommandWithOutput(commands);
                LOG.debug("stringResult: " + stringResult);
		stringResult = stringResult.trim().split("\\.")[0];
		long longResult = Long.parseLong(stringResult);
		LOG.debug("result: " + longResult);
		return longResult;
	}

}
