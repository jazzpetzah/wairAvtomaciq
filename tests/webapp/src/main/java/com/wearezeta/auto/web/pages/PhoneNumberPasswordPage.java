package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class PhoneNumberPasswordPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(PhoneNumberPasswordPage.class.getSimpleName());

    @FindBy(css = "#wire-verify-password-input")
    private WebElement phonePasswordInput;
    
    @FindBy(css = "#wire-verify-password")
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
}
