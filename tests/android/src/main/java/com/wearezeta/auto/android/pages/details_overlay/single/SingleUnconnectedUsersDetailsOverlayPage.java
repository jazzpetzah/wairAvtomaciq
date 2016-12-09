package com.wearezeta.auto.android.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.BaseUnconnectedUserOverlay;
import com.wearezeta.auto.android.pages.details_overlay.ISupportsCommonConnections;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class SingleUnconnectedUsersDetailsOverlayPage extends BaseUnconnectedUserOverlay implements ISupportsCommonConnections {
    public SingleUnconnectedUsersDetailsOverlayPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }
}