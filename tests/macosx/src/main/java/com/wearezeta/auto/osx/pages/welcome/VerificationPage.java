package com.wearezeta.auto.osx.pages.welcome;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;

public class VerificationPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(VerificationPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.VerificationPage.idEnvelopeImage)
	private WebElement envelopeImage;

	@FindBy(how = How.ID, using = OSXLocators.VerificationPage.idEmailSentMessage)
	private WebElement emailSentMessage;

	@FindBy(how = How.ID, using = OSXLocators.VerificationPage.idDidntGetTheMessageMessage)
	private WebElement didntGetTheMessageMessage;

	@FindBy(how = How.NAME, using = OSXLocators.VerificationPage.nameReSendLink)
	private WebElement reSendLink;

	private Future<String> activationMessage;

	private String activationResponse = null;
	
	public VerificationPage(ZetaOSXDriver driver, WebDriverWait wait, String email)
			throws Exception {
		super(driver, wait);

		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", email);
		activationMessage = IMAPSMailbox
				.getInstance().getMessage(expectedHeaders,
						BackendAPIWrappers.UI_ACTIVATION_TIMEOUT);
	}

	public void reSendEmailMessage() {
		reSendLink.click();
	}

	public boolean isVerificationRequested() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.VerificationPage.idEmailSentMessage), 60);
	}

	public boolean isEmailSentForEmailMessageVisible(String email)
			throws Exception {
		String xpath = String
				.format(OSXLocators.VerificationPage.xpathFormatEmailSentMessage,
						email);
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 10);
	}

	public boolean isReSendLinkVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.name(OSXLocators.VerificationPage.nameReSendLink), 10);
	}

	public boolean isReSendLinkDisappears() throws Exception {
		return DriverUtils.waitUntilElementDissapear(driver,
				By.name(OSXLocators.VerificationPage.nameReSendLink), 10);
	}
	
	//browser activation page
	public void activateUserFromBrowser() throws Exception {
		String activationLink = BackendAPIWrappers
				.getUserActivationLink(this.activationMessage);
		String script = String
				.format(OSXCommonUtils
						.readTextFileFromResources(OSXConstants.Scripts.ACTIVATE_USER_SCRIPT),
						activationLink);

		@SuppressWarnings("unchecked")
		Map<String, String> value = (Map<String, String>) driver
				.executeScript(script);
		activationResponse = value.get("result");
		log.debug(activationResponse);
	}
	

	public boolean isUserActivated() {
		return activationResponse
				.contains(OSXLocators.RegistrationPage.ACTIVATION_RESPONSE_VERIFIED);
	}

	public Future<String> getActivationMessage() {
		return activationMessage;
	}
}
