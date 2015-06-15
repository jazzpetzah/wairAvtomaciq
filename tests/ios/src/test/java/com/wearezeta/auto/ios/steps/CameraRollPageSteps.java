package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollPage;

import cucumber.api.java.en.When;

public class CameraRollPageSteps {

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private CameraRollPage getCameraRollPage() throws Exception {
		return (CameraRollPage) pagesCollecton.getPage(CameraRollPage.class);
	}

	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		getCameraRollPage().pressSelectFromLibraryButton();
	}

	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		getCameraRollPage().selectImageFromLibrary();
	}

	@When("^I press Confirm button$")
	public void IPressConfirmButton() throws Throwable {
		getCameraRollPage().pressConfirmButton();
	}

}
