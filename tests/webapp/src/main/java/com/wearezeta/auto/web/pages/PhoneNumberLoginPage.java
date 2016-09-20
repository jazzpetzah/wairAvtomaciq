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

    @FindBy(css = "[data-uie-name='do-sign-in-phone']")
    private WebElement signInButton;

    @FindBy(css = WebAppLocators.PhoneNumberLoginPage.cssErrorMessage)
    private WebElement errorMessage;

    @FindBy(css = WebAppLocators.PhoneNumberLoginPage.cssRememberMe)
    private WebElement rememberMe;

    public PhoneNumberLoginPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public PhoneNumberLoginPage(Future<ZetaWebAppDriver> lazyDriver, String url) throws Exception {
        super(lazyDriver, url);
    }

    public void enterCountryCode(String countryCode) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), countryCodeField);
        countryCodeField.click();
        countryCodeField.clear();
        countryCodeField.sendKeys(countryCode);
    }

    public void enterPhoneNumber(String phoneNumber) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), phoneNumberField);
        phoneNumberField.click();
        phoneNumberField.clear();
        phoneNumberField.sendKeys(phoneNumber);
    }

    public void clickSignInButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), signInButton);
        signInButton.click();
    }

    public String getErrorMessage() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(
                getDriver(),
                By.cssSelector(WebAppLocators.PhoneNumberLoginPage.cssErrorMessage));
        return errorMessage.getText();
    }

    public void checkRememberMe() throws Exception {
        if (!rememberMe.isSelected()) {
            DriverUtils.waitUntilElementClickable(getDriver(), rememberMe);
            rememberMe.click();
        }
    }

    public void uncheckRememberMe() throws Exception {
        if (rememberMe.isSelected()) {
            DriverUtils.waitUntilElementClickable(getDriver(), rememberMe);
            rememberMe.click();
        }
    }
}
