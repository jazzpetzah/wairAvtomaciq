package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseBlockedUserOverlay extends BaseUserDetailsOverlay {
    private final static String idStrUserName = "tv__blocked_user__toolbar__tile";
    private final static By idAvatar = By.id("iaiv__blocked_user");

    public BaseBlockedUserOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    protected By getAvatarLocator(){
        return idAvatar;
    }

    protected String getUserNameId() {
        return idStrUserName;
    }
}
