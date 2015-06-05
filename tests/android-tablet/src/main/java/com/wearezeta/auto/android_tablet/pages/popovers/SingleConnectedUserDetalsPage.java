package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SingleConnectedUserDetalsPage extends AbstractPopoverPage {
	public final static String idOptionsButton = "ll__participants__right__action";
	@FindBy(id = idOptionsButton)
	private WebElement optionsButton;

	public final static String idOptionsContainer = "ll__settings_box_container";
	public final static Function<String, String> xpathOptionMenuItemByName = itemName -> String
			.format("//*[@id='%s']//*[@value='%s']", idOptionsContainer,
					itemName.toUpperCase());

	public SingleConnectedUserDetalsPage(Future<ZetaAndroidDriver> lazyDriver,
			SingleUserPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapOptionsButton() {
		optionsButton.click();
	}

	public void selectMenuItem(String itemName) throws Exception {
		final By locator = By.xpath(xpathOptionMenuItemByName.apply(itemName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Options menu item '%s' is not displayed", itemName);
		getDriver().findElement(locator).click();
	}

	public boolean isMenuItemVisible(String itemName) throws Exception {
		final By locator = By.xpath(xpathOptionMenuItemByName.apply(itemName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

}
