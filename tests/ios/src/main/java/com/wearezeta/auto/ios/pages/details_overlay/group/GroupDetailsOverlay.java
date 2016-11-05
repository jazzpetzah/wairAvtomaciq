package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class GroupDetailsOverlay extends BaseDetailsOverlay {
    public static final By namLeftActionButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    public static final By nameRightActionButton = MobileBy.AccessibilityId("metaControllerRightButton");

    public GroupDetailsOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getRightActionButtonLocator() {
        return nameRightActionButton;
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return namLeftActionButton;
    }
}
