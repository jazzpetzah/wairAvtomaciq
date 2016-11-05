package com.wearezeta.auto.ios.pages.details_overlay.single;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseDevicesOverlay;

import java.util.concurrent.Future;

public class SingleConnectedUserDevicesPage extends BaseDevicesOverlay {
    public SingleConnectedUserDevicesPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }
}
