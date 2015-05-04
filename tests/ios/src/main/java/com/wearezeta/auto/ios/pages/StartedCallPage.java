package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

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

	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathEndCallButton)
	private WebElement endCallButton;

	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathSpeakersButton)
	private WebElement speakersButton;

	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathMuteCallButton)
	private WebElement muteCallButton;

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.isElementDisplayed(getDriver(), callingMessage);
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

	public boolean isEndCallVisible() {
		return endCallButton.isDisplayed();
	}

	public boolean isSpeakersVisible() {
		return speakersButton.isDisplayed();
	}

	public boolean isMuteCallVisible() {
		return muteCallButton.isDisplayed();
	}

	public void clickEndCallButton() {
		endCallButton.click();
	}

}
