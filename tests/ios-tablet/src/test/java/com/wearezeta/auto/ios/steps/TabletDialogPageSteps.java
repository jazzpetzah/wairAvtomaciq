package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRolliPadPopoverPage;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		CameraRolliPadPopoverPage page = TabletPagesCollection.tabletDialogPage.pressAddPictureiPadButton();
		TabletPagesCollection.cameraRolliPadPopoverPage = (CameraRolliPadPopoverPage) page;
	}

}
