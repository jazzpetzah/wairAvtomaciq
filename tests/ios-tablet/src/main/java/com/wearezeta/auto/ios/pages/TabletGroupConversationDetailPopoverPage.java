package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {
    private static final By nameConversationMenu = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final Function<String, String> xpathStrPopoverParticipantByName = name ->
            String.format("(//XCUIElementTypeTableView)[last()]//XCUIElementTypeStaticText[@name='%s']" +
                    "/parent::*[1]", name.toUpperCase());

    private static final Function<Integer, String> xpathStrGroupCountByNumber = number ->
            String.format("(//XCUIElementTypeTableView)[last()]//UIAStaticText[contains(@name,'%s PEOPLE')]",
                    number);

    private static final By namePopoverDismissRegion = MobileBy.AccessibilityId("PopoverDismissRegion");

    public TabletGroupConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void openConversationMenuOnPopover() throws Exception {
        getElement(nameConversationMenu).click();
    }

    public void dismissPopover() throws Exception {
        getElement(namePopoverDismissRegion).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public void selectUserByNameOniPadPopover(String name) throws Exception {
        final By locator = By.xpath(xpathStrPopoverParticipantByName.apply(name));
        getElement(locator).click();
    }

    public boolean isNumberOfPeopleInGroupEqualToExpected(int expectedNumber) throws Exception {
        final By locator = By.xpath(xpathStrGroupCountByNumber.apply(expectedNumber));
        return isElementDisplayed(locator);
    }

    public void selectEllipsisMenuAction(String actionName) throws Exception {
        getElement(MobileBy.AccessibilityId(actionName.toUpperCase())).click();
    }
}
