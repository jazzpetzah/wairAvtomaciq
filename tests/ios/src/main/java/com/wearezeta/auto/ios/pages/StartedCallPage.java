package com.wearezeta.auto.ios.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class StartedCallPage extends CallPage {
	
	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathCallingMessage)
	private WebElement callingMessage;

	
	public boolean isCallingMessageVisible() {
		return callingMessage.isDisplayed();
	}
	
	public boolean isCallingMessageVisible(String contact) {
		return driver.findElementByXPath(
				String.format(IOSLocators.StartedCallPage.xpathCallingMessageUser, contact)).isDisplayed();
	}
	
	public StartedCallPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
