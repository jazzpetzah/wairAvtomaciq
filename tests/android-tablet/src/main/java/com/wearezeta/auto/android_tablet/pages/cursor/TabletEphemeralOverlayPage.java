package com.wearezeta.auto.android_tablet.pages.cursor;


import com.wearezeta.auto.android.pages.cursor.EphemeralOverlayPage;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletEphemeralOverlayPage extends AndroidTabletPage {

    public TabletEphemeralOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilTimerVisible(String expectedTimerValue) throws Exception {
        return getEphemeralOverlayPage().waitUntilTimerVisible(expectedTimerValue);
    }

    public void setTimeout(String timeoutStr) throws Exception {
        getEphemeralOverlayPage().setTimeout(timeoutStr);
    }

    private EphemeralOverlayPage getEphemeralOverlayPage() throws Exception {
        return this.getAndroidPageInstance(EphemeralOverlayPage.class);
    }
}
