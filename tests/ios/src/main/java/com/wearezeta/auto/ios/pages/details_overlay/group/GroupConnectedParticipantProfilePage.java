package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.ISupportsTabSwitching;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupConnectedParticipantProfilePage extends BaseGroupParticipantProfilePage
        implements ISupportsTabSwitching {

    public GroupConnectedParticipantProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        super.switchToTab(tabName);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "create group":
            case "open conversation":
                return getLeftActionButtonLocator();
            case "remove from conversation":
            case "open menu":
                return getRightActionButtonLocator();
            case "confirm removal":
                return xpathConfirmRemoveButton;
            case "confirm deletion":
                return xpathConfirmDeleteButton;
            case "x":
                return nameXButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(1000);
    }
}
