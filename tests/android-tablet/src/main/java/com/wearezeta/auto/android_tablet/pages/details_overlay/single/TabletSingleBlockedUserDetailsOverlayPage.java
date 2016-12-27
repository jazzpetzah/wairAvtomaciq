package com.wearezeta.auto.android_tablet.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.single.SingleBlockedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseBlockedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletSingleBlockedUserDetailsOverlayPage extends TabletBaseBlockedUserOverlay implements ISupportsPopoverRelatedActions {
    public TabletSingleBlockedUserDetailsOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
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
    public void doShortSwipeDownOnPopover() throws Exception {
        super.doShortSwipeDownOnPopover();
    }

    @Override
    protected SingleBlockedUserDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(SingleBlockedUserDetailsOverlayPage.class);
    }
}
