package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollTabletPopoverPage;

import cucumber.api.java.en.When;

public class CameraRollTabletPopoverPageSteps {

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private CameraRollTabletPopoverPage getCameraRollTabletPopoverPage()
			throws Exception {
		return (CameraRollTabletPopoverPage) pagesCollecton
				.getPage(CameraRollTabletPopoverPage.class);
	}

	/**
	 * Tap on Camera Roll button
	 * 
	 * @step. ^I press Camera Roll button on iPad$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Camera Roll button on iPad$")
	public void IPressCameraRollButton() throws Throwable {
		getCameraRollTabletPopoverPage().pressSelectFromLibraryButtoniPad();
	}

	/**
	 * Select image from library
	 * 
	 * @step ^I choose a picture from camera roll on iPad popover$
	 * 
	 * @throws Throwable
	 */
	@When("^I choose a picture from camera roll on iPad popover$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		getCameraRollTabletPopoverPage().selectImageFromLibraryiPad();
	}

	/**
	 * Tap on confirm button
	 * 
	 * @step. ^I press Confirm button on iPad popover$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Confirm button on iPad popover$")
	public void IPressConfirmButton() throws Throwable {
		getCameraRollTabletPopoverPage().pressConfirmButtoniPad();
	}

}
