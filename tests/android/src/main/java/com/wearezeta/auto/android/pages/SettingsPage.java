package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

    private static final String xpathSettingsTitle = "//*[@id='action_bar_container' and .//*[@value='Settings']]";

    private static final Function<String, String> xpathSettingsMenuItemByText = text -> String
            .format("//*[@id='title' and @value='%s']", text);

    private static final Function<String, String> xpathConfirmBtnByName = name -> String
            .format("//*[starts-with(@id, 'button') and @value='%s']", name);

    public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathSettingsTitle));
    }

    public void selectMenuItem(String name) throws Exception {
        final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
        assert scrollUntilMenuElementVisible(locator, 5) : String
                .format("Menu item '%s' is not present", name);
        getDriver().findElement(locator).click();
    }

    public boolean isMenuItemVisible(String name) throws Exception {
        final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
        return scrollUntilMenuElementVisible(locator, 5);
    }

    public void confirmLogout() throws Exception {
        final By locator = By.xpath(xpathConfirmBtnByName.apply("Log out"));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                "Log out confirmation is not visible";
        getDriver().findElement(locator).click();
    }
}
