package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {
    private static final By nameConversationMenu = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By nameRenameButtonEllipsisMenu = MobileBy.AccessibilityId("RENAME");

    private static final String xpathStrPopover = "//UIAPopover[@visible='true']";
    private static final By xpathPopover = By.xpath(xpathStrPopover);

    private static final By xpathSilenceButtonEllipsisMenu = By.xpath(xpathStrPopover + "//UIAButton[@name='SILENCE']");

    private static final By xpathNotifyButtonEllipsisMenu = By.xpath(xpathStrPopover + "//UIAButton[@name='NOTIFY']");

    private static final Function<String, String> xpathStrPopoverParticipantByName = name ->
            String.format("%s//UIAStaticText[@name='%s']/parent::*", xpathStrPopover, name.toUpperCase());

    private static final Function<Integer, String> xpathStrGroupCountByNumber = number ->
            String.format("%s//UIAStaticText[contains(@name,'%s PEOPLE')]", xpathStrPopover, number);

    public TabletGroupConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void openConversationMenuOnPopover() throws Exception {
        getElement(nameConversationMenu).click();
    }

    public boolean waitConversationInfoPopoverToClose() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), xpathPopover);
    }

    public void dismissPopover() throws Exception {
        final WebElement popover = getElement(xpathPopover);
        for (int i = 0; i < 3; i++) {
            DriverUtils.tapOutsideOfTheElement(getDriver(), popover, 100, 0);
            if (waitConversationInfoPopoverToClose()) {
                // Wait for animation
                Thread.sleep(1000);
                return;
            }
        }
    }

    public void selectUserByNameOniPadPopover(String name) throws Exception {
        final By locator = By.xpath(xpathStrPopoverParticipantByName.apply(name));
        getElement(locator).click();
    }

    public void pressRenameEllipsesButton() throws Exception {
        getElement(nameRenameButtonEllipsisMenu).click();
    }

    public void exitGroupChatPopover() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(nameConversationMenu), 50, 50);
    }

    public boolean isNumberOfPeopleInGroupEqualToExpected(int expectedNumber) throws Exception {
        final By locator = By.xpath(xpathStrGroupCountByNumber.apply(expectedNumber));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void pressSilenceEllipsisButton() throws Exception {
        getElement(xpathSilenceButtonEllipsisMenu).click();
    }

    public void pressNotifyEllipsisButton() throws Exception {
        getElement(xpathNotifyButtonEllipsisMenu).click();
    }

}
