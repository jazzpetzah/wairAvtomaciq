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
    protected By getLeftActionButtonLocator() {
        return GroupDetailsOverlay.namLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return GroupDetailsOverlay.nameRightActionButton;
    }
}
