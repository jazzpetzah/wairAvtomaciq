package com.wearezeta.auto.ios.steps;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.When;

public class CameraRolliPadPopoverPageSteps {
	@When("^I press Camera Roll button on iPad$")
	public void IPressCameraRollButton() throws Throwable {
		TabletPagesCollection.cameraRolliPadPopoverPage.pressSelectFromLibraryButtoniPad();
	}
	
	@When("^I choose a picture from camera roll on iPad popover$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		TabletPagesCollection.cameraRolliPadPopoverPage.selectImageFromLibraryiPad();
	}
	
	@When("^I press Confirm button on iPad popover$")
	public void IPressConfirmButton() throws Throwable {
		TabletPagesCollection.cameraRolliPadPopoverPage.pressConfirmButtoniPad();
	}

}
