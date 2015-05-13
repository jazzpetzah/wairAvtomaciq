package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollTabletPopoverPage;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		CameraRollTabletPopoverPage page = TabletPagesCollection.tabletDialogPage.pressAddPictureiPadButton();
		TabletPagesCollection.cameraRolliPadPopoverPage = (CameraRollTabletPopoverPage) page;
	}

}
