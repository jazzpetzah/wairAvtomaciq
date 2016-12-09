package com.wearezeta.auto.android.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.BaseConnectedUserOverlay;
import com.wearezeta.auto.android.pages.details_overlay.ISupportsShieldIcon;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class SingleConnectedUserDetailsOverlayPage extends BaseConnectedUserOverlay implements ISupportsShieldIcon {
    private static final String idStrUserName = "ttv__participants__header";

    public SingleConnectedUserDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected String getUserNameId() {
        return idStrUserName;
    }

    @Override
    public boolean waitUntilShieldIconVisible() throws Exception {
        return super.waitUntilShieldIconVisible();
    }

    @Override
    public boolean waitUntilShieldIconInvisible() throws Exception {
        return super.waitUntilShieldIconInvisible();
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "create group":
                return super.getLeftActionButtonLocator();
            case "open menu":
                return super.getRightActionButtonLocator();
            case "x":
                return super.getCloseButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }
}
