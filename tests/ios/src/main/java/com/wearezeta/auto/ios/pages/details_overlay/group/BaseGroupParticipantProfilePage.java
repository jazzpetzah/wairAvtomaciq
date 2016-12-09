package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;

import java.util.concurrent.Future;

public abstract class BaseGroupParticipantProfilePage extends BaseUserDetailsOverlay {

    public BaseGroupParticipantProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        super.switchToTab(tabName);
    }
}
