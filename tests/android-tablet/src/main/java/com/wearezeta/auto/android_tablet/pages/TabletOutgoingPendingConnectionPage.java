package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.details_overlay.single.SinglePendingOutgoingConnectionPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class TabletOutgoingPendingConnectionPage extends AndroidTabletPage {

    public TabletOutgoingPendingConnectionPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private SinglePendingOutgoingConnectionPage getAndroidOPCPage() throws Exception {
        return this.getAndroidPageInstance(SinglePendingOutgoingConnectionPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getAndroidOPCPage().waitUntilPageVisible();
    }

    public boolean waitUntilNameVisible(ClientUser client) throws Exception {
        return getAndroidOPCPage().waitUntilUserDataVisible("user name", client);
    }
}
