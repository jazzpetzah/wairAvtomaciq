package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DeviceDetailPage;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;


import cucumber.api.java.en.And;
import java.util.function.Function;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeviceDetailPageSteps {

    private static final Logger log = ZetaLogger.getLog(DeviceDetailPageSteps.class.getSimpleName());

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    
    private static final Function<String,String> NON_ENCRYPTED_DEVICE_HEADER_TEXT = username -> String.format("%s is using an old version of Wire. No devices are shown here.", username);
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
    
    /**
     * Verifies no encrypted device text for given user in header of device detail page
     *
     * @throws Exception
     * @step. ^I see no encrypted device text for user (.*) in header of device detail page$
     */
    @And("^I see no encrypted device text for user (.*) in header of device detail page$")
    public void ISeeNoEncryptedDeviceTextInHeaderForUser(String username) throws Exception {
        username = usrMgr.findUserByNameOrNameAlias(username).getName();
        String expected = NON_ENCRYPTED_DEVICE_HEADER_TEXT.apply(username);
        assertTrue(String.format("No encrypted device text in header of device detail page with value '%s' is not visible", expected), 
                getPage().isHeaderTextVisible(expected));
    }
}
