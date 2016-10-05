package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletPendingUserPopoverPage extends OtherUserOnPendingProfilePage {
    public static final Function<String, String> xpathStrUserByName = name ->
            String.format("(//XCUIElementTypeStaticText[contains(@name, '%s')])[last()]", name);

    public static final By xpathConnectButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    public TabletPendingUserPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserByName.apply(name));
        return isElementDisplayed(locator);
    }

    public boolean isConnectButtonDisplayed() throws Exception {
        return isElementDisplayed(xpathConnectButton);
    }

}
