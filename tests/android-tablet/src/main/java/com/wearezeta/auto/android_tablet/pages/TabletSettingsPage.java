package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.SettingsPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class TabletSettingsPage extends AndroidTabletPage {

    public TabletSettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private SettingsPage getSettingsPage() throws Exception {
        return this.getAndroidPageInstance(SettingsPage.class);
    }

    private boolean scrollUntilMenuElementVisible(By locator, int maxScrolls) throws Exception {
        int nScrolls = 0;
        while (nScrolls < maxScrolls) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return true;
            }
            this.swipeUpCoordinates(500, 50);
            nScrolls++;
        }
        return false;
    }

    public boolean waitUntilVisible() throws Exception {
        return getSettingsPage().waitUntilVisible();
    }

    public void selectMenuItem(String name) throws Exception {
        getSettingsPage().selectMenuItem(name);
    }

    public void confirmLogout() throws Exception {
        getSettingsPage().confirmLogout();
    }

    public void enterConfirmationPassword(String password) throws Exception {
        getSettingsPage().enterConfirmationPassword(password);
    }

    public void tapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getSettingsPage().tapOKButtonOnPasswordConfirmationDialog();
    }

    public void tapCurrentDevice() throws Exception {
        getSettingsPage().tapCurrentDevice();
    }

    public boolean waitUntilMenuItemVisible(String name) throws Exception {
        return getSettingsPage().waitUntilMenuItemVisible(name);
    }

    public boolean waitUntilMenuItemInvisible(String name) throws Exception {
        return  getSettingsPage().waitUntilMenuItemInvisible(name);
    }

    public void commitNewName(String newName) throws Exception {
        getSettingsPage().commitNewName(newName);
    }

    public void commitNewEmail(String newValue) throws Exception {
        getSettingsPage().commitNewEmail(newValue);
    }
}
