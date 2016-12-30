package com.wearezeta.auto.android_tablet.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.ISupportsShieldIcon;
import com.wearezeta.auto.android.pages.details_overlay.single.SingleConnectedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseConnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletSingleConnectedUserDetailsOverlayPage extends TabletBaseConnectedUserOverlay
        implements ISupportsShieldIcon, ISupportsPopoverRelatedActions {
    public TabletSingleConnectedUserDetailsOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
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
    public boolean waitUntilShieldIconInvisible() throws Exception {
        return getDedicatePage().waitUntilShieldIconInvisible();
    }

    @Override
    public boolean waitUntilShieldIconVisible() throws Exception {
        return getDedicatePage().waitUntilShieldIconVisible();
    }

    @Override
    public void doShortSwipeDownOnPopover() throws Exception {
        super.doShortSwipeDownOnPopover();
    }

    @Override
    protected SingleConnectedUserDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(SingleConnectedUserDetailsOverlayPage.class);
    }
}
