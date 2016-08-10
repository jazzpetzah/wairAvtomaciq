package com.wearezeta.auto.android.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
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

    private static final By idNameEdit = By.id("edit");

    private static final By xpathOKButton = By.xpath("//*[starts-with(@id, 'button') and @value='OK']");

    private static final By idEmailEdit = By.id("acet__preferences__email");

    private static final By idCountryIdxInput = By.id("acet__preferences__country");

    private static final By idPhoneNumberInput = By.id("acet__preferences__phone");

    private static final By idEmailPasswordInput = By.id("acet__preferences__password");

    // index starts from 1
    private static final Function<Integer, String> idStrVerificationCodeDigitInput = idx ->
            String.format("et__verification_code__%s", idx);

    private static final int SCREEN_HEIGHT_THRESHOLD = 10;

    public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private boolean scrollUntilMenuElementVisible(By locator, int maxScrolls) throws Exception {
        int nScrolls = 0;
        int screenHeight = AndroidCommonUtils.getScreenSize(getDriver()).getHeight();

        while (nScrolls < maxScrolls) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                WebElement menuElement = getElement(locator);
                if (menuElement.getLocation().getY() + menuElement.getSize().getHeight()
                        <= screenHeight + SCREEN_HEIGHT_THRESHOLD) {
                    return true;
                }
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
        Thread.sleep(3000);
        confirmationPasswordInput.sendKeys(password);
    }

    public void tapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getElement(xpathOKButton).click();
        // TODO: How to defect that element has changed his location?
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOKButton, 5)) {
            DriverUtils.tapByCoordinatesWithPercentOffcet(getDriver(), getElement(xpathOKButton), 50, -200);
        }
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
        commitNewEmailWithPassword(newValue, Optional.empty());
    }

    public void commitNewEmailWithPassword(String newValue, Optional<String> password) throws Exception {
        final WebElement emailEdit = getElement(idEmailEdit);
        emailEdit.clear();
        emailEdit.sendKeys(newValue);

        if(password.isPresent()) {
            getElement(idEmailPasswordInput, "The password input is not visible").sendKeys(password.get());
        }
        getElement(xpathOKButton).click();
    }

    public void commitNewPhoneNumber(PhoneNumber phoneNumber) throws Exception {
        final WebElement countryIdxInput = getElement(idCountryIdxInput);
        countryIdxInput.clear();
        countryIdxInput.sendKeys(phoneNumber.getPrefix());
        final WebElement phoneNumberInput = getElement(idPhoneNumberInput);
        phoneNumberInput.clear();
        phoneNumberInput.sendKeys(phoneNumber.withoutPrefix());
        this.pressKeyboardSendButton();
        Thread.sleep(1000);
        getElement(xpathOKButton).click();
        // Confirm the alert
        Thread.sleep(1000);
        getElementIfDisplayed(xpathOKButton, 3).orElseGet(DummyElement::new).click();
    }

    public void commitPhoneNumberVerificationCode(String activationCode) throws Exception {
        for (int charIdx = 0; charIdx < activationCode.length(); charIdx++) {
            final By locator = By.id(idStrVerificationCodeDigitInput.apply(charIdx + 1));
            getElement(locator).sendKeys(activationCode.substring(charIdx, charIdx + 1));
        }
        this.pressKeyboardSendButton();
        Thread.sleep(1000);
        this.pressKeyboardSendButton();
    }
}
