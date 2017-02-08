package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PreferencesPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(PreferencesPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.PreferencesPage.cssPreferencesCloseButton)
    private WebElement preferencesCloseButton;
    
    @FindBy(css = WebAppLocators.PreferencesPage.cssPreferencesAccountButton)
    private WebElement preferencesAccountButton;
    
    @FindBy(css = WebAppLocators.PreferencesPage.cssPreferencesDevicesButton)
    private WebElement preferencesDevicesButton;
    
    @FindBy(css = WebAppLocators.PreferencesPage.cssPreferencesOptionsButton)
    private WebElement preferencesOptionsButton;
    
    @FindBy(css = WebAppLocators.PreferencesPage.cssPreferencesAboutButton)
    private WebElement preferencesAboutButton;

    public PreferencesPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void closePreferences() throws Exception {
        preferencesCloseButton.click();
    }
    
    public void openAccount() throws Exception {
        preferencesAccountButton.click();
    }
    
    public void openDevices() throws Exception {
        preferencesDevicesButton.click();
    }
    
    public void openOptions() throws Exception {
        preferencesOptionsButton.click();
    }
    
    public void openAbout() throws Exception {
        preferencesAboutButton.click();
    }

    public boolean isDevicesOpened() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.DevicesPage.cssCurrentDeviceId);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

}
