package com.wearezeta.auto.ios.pages.details_overlay.single;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class SingleUserDetailsOverlay extends BaseUserDetailsOverlay {
    public SingleUserDetailsOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
}
