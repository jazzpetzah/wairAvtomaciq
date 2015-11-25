package com.wearezeta.auto.common.driver;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.ocr.OnScreenKeyboardScanner;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.opencv.core.Rect;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ZetaAndroidDriver extends AndroidDriver<WebElement> implements
		ZetaDriver, HasTouchScreen {

	private static final Logger log = ZetaLogger.getLog(ZetaAndroidDriver.class
			.getSimpleName());
	 public static final String ADB_PREFIX = "";
//	public static final String ADB_PREFIX = "/Applications/android-sdk/platform-tools/";

	private SessionHelper sessionHelper;
	private RemoteTouchScreen touch;
	private String androidOSVersion;

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
		try {
			initOSVersionString();
		} catch (Exception e) {
			Throwables.propagate(e);
		}
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

	private static int getNextCoord(double startC, double endC, double current,
			double duration) {
		return (int) Math.round(startC + (endC - startC) / duration * current);
	}

	private final static int SWIPE_STEP_DURATION_MILLISECONDS = 40;

	private void swipeViaTouchActions(int startx, int starty, int endx,
			int endy, int durationMilliseconds) {
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
			ta.move(getNextCoord(startx, endx, current, duration),
					getNextCoord(starty, endy, current, duration)).perform();
			current++;
		} while (current <= duration);
		ta.up(endx, endy).perform();
	}

	@Override
	public void swipe(int startx, int starty, int endx, int endy,
			int durationMilliseconds) {
		if (androidOSVersion.compareTo("4.3") < 0) {
			// adb swipe command under 4.2 does not support duration parameter
			// and this fucks up all the tests
			swipeViaTouchActions(startx, starty, endx, endy,
					durationMilliseconds);
			return;
		}

		final String adbCommand = String.format(ADB_PREFIX
				+ "adb shell input touchscreen swipe %d %d %d %d %d", startx,
				starty, endx, endy, durationMilliseconds);
		log.debug("ADB swipe: " + adbCommand);
		try {
			Runtime.getRuntime()
					.exec(new String[] { "/bin/bash", "-c", adbCommand })
					.waitFor();
		} catch (Exception e) {
			throw new WebDriverException(e.getMessage(), e);
		}
	}

	public String getOSVersionString() {
		return this.androidOSVersion;
	}

	private void initOSVersionString() throws Exception {
		this.androidOSVersion = getAdbOutput(
				"shell getprop ro.build.version.release").trim();
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
		final String adbCommand = String.format(ADB_PREFIX
				+ "adb shell input touchscreen tap %d %d", x, y);
		try {
			Runtime.getRuntime().exec(
					new String[] { "/bin/bash", "-c", adbCommand });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public TouchScreen getTouch() {
		return this.touch;
	}

	private File getScreenshotInFile() throws Exception {
		File result = File.createTempFile("tmp", ".png", null);
		final String pathOnPhone = String.format("/sdcard/%s.png", CommonUtils
				.generateGUID().replace("-", "").substring(0, 8));
		final String adbCommandsChain = String.format(ADB_PREFIX
				+ "adb shell screencap -p %1$s; " + ADB_PREFIX
				+ "adb pull %1$s %2$s; " + ADB_PREFIX + "adb shell rm %1$s",
				pathOnPhone, result.getCanonicalPath());
		Runtime.getRuntime()
				.exec(new String[] { "/bin/bash", "-c", adbCommandsChain })
				.waitFor();
		byte[] output = FileUtils.readFileToByteArray(result);
		if (CommonUtils.getIsTabletFromConfig(this.getClass())) {
			final SurfaceOrientation currentOrientation = this
					.getSurfaceOrientation();
			log.debug(String.format("Current screen orientation value -> %s",
					currentOrientation.getCode()));
			output = fixScreenshotOrientation(output, currentOrientation);
			IOUtils.write(output, new FileOutputStream(result));
		}
		return result;
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
			tmpScreenshot = getScreenshotInFile();
			result.setSessionId(this.getSessionId().toString());
			result.setStatus(HttpStatus.SC_OK);
			result.setValue(Base64.encodeBase64(IOUtils
					.toByteArray(new FileInputStream(tmpScreenshot))));
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

	@Override
	public <X> X getScreenshotAs(OutputType<X> outputType)
			throws WebDriverException {
		final Object result = takeFullScreenShotWithAdb().getValue();
		final String base64EncodedPng = new String((byte[]) result);
		return outputType.convertFromBase64Png(base64EncodedPng);
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
			return super.execute(driverCommand, parameters);
		} catch (WebDriverException e) {
			if (e.getMessage().contains(SERVER_SIDE_ERROR_SIGNATURE)) {
				final long millisecondsStarted = System.currentTimeMillis();
				while (System.currentTimeMillis() - millisecondsStarted <= DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS) {
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
	 * @return 0 to 3. 0 is default portrait
	 * @throws Exception
	 */
	private SurfaceOrientation getSurfaceOrientation() throws Exception {
		final String output = getAdbOutput(
				"shell dumpsys input | grep 'SurfaceOrientation' | awk '{ print $2 }' | head -n 1")
				.trim();
		return SurfaceOrientation.getByCode(Integer.parseInt(output));
	}

	/**
	 * Workaround for Selendroid issue when correct screen orientation is
	 * returned only for the step where it is actually changed :-@
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

	/**
	 * This method requires the on-screen keyboard to be already visible. Also,
	 * it's important, that keyboard look and feel is set to Google Keyboard ->
	 * Holo White
	 *
	 * @throws Exception
	 */
	public void tapSendButton() throws Exception {
		final File screenshot = getScreenshotInFile();
		try {
			final List<List<Rect>> keyboardButtons = new OnScreenKeyboardScanner()
					.getButtonCoordinates(screenshot.getCanonicalPath());
			int sendButtonRow = -1;
			if (CommonUtils.getIsTabletFromConfig(this.getClass())) {
				sendButtonRow = -3;
			}
			assert keyboardButtons.size() >= Math.abs(sendButtonRow) : "Send button cannot be found on the keyboard";
			final List<Rect> dstRow = keyboardButtons.get(keyboardButtons
					.size() + sendButtonRow);
			final Rect dstRect = dstRow.get(dstRow.size() - 1);
			this.tap(1, dstRect.x + dstRect.width / 2, dstRect.y
					+ dstRect.height / 2, 50);
		} finally {
			screenshot.delete();
		}
	}
}
