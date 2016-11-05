package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BasePendingOutgoingConnectionPage extends BaseUserDetailsOverlay {
    private static final By xpathConnectOtherUserButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    private static final By xpathCancelRequestButton =
            By.xpath("(//XCUIElementTypeButton[@label='CANCEL REQUEST'])[last()]");

    public BasePendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "connect":
                return xpathConnectOtherUserButton;
            case "cancel request":
                return xpathCancelRequestButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(2000);
    }
}
