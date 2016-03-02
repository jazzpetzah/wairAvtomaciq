package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {
    private static final By nameConversationMenu = By.name("metaControllerRightButton");

    private static final By nameRenameButtonEllipsisMenu = By.name("RENAME");

    private static final By xpathSilenceButtonEllipsisMenu = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIAButton[@name='SILENCE']");

    private static final By xpathNotifyButtonEllipsisMenu = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIAButton[@name='NOTIFY']");

    private static final Function<Integer, String> xpathStrGroupCountByNumber = number ->
            String.format("//UIAPopover//UIAStaticText[contains(@name,'%s PEOPLE')]", number);

    private static final By xpathPopover = By.xpath("//UIAPopover[@visible='true']");

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
        for (int i = 0; i < 3; i++) {
            DriverUtils.tapOutsideOfTheElement(getDriver(), getElement(xpathPopover), 100, 0);
            if (waitConversationInfoPopoverToClose()) {
                // Wait for animation
                Thread.sleep(1000);
                return;
            }
        }
    }

    public void selectUserByNameOniPadPopover(String name) throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), getElement(By.name(name.toUpperCase())));
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
