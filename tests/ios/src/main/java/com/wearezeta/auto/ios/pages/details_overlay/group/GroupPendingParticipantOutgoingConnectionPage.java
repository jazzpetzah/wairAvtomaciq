package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BasePendingOutgoingConnectionPage;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupPendingParticipantOutgoingConnectionPage extends BasePendingOutgoingConnectionPage {
    public GroupPendingParticipantOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "start conversation":
            case "open conversation":
                return getLeftActionButtonLocator();
            case "connect":
                return xpathConnectOtherUserButton;
            case "cancel request":
                return xpathCancelRequestButton;
            case "x":
                return nameXButton;
            case "remove from group":
                return getRightActionButtonLocator();
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }
}
