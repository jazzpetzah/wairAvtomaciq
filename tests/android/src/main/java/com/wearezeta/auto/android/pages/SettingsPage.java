package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

    private static final String xpathSettingsTitle = "//*[@id='action_bar_container' and .//*[@value='Settings']]";

    private static final Function<String, String> xpathSettingsMenuItemByText = text -> String
            .format("//*[@id='title' and @value='%s']", text);

    private static final Function<String, String> xpathSettingsMenuItemByPartOfText = text -> String
            .format("//*[@id='title' and contains(@value, '%s')]", text);

    private static final String xpathThemeSwitch = String
            .format("%s/parent::*/parent::*//*[@id='switchWidget']", xpathSettingsMenuItemByPartOfText.apply("Theme"));

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

    public void confirmSignOut() throws Exception {
        final By locator = By.xpath(xpathConfirmBtnByName.apply("Log out"));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                "Sign out confirmation is not visible";
        getDriver().findElement(locator).click();
    }

    public Optional<BufferedImage> getThemeSwitcherState() throws Exception {
        final By itemLocator = By.xpath(xpathSettingsMenuItemByPartOfText.apply(" Theme"));
        assert scrollUntilMenuElementVisible(itemLocator, 5) : "Theme menu item is not visible";
        return this.getElementScreenshot(getDriver().findElement(By.xpath(xpathThemeSwitch)));
    }

    public void switchTheme() throws Exception {
        final By switchLocator = By.xpath(xpathThemeSwitch);
        assert scrollUntilMenuElementVisible(By.xpath(xpathSettingsMenuItemByPartOfText.apply(" Theme")), 5)
                : "Theme menu item is not visible";
        getDriver().findElement(switchLocator).click();
    }
}
