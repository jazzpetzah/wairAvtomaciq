package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
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
    private static final By fbXpathDeclineActionButtonByName =
            FBBy.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    private static final Function<String, String> xpathStrConnectActionButtonByName = name ->
            String.format("//XCUIElementTypeButton[@name='IGNORE']/following::XCUIElementTypeButton[@name='%s']",
                    name.toUpperCase());
    private static final By fbXpathIgnoreActionButtonByName =
            FBBy.xpath("//XCUIElementTypeButton[@name='IGNORE']");

//    private static final By fbXpathYesActionButton =
//            FBBy.xpath("//XCUIElementTypeButton[@name='NO']/following::XCUIElementTypeButton[@name='YES']");
//    private static final By fbXpathNoActionButton =
//            FBBy.xpath("//XCUIElementTypeButton[@name='NO']");

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
        // Wait for animation
        Thread.sleep(2000);
        final WebElement btn = getElement(getActionButtonByName(buttonTitle));
        btn.click();
    }

    private static final long TRANSITION_DURATION_MS = 4500;

    public void confirmAction(String actionName) throws Exception {
        // Wait for animation
        Thread.sleep(TRANSITION_DURATION_MS);
        By locator = FBBy.xpath(xpathStrConfirmActionButtonByName.apply(actionName));
        switch (actionName.toLowerCase()) {
            case "connect":
                locator = FBBy.xpath(xpathStrConnectActionButtonByName.apply(actionName));
                break;
        }
        final FBElement btn = (FBElement) getElement(locator);
        btn.click();
        this.isElementInvisible(btn);
    }

    public boolean isVisibleForConversation(String conversation) throws Exception {
        final By locator = MobileBy.AccessibilityId(conversation.toUpperCase());
        return selectVisibleElements(locator).size() > 0;
    }

    public void declineAction(String actionName) throws Exception {
        // Wait for animation
        Thread.sleep(TRANSITION_DURATION_MS);
        By locator = fbXpathDeclineActionButtonByName;
        switch (actionName.toLowerCase()) {
            case "connect":
                locator = fbXpathIgnoreActionButtonByName;
                break;
        }
        final FBElement btn = (FBElement) getElement(locator);
        btn.click();
        this.isElementInvisible(btn);
    }
}
