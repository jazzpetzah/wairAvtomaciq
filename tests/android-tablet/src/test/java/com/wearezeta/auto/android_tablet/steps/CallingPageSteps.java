package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletCallingOverlayPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class CallingPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletCallingOverlayPage getCallingOverlayPage() throws Exception {
		return (TabletCallingOverlayPage) pagesCollection
				.getPage(TabletCallingOverlayPage.class);
	}

	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Check calling Big bar is visible or not
	 * 
	 * @step. ^I (do not )?see calling overlay Big bar$
	 * 
	 * @param shouldNotSee
	 *            equals to null is "do not " part is NOT present
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see calling overlay Big bar$")
	public void WhenISeeCallingOverlayBigBar(String shouldNotSee)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(getCallingOverlayPage().callingOverlayIsVisible());
			Assert.assertTrue(getCallingOverlayPage()
					.incomingCallerAvatarIsVisible());
		} else {
			Assert.assertTrue(getCallingOverlayPage()
					.incomingCallerAvatarIsInvisible());
		}
	}

}
