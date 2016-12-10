package com.wearezeta.auto.android.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.BaseConnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupConnectedUserDetailsOverlayPage extends BaseConnectedUserOverlay {
    private static final String idStrUserName = "tv__single_participant__toolbar__title";
    public GroupConnectedUserDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch(name.toLowerCase()) {
            case "open conversation":
                return super.getLeftActionButtonLocator();
            case "remove":
                return super.getRightActionButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }

    @Override
    protected String getUserNameId() {
        return idStrUserName;
    }
}
