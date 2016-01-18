package com.wearezeta.auto.osx.pages.osx;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class SingleUserPeoplePopoverPage extends WebPage {
	
	@FindBy(xpath = PopoverLocators.SingleUserPopover.xpathRootLocator)
	private WebElement rootElement;

	@FindBy(xpath = PopoverLocators.SingleUserPopover.xpathUserName)
	private WebElement userName;

	public SingleUserPeoplePopoverPage(Future<ZetaWebAppDriver> lazyDriver)
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

	public String getUserName() {
		return userName.getText();
	}

}
