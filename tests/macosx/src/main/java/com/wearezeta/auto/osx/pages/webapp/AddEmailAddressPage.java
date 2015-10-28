package com.wearezeta.auto.osx.pages.webapp;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class AddEmailAddressPage extends WebPage {

	@FindBy(id = "wire-mail-email")
	private WebElement emailField;

	@FindBy(id = "wire-mail-password")
	private WebElement passwordField;

	@FindBy(id = "wire-mail-add")
	private WebElement addButton;

	@FindBy(css = WebAppLocators.AddEmailAddressPage.cssErrorMessage)
	private WebElement errorMessage;

	public AddEmailAddressPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setEmail(String email) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), emailField);
		emailField.click();
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void setPassword(String password) throws Exception {
		passwordField.click();
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void clickAddButton() {
		addButton.click();
	}

	public String getErrorMessage() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.cssSelector(WebAppLocators.PhoneNumberVerificationPage.cssErrorMessage));
		return errorMessage.getText();
	}

	public boolean isRedDotOnEmailField() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.AddEmailAddressPage.cssErrorMarkedEmailField));
	}

	public boolean isRedDotOnPasswordField() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.AddEmailAddressPage.cssErrorMarkedPasswordField));
	}
}
