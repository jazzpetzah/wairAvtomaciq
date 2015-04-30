package com.wearezeta.auto.osx.pages.calling;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class StartedCallPage extends CallPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(StartedCallPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.CallPage.idCancelCallButton)
	private WebElement cancelCallButton;

	@FindBy(how = How.ID, using = OSXLocators.CallPage.idMuteMicrophoneButton)
	private WebElement muteMicrophoneButton;

	public StartedCallPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isOngoingCallVisible(String subscriberName) throws Exception {
		String xpath = String.format(
				OSXLocators.CallPage.xpathFormatSubscriberName, subscriberName);
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 30);
	}

	public boolean isPendingCallVisible(String subscriberName) throws Exception {
		String xpath = String.format(
				OSXLocators.CallPage.xpathFormatCallingUserMessage,
				subscriberName);
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 30);
	}

	public void cancelCallButton() {
		cancelCallButton.click();
	}

	public boolean isMicrophoneMuted() {
		String state = muteMicrophoneButton.getAttribute(OSXConstants.Attributes.AXVALUE);
		return OSXCommonUtils.osxAXValueToBoolean(state);
	}

	public void toggleMute() {
		muteMicrophoneButton.click();
	}
}
