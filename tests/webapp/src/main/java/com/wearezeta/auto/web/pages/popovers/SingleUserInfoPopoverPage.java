package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathAddButton)
	private WebElement addButton;

	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;

	public SingleUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	public boolean isAddPeopleButtonVisible() throws Exception {
		return DriverUtils
				.isElementDisplayed(
						driver,
						By.xpath(PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathAddButton));
	}

	public boolean isBlockPeopleButtonVisible() throws Exception {
		return DriverUtils
				.isElementDisplayed(
						driver,
						By.xpath(PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton));
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathAddButton;
	}
}
