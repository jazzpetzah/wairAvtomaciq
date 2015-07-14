package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PhoneNumberLoginPage extends WebPage {

	@FindBy(css = "[data-uie-name='enter-county-code']")
	private WebElement countryCodeField;

	@FindBy(css = "[data-uie-name='enter-phone']")
	private WebElement phoneNumberField;

	@FindBy(id = "wire-login-phone")
	private WebElement forwardButton;

	@FindBy(css = WebAppLocators.PhoneNumberLoginPage.cssErrorMessage)
	private WebElement errorMessage;

	public PhoneNumberLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public PhoneNumberLoginPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver, url);
	}

	public void enterCountryCode(String countryCode) {
		countryCodeField.clear();
		countryCodeField.sendKeys(countryCode);
	}

	public void enterPhoneNumber(String phoneNumber) {
		phoneNumberField.clear();
		phoneNumberField.sendKeys(phoneNumber);
	}

	public PhoneNumberVerificationPage clickForwardButton() throws Exception {
		forwardButton.click();
		return new PhoneNumberVerificationPage(getLazyDriver());
	}

	public String getErrorMessage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.cssSelector(WebAppLocators.PhoneNumberLoginPage.cssErrorMessage));
		return errorMessage.getText();
	}
}
