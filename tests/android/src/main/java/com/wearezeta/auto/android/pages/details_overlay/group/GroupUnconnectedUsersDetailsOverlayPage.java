package com.wearezeta.auto.android.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.BaseUnconnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupUnconnectedUsersDetailsOverlayPage extends BaseUnconnectedUserOverlay {
    public GroupUnconnectedUsersDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch(name.toLowerCase()) {
            case "+connect":
                return super.getLeftActionButtonLocator();
            case "-remove":
                return super.getRightActionButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }
}
