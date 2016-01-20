package com.wearezeta.auto.android_tablet.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegistrationFormPage extends AndroidTabletPage {
    public static final By idNameInput = By.id("ttv__signup__name");

    public static final By idEmailInput = By.id("ttv__signup__email");

    public static final By idPasswordInput = By.id("ttv__signup__password");

    public static final By idSubmitButton = By.id("pcb__signin__email");

    public TabletRegistrationFormPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void enterName(String name) throws Exception {
        final WebElement nameInput = getElement(idNameInput);
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void enterEmail(String email) throws Exception {
        final WebElement emailInput = getElement(idEmailInput);
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) throws Exception {
        final WebElement passwordInput = getElement(idPasswordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void tapSubmitButton() throws Exception {
        getElement(idSubmitButton).click();
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idEmailInput);
    }

}
