package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;
import org.openqa.selenium.WebElement;

public class SettingsPage extends AndroidPage {

    private static final By xpathSettingsTitle = By.xpath("//*[@id='action_bar_container' and .//*[@value='Settings']]");

    private static final Function<String, String> xpathStrSettingsMenuItemByText = text -> String
            .format("//*[@id='title' and @value='%s']", text);

    private static final Function<String, String> xpathStrConfirmBtnByName = name -> String
            .format("//*[starts-with(@id, 'button') and @value='%s']", name);

    private static final By idPasswordConfirmationInput = By.id("acet__remove_otr__password");

    private static final By xpathConfirmationInputOKButton = By.xpath("//*[starts-with(@id, 'button') and @value='OK']");

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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsTitle);
    }

    public void selectMenuItem(String name) throws Exception {
        final By locator = By.xpath(xpathStrSettingsMenuItemByText.apply(name));
        assert scrollUntilMenuElementVisible(locator, 5) : String
                .format("Menu item '%s' is not present", name);
        getElement(locator).click();
    }

    public void confirmLogout() throws Exception {
        final By locator = By.xpath(xpathStrConfirmBtnByName.apply("Log out"));
        getElement(locator, "Log out confirmation is not visible").click();
    }

    public boolean waitUntilPasswordConfirmationIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPasswordConfirmationInput);
    }

    public void enterConfirmationPassword(String password) throws Exception {
        final WebElement confirmationPasswordInput = getElement(idPasswordConfirmationInput);
        confirmationPasswordInput.click();
        confirmationPasswordInput.sendKeys(password);
    }

    public void tapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getElement(xpathConfirmationInputOKButton).click();
    }

    public boolean waitUntilMenuItemVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrSettingsMenuItemByText.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilMenuItemInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrSettingsMenuItemByText.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
}
