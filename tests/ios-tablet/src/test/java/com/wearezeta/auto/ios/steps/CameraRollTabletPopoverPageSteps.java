package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.When;

public class CameraRollTabletPopoverPageSteps {

	/**
	 * Tap on Camera Roll button
	 * 
	 * @step ^I press Camera Roll button on iPad$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Camera Roll button on iPad$")
	public void IPressCameraRollButton() throws Throwable {
		TabletPagesCollection.cameraRolliPadPopoverPage
				.pressSelectFromLibraryButtoniPad();
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
		TabletPagesCollection.cameraRolliPadPopoverPage
				.selectImageFromLibraryiPad();
	}

	/**
	 * Tap on confirm button
	 * 
	 * @step ^I press Confirm button on iPad popover$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Confirm button on iPad popover$")
	public void IPressConfirmButton() throws Throwable {
		TabletPagesCollection.cameraRolliPadPopoverPage
				.pressConfirmButtoniPad();
	}

}
