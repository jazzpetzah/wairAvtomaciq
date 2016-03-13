package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollPage;

import cucumber.api.java.en.When;

public class CameraRollPageSteps {

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private CameraRollPage getCameraRollPage() throws Exception {
		return pagesCollection.getPage(CameraRollPage.class);
	}

	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		getCameraRollPage().pressSelectFromLibraryButton();
	}

	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		getCameraRollPage().selectImageFromLibrary();
	}

	@When("^I confirm Camera Roll picture selection$")
	public void IConfirmImageSelection() throws Throwable {
		getCameraRollPage().pressConfirmButton();
	}

	/**
	 * Presses the sketch button on the camera roll page
	 * 
	 * @step. ^I press sketch button on camera roll page$
	 * @throws Throwable
	 */
	@When("^I press sketch button on camera roll page$")
	public void IPressSketchButtonOnCameraRollPage() throws Throwable {
		getCameraRollPage().clickCameraRollSketchButton();
	}

}
