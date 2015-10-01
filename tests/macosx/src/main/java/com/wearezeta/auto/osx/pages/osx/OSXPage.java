package com.wearezeta.auto.osx.pages.osx;

import java.io.IOException;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;

import java.util.Map;
import java.util.concurrent.Future;
import org.openqa.selenium.By;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;

public abstract class OSXPage extends BasePage {

	private String path = null;

	@Override
	protected ZetaOSXDriver getDriver() throws Exception {
		return (ZetaOSXDriver) super.getDriver();
	}

	public OSXPage(Future<ZetaOSXDriver> osxDriver) throws Exception {
		super(osxDriver);
	}

	public OSXPage(Future<ZetaOSXDriver> osxDriver, String path)
			throws Exception {
		super(osxDriver);
		this.path = path;
	}

	// TODO create constants
	public void switchEnvironmentToStaging() throws Exception {
		// click version menu item twice to get dev and env menu items
		for (int i = 0; i < 2; i++) {
			clickMenuBarItem("Help");
			clickMenuItem("Version");
		}
		clickMenuBarItem("Help");
		clickMenuItem("Staging");
	}

	public void clickPing() throws Exception {
		clickMenuBarItem("Conversation");
		clickMenuItem("Ping");
	}

	public void clickMenuBarItem(String name) throws Exception {
		By locator = By.xpath(OSXLocators.AppMenu.xpathMenuBarItem.apply(name));
		DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
		getDriver().findElement(locator).click();
	}

	public void clickMenuItem(String name) throws Exception {
		By locator = By.xpath(OSXLocators.AppMenu.xpathMenuItem.apply(name));
		DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
		getDriver().findElement(locator).click();
	}

	public void navigateTo() throws Exception {
		if (this.path == null) {
			throw new RuntimeException(String.format(
					"The page %s does not support direct navigation", this
							.getClass().getName()));
		}
		this.getDriver().navigate().to(this.path);
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public void startApp() throws Exception {
		this.getDriver().navigate().to(OSXExecutionContext.WIRE_APP_PATH);
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(OSXPagesCollection.class, OSXPage.class);
	}

	// not used in OS X
	@Override
	public BasePage swipeLeft(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeRight(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeUp(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeDown(int time) throws IOException {
		return null;
	}

	public Dimension getDesktopSize() throws Exception {
		Dimension dimension = null;
		Object object = null;
		String script = "tell application \"Finder\"\n"
				+ "set _b to bounds of window of desktop\n"
				+ "set _width to get item 3 of _b\n"
				+ "set _height to get item 4 of _b\n"
				+ "set k to \"\" & _width & \"x\" & _height\n" + "return k\n"
				+ "end tell";
		try {
			object = getDriver().executeScript(script);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) object;
			String result = map.get("result");
			String[] sizes = result.split("x");
			dimension = new Dimension(Integer.parseInt(sizes[0]),
					Integer.parseInt(sizes[1]));
		} catch (WebDriverException e) {
			throw new Exception("Apple script failed:\n" + script);
		} catch (Exception e) {
			throw new Exception("Could not parse return value " + object
					+ " of apple script: " + e);
		}
		return dimension;
	}

	public Dimension getDockSize() throws Exception {
		Dimension dimension = null;
		Object object = null;
		String script = "tell application \"System Events\" to tell process \"Dock\"\n"
				+ "set dock_dimensions to size in list 1\n"
				+ "set _width to item 1 of dock_dimensions\n"
				+ "set _height to item 2 of dock_dimensions\n"
				+ "set k to \"\" & _width & \"x\" & _height\n"
				+ "return k\n"
				+ "end tell";
		try {
			object = getDriver().executeScript(script);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) object;
			String result = map.get("result");
			String[] sizes = result.split("x");
			dimension = new Dimension(Integer.parseInt(sizes[0]),
					Integer.parseInt(sizes[1]));
		} catch (WebDriverException e) {
			throw new Exception("Apple script failed:\n" + script);
		} catch (Exception e) {
			throw new Exception("Could not parse return value " + object
					+ " of apple script: " + e);
		}
		return dimension;
	}
}
