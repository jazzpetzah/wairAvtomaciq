package com.wearezeta.auto.ios.steps;


import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class CameraRollPageSteps {
	
	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		PagesCollection.cameraRollPage.pressSelectFromLibraryButton();
	}
	
	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		PagesCollection.cameraRollPage.selectImageFromLibrary();
	}
	
	@When("^I press Confirm button$")
	public void IPressConfirmButton() throws Throwable {
		PagesCollection.cameraRollPage.pressConfirmButton();
	}
	
}
