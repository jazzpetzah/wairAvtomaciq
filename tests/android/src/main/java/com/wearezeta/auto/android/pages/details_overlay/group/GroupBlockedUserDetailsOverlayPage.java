package com.wearezeta.auto.android.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.BaseBlockedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupBlockedUserDetailsOverlayPage extends BaseBlockedUserOverlay {
    public GroupBlockedUserDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "blocked":
                return super.getLeftActionButtonLocator();
            case "remove":
                return super.getRightActionButtonLocator();
            case "unblock":
                return super.getAcceptButtonLocator();
            case "cancel":
                return super.getIgnoreButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }
}
