package com.wearezeta.auto.web.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.hamcrest.collection.IsCollectionWithSize;

import com.wearezeta.auto.web.pages.DeviceDetailPage;
import com.wearezeta.auto.web.pages.DeviceLimitPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceLimitPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@When("I click on Sign Out on the device limit page$")
	public void IClickSignOutButton() throws Exception {
		webappPagesCollection.getPage(DeviceLimitPage.class)
				.clickSignOutButton();
	}

	@When("I click button to manage devices$")
	public void IClickManageDevicesButton() throws Exception {
		webappPagesCollection.getPage(DeviceLimitPage.class)
				.clickManageDevicesButton();
	}

	@Then("I am informed about the device limit$")
	public void IAmInformedAboutDeviceLimit() throws Exception {
		assertThat("Device limit info not shown", webappPagesCollection
				.getPage(DeviceLimitPage.class).isDeviceLimitInfoShown());
	}

	/**
	 * Verify if you see correct label of device in the device list
	 * 
	 * @param label
	 *            model and label of the device
	 * @throws Exception
	 */
	@Then("I see a device named (.*) with label (.*) under managed devices$")
	public void ISeeACertainDevice(String name, String label)
			throws Exception {
		assertThat(webappPagesCollection.getPage(DeviceLimitPage.class)
				.getDevicesNames(), hasItem(name.toUpperCase()));
	}

	@Then("I see (\\d+) devices under managed devices$")
	public void ISeeXDevices(int size) throws Exception {
		assertThat(webappPagesCollection.getPage(DeviceLimitPage.class)
				.getDevicesNames(), hasSize(size));
	}
}
