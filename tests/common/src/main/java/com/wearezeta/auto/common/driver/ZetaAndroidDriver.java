package com.wearezeta.auto.common.driver;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
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
import com.wearezeta.auto.common.log.ZetaLogger;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver,
		HasTouchScreen {

	private static final Logger log = ZetaLogger.getLog(ZetaAndroidDriver.class
			.getSimpleName());

	private SessionHelper sessionHelper;
	private RemoteTouchScreen touch;

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		this.touch = new RemoteTouchScreen(getExecuteMethod());
		sessionHelper = new SessionHelper();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.sessionHelper.wrappedFindElements(super::findElements, by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.sessionHelper.wrappedFindElement(super::findElement, by);
	}

	private Void closeDriver() {
		super.close();
		return null;
	}

	@Override
	public void close() {
		this.sessionHelper.wrappedClose(this::closeDriver);
	}

	private Void quitDriver() {
		super.quit();
		return null;
	}

	@Override
	public void quit() {
		this.sessionHelper.wrappedQuit(this::quitDriver);
	}

	@Override
	public boolean isSessionLost() {
		return this.sessionHelper.isSessionLost();
	}

	private int getCoord(double startC, double endC, double current,
			double duration) {
		return (int) Math.round(startC + (endC - startC) / duration * current);
	}

	private final static int SWIPE_STEP_DURATION_MILLISECONDS = 50;

	@Override
	public void swipe(int startx, int starty, int endx, int endy,
			int durationMilliseconds) {
		int duration = 1;
		if (durationMilliseconds > SWIPE_STEP_DURATION_MILLISECONDS) {
			duration = (durationMilliseconds % SWIPE_STEP_DURATION_MILLISECONDS == 0) ? (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS)
					: (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS + 1);
		}
		int current = 1;
		final TouchActions ta = new TouchActions(this);
		ta.down(startx, starty).perform();
		do {
			try {
				Thread.sleep(SWIPE_STEP_DURATION_MILLISECONDS);
			} catch (InterruptedException e) {
				Throwables.propagate(e);
			}
			ta.move(getCoord(startx, endx, current, duration),
					getCoord(starty, endy, current, duration)).perform();
			current++;
		} while (current <= duration);
		ta.up(endx, endy).perform();
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
			final String adbCommandsChain = String.format(
					"adb shell screencap -p /sdcard/fullscreen.png; "
							+ "adb pull /sdcard/fullscreen.png %s; "
							+ "adb shell rm /sdcard/fullscreen.png",
					tmpScreenshot.getCanonicalPath());
			CommonUtils.executeOsXCommand(new String[] { "/bin/bash", "-c",
					adbCommandsChain });
			final byte[] output = FileUtils.readFileToByteArray(tmpScreenshot);
			result.setSessionId(this.getSessionId().toString());
			result.setStatus(HttpStatus.OK_200);
			result.setValue(Base64.encodeBase64(output));
			return result;
		} catch (Exception e) {
			// Wrap generic error into WebDriverException
			throw new WebDriverException(e.getMessage(), e);
		} finally {
			if (tmpScreenshot != null) {
				tmpScreenshot.delete();
			}
		}
	}

	private static final long DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS = 3000;
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
			return super.execute(driverCommand, parameters);
		} catch (WebDriverException e) {
			if (e.getMessage().contains(SERVER_SIDE_ERROR_SIGNATURE)) {
				if (driverCommand.equals(DriverCommand.SCREENSHOT)) {
					log.warn("Selenium has failed to take a screenshot using the standard instrumentation. Trying ADB workaround...");
					return this.takeFullScreenShotWithAdb();
				} else {
					final long milliscondsStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - milliscondsStarted <= DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS) {
						try {
							Thread.sleep(200);
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
				} // if command is screenshot
			} // if getMessage contains
			log.error(String
					.format("Android driver is still not avilable after '%s' seconds timeout",
							DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS / 1000));
			throw e;
		}
	}
}
