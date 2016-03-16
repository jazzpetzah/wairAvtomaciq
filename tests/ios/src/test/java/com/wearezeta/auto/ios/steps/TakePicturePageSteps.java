package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TakePicturePage;

import cucumber.api.java.en.When;

public class TakePicturePageSteps {

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private TakePicturePage getTakePicturePage() throws Exception {
		return pagesCollection.getPage(TakePicturePage.class);
	}

	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		getTakePicturePage().pressSelectFromLibraryButton();
	}

	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		getTakePicturePage().selectImageFromLibrary();
	}

	/**
	 * Presses the sketch button on the camera roll page
	 * 
	 * @step. ^I press sketch button on camera roll page$
	 * @throws Throwable
	 */
	@When("^I press sketch button on camera roll page$")
	public void IPressSketchButtonOnCameraRollPage() throws Throwable {
		getTakePicturePage().clickCameraRollSketchButton();
	}


    /**
     * Tap the shutter button to take a camera picture
     *
     * @step. ^I tap Camera Shutter button$
     *
     * @throws Exception
     */
	@When("^I tap Camera Shutter button$")
    public void ITapCameraShutterButton() throws Exception {
        getTakePicturePage().tapShutterButton();
    }

	/**
	 * Taps lens camera button to take picture
	 *
	 * @throws Exception
	 * @step. ^I tap Lens button$
	 */
	@When("^I tap Lens button$")
	public void ITapLensButton() throws Exception {
		getTakePicturePage().tapCameraButton();
	}
}
