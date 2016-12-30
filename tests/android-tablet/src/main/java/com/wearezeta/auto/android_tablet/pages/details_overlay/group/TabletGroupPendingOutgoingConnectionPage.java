package com.wearezeta.auto.android_tablet.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupPendingOutgoingConnectionPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBasePendingOutgoingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupPendingOutgoingConnectionPage extends TabletBasePendingOutgoingConnectionOverlay {
    public TabletGroupPendingOutgoingConnectionPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected GroupPendingOutgoingConnectionPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupPendingOutgoingConnectionPage.class);
    }
}
