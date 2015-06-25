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

public class StartedCallPage extends CallPage {

	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathCallingMessage)
	private WebElement callingMessage;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameEndCallButton)
	private WebElement endCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameSpeakersButton)
	private WebElement speakersButton;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameMuteCallButton)
	private WebElement muteCallButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameCallingMessageUser)
	private WebElement callingMessageUser;

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(IOSLocators.StartedCallPage.xpathCallingMessage), 10);
	}

	public boolean isIncomingCallMessageVisible(String contact)
			throws Exception {
		return getDriver().findElementByXPath(
				String.format(
						IOSLocators.StartedCallPage.xpathCallingMessageUser,
						contact)).isDisplayed();
	}

	public boolean isStartedCallMessageVisible(String contact) throws Exception {
		return getDriver()
				.findElementByXPath(
						String.format(
								IOSLocators.StartedCallPage.xpathStartedCallMessageUser,
								contact)).isDisplayed();
	}

	public StartedCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEndCallVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameEndCallButton));
	}

	public boolean isSpeakersVisible() throws Exception {
		return  DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameSpeakersButton));
	}

	public boolean isMuteCallVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameMuteCallButton));
	}

	public void clickEndCallButton() {
		endCallButton.click();
	}


}
