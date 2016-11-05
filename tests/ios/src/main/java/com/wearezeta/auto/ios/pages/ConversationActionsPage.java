package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ConversationActionsPage extends IOSPage {
    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    private static final Function<String,String> xpathStrConfirmButtonByName = name ->
            String.format("//XCUIElementTypeButton[@name='CANCEL']/following::XCUIElementTypeButton[@name='%s']",
                    name.toUpperCase());

    public ConversationActionsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isMenuVisible() throws Exception {
        return isLocatorDisplayed(xpathActionsMenu);
    }

    private static By getActionButtonByName(String buttonTitle) {
        return MobileBy.AccessibilityId(buttonTitle.toUpperCase());
    }

    public boolean isItemVisible(String buttonTitle) throws Exception {
        return isLocatorDisplayed(getActionButtonByName(buttonTitle));
    }

    public void tapMenuItem(String buttonTitle) throws Exception {
        getElement(getActionButtonByName(buttonTitle)).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public void confirmAction(String actionName) throws Exception {
        final By locator = By.xpath(xpathStrConfirmButtonByName.apply(actionName));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(2000);
    }
}
