package com.wearezeta.auto.ios.pages.details_overlay.single;

import java.util.concurrent.Future;

import com.wearezeta.auto.ios.pages.details_overlay.BasePendingOutgoingConnectionPage;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SinglePendingUserOutgoingConnectionPage extends BasePendingOutgoingConnectionPage {
    public SinglePendingUserOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "connect":
                return xpathConnectOtherUserButton;
            case "cancel request":
                return xpathCancelRequestButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
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
