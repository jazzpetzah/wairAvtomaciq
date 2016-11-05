package com.wearezeta.auto.ios.pages.details_overlay.single;

import java.util.concurrent.Future;

import com.wearezeta.auto.ios.pages.details_overlay.ICanContainVerificationShield;
import com.wearezeta.auto.ios.pages.details_overlay.ISupportsTabSwitching;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SingleConnectedUserProfilePage extends SingleUserDetailsOverlay
        implements ICanContainVerificationShield, ISupportsTabSwitching {
    public SingleConnectedUserProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
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
                return getLeftActionButtonLocator();
            case "open menu":
                return getRightActionButtonLocator();
            case "x":
                return nameXButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        Thread.sleep(1000);
    }

    @Override
    public boolean isShieldIconVisible() throws Exception {
        return super.isShieldIconVisible();
    }

    @Override
    public boolean isShieldIconNotVisible() throws Exception {
        return super.isShieldIconNotVisible();
    }
}
