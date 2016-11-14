package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ConversationActionsPage extends IOSPage {
    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    private static final Function<String, String> xpathStrConfirmActionButtonByName = name ->
            String.format("//XCUIElementTypeButton[@name='CANCEL']/following::XCUIElementTypeButton[@name='%s']",
                    name.toUpperCase());

    private static final Function<String, String> xpathStrConnectActionButtonByName = name ->
            String.format("//XCUIElementTypeButton[@name='IGNORE']/following::XCUIElementTypeButton[@name='%s']",
                    name.toUpperCase());

    private static final By xpathYesActionButton =
            By.xpath("//XCUIElementTypeButton[@name='NO']/following::XCUIElementTypeButton[@name='YES']");

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
        final WebElement btn = getElement(getActionButtonByName(buttonTitle));
        btn.click();
        isElementInvisible(btn);
    }

    public void confirmAction(String actionName) throws Exception {
        By locator = By.xpath(xpathStrConfirmActionButtonByName.apply(actionName));
        switch (actionName.toLowerCase()) {
            case "cancel request":
                locator = xpathYesActionButton;
                break;
            case "connect":
                locator = By.xpath(xpathStrConnectActionButtonByName.apply(actionName));
                break;
        }
        final WebElement btn = getElement(locator);
        btn.click();
        isElementInvisible(btn);
    }

    public boolean isVisibleForConversation(String conversation) throws Exception {
        final By locator = MobileBy.AccessibilityId(conversation.toUpperCase());
        return isLocatorDisplayed(locator);
    }
}
