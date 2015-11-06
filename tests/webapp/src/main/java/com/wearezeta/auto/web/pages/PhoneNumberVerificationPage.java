package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PhoneNumberVerificationPage extends WebPage {

	private static final Logger log = ZetaLogger
			.getLog(PhoneNumberVerificationPage.class.getSimpleName());

	@FindBy(css = "[data-uie-name='enter-phone-code']")
	private List<WebElement> phoneCodeFields;

	@FindBy(css = WebAppLocators.PhoneNumberVerificationPage.cssErrorMessage)
	private WebElement errorMessage;

	public PhoneNumberVerificationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void enterCode(String code) throws Exception {
		log.info("Enter code: " + code);
		for (int i = 0; i < code.length(); i++) {
			WebElement phoneCodeField = phoneCodeFields.get(i);
			DriverUtils.waitUntilElementClickable(getDriver(), phoneCodeField);
			phoneCodeField.clear();
			phoneCodeField.sendKeys(String.valueOf(code.charAt(i)));
		}
	}

	public String getErrorMessage() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.cssSelector(WebAppLocators.PhoneNumberVerificationPage.cssErrorMessage));
		return errorMessage.getText();
	}

	public void enterCodeForEmaillessUser(String code) throws Exception {
		enterCode(code);
	}
}
