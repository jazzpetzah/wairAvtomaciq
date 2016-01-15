package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletPendingUserPopoverPage extends OtherUserOnPendingProfilePage {
	public static final String xpathUserName = "//UIAPopover/UIAStaticText[contains(@name, '%s')]";
	@FindBy(xpath = xpathUserName)
	private WebElement userName;

    public static final String xpathConnectButton = "//UIAPopover/*[@name='CONNECT' and @visible='true']";
    @FindBy(xpath = xpathConnectButton)
	private WebElement connectButton;

	public TabletPendingUserPopoverPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	public boolean isUserNameDisplayed(String name) throws Exception {
		String formatXpath = String.format(xpathUserName, name);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.xpath(formatXpath), 2);
	}

	public boolean isConnectButtonDisplayed() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(), connectButton);
	}

}
