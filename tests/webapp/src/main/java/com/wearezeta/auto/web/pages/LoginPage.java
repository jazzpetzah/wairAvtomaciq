package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.CommonUtils;

import java.net.URL;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class LoginPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(LoginPage.class.getName());

    private static final int TIMEOUT_SIGNED_IN_PROPERLY = 80; // seconds

    @FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathCreateAccountButton)
    private WebElement createAccountButton;

    @FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathSwitchToRegisterButtons)
    private WebElement switchToRegisterButton;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssSignInButton)
    private WebElement signInButton;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssPhoneSignInButton)
    private WebElement phoneSignInButton;

    @FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathChangePasswordButton)
    private WebElement changePasswordButton;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssEmailInput)
    private WebElement emailInput;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssPasswordInput)
    private WebElement passwordInput;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssRememberMe)
    private WebElement rememberMe;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssForgotPassword)
    private WebElement forgotPasswordButton;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssLoginErrorText)
    private WebElement loginErrorText;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssSessionExpiredErrorText)
    private WebElement sessionExpiredErrorText;

    @FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssDescriptionText)
    private WebElement descriptionText;

    public LoginPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public LoginPage(Future<ZetaWebAppDriver> lazyDriver, String url)
            throws Exception {
        super(lazyDriver, url);
    }

    public void switchToRegistrationPage() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), switchToRegisterButton);
        switchToRegisterButton.click();
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(),
                createAccountButton)
                && DriverUtils.waitUntilElementClickable(this.getDriver(),
                        forgotPasswordButton);
    }

    public boolean isSignInFormVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(),
                switchToRegisterButton)
                && DriverUtils.waitUntilElementClickable(this.getDriver(),
                        forgotPasswordButton);
    }

    public boolean isSignInButtonDisabled() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssSignInButton));
        return !signInButton.isEnabled();
    }

    public boolean isSignInButtonEnabled() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssSignInButton));
        return signInButton.isEnabled();
    }

    public void inputEmail(String email) {
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void inputPassword(String password) {
        passwordInput.click();
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    private boolean waitForLoginButtonDisappearance() throws Exception {
        // workarounds for IE driver bugs:
        // 1. when findElements() returns one RemoteWebElement instead of list
        // of elements and throws WebDriverException
        // 2. NPE when findElements() call
        boolean noSignIn = false;
        try {
            noSignIn = DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                    By.xpath(WebAppLocators.LoginPage.cssSignInButton), 60);
        } catch (WebDriverException e) {
            if (WebAppExecutionContext.getBrowser() == Browser.InternetExplorer) {
                noSignIn = true;
            } else {
                throw e;
            }
        }
        return noSignIn;
    }

    private boolean waitForRememberMeDisappearance() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.cssSelector(
                WebAppLocators.LoginPage.cssRememberMe));
    }

    private boolean waitForEmailFieldDisappearance() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.cssSelector(
                WebAppLocators.LoginPage.cssEmailInput));
    }

    private boolean waitForHistoryPageDisappearance() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.cssSelector(
                WebAppLocators.HistoryInfoPage.cssConfirmButton));
    }

    public boolean waitForLogin() throws Exception {
        boolean noSignIn = waitForLoginButtonDisappearance();
        boolean noRememberMe = waitForRememberMeDisappearance();
        boolean noEmailField = waitForEmailFieldDisappearance();
        boolean noHistoryPage = waitForHistoryPageDisappearance();
        boolean noSignInSpinner = DriverUtils.waitUntilLocatorDissapears(
                this.getDriver(),
                By.className(WebAppLocators.LoginPage.classNameProgressBar),
                TIMEOUT_SIGNED_IN_PROPERLY);
        return noSignIn && noRememberMe && noEmailField && noHistoryPage && noSignInSpinner;
    }

    public void clickSignInButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), signInButton);
        signInButton.click();
    }

    public void clickChangePasswordButton(WebappPagesCollection webappPagesCollection) throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                changePasswordButton);
        changePasswordButton.click();
    }

    public String getErrorMessage() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.cssSelector(WebAppLocators.LoginPage.cssLoginErrorText));
        return loginErrorText.getText();
    }

    public boolean isEmailFieldMarkedAsError() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.errorMarkedEmailField));
    }

    public boolean isPasswordFieldMarkedAsError() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.LoginPage.errorMarkedPasswordField));
    }

    public void visitSessionExpiredPage(String langKey) throws Exception {

        String currentUrl = this.getDriver().getCurrentUrl();
        URL oldUrl = new URL(currentUrl);
        StringBuilder newUrl = new StringBuilder();
        newUrl.append(oldUrl.getProtocol())
                .append("://")
                .append(oldUrl.getHost())
                .append(oldUrl.getPath());
        if (oldUrl.getQuery() == null) {
            newUrl.append("?");
        } else {
            newUrl.append("?" + oldUrl.getQuery()).append("&");
        }
        newUrl.append("expired&hl=").append(langKey);
        if (oldUrl.getRef() != null) {
            newUrl.append("#").append(oldUrl.getRef());
        }
        this.getDriver().get(newUrl.toString());
    }

    public String getSessionExpiredErrorMessage() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssSessionExpiredErrorText));
        return sessionExpiredErrorText.getText();
    }

    public boolean isSessionExpiredErrorMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssSessionExpiredErrorText));
    }

    public void visitRedirectedPage(String langKey) throws Exception {
        String webappApplicationPath = CommonUtils.getWebAppApplicationPathFromConfig(LoginPage.class);
        String separator = webappApplicationPath.contains("?") ? "&" : "?";
        getDriver().get(webappApplicationPath + separator + "connect&hl=" + langKey);
    }

    public String getDescriptionMessage() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssDescriptionText));
        return descriptionText.getText();
    }

    public boolean isDescriptionMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.LoginPage.cssDescriptionText));
    }

    public void switchToPhoneNumberLoginPage() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), phoneSignInButton);
        phoneSignInButton.click();
    }

    public void checkRememberMe() {
        if (!rememberMe.isSelected()) {
            rememberMe.click();
        }
    }

    public void uncheckRememberMe() {
        if (rememberMe.isSelected()) {
            rememberMe.click();
        }
    }
}
