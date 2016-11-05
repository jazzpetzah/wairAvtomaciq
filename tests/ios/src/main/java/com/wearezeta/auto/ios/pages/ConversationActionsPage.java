package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ConversationActionsPage extends IOSPage {
    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    public ConversationActionsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isActionMenuVisible() throws Exception {
        return isLocatorDisplayed(xpathActionsMenu);
    }
}
