package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractConversationDetailsPage extends
		AbstractPopoverPage {
	public final static String idOptionsButton = "ll__participants__right__action";
	@FindBy(id = idOptionsButton)
	private WebElement optionsButton;

	public final static String idOptionsContainer = "fl__participant__settings_box";
	public final static Function<String, String> xpathOptionMenuItemByName = itemName -> String
			.format("//*[@id='%s']//*[@value='%s']/parent::*", idOptionsContainer,
					itemName.toUpperCase());

	public final static String xpathAddPeopleButton =
			"//*[@id='ttv__participants__left_label' and @value='ADD PEOPLE']";
	@FindBy(xpath = xpathAddPeopleButton)
	private WebElement addPeopleButton;

	public final static String idCloseButton = "gtv__participants__close";

	public AbstractConversationDetailsPage(
			Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
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

	public boolean isMenuItemInvisible(String itemName) throws Exception {
		final By locator = By.xpath(xpathOptionMenuItemByName.apply(itemName));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void tapAddPeopleButton() {
		addPeopleButton.click();
	}

	private WebElement getCloseButton() throws Exception {
		return this.getDriver().findElement(this.getContainer().getLocator())
				.findElement(By.id(idCloseButton));
	}

	public void tapCloseButton() throws Exception {
		getCloseButton().click();
	}
}
