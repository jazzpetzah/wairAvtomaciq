package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;

	public SingleUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton;
	}

	private WebElement getAddButtonElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public boolean isAddButtonVisible() {
		return getAddButtonElement().isDisplayed();
	}

	public boolean isBlockButtonVisible() {
		return blockButton.isDisplayed();
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddButtonElement());
		getAddButtonElement().click();
	}

	public void clickBlockButton() {
		blockButton.click();
	}
}
