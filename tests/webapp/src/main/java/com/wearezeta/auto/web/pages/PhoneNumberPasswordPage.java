package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.openqa.selenium.By;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PhoneNumberPasswordPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(PhoneNumberPasswordPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.PhoneNumberPasswordPage.cssPasswordInput)
    private WebElement phonePasswordInput;

    @FindBy(css = WebAppLocators.PhoneNumberPasswordPage.cssSignInButton)
    private WebElement signInButton;

    public PhoneNumberPasswordPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void enterPassword(String password) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), phonePasswordInput);
        phonePasswordInput.click();
        phonePasswordInput.clear();
        phonePasswordInput.sendKeys(password);
    }

    public void clickSignInButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), signInButton);
        signInButton.click();
    }

    public String getFirstErrorText() throws Exception {
        By errorMessageLocator = By.cssSelector(WebAppLocators.PhoneNumberPasswordPage.cssErrorMessage);
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), errorMessageLocator);
        return this.getDriver().findElement(errorMessageLocator).getText();
    }
}
