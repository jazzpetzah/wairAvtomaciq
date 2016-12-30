package com.wearezeta.auto.android_tablet.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.single.SinglePendingOutgoingConnectionPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBasePendingOutgoingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletSinglePendingOutgoingConnectionPage extends TabletBasePendingOutgoingConnectionOverlay {
    public TabletSinglePendingOutgoingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected SinglePendingOutgoingConnectionPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(SinglePendingOutgoingConnectionPage.class);
    }
}
