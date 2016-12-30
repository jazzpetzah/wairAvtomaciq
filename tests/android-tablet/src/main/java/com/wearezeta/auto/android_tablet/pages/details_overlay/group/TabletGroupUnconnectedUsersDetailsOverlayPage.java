package com.wearezeta.auto.android_tablet.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupUnconnectedUsersDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseUnconnectedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupUnconnectedUsersDetailsOverlayPage extends TabletBaseUnconnectedUserOverlay {
    public TabletGroupUnconnectedUsersDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected GroupUnconnectedUsersDetailsOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupUnconnectedUsersDetailsOverlayPage.class);
    }
}
