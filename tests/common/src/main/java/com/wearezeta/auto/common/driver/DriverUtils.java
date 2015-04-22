package com.wearezeta.auto.common.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DriverUtils {
	public static final int DEFAULT_VISIBILITY_TIMEOUT = 20;
	public static final int DEFAULT_PERCENTAGE = 50;

	private static final Logger log = ZetaLogger.getLog(DriverUtils.class
			.getSimpleName());

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Please use this method ONLY if you know that the element already exists
	 * on the current page. If not then it will cause 10-15 seconds delay every
	 * time it is called.
	 * 
	 * There is overloaded version of this method, which accepts By parameter,
	 * and it should be used in case when an element has not been checked for
	 * existence yet
	 * 
	 * @param driver
	 * @param element
	 * @return boolean value
	 * @throws Exception
	 */
	public static boolean isElementDisplayed(RemoteWebDriver driver,
			WebElement element) throws Exception {
		try {
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static boolean isElementDisplayed(RemoteWebDriver driver, By by)
			throws Exception {
		return isElementDisplayed(driver, by, 1);
	}

	public static boolean isElementDisplayed(RemoteWebDriver driver,
			final By by, int timeoutSeconds) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS);
			try {
				return wait.until(drv -> {
					return (drv.findElements(by).size() > 0)
							&& drv.findElement(by).isDisplayed();
				});
			} catch (TimeoutException e) {
				return false;
			}
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	private static final int DEFAULT_LOOKUP_TIMEOUT = 20;

	public static boolean waitUntilElementDissapear(RemoteWebDriver driver,
			final By by) throws Exception {
		return waitUntilElementDissapear(driver, by, DEFAULT_LOOKUP_TIMEOUT);
	}

	public static boolean waitUntilElementDissapear(RemoteWebDriver driver,
			final By by, int timeout) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			return wait.until(drv -> {
				try {
					return (drv.findElements(by).size() == 0)
							|| (drv.findElements(by).size() > 0 && !drv
									.findElement(by).isDisplayed());
				} catch (StaleElementReferenceException e) {
					return true;
				}
			});
		} catch (TimeoutException ex) {
			return false;
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	public static boolean waitUntilElementAppears(RemoteWebDriver driver,
			final By locator, int timeout) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			return wait.until(drv -> {
				return (drv.findElements(locator).size() > 0);
			});
		} catch (TimeoutException ex) {
			return false;
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	public static boolean waitUntilElementAppears(RemoteWebDriver driver,
			final By locator) throws Exception {
		return waitUntilElementAppears(driver, locator, DEFAULT_LOOKUP_TIMEOUT);
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element) throws Exception {
		return waitUntilElementClickable(driver, element,
				DEFAULT_LOOKUP_TIMEOUT);
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element, int timeout) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	public static boolean waitUntilWebPageLoaded(RemoteWebDriver driver)
			throws Exception {
		return waitUntilWebPageLoaded(driver, DEFAULT_LOOKUP_TIMEOUT);
	}

	public static boolean waitUntilWebPageLoaded(RemoteWebDriver driver,
			int timeout) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			return wait.until(drv -> {
				return String.valueOf(
						((JavascriptExecutor) drv)
								.executeScript("return document.readyState"))
						.equals("complete");
			});
		} catch (TimeoutException e) {
			return false;
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	public static void waitUntilAlertAppears(AppiumDriver driver)
			throws Exception {
		DriverUtils.turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.alertIsPresent());
		} finally {
			setDefaultImplicitWait(driver);
		}
	}

	public static void setTextForChildByClassName(WebElement parent,
			String childClassName, String value) {
		parent.findElement(By.className(childClassName)).sendKeys(value);
	}

	public static boolean waitForElementWithTextByXPath(String xpath,
			String name, AppiumDriver driver) throws InterruptedException {
		int counter = 0;
		while (true) {
			counter++;
			List<WebElement> contactsList = driver.findElementsByXPath(String
					.format(xpath, name));
			if (contactsList.size() > 0) {
				return true;
			}
			Thread.sleep(200);
			if (counter >= 10) {
				return false;
			}
		}
	}

	public static void scrollToElement(AppiumDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollToObject = new HashMap<String, String>();
		scrollToObject.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: scrollTo", scrollToObject);
	}

	/**
	 * 
	 * @param driver
	 * @param element
	 * @param time
	 * @param percentX
	 *            min value is 1. Where to swipe (in percents relative to the
	 *            original control width)
	 * @param percentY
	 *            min value is 1. Where to swipe (in percents relative to the
	 *            original control height)
	 */
	public static void swipeLeft(AppiumDriver driver, WebElement element,
			int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();
		final int xOffset = (int) Math.round(elementSize.width
				* (percentX / 100.0));
		final int yOffset = (int) Math.round(elementSize.height
				* (percentY / 100.0));
		try {
			driver.swipe(coords.x + xOffset, coords.y + yOffset, coords.x,
					coords.y + yOffset, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static final int SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL = 100;
	
	public static void swipeLeft(AppiumDriver driver, WebElement element,
			int time) {
		swipeLeft(driver, element, time, SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeRight(AppiumDriver driver, WebElement element,
			int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();
		final int xOffset = (int) Math.round(elementSize.width
				* (percentX / 100.0));
		final int yOffset = (int) Math.round(elementSize.height
				* (percentY / 100.0));
		try {
			driver.swipe(coords.x, coords.y + yOffset, coords.x + xOffset,
					coords.y + yOffset, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void swipeRight(AppiumDriver driver, WebElement element,
			int time) {
		swipeRight(driver, element, time,
				SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeUp(AppiumDriver driver, WebElement element,
			int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();
		final int xOffset = (int) Math.round(elementSize.width
				* (percentX / 100.0));
		final int yOffset = (int) Math.round(elementSize.height
				* (percentY / 100.0));
		try {
			driver.swipe(coords.x + xOffset, coords.y + yOffset, coords.x
					+ xOffset, coords.y, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL = 100;
	
	public static void swipeUp(AppiumDriver driver, WebElement element, int time) {
		swipeUp(driver, element, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL);
	}

	public static void swipeDown(AppiumDriver driver, WebElement element,
			int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();
		final int xOffset = (int) Math.round(elementSize.width
				* (percentX / 100.0));
		final int yOffset = (int) Math.round(elementSize.height
				* (percentY / 100.0));
		try {
			driver.swipe(coords.x + xOffset, coords.y, coords.x + xOffset,
					coords.y + yOffset, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void swipeDown(AppiumDriver driver, WebElement element,
			int time) {
		swipeDown(driver, element, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL);
	}

	public static void swipeByCoordinates(AppiumDriver driver, int time,
			int startPercentX, int startPercentY, int endPercentX,
			int endPercentY) throws Exception {
		driver.context("NATIVE_APP");
		final Dimension screenSize = driver.manage().window().getSize();
		final int startX = (int) Math.round(screenSize.width
				* (startPercentX / 100.0));
		final int startY = (int) Math.round(screenSize.height
				* (startPercentY / 100.0));
		final int endX = (int) Math.round(screenSize.width
				* (endPercentX / 100.0));
		final int endY = (int) Math.round(screenSize.height
				* (endPercentY / 100.0));
		try {
			driver.swipe(startX, startY, endX, endY, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static final int DEFAULT_TIME = 500; // milliseconds
	public static final int DEFAULT_FINGERS = 1;

	public static void genericTap(AppiumDriver driver) {
		genericTap(driver, DEFAULT_TIME, DEFAULT_FINGERS, DEFAULT_PERCENTAGE,
				DEFAULT_PERCENTAGE);
	}

	public static void genericTap(AppiumDriver driver, int percentX,
			int percentY) {
		genericTap(driver, DEFAULT_TIME, DEFAULT_FINGERS, percentX, percentY);
	}

	public static void genericTap(AppiumDriver driver, int time, int percentX,
			int percentY) {
		genericTap(driver, time, DEFAULT_FINGERS, percentX, percentY);
	}

	public static void genericTap(AppiumDriver driver, int time, int fingers,
			int percentX, int percentY) {
		final Dimension screenSize = driver.manage().window().getSize();
		final int xCoords = (int) Math.round(screenSize.width
				* (percentX / 100.0));
		final int yCoords = (int) Math.round(screenSize.height
				* (percentY / 100.0));
		try {
			driver.tap(fingers, xCoords, yCoords, time);
		} catch (Exception ex) {
			// ignore;
		}
	}

	public static final int SWIPE_X_DEFAULT_PERCENTAGE_START = 10;
	public static final int SWIPE_X_DEFAULT_PERCENTAGE_END = 90;
	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_START = 10;
	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_END = 90;

	public static void swipeRightCoordinates(AppiumDriver driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_START,
				DEFAULT_PERCENTAGE, SWIPE_X_DEFAULT_PERCENTAGE_END,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeRightCoordinates(AppiumDriver driver, int time,
			int percentY) throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_START,
				percentY, SWIPE_X_DEFAULT_PERCENTAGE_END, percentY);
	}

	public static void swipeLeftCoordinates(AppiumDriver driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_END,
				DEFAULT_PERCENTAGE, SWIPE_X_DEFAULT_PERCENTAGE_START,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeLeftCoordinates(AppiumDriver driver, int time,
			int percentY) throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_END,
				percentY, SWIPE_X_DEFAULT_PERCENTAGE_START, percentY);
	}

	public static void swipeUpCoordinates(AppiumDriver driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_END, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_START);
	}

	public static void swipeUpCoordinates(AppiumDriver driver, int time,
			int percentX) throws Exception {
		swipeByCoordinates(driver, time, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_END, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_START);
	}

	public static void swipeDownCoordinates(AppiumDriver driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_START, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_END);
	}

	public static void swipeDownCoordinates(AppiumDriver driver, int time,
			int percentX) throws Exception {
		swipeByCoordinates(driver, time, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_START, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_END);
	}

	public static void androidMultiTap(AppiumDriver driver, WebElement element,
			int tapNumber, double duration) throws InterruptedException {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		tapObject.put("tapCount", (double) 1);
		tapObject.put("touchCount", (double) 1);
		tapObject.put("duration", duration);
		tapObject.put("x", (double) (coords.x + elementSize.width / 2));
		tapObject.put("y", (double) (coords.y + elementSize.height / 2));

		for (int i = 0; i < tapNumber; i++) {
			js.executeScript("mobile: tap", tapObject);
			Thread.sleep(100);
		}

	}

	public static void mobileTapByCoordinates(AppiumDriver driver,
			WebElement element, int offsetX, int offsetY) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		double x = (double) ((coords.x + offsetX + elementSize.width) - elementSize.width / 2);
		tapObject.put("x", x);
		double y = (double) ((coords.y + offsetY + elementSize.height) - elementSize.height / 2);
		tapObject.put("y", y);
		js.executeScript("mobile: tap", tapObject);
	}

	public static void mobileTapByCoordinates(AppiumDriver driver,
			WebElement element) {
		mobileTapByCoordinates(driver, element, 0, 0);
	}

	public static void androidLongClick(AppiumDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> tapObject = new HashMap<String, String>();
		tapObject.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: longClick", tapObject);
	}

	public static void iOSSimulatorSwipeDown(String scriptPath)
			throws Exception {
		// CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
		// "python", scriptPath,"0.65", "0.1", "0.65", "0.7"});
		Runtime.getRuntime().exec(
				"/usr/bin/open -a Terminal " + scriptPath + "Down.py");
	}

	public static void iOSSimulatorSwipeDialogPageDown(String scriptPath)
			throws Exception {
		Process process = Runtime.getRuntime()
				.exec("/usr/bin/open -a Terminal " + scriptPath
						+ "DialogPageDown.py");
		InputStream stream = process.getErrorStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String s;
		while ((s = br.readLine()) != null) {
			log.debug(s);
		}
		stream.close();
		log.debug("Process Code " + process.waitFor());
	}

	public static void iOSSimulatorSwipeDialogPageUp(String scriptPath)
			throws Exception {
		Process process = Runtime.getRuntime().exec(
				"/usr/bin/open -a Terminal " + scriptPath + "DialogPageUp.py");
		InputStream stream = process.getErrorStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String s;
		while ((s = br.readLine()) != null) {
			log.debug(s);
		}
		stream.close();
		log.debug("Process Code " + process.waitFor());
	}

	public static void iOSSimulatorSwipeUp(String scriptPath) throws Exception {
		// CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
		// "python", scriptPath,"0.65", "0.95", "0.65", "0.7"});
		Runtime.getRuntime().exec(
				"/usr/bin/open -a Terminal " + scriptPath + "Up.py");
	}

	public static void iOSMultiTap(AppiumDriver driver, WebElement element,
			int tapNumber) throws InterruptedException {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		tapObject.put("tapCount", (double) tapNumber);
		tapObject.put("touchCount", (double) 1);
		tapObject.put("duration", 0.2);
		tapObject.put("x", (double) (coords.x + elementSize.width / 2));
		tapObject.put("y", (double) (coords.y + elementSize.height / 2));

		for (int i = 0; i < tapNumber; i++) {
			js.executeScript("mobile: tap", tapObject);
			Thread.sleep(100);
		}
	}

	public static void iOS3FingerTap(AppiumDriver driver, WebElement element,
			int fingerNumber) {
		driver.tap(fingerNumber, element, 1);
	}

	public static void turnOffImplicitWait(RemoteWebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public static void setDefaultImplicitWait(RemoteWebDriver driver)
			throws Exception {
		driver.manage()
				.timeouts()
				.implicitlyWait(
						Integer.parseInt(CommonUtils
								.getDriverTimeoutFromConfig(BasePage.class)),
						TimeUnit.SECONDS);
	}

	public static void setImplicitWaitValue(RemoteWebDriver driver, int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	public static BufferedImage takeScreenshot(ZetaDriver driver)
			throws IOException {
		if (!driver.isSessionLost()) {
			byte[] scrImage = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.BYTES);
			InputStream in = new ByteArrayInputStream(scrImage);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			return bImageFromConvert;
		} else {
			return null;
		}
	}

	public static void ToggleNetworkConnectionAndroid(AndroidDriver driver,
			boolean airplane, boolean wifi, boolean data) {

		NetworkConnectionSetting connection = new NetworkConnectionSetting(
				airplane, wifi, data);

		driver.setNetworkConnection(connection);
	}

	public static void iOSLongTap(AppiumDriver driver, WebElement element) {
		driver.tap(1, element, 1000);
	}

	public static void moveMouserOver(RemoteWebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.perform();
	}

	/*
	 * This is a work around for pressing the silence button. The ID is not
	 * visible through Appium, thats why it is tapped by its location
	 * coordinates.
	 */
	public static void clickSilenceConversationButton(AppiumDriver driver,
			WebElement element) {
		Point coords = element.getLocation();
		driver.tap(1, coords.x - (coords.x / 2 - coords.x / 8), coords.y, 1);
	}

	/*
	 * This is a work around for pressing the archive button. The ID is not
	 * visible through Appium, thats why it is tapped by its location
	 * coordinates.
	 */
	public static void clickArchiveConversationButton(AppiumDriver driver,
			WebElement element) {
		Point coords = element.getLocation();
		driver.tap(1, coords.x - (coords.x / 2 + coords.x / 8), coords.y, 1);
	}

	public static void resetApp(AppiumDriver driver) {
		driver.resetApp();
	}
}
