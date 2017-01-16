package com.wearezeta.auto.android_tablet.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupConnectedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseConnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupConnectedUserDetailsOverlayPage extends TabletBaseConnectedUserOverlay {
    public TabletGroupConnectedUserDetailsOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected GroupConnectedUserDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupConnectedUserDetailsOverlayPage.class);
    }
}
