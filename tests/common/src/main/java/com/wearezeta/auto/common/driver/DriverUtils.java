package com.wearezeta.auto.common.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DriverUtils {
	public static final int DEFAULT_PERCENTAGE = 50;

	private static final Logger log = ZetaLogger.getLog(DriverUtils.class
			.getSimpleName());

	public static int getDefaultLookupTimeoutSeconds() throws Exception {
		return Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(DriverUtils.class));
	}

	/**
	 * https://code.google.com/p/selenium/issues/detail?id=1880
	 * 
	 * DO NOT use this method if you want to check whether the element is NOT
	 * visible, because it will wait at least "imlicitTimeout" seconds until the
	 * actual result is returned. This slows down automated tests!
	 * 
	 * Use "waitUntilLocatorDissapears" method instead. That's quick and does
	 * exactly what you need
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isElementPresentAndDisplayed(RemoteWebDriver driver,
			final WebElement element) {
		try {
			return (element.isDisplayed() && isElementInScreenRect(driver,
					element));
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private static boolean isElementInScreenRect(RemoteWebDriver driver,
			final WebElement el) {
		final Rectangle elementRect = new Rectangle(el.getLocation().x,
				el.getLocation().y, el.getSize().width, el.getSize().height);
		final Rectangle screenRect = new Rectangle(0, 0, driver.manage()
				.window().getSize().width,
				driver.manage().window().getSize().height);
		return elementRect.intersects(screenRect);
	}

	public static boolean waitUntilLocatorIsDisplayed(RemoteWebDriver driver,
			By by) throws Exception {
		return waitUntilLocatorIsDisplayed(driver, by,
				getDefaultLookupTimeoutSeconds());
	}

	public static boolean waitUntilLocatorIsDisplayed(RemoteWebDriver driver,
			final By by, int timeoutSeconds) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(InvalidElementStateException.class);
			try {
				return wait.until(drv -> {
					return (drv.findElements(by).size() > 0)
							&& isElementPresentAndDisplayed(driver,
									drv.findElement(by));
				});
			} catch (TimeoutException e) {
				return false;
			}
		} finally {
			restoreImplicitWait(driver);
		}
	}

	public static boolean waitUntilLocatorDissapears(RemoteWebDriver driver,
			final By by) throws Exception {
		return waitUntilLocatorDissapears(driver, by,
				getDefaultLookupTimeoutSeconds());
	}

	public static boolean waitUntilLocatorDissapears(RemoteWebDriver driver,
			final By by, int timeoutSeconds) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			return wait.until(drv -> {
				try {
					return (drv.findElements(by).size() == 0)
							|| (drv.findElements(by).size() > 0 && !drv
									.findElement(by).isDisplayed())
							|| !isElementInScreenRect(driver,
									driver.findElement(by));
				} catch (SessionNotFoundException e) {
					log.debug(e.getMessage());
					return true;
				} catch (WebDriverException e) {
					return true;
				}
			});
		} catch (TimeoutException ex) {
			return false;
		} finally {
			restoreImplicitWait(driver);
		}
	}

	public static boolean waitUntilLocatorAppears(RemoteWebDriver driver,
			final By locator) throws Exception {
		return waitUntilLocatorAppears(driver, locator,
				getDefaultLookupTimeoutSeconds());
	}

	public static boolean waitUntilLocatorAppears(RemoteWebDriver driver,
			final By locator, int timeoutSeconds) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(InvalidElementStateException.class);
			return wait.until(drv -> {
				return (drv.findElements(locator).size() > 0);
			});
		} catch (TimeoutException ex) {
			return false;
		} finally {
			restoreImplicitWait(driver);
		}
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element) throws Exception {
		return waitUntilElementClickable(driver, element,
				getDefaultLookupTimeoutSeconds());
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element, int timeoutSeconds) throws Exception {
		turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		} finally {
			restoreImplicitWait(driver);
		}
	}

	public static boolean waitUntilAlertAppears(
			AppiumDriver<? extends WebElement> driver) throws Exception {
		return waitUntilAlertAppears(driver, 20);
	}

	public static boolean waitUntilAlertAppears(
			AppiumDriver<? extends WebElement> driver, long timeout)
			throws Exception {
		DriverUtils.turnOffImplicitWait(driver);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			return (wait.until(ExpectedConditions.alertIsPresent()) != null);
		} catch (TimeoutException e) {
			return false;
		} finally {
			restoreImplicitWait(driver);
		}
	}

	public static void scrollToElement(
			AppiumDriver<? extends WebElement> driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollToObject = new HashMap<String, String>();
		scrollToObject.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: scrollTo", scrollToObject);
	}

	/**
	 * Swipes from point inside element given by percent of element size.
	 * Default swipe size is 2/3 of window.width, but is end point outsie of
	 * screen then it is changed to 0;
	 * 
	 * @param driver
	 * @param element
	 * @param time
	 * @param elementPercentX
	 *            min value is 1. Starting point of swipe (in percents relative
	 *            to the original control width)
	 * @param elementPercentY
	 *            min value is 1. Starting point of swipe (in percents relative
	 *            to the original control height)
	 */
	public static void swipeLeft(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time, int elementPercentX,
			int elementPercentY) {
		final Point coords = element.getLocation();
		final Dimension screenSize = driver.manage().window().getSize();
		final Dimension elementSize = element.getSize();
		final int xOffset = elementSize.width * elementPercentX / 100;
		final int yOffset = elementSize.height * elementPercentY / 100;
		int endX = coords.x + xOffset - screenSize.width * 2 / 3 < 0 ? 0
				: coords.x + xOffset - screenSize.width * 2 / 3;
		driver.swipe(coords.x + xOffset, coords.y + yOffset, endX, coords.y
				+ yOffset, time);
	}

	public static final int SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL = 100;

	public static void swipeLeft(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time) {
		swipeLeft(driver, element, time, SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeRight(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();
		final int xOffset = elementSize.width * percentX / 100;
		final int yOffset = elementSize.height * percentY / 100;
		driver.swipe(coords.x, coords.y + yOffset, coords.x + xOffset, coords.y
				+ yOffset, time);
	}

	public static void swipeRight(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time, int startPercentX, int startPercentY,
			int endPercentX, int endPercentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();

		final int xStartOffset = elementSize.width * startPercentX / 100;
		final int yStartOffset = elementSize.height * startPercentY / 100;
		final int xEndOffset = elementSize.width * endPercentX / 100;
		final int yEndOffset = elementSize.height * endPercentY / 100;

		driver.swipe(coords.x + xStartOffset, coords.y + yStartOffset, coords.x
				+ xEndOffset, coords.y + yEndOffset, time);

	}

	public static void swipeRight(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time) {
		swipeRight(driver, element, time,
				SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, DEFAULT_PERCENTAGE);
	}

	public static void swipeUp(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();

		final int xOffset = elementSize.width * percentX / 100;
		final int yOffset = elementSize.height * percentY / 100;

		driver.swipe(coords.x + xOffset, coords.y + yOffset,
				coords.x + xOffset, coords.y, time);
	}

	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL = 100;

	public static void swipeUp(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time) {
		swipeUp(driver, element, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL);
	}

	public static void swipeDown(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time, int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension elementSize = element.getSize();

		final int xOffset = elementSize.width * percentX / 100;
		final int yOffset = elementSize.height * percentY / 100;

		driver.swipe(coords.x + xOffset, coords.y, coords.x + xOffset, coords.y
				+ yOffset, time);
	}

	public static void swipeDown(AppiumDriver<? extends WebElement> driver,
			WebElement element, int time) {
		swipeDown(driver, element, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_VERTICAL);
	}

	public static void swipeElementPointToPoint(
			AppiumDriver<? extends WebElement> driver, WebElement element,
			int time, int startPercentX, int startPercentY, int endPercentX,
			int endPercentY) {

		final Point coords = element.getLocation();
		final Dimension size = element.getSize();

		final int startX = coords.x + size.getWidth() * startPercentX / 100;
		final int startY = coords.y + size.getHeight() * startPercentY / 100;
		final int endX = coords.x + size.getWidth() * endPercentX / 100;
		final int endY = coords.y + size.getHeight() * endPercentY / 100;

		driver.swipe(startX, startY, endX, endY, time);
	}

	public static void swipeByCoordinates(
			AppiumDriver<? extends WebElement> driver, int time,
			int startPercentX, int startPercentY, int endPercentX,
			int endPercentY) throws Exception {
		final Dimension screenSize = driver.manage().window().getSize();

		final int startX = screenSize.width * startPercentX / 100;
		final int startY = screenSize.height * startPercentY / 100;
		final int endX = screenSize.width * endPercentX / 100;
		final int endY = screenSize.height * endPercentY / 100;

		driver.swipe(startX, startY, endX, endY, time);

	}

	public static final int DEFAULT_SWIPE_DURATION = 1000; // milliseconds
	public static final int DEFAULT_FINGERS = 1;

	public static void genericTap(AppiumDriver<? extends WebElement> driver) {
		genericTap(driver, DEFAULT_SWIPE_DURATION, DEFAULT_FINGERS,
				DEFAULT_PERCENTAGE, DEFAULT_PERCENTAGE);
	}

	public static void genericTap(AppiumDriver<? extends WebElement> driver,
			int percentX, int percentY) {
		genericTap(driver, DEFAULT_SWIPE_DURATION, DEFAULT_FINGERS, percentX,
				percentY);
	}

	public static void genericTap(AppiumDriver<? extends WebElement> driver,
			int time, int percentX, int percentY) {
		genericTap(driver, time, DEFAULT_FINGERS, percentX, percentY);
	}

	public static void genericTap(AppiumDriver<? extends WebElement> driver,
			int time, int fingers, int percentX, int percentY) {
		final Dimension screenSize = driver.manage().window().getSize();
		final int xCoords = screenSize.width * percentX / 100;
		final int yCoords = screenSize.height * percentY / 100;
		driver.tap(fingers, xCoords, yCoords, time);
	}

	public static final int SWIPE_X_DEFAULT_PERCENTAGE_START = 10;
	public static final int SWIPE_X_DEFAULT_PERCENTAGE_END = 90;
	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_START = 10;
	public static final int SWIPE_Y_DEFAULT_PERCENTAGE_END = 90;

	public static void swipeRightCoordinates(
			AppiumDriver<? extends WebElement> driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_START,
				DEFAULT_PERCENTAGE, SWIPE_X_DEFAULT_PERCENTAGE_END,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeRightCoordinates(
			AppiumDriver<? extends WebElement> driver, int time, int percentY)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_START,
				percentY, SWIPE_X_DEFAULT_PERCENTAGE_END, percentY);
	}

	public static void swipeLeftCoordinates(
			AppiumDriver<? extends WebElement> driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_END,
				DEFAULT_PERCENTAGE, SWIPE_X_DEFAULT_PERCENTAGE_START,
				DEFAULT_PERCENTAGE);
	}

	public static void swipeLeftCoordinates(
			AppiumDriver<? extends WebElement> driver, int time, int percentY)
			throws Exception {
		swipeByCoordinates(driver, time, SWIPE_X_DEFAULT_PERCENTAGE_END,
				percentY, SWIPE_X_DEFAULT_PERCENTAGE_START, percentY);
	}

	public static void swipeUpCoordinates(
			AppiumDriver<? extends WebElement> driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_END, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_START);
	}

	public static void swipeUpCoordinates(
			AppiumDriver<? extends WebElement> driver, int time, int percentX)
			throws Exception {
		swipeByCoordinates(driver, time, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_END, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_START);
	}

	public static void swipeDownCoordinates(
			AppiumDriver<? extends WebElement> driver, int time)
			throws Exception {
		swipeByCoordinates(driver, time, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_START, DEFAULT_PERCENTAGE,
				SWIPE_Y_DEFAULT_PERCENTAGE_END);
	}

	public static void swipeDownCoordinates(
			AppiumDriver<? extends WebElement> driver, int time, int percentX)
			throws Exception {
		swipeByCoordinates(driver, time, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_START, percentX,
				SWIPE_Y_DEFAULT_PERCENTAGE_END);
	}

	public static void androidMultiTap(
			AppiumDriver<? extends WebElement> driver, WebElement element,
			int tapNumber, int millisecondsDuration) {
		for (int i = 0; i < tapNumber; i++) {
			driver.tap(1, element, millisecondsDuration);
		}
	}

	public static void mobileTapByCoordinates(
			AppiumDriver<? extends WebElement> driver, WebElement element,
			int offsetX, int offsetY) {
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

	public static void mobileTapByCoordinates(
			AppiumDriver<? extends WebElement> driver, WebElement element) {
		mobileTapByCoordinates(driver, element, 0, 0);
	}

	public static void iOSSimulatorSwipeDown(String scriptPath)
			throws Exception {
		// CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
		// "python", scriptPath,"0.65", "0.1", "0.65", "0.7"});
		Runtime.getRuntime().exec(
				"/usr/bin/open -a Terminal " + scriptPath + "Down.py");
	}

	public static void iOSSimulatorSwipeRight(String scriptPath)
			throws Exception {

		Runtime.getRuntime().exec(
				"/usr/bin/open -a Terminal " + scriptPath + "Right.py");
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

	public static void iOSMultiTap(AppiumDriver<? extends WebElement> driver,
			WebElement element, int tapNumber) throws InterruptedException {
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

	public static void iOS3FingerTap(AppiumDriver<? extends WebElement> driver,
			WebElement element, int fingerNumber) {
		driver.tap(fingerNumber, element, 1);
	}

	public static void addClass(RemoteWebDriver driver, WebElement element,
			String cssClass) {
		String addHoverClassScript = "arguments[0].classList.add('" + cssClass
				+ "');";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(addHoverClassScript, element);
	}

	public static void addClassToParent(RemoteWebDriver driver,
			WebElement element, String cssClass) {
		String addHoverClassScript = "arguments[0].parentNode.classList.add('"
				+ cssClass + "');";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(addHoverClassScript, element);
	}

	public static void removeClass(RemoteWebDriver driver, WebElement element,
			String cssClass) {
		String script = "arguments[0].classList.remove('" + cssClass + "');";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, element);
	}

	public static void removeClassFromParent(RemoteWebDriver driver,
			WebElement element, String cssClass) {
		String script = "arguments[0].parentNode.classList.remove('" + cssClass
				+ "');";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, element);
	}

	public static void turnOffImplicitWait(RemoteWebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public static void restoreImplicitWait(RemoteWebDriver driver)
			throws Exception {
		PlatformDrivers.setDefaultImplicitWaitTimeout(driver);
	}

	public static Optional<BufferedImage> takeFullScreenShot(ZetaDriver driver)
			throws Exception {
		try {
			final byte[] srcImage = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.BYTES);
			final BufferedImage bImageFromConvert = ImageIO
					.read(new ByteArrayInputStream(srcImage));
			return Optional.ofNullable(bImageFromConvert);
		} catch (WebDriverException | NoClassDefFoundError e) {
			// e.printStackTrace();
			log.error("Selenium driver has failed to take the screenshot of the current screen!");
		}
		return Optional.empty();
	}

	public static void ToggleNetworkConnectionAndroid(
			AndroidDriver<? extends WebElement> driver, boolean airplane,
			boolean wifi, boolean data) {
		NetworkConnectionSetting connection = new NetworkConnectionSetting(
				airplane, wifi, data);
		driver.setNetworkConnection(connection);
	}

	public static void iOSLongTap(AppiumDriver<? extends WebElement> driver,
			WebElement element) {
		driver.tap(1, element, 1000);
	}

	public static void moveMouserOver(RemoteWebDriver driver, WebElement element) {
		/**
		 * Method seems to work for Chrome and FireFox but is not working for
		 * Safari <= 8. https://code.google.com/p/selenium/issues/detail?id=4136
		 */
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.perform();
	}

	/*
	 * This is a work around for pressing the silence button. The ID is not
	 * visible through Appium, thats why it is tapped by its location
	 * coordinates.
	 */
	public static void clickSilenceConversationButton(
			AppiumDriver<? extends WebElement> driver, WebElement element) {
		Point coords = element.getLocation();
		driver.tap(1, coords.x - (coords.x / 2 - coords.x / 8), coords.y, 1);
	}

	/*
	 * This is a work around for pressing the archive button. The ID is not
	 * visible through Appium, thats why it is tapped by its location
	 * coordinates.
	 */
	public static void clickArchiveConversationButton(
			AppiumDriver<? extends WebElement> driver, WebElement element) {
		Point coords = element.getLocation();
		driver.tap(1, coords.x - (coords.x / 2 + coords.x / 8), coords.y, 1);
	}

	public static void resetApp(AppiumDriver<? extends WebElement> driver) {
		driver.resetApp();
	}

	public static void tapInTheCenterOfTheElement(
			AppiumDriver<? extends WebElement> driver, WebElement element) {
		final Point coords = element.getLocation();
		final Dimension size = element.getSize();
		final int x = coords.x + size.getWidth() / 2;
		final int y = coords.y + size.getHeight() / 2;
		log.info("Tap on " + x + ":" + y);
		driver.tap(1, x, y, 1);
	}

	public static void tapOnPercentOfElement(
			AppiumDriver<? extends WebElement> driver, WebElement element,
			int percentX, int percentY) {
		final Point coords = element.getLocation();
		final Dimension size = element.getSize();
		final int x = coords.x + size.getWidth() * percentX / 100;
		final int y = coords.y + size.getHeight() * percentY / 100;
		log.info("Tap on " + x + ":" + y);
		driver.tap(1, x, y, 1);

	}

	public static void tapOutsideOfTheElement(
			AppiumDriver<? extends WebElement> driver, WebElement element,
			int xOffset, int yOffset) {
		assert xOffset != 0 && yOffset != 0;
		final Point coords = element.getLocation();
		final Dimension size = element.getSize();
		int dstX = 0;
		int dstY = 0;
		if (xOffset > 0) {
			dstX = coords.getX() + size.getWidth() + xOffset;
		} else {
			dstX = coords.getX() - xOffset;
		}
		if (yOffset > 0) {
			dstY = coords.getY() + size.getHeight() + yOffset;
		} else {
			dstY = coords.getY() - yOffset;
		}
		dstY = (driver.manage().window().getSize().getHeight() < dstY) ? driver
				.manage().window().getSize().getHeight() : dstY;
		dstX = (driver.manage().window().getSize().getWidth() < dstX) ? driver
				.manage().window().getSize().getWidth() : dstX;
		dstY = (dstY < 0) ? 0 : dstY;
		dstX = (dstX < 0) ? 0 : dstX;
		log.info("Tap on " + dstX + ":" + dstY);
		driver.tap(1, dstX, dstY, 1);
	}

	public static void sendTextToInputByScript(RemoteWebDriver driver,
			String scriptLocator, String text) {
		String script = String
				.format(scriptLocator + ".setValue(\"%s\")", text);
		int maxRetrys = 3;
		int retryCounter = 0;
		while (retryCounter < maxRetrys) {
			try {
				driver.executeScript(script);
				retryCounter = maxRetrys;
			} catch (WebDriverException ex) {
				log.debug("Appium execute script fail. " + ex.getMessage());
				retryCounter++;
			}
		}
	}
}
