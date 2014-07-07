package com.wearezeta.auto.ios;


import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class CameraRollPageSteps {
	
	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		PagesCollection.cameraRollPage.pressCameraRollButton();
	}
	
	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		PagesCollection.cameraRollPage.openCameraRoll();
	}
	
	@When("^I press Confirm button$")
	public void IPressConfirmButton() throws Throwable {
		PagesCollection.cameraRollPage.pressConfirmButton();
	}
	
}
