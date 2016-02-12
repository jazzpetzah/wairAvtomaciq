package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AlertPage extends AndroidPage {

    private static final By idAlertPage = By.id("cm__confirm_action_light");
    private static final By idPositive = By.id("positive");
    private static final By idNegative = By.id("negative");
    private static final By idCancel = By.id("cancel");
    private static final By idHeader = By.id("header");
    private static final By idText = By.id("text");

    public AlertPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idAlertPage);
    }
    
    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idAlertPage);
    }

    public void tapPositive() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPositive)) {
            throw new Exception("Positive button is not present");
        }
        getElement(idPositive).click();
    }

    public void tapNegative() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idNegative)) {
            throw new Exception("Negative button is not present");
        }
        getElement(idNegative).click();
    }
    
    public void tapCancel() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCancel)) {
            throw new Exception("Cancel button is not present");
        }
        getElement(idCancel).click();
    }
    
    public String getHeaderText() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHeader)) {
            throw new Exception("Header is not present");
        }
        return getElement(idHeader).getText();
    }
    
    public String getText() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idText)) {
            throw new Exception("Text is not present");
        }
        return getElement(idText).getText();
    }

}
