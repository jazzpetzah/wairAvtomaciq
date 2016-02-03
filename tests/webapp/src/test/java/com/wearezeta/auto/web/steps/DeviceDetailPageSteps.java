package com.wearezeta.auto.web.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.wearezeta.auto.web.pages.DeviceDetailPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceDetailPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verify if you see correct name and label of device in the device details
	 * 
	 * @param name
	 *            model of the device
	 * @param label
	 *            label of the device
	 * @throws Exception
	 */
	@Then("I see a device named (.*) with label (.*) in the device details")
	public void ISeeACertainDeviceInDevicesSection(String name, String label)
			throws Exception {
		assertThat(webappPagesCollection.getPage(DeviceDetailPage.class)
				.getDeviceLabel(), equalTo(label.toUpperCase()));
		assertThat(webappPagesCollection.getPage(DeviceDetailPage.class)
				.getDeviceLabel(), equalTo(label.toUpperCase()));
	}

	@When("I click the remove device link")
	public void IClickTheRemoveDeviceButton() throws Exception {
		webappPagesCollection.getPage(DeviceDetailPage.class)
				.clickRemoveDeviceLink();
	}

	@When("I type password \"([^\"]*)\" into the device remove form")
	public void ITypePassword(String password) throws Exception {
		webappPagesCollection.getPage(DeviceDetailPage.class).setPassword(
				"aqa123456!");
	}

	@When("I click the remove button")
	public void IClickTheRemoveDeviceButtonOnForm() throws Exception {
		webappPagesCollection.getPage(DeviceDetailPage.class)
				.clickRemoveButton();
	}
}
