package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletGroupConversationInfoPopoverPage extends GroupInfoPage {
    private static final Function<String, String> xpathStrPopoverParticipantByName = name ->
            String.format("//XCUIElementTypeTextView[@name='ParticipantsView_GroupName']" +
                    "/following::XCUIElementTypeCollectionView[1]/" +
                    "XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]", name.toUpperCase());

    private static final Function<Integer, String> xpathStrGroupCountByNumber = number ->
            String.format("//XCUIElementTypeStaticText[contains(@name,'%s PEOPLE')]", number);

    private static final By fbNamePopoverDismissRegion = FBBy.AccessibilityId("PopoverDismissRegion");

    public TabletGroupConversationInfoPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void dismissPopover() throws Exception {
        this.tapByPercentOfElementSize((FBElement) getElement(fbNamePopoverDismissRegion), 10, 10);
        // Wait for animation
        Thread.sleep(1000);
    }

    public void selectUserByNameOniPadPopover(String name) throws Exception {
        final By locator = By.xpath(xpathStrPopoverParticipantByName.apply(name));
        getElement(locator).click();
    }

    public boolean isNumberOfPeopleInGroupEqualToExpected(int expectedNumber) throws Exception {
        final By locator = By.xpath(xpathStrGroupCountByNumber.apply(expectedNumber));
        return isLocatorDisplayed(locator);
    }
}
