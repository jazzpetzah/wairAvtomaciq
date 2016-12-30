package com.wearezeta.auto.android_tablet.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.ISupportsCommonConnections;
import com.wearezeta.auto.android.pages.details_overlay.single.SingleUnconnectedUsersDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseUnconnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletSingleUnconnectedUsersDetailsOverlayPage extends TabletBaseUnconnectedUserOverlay
        implements ISupportsCommonConnections, ISupportsPopoverRelatedActions {
    public TabletSingleUnconnectedUsersDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    public boolean waitUntilPopoverVisible() throws Exception {
        return super.waitUntilPopoverVisible();
    }

    @Override
    public boolean waitUntilPopoverInvisible() throws Exception {
        return super.waitUntilPopoverInvisible();
    }

    @Override
    public void tapInTheCenterOfPopover() throws Exception {
        super.tapInTheCenterOfPopover();
    }

    @Override
    public void tapOutsideOfPopover() throws Exception {
        super.tapOutsideOfPopover();
    }

    @Override
    public void doShortSwipeDownOnPopover() throws Exception {
        super.doShortSwipeDownOnPopover();
    }

    @Override
    public boolean waitUntilCommonUserVisible(String userName) throws Exception {
        return this.getDedicatePage().waitUntilCommonUserVisible(userName);
    }

    @Override
    public boolean waitUntilCommonUserInvisible(String userName) throws Exception {
        return this.getDedicatePage().waitUntilCommonUserVisible(userName);
    }

    @Override
    protected SingleUnconnectedUsersDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(SingleUnconnectedUsersDetailsOverlayPage.class);
    }
}