package com.wearezeta.auto.android.pages.details_overlay;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BasePendingOutgoingConnectionOverlay extends BasePendingConnectionOverlay {
    private static final By idPendingPageRoot = By.id("fl__pending_connect_request");
    public BasePendingOutgoingConnectionOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public boolean waitUntilPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPendingPageRoot);
    }
}
