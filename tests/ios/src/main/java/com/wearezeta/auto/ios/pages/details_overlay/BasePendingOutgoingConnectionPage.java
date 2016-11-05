package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BasePendingOutgoingConnectionPage extends BaseUserDetailsOverlay {
    protected static final By xpathConnectOtherUserButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    protected static final By xpathCancelRequestButton =
            By.xpath("(//XCUIElementTypeButton[@label='CANCEL REQUEST'])[last()]");

    public BasePendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(2000);
    }
}
