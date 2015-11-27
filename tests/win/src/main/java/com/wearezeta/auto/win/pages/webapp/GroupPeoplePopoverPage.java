package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class GroupPeoplePopoverPage extends WebPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitle)
	private WebElement conversationTitle;

	public GroupPeoplePopoverPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(PopoverLocators.SingleUserPopover.xpathRootLocator)) : "Popover "
				+ PopoverLocators.SingleUserPopover.xpathRootLocator
				+ " has not been shown";
	}

	public void waitUntilNotVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(PopoverLocators.SingleUserPopover.xpathRootLocator)) : "Popover "
				+ PopoverLocators.SingleUserPopover.xpathRootLocator
				+ " has not been shown";
	}

	public boolean isAddPeopleMessageShown() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(PopoverLocators.Shared.xpathContinueButton));
	}

	public boolean isAddToConversationButtonShown() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(PopoverLocators.Shared.cssCreateGroupConversationButton));
	}

	public String getConversationTitle() {
		return conversationTitle.getText();
	}
}
