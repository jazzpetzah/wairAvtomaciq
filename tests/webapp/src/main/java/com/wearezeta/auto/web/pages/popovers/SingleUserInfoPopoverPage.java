package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;
	
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathUnblockButton)
	private WebElement unblockButton;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDevicesTab)
	private WebElement devicesTab;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDetailsTab)
	private WebElement detailsTab;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDevicesText)
	private WebElement devicesText;

	public SingleUserInfoPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton;
	}

	private WebElement getAddButtonElement() throws Exception {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public boolean isAddButtonVisible() throws Exception {
		return getAddButtonElement().isDisplayed();
	}

	public boolean isBlockButtonVisible() {
		return blockButton.isDisplayed();
	}
	
	public boolean isUnblockButtonVisible() {
		return unblockButton.isDisplayed();
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddButtonElement());
		getAddButtonElement().click();
	}

	public void clickBlockButton() {
		blockButton.click();	
	}
	public void clickUnblockButton() {
		unblockButton.click();
	}

	public void switchToDevicesTab() {
		devicesTab.click();
	}
	public void switchToDetailsTab() {
		detailsTab.click();
	}

	public String getDevicesText() {
		return devicesText.getText();
	}
}
