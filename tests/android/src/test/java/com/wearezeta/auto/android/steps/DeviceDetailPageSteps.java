package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DeviceDetailPage;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.junit.Assert;


import cucumber.api.java.en.And;
import org.apache.log4j.Logger;

public class DeviceDetailPageSteps {

    private static final Logger log = ZetaLogger.getLog(DeviceDetailPageSteps.class.getSimpleName());

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private String rememberedDeviceId;

    private DeviceDetailPage getPage() throws Exception {
        return pagesCollection.getPage(DeviceDetailPage.class);
    }

    /**
     * I remember the presented device id in device detail view
     *
     * @throws Exception
     * @step. ^I tap current device in devices settings menu$
     */
    @And("^I remember the device id shown in the device detail view$")
    public void IRememberTheDeviceId() throws Exception {
        rememberedDeviceId = getPage().getId();
        log.info("Remembered device ID = " + rememberedDeviceId);
    }

    /**
     * Verifies equality of remembered ID and currently shown in device detail
     * view
     *
     * @throws Exception
     * @step. ^I verify the remembered device id is shown in the device detail
     * view$
     */
    @And("^I verify the remembered device id is shown in the device detail view$")
    public void IVerifyRememberedDeviceIdIsShown() throws Exception {
        String actualDeviceId = getPage().getId();
        Assert.assertEquals("Actual ID " + actualDeviceId + " is not equal to " + rememberedDeviceId, rememberedDeviceId, actualDeviceId);
    }
}
