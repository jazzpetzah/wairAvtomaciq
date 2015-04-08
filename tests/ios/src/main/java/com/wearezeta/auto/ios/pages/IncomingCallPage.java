package com.wearezeta.auto.ios.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class IncomingCallPage extends CallPage {
	
	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathAcceptCallButton)
	private WebElement acceptCallButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathEndCallButton)
	private WebElement endCallButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathCallingMessage)
	private WebElement callingMessage;

	public IncomingCallPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isCallingMessageVisible(String contact) {
		return driver.findElementByXPath(
				String.format(IOSLocators.IncomingCallPage.xpathCallingMessageUser, contact)).isDisplayed();
	}
	
	public void acceptIncomingCallClick() {
		acceptCallButton.click();
	}
	
	public void endIncomingCallClick() {
		endCallButton.click();
	}

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.isElementDisplayed(getDriver(), callingMessage);
	}
}
