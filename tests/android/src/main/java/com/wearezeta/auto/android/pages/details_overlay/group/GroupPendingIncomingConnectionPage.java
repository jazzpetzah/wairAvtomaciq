package com.wearezeta.auto.android.pages.details_overlay.group;


import com.wearezeta.auto.android.pages.details_overlay.BasePendingIncomingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupPendingIncomingConnectionPage extends BasePendingIncomingConnectionOverlay {
    public GroupPendingIncomingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "ignore":
                return super.getIgnoreButtonLocator();
            case "connect":
                return super.getAcceptButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }
}
