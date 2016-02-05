package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.DeviceDetailsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class DeviceDetailsPageSteps {

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private DeviceDetailsPage getDeviceDetailsPage() throws Exception {
		return pagesCollection.getPage(DeviceDetailsPage.class);
	}

    /**
     * Tap the Verify switcher
     *
     * @step. I tap Verify switcher on device details page$
     *
     * @throws Exception
     */
	@When("^I tap Verify switcher on Device Details page$")
    public void ITapVerifySwitcher() throws Exception {
        getDeviceDetailsPage().tapVerifySwitcher();
    }

    /**
     * Navigate back to the previous page
     *
     * @step. ^I navigate back from device details page$
     *
     * @throws Exception
     */
    @And("^I navigate back from Device Details page$")
    public void INavigateBack() throws Exception {
        getDeviceDetailsPage().tapBackButton();
    }
}
