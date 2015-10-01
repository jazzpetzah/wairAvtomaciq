package com.wearezeta.auto.web.pages;

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
	private WebElement phoneCodeField;

	@FindBy(css = WebAppLocators.PhoneNumberVerificationPage.cssErrorMessage)
	private WebElement errorMessage;

	public PhoneNumberVerificationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public ContactListPage enterCode(String code) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), phoneCodeField);
		log.info("Enter code: " + code);
		phoneCodeField.clear();
		phoneCodeField.sendKeys(code);
		return new ContactListPage(getLazyDriver());
	}

	public String getErrorMessage() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.cssSelector(WebAppLocators.PhoneNumberVerificationPage.cssErrorMessage));
		return errorMessage.getText();
	}

	public AddEmailAddressPage enterCodeForEmaillessUser(String code)
			throws Exception {
		enterCode(code);
		return new AddEmailAddressPage(getLazyDriver());
	}
}
