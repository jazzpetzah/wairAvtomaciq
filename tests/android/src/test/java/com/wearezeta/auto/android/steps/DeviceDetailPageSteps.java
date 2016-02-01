package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DeviceDetailPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;


import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class DeviceDetailPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private String rememberedDeviceId;

    private DeviceDetailPage getPage() throws Exception {
        return pagesCollection.getPage(DeviceDetailPage.class);
    }

    /**
     * Checks to see that the settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see device detail page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Device detail page is not visible", getPage()
                .waitUntilVisible());
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
        System.out.println("ID = " + rememberedDeviceId);
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
        Assert.assertEquals("ID's are not equal", rememberedDeviceId, actualDeviceId);
    }
}
