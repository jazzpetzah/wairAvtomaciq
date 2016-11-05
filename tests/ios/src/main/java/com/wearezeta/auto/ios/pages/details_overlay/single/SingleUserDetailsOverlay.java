package com.wearezeta.auto.ios.pages.details_overlay.single;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class SingleUserDetailsOverlay extends BaseUserDetailsOverlay {
    public static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    public static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    public SingleUserDetailsOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return nameLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return nameRightActionButton;
    }
}
