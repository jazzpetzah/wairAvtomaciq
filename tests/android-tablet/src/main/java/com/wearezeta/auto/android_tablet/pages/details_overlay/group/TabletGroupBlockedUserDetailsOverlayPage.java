package com.wearezeta.auto.android_tablet.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupBlockedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseBlockedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupBlockedUserDetailsOverlayPage extends TabletBaseBlockedUserOverlay {

    public TabletGroupBlockedUserDetailsOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected GroupBlockedUserDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupBlockedUserDetailsOverlayPage.class);
    }
}
