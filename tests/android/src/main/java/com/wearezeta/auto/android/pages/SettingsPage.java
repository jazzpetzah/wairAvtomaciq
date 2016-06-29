package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;
import org.openqa.selenium.WebElement;

public class SettingsPage extends AndroidPage {

    private static final String CURRENT_DEVICE = "Current Device";

    private static final By xpathSettingsTitle = By.xpath("//*[@id='toolbar' and .//*[@value='Settings']]");

    private static final Function<String, String> xpathStrSettingsMenuItemByText = text -> String
            .format("//*[@id='title' and @value='%s']", text);

    private static final Function<String, String> xpathStrConfirmBtnByName = name -> String
            .format("//*[starts-with(@id, 'button') and @value='%s']", name);

    private static final By xpathCurrentDevice = By.xpath(xpathStrSettingsMenuItemByText.apply(CURRENT_DEVICE)
            + "/following::*[@id='title']");

    private static final By idPasswordConfirmationInput = By.id("acet__remove_otr__password");

    private static final By xpathConfirmationInputOKButton = By.xpath("//*[starts-with(@id, 'button') and @value='OK']");

    private static final By idNameEdit = By.id("edit");

    private static final By xpathOKButton = By.xpath("//*[starts-with(@id, 'button') and @value='OK']");

    private static final By idEmailEdit = By.id("acet__preferences__email");

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

    public void enterConfirmationPassword(String password) throws Exception {
        final WebElement confirmationPasswordInput = getElement(idPasswordConfirmationInput);
        confirmationPasswordInput.click();
        confirmationPasswordInput.sendKeys(password);
    }

    public void tapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getElement(xpathConfirmationInputOKButton).click();
    }

    public void tapCurrentDevice() throws Exception {
        getElement(xpathCurrentDevice).click();
    }

    public boolean waitUntilMenuItemVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrSettingsMenuItemByText.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilMenuItemInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrSettingsMenuItemByText.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void commitNewName(String newName) throws Exception {
        final WebElement nameEdit = getElement(idNameEdit);
        nameEdit.clear();
        nameEdit.sendKeys(newName);
        getElement(xpathOKButton).click();
    }

    public void commitNewEmail(String newValue) throws Exception {
        final WebElement emailEdit = getElement(idEmailEdit);
        emailEdit.clear();
        emailEdit.sendKeys(newValue);
        getElement(xpathOKButton).click();
    }

    public void commitNewPhoneNumber(PhoneNumber phoneNumber) throws Exception {
        // TODO: Implement phone number input
        getElement(xpathOKButton).click();
    }
}
