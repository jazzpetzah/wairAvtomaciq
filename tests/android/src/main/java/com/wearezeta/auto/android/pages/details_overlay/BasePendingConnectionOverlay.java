package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BasePendingConnectionOverlay extends BaseUserDetailsOverlay {
    private static final String idStrUserName = "tv__pending_connect_toolbar__title";
    private static final By idAvatar = By.id("iaiv__pending_connect");

    public BasePendingConnectionOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    protected By getAvatarLocator() {
        return idAvatar;
    }

    protected String getUserNameId() {
        return idStrUserName;
    }
}
