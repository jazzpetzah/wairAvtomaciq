package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PreferencesAccountPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(PreferencesAccountPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssPreferencesAccountLogoutButton)
    private WebElement logoutButton;

    @FindBy(css = "[data-uie-name='go-logout']")
    private WebElement logoutInDialogButton;

    @FindBy(css = ".modal-logout .checkbox span")
    private WebElement clearDataCheckbox;

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssSelfUserName)
    private WebElement userName;

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssSelfUserNameInput)
    private WebElement userNameInput;

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssNameSelfUserMail)
    private WebElement userMail;

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssNameSelfUserPhoneNumber)
    private WebElement userPhoneNumber;

    public PreferencesAccountPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void logout() throws Exception {
        logoutButton.click();
    }

    public boolean isLogoutDialogShown() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), logoutButton);
    }

    public void checkClearDataInLogoutDialog() {
        if (!clearDataCheckbox.isSelected()) {
            clearDataCheckbox.click();
        }
    }

    public void logoutInLogoutDialog() {
        logoutInDialogButton.click();
    }

    public boolean checkNameInSelfProfile(String name) throws Exception {
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(WebAppLocators.PreferencesAccountPage.cssSelfUserName));

        WebDriverWait wait = new WebDriverWait(this.getDriver(), 10);

        return wait.until((WebDriver driver) -> {
            if (userName.getText().equals(name)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        });
    }

    public String getUserName() throws Exception {
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(WebAppLocators.PreferencesAccountPage.cssSelfUserName));
        return userName.getText();
    }

    public String getUserMail() {
        return userMail.getText();
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber.getText();
    }

    public void setUserName(String name) {
        userName.click();
        userNameInput.clear();
        userNameInput.sendKeys(name + "\n");
    }


}
