package com.wearezeta.auto.common.driver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.Response;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver,
		HasTouchScreen {

	private static final Logger log = ZetaLogger.getLog(ZetaAndroidDriver.class
			.getSimpleName());
	public static final String ADB_PREFIX = "";
	// public static final String ADB_PREFIX =
	// "/Applications/android-sdk/platform-tools/";

	private SessionHelper sessionHelper;
	private RemoteTouchScreen touch;

	private enum SurfaceOrientation {
		ROTATION_0(0), ROTATION_90(1), ROTATION_180(2), ROTATION_270(3);

		final int code;

		private SurfaceOrientation(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

		public static SurfaceOrientation getByCode(int code) {
			for (SurfaceOrientation item : SurfaceOrientation.values()) {
				if (code == item.getCode()) {
					return item;
				}
			}
			throw new NoSuchElementException(String.format(
					"There is no SurfaceOrientation item with code '%s'", code));
		}
	}

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		this.touch = new RemoteTouchScreen(getExecuteMethod());
		sessionHelper = new SessionHelper(this);
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.sessionHelper.wrappedFindElements(super::findElements, by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.sessionHelper.wrappedFindElement(super::findElement, by);
	}

	@Override
	public void close() {
		this.sessionHelper.wrappedClose(super::close);
	}

	@Override
	public void quit() {
		this.sessionHelper.wrappedQuit(super::quit);
	}

	@Override
	public boolean isSessionLost() {
		return this.sessionHelper.isSessionLost();
	}

	@Override
	public void swipe(int startx, int starty, int endx, int endy,
			int durationMilliseconds) {
		String adbCommand = ADB_PREFIX
				+ "adb shell input touchscreen swipe %d %d %d %d";
		if (lowerThanFourDotThree()) {
			adbCommand = String.format(adbCommand, startx, starty, endx, endy);
			getScreenEvent42();
		} else {
			adbCommand = String.format(adbCommand + " %d", startx, starty,
					endx, endy, durationMilliseconds);
		}
		log.debug("ADB swipe: " + adbCommand);

		try {
			Runtime.getRuntime()
					.exec(new String[] { "/bin/bash", "-c", adbCommand })
					.waitFor();
		} catch (Exception e) {
			throw new WebDriverException(e.getMessage(), e);
		}
	}

	/*
	 * Checks to see that the android version is less than 4.3
	 */
	public static boolean lowerThanFourDotThree() {
		Scanner s;
		String result = "";
		try {
			s = new java.util.Scanner(Runtime
					.getRuntime()
					.exec(ADB_PREFIX
							+ "adb shell getprop ro.build.version.release")
					.getInputStream()).useDelimiter("\\A");
			result = s.hasNext() ? s.next() : "";
			log.debug("Detected Android: " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (result.compareTo("4.3") < 0) {
			// comparing lexicographically, 4.2.2 < 4.3 for example
			return true;
		}
		return false;
	}

	/*
	 * Detects touchscreen event to use it in swipe for 4.2
	 */
	public static String getScreenEvent42() {
		Scanner s;
		String result = null;
		String adbCommand = ADB_PREFIX + "adb shell getevent -p";
		try {
			s = new java.util.Scanner(Runtime.getRuntime()
					.exec(new String[] { "/bin/bash", "-c", adbCommand })
					.getInputStream()).useDelimiter("0036").useDelimiter(
					"add device [0-9]+: ");
			do {
				result = s.hasNext() ? s.next() : "";
			} while (!result.contains("0035"));
			if (result.contains("0035")) {
				result = result.split("\n")[0].trim();
			}
			log.debug("Detected screen event: " + result);
		} catch (Exception e) {
			new Exception(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public void tap(int fingers, WebElement element, int durationMilliseconds) {
		final TouchActions ta = new TouchActions(this);
		for (int i = 0; i < fingers; i++) {
			ta.singleTap(element);
		}
		ta.perform();
		try {
			Thread.sleep(durationMilliseconds);
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public void tap(int fingers, int x, int y, int durationMilliseconds) {
		final TouchActions ta = new TouchActions(this);
		for (int i = 0; i < fingers; i++) {
			ta.down(x, y).up(x, y);
		}
		ta.perform();
		try {
			Thread.sleep(durationMilliseconds);
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public TouchScreen getTouch() {
		return this.touch;
	}

	/**
	 * Workaround for selendroid when it cannot take a screenshot of the screen
	 * if main app is not in foreground
	 * 
	 * @return Selenium Response instance
	 */
	private Response takeFullScreenShotWithAdb() {
		final Response result = new Response();
		File tmpScreenshot = null;
		try {
			tmpScreenshot = File.createTempFile("tmp", ".png", null);
			final String pathOnPhone = String
					.format("/sdcard/%s.png", CommonUtils.generateGUID()
							.replace("-", "").substring(0, 8));
			final String adbCommandsChain = String.format(
					ADB_PREFIX + "adb shell screencap -p %1$s; " + ADB_PREFIX
							+ "adb pull %1$s %2$s; " + ADB_PREFIX
							+ "adb shell rm %1$s", pathOnPhone,
					tmpScreenshot.getCanonicalPath());
			Runtime.getRuntime()
					.exec(new String[] { "/bin/bash", "-c", adbCommandsChain })
					.waitFor();
			byte[] output = FileUtils.readFileToByteArray(tmpScreenshot);
			if (CommonUtils.getIsTabletFromConfig(this.getClass())) {
				final SurfaceOrientation currentOrientation = this
						.getSurfaceOrientation();
				log.debug(String.format(
						"Current screen orientation value -> %s",
						currentOrientation.getCode()));
				output = fixScreenshotOrientation(output, currentOrientation);
			}
			result.setSessionId(this.getSessionId().toString());
			result.setStatus(HttpStatus.OK_200);
			result.setValue(Base64.encodeBase64(output));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			// Wrap generic error into WebDriverException
			throw new WebDriverException(e.getMessage(), e);
		} finally {
			if (tmpScreenshot != null) {
				tmpScreenshot.delete();
			}
		}
	}

	private byte[] fixScreenshotOrientation(final byte[] initialScreenshot,
			SurfaceOrientation currentOrientation) throws IOException {
		if (currentOrientation != SurfaceOrientation.ROTATION_0) {
			BufferedImage screenshotImage = ImageIO
					.read(new ByteArrayInputStream(initialScreenshot));
			screenshotImage = ImageUtil.tilt(screenshotImage,
					-currentOrientation.getCode() * Math.PI / 2);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(screenshotImage, "png", baos);
			return baos.toByteArray();
		} else {
			return initialScreenshot;
		}
	}

	private static final long DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS = 2000;
	private static final String SERVER_SIDE_ERROR_SIGNATURE = "unknown server-side error";

	/**
	 * This is workaround for some Selendroid issues when driver just generates
	 * unknown error when some transition in AUT is currently in progress. Retry
	 * helps
	 * 
	 * @param driverCommand
	 * @param parameters
	 * @return
	 */
	@Override
	public Response execute(String driverCommand, Map<String, ?> parameters) {
		try {
			if (driverCommand.equals(DriverCommand.SCREENSHOT)) {
				return this.takeFullScreenShotWithAdb();
			}
			return super.execute(driverCommand, parameters);
		} catch (WebDriverException e) {
			if (e.getMessage().contains(SERVER_SIDE_ERROR_SIGNATURE)) {
				final long milliscondsStarted = System.currentTimeMillis();
				while (System.currentTimeMillis() - milliscondsStarted <= DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						Throwables.propagate(e1);
					}
					try {
						return super.execute(driverCommand, parameters);
					} catch (WebDriverException e1) {
						if (!e.getMessage().contains(
								SERVER_SIDE_ERROR_SIGNATURE)) {
							throw e1;
						}
					}
				} // while have time
			} // if getMessage contains
			log.error(String
					.format("Android driver is still not available after %s seconds timeout. The recent webdriver command was '%s'",
							DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS / 1000,
							driverCommand));
			throw e;
		}
	}

	private static String getAdbOutput(String cmdLine) throws Exception {
		String result = "";
		String adbCommand = ADB_PREFIX + "adb " + cmdLine;
		final Process process = Runtime.getRuntime().exec(
				new String[] { "/bin/bash", "-c", adbCommand });
		if (process == null) {
			throw new RuntimeException(String.format(
					"Failed to execute command line '%s'", cmdLine));
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s;
			while ((s = in.readLine()) != null) {
				result = result + s + "\n";
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @return 0 to 3. 0 is default portrait
	 * @throws Exception
	 */
	private SurfaceOrientation getSurfaceOrientation() throws Exception {
		final String output = getAdbOutput("shell dumpsys input | grep 'SurfaceOrientation' | awk '{ print $2 }' | head -n 1");
		return SurfaceOrientation.getByCode(Integer.parseInt(output));
	}

	/**
	 * Workaround for Selendroid issue when correct screen orientation is
	 * returned only for the step where it is actually changed :-@
	 *
	 */
	@Override
	public ScreenOrientation getOrientation() {
		SurfaceOrientation value = SurfaceOrientation.ROTATION_0;
		try {
			value = getSurfaceOrientation();
		} catch (Exception e) {
			e.printStackTrace();
			return super.getOrientation();
		}
		if (value.getCode() % 2 == 1) {
			return ScreenOrientation.LANDSCAPE;
		} else {
			return ScreenOrientation.PORTRAIT;
		}
	}
}
