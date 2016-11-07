package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BasePendingIncomingConnectionPage extends BaseUserDetailsOverlay {
    public BasePendingIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(2000);
    }
}
