package com.wearezeta.auto.ios.pages.details_overlay.single;

import java.util.concurrent.Future;

import com.wearezeta.auto.ios.pages.details_overlay.BasePendingIncomingConnectionPage;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SinglePendingUserIncomingConnectionPage extends BasePendingIncomingConnectionPage {
    public SinglePendingUserIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "ignore":
                return xpathPendingRequestIgnoreButton;
            case "connect":
                return xpathPendingRequestConnectButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s' for Pending requests ",
                        name));
        }
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return SingleUserDetailsOverlay.nameLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return SingleUserDetailsOverlay.nameRightActionButton;
    }
}
