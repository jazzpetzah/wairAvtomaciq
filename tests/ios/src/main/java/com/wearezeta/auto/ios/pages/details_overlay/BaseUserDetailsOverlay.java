package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseUserDetailsOverlay extends BaseDetailsOverlay {
    protected static final By nameXButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    public BaseUserDetailsOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    protected void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
        // Wait for animation
        Thread.sleep(1000);
    }
}
