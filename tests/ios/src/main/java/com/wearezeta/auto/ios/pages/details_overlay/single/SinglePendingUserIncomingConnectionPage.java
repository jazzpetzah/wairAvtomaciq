package com.wearezeta.auto.ios.pages.details_overlay.single;

import java.util.concurrent.Future;

import com.wearezeta.auto.ios.pages.details_overlay.BasePendingOutgoingConnectionPage;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SinglePendingUserIncomingConnectionPage extends BasePendingOutgoingConnectionPage {
    public SinglePendingUserIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
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
