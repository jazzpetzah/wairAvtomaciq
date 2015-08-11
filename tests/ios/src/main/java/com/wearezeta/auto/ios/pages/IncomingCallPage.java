package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class IncomingCallPage extends CallPage {

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameAcceptCallButton)
	private WebElement acceptCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameEndCallButton)
	private WebElement endCallButton;

	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathCallingMessage)
	private WebElement callingMessage;
	
	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameIgnoreCallButton)
	private WebElement ignoreCallButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameCallingMessageUser)
	private WebElement callingMessageUser;

	public IncomingCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCallingMessageVisible(String contact) throws Exception {
		return getDriver().findElementByName(
				String.format(
						IOSLocators.IncomingCallPage.nameCallingMessageUser,
						contact)).isDisplayed();
	}

	public boolean isUserCallingMessageShown(String contact) throws Exception {
		String locator = String.format(
				IOSLocators.IncomingCallPage.nameCallingMessageUser, contact);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(locator));
	}

	public StartedCallPage acceptIncomingCallClick() throws Exception {
		acceptCallButton.click();
		return new StartedCallPage(getLazyDriver());
	}

	public void ignoreIncomingCallClick() {
		ignoreCallButton.click();
	}

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(IOSLocators.IncomingCallPage.xpathCallingMessage), 10);
	}
}
