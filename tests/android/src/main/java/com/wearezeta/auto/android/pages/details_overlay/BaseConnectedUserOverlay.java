package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseConnectedUserOverlay extends BaseUserDetailsOverlay implements ISupportsTabSwitching {
    private static final By idAvatar = By.id("iaiv__single_participant");

    public BaseConnectedUserOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        super.switchToTab(tabName);
    }

    @Override
    protected By getAvatarLocator() {
        return idAvatar;
    }
}
