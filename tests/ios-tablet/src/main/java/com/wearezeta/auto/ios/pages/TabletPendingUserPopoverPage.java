package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletPendingUserPopoverPage extends OtherUserOnPendingProfilePage{
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletPendingUserPopoverPage.xpathUserName)
	private WebElement userName;

	public TabletPendingUserPopoverPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isUserNameDisplayed(String name) throws Exception {
		String formatXpath = String.format(IOSTabletLocators.TabletPendingUserPopoverPage.xpathUserName, name);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.xpath(formatXpath), 2);
	}
	

}
