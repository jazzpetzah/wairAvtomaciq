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
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DriverUtils {
	private static final Logger log = ZetaLogger.getLog(DriverUtils.class
			.getSimpleName());

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static boolean isElementDisplayed(WebElement element) {
		boolean flag = true;
		try {
			flag = element.isDisplayed();
		} catch (Exception ex) {
			flag = false;
		}
		return flag;
	}

	public static boolean waitUntilElementDissapear(RemoteWebDriver driver,
			final By by) {
		return waitUntilElementDissapear(driver, by, 20);
	}

	public static boolean waitUntilElementDissapear(RemoteWebDriver driver,
			final By by, int timeout) {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		// changing to true may cause false positive result
		Boolean bool = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			bool = wait.until(new Function<WebDriver, Boolean>() {

				public Boolean apply(WebDriver driver) {
					return (driver.findElements(by).size() == 0);
				}
			});
		} catch (Exception ex) {
			// do nothing
		} finally {
			setDefaultImplicitWait(driver);
		}
		return bool;
	}

	public static boolean waitUntilElementAppears(RemoteWebDriver driver,
			final By by) {
		return waitUntilElementAppears(driver, by, 20);
	}

	public static boolean waitUntilElementAppears(RemoteWebDriver driver,
			final By by, int timeout) {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Boolean bool = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			bool = wait.until(new Function<WebDriver, Boolean>() {

				public Boolean apply(WebDriver driver) {
					return (driver.findElements(by).size() > 0);
				}
			});
		} catch (Exception ex) {

		} finally {
			setDefaultImplicitWait(driver);
		}
		return bool;
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element) {
		return waitUntilElementClickable(driver, element, 20);
	}

	public static boolean waitUntilElementClickable(RemoteWebDriver driver,
			final WebElement element, int timeout) {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Boolean bool = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			bool = true;
		} catch (Exception ex) {
			bool = false;
		} finally {
			setDefaultImplicitWait(driver);
		}
		return bool;
	}
	
	public static boolean waitUntilWebPageLoaded(RemoteWebDriver driver) {
		return waitUntilWebPageLoaded(driver, 20);
	}

	public static boolean waitUntilWebPageLoaded(RemoteWebDriver driver,
			int timeout) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Boolean bool = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(timeout, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			bool = wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver t) {
					return String
							.valueOf(
									((JavascriptExecutor) driver)
											.executeScript("return document.readyState"))
							.equals("complete");
				}
			});
		} catch (Exception ex) {
			// do nothing
		} finally {
			setDefaultImplicitWait(driver);
		}
		return bool;
	}

	public static boolean waitUntilElementVisible(RemoteWebDriver driver,
			final WebElement element) {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Boolean bool = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.visibilityOf(element));
			bool = true;
		} catch (Exception ex) {
			bool = false;
		} finally {
			setDefaultImplicitWait(driver);
		}
		return bool;
	}

	public static void waitUntilAlertAppears(AppiumDriver driver) {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.alertIsPresent());
		} catch (Exception ex) {

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
		boolean flag = true;
		boolean found = false;
		int counter = 0;
		while (flag) {
			counter++;
			List<WebElement> contactsList = driver.findElementsByXPath(String
					.format(xpath, name));
			if (contactsList.size() > 0) {
				found = true;
				break;
			}
			Thread.sleep(200);
			if (counter == 10) {
				flag = false;
			}
		}
		return found;
	}

	public static void scrollToElement(AppiumDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollToObject = new HashMap<String, String>();
		scrollToObject.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: scrollTo", scrollToObject);
	}

	public static void swipeLeft(AppiumDriver driver, WebElement element,
			int time) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		try {
			driver.swipe(coords.x + elementSize.width - 20, coords.y
					+ elementSize.height / 2, coords.x + 20, coords.y
					+ elementSize.height / 2, time);
		} catch (Exception ex) {

		}
	}

	public static void swipeRight(AppiumDriver driver, WebElement element,
			int time) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		try {
			driver.swipe(coords.x, coords.y + elementSize.height / 2, coords.x
					+ elementSize.width - 10,
					coords.y + elementSize.height / 2, time);
		} catch (Exception ex) {

		}
	}

	public static void swipeUp(AppiumDriver driver, WebElement element, int time) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		try {
			driver.swipe(coords.x + elementSize.width / 2, coords.y
					+ elementSize.height - 170, coords.x + elementSize.width
					/ 2, coords.y + 120, time);
		} catch (Exception ex) {

		}
	}

	public static void swipeDown(AppiumDriver driver, WebElement element,
			int time) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		try {
			driver.swipe(coords.x + elementSize.width / 2, coords.y + 150,
					coords.x + elementSize.width / 2, coords.y
							+ elementSize.height - 200, time);
		} catch (Exception ex) {

		}
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
			WebElement element) {
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		tapObject
				.put("x",
						(double) (coords.x + elementSize.width - elementSize.width / 2)); // in
																							// pixels
																							// from
																							// left
		tapObject.put("y", (double) (coords.y + elementSize.height - 20));// in
																			// pixels
																			// from
																			// top
		js.executeScript("mobile: tap", tapObject);
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

	public static void setDefaultImplicitWait(RemoteWebDriver driver) {
		try {
			driver.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(CommonUtils
									.getDriverTimeoutFromConfig(BasePage.class)),
							TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		driver.tap(1, coords.x - 70, coords.y, 1);
	}
	
	/*
	 * This is a work around for pressing the archive button. The ID is not
	 * visible through Appium, thats why it is tapped by its location
	 * coordinates.
	 */
	public static void clickArchiveConversationButton(AppiumDriver driver,
			WebElement element) {
		Point coords = element.getLocation();
		driver.tap(1, coords.x - 126, coords.y, 1);
	}
}
