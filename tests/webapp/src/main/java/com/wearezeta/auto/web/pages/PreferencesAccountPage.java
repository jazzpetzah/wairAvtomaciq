package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PreferencesAccountPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(PreferencesAccountPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.PreferencesAccountPage.cssPreferencesAccountLogoutButton)
    private WebElement logoutButton;
    
    @FindBy(css = "[data-uie-name='go-logout']")
    private WebElement logoutInDialogButton;

    @FindBy(css = ".modal-logout .checkbox span")
    private WebElement clearDataCheckbox;
    

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


}
