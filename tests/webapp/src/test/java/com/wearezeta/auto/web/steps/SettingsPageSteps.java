package com.wearezeta.auto.web.steps;

import java.util.List;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.DevicesPage;

import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SettingsPageSteps {

	private String currentDeviceId = null;
        
        private final TestContext context;
        
    public SettingsPageSteps() {
        this.context = new TestContext();
    }

    public SettingsPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Remember the device id of the current device
	 * 
	 * @step. I remember the device id of the current device
	 *
	 * @throws Exception
	 */
	@When("^I remember the device id of the current device$")
	public void IRememberCurrentDeviceId() throws Exception {
		currentDeviceId = context.getPagesCollection().getPage(DevicesPage.class).getCurrentDeviceId();
	}

	/**
	 * Verify that the device id of the current device is still the same
	 *
	 * @step. I verify that the device id of the current device is (not) the
	 *        same
	 *
	 * @param not
	 *            If not is this set to null
	 * @throws Exception
	 */
	@When("^I verify that the device id of the current device is( not)? the same$")
	public void IVerifyCurrentDeviceId(String not) throws Exception {
		if (currentDeviceId == null) {
			throw new RuntimeException(
					"currentDeviceId was not remembered, please use the according step first");
		} else {
			if (not == null) {
				assertThat(context.getPagesCollection().getPage(DevicesPage.class)
						.getCurrentDeviceId(), equalTo(currentDeviceId));
			} else {
				assertThat(context.getPagesCollection().getPage(DevicesPage.class)
						.getCurrentDeviceId(), not(equalTo(currentDeviceId)));
			}
		}
	}

}
