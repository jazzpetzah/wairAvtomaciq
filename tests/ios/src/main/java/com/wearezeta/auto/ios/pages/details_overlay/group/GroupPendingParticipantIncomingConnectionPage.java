package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BasePendingIncomingConnectionPage;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupPendingParticipantIncomingConnectionPage extends BasePendingIncomingConnectionPage {
    public GroupPendingParticipantIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "start conversation":
            case "open conversation":
                return getLeftActionButtonLocator();
            case "ignore":
                return xpathPendingRequestIgnoreButton;
            case "connect":
                return xpathPendingRequestConnectButton;
            case "x":
                return nameXButton;
            case "remove from group":
                return getRightActionButtonLocator();
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s' for Pending requests ",
                        name));
        }
    }
}
