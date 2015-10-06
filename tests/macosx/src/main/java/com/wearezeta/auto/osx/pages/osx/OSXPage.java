package com.wearezeta.auto.osx.pages.osx;

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
import org.openqa.selenium.WebElement;

public abstract class OSXPage extends BasePage {

	private static final String MENU_ITEM_VERSION = "Version";
	private static final String MENUBAR_ITEM_HELP = "Help";

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

	public void switchEnvironment(String environmentName) throws Exception {
		// click version menu item twice to get dev and env menu items
		for (int i = 0; i < 2; i++) {
			clickMenuBarItem(MENUBAR_ITEM_HELP, MENU_ITEM_VERSION);
		}

		clickMenuBarItem(MENUBAR_ITEM_HELP, environmentName);
		// FIXME: Have to click environment twice to actually trigger page load
		// to reg page
		clickMenuBarItem(MENUBAR_ITEM_HELP, environmentName);
	}

	public void clickMenuBarItem(String firstItem, String... items)
			throws Exception {
		String locatorBar = OSXLocators.AppMenu.xpathMenuBarItem
				.apply(firstItem);
		By locator = By.xpath(locatorBar);
		DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
		WebElement menuElement = getDriver().findElement(locator);
		DriverUtils.waitUntilElementClickable(getDriver(), menuElement);
		menuElement.click();
		String locatorItems = "";
		for (String item : items) {
			locatorItems += OSXLocators.AppMenu.xpathMenuItem.apply(item);
			By itemLocator = By.xpath(locatorBar + locatorItems);
			DriverUtils.waitUntilLocatorAppears(getDriver(), itemLocator);
			WebElement itemElement = getDriver().findElement(itemLocator);
			DriverUtils.waitUntilElementClickable(getDriver(), itemElement);
			itemElement.click();
		}
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
