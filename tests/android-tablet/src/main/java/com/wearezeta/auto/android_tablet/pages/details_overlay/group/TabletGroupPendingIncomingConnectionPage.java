package com.wearezeta.auto.android_tablet.pages.details_overlay.group;


import com.wearezeta.auto.android.pages.details_overlay.group.GroupPendingIncomingConnectionPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBasePendingIncomingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupPendingIncomingConnectionPage extends TabletBasePendingIncomingConnectionOverlay {
    public TabletGroupPendingIncomingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected GroupPendingIncomingConnectionPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupPendingIncomingConnectionPage.class);
    }
}
