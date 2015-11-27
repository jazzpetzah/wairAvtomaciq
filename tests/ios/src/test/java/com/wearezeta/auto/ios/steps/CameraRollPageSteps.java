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
	
	/**
	 * Select last image from camera roll library
	 * 
	 * @step. ^I choose last picture from camera roll$
	 * 
	 * @throws Throwable
	 */
	@When("^I choose last picture from camera roll$")
	public void IChooseLastPictureFromCameraRoll() throws Throwable {
		getCameraRollPage().clickFirstLibraryFolder();
		getCameraRollPage().clickLastImage();
	}

	@When("^I press Confirm button$")
	public void IPressConfirmButton() throws Throwable {
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
