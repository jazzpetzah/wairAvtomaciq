package com.wearezeta.auto.android.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.BaseBlockedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class SingleBlockedUserDetailsOverlayPage extends BaseBlockedUserOverlay {
    private static final By idUnblockButton = By.id("zb__connect_request__unblock_button");

    public SingleBlockedUserDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "unblock": {
                return idUnblockButton;
            }
        }
        return super.getButtonLocatorByName(name);
    }
}
