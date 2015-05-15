package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollTabletPopoverPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;
import com.wearezeta.auto.ios.pages.TabletDialogPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	
	/**
	 * Presses the add picture button on iPad to open a CameraRollPopoverPage
	 * @step. ^I press Add Picture button on iPad$
	 * @throws Throwable
	 */
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		TabletPagesCollection.tabletDialogPage = (TabletDialogPage) PagesCollection.loginPage
				.instantiatePage(TabletDialogPage.class);
		CameraRollTabletPopoverPage page = TabletPagesCollection.tabletDialogPage.pressAddPictureiPadButton();
		TabletPagesCollection.cameraRolliPadPopoverPage = (CameraRollTabletPopoverPage) page;
	}

}
