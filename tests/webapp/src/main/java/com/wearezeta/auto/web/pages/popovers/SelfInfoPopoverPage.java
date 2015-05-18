package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class SelfInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.SelfInfoPage.xpathProfileButton)
	private WebElement profileButton;

	public SelfInfoPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.SelfInfoPage.xpathProfileButton;
	}

	public boolean isProfileButtonVisible() {
		return profileButton.isDisplayed();
	}

	public void clickProfileButton() {
		profileButton.click();
	}

	public String getProfileButtonCaption() {
		return profileButton.getText();
	}

	public String getProfileButtonToolTip() {
		return profileButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}
}
