package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletFullScreenCallingOverlayPage;

import cucumber.api.java.en.When;

public class FullScreenCallingPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletFullScreenCallingOverlayPage getFullScreenCallingOverlayPage()
			throws Exception {
		return (TabletFullScreenCallingOverlayPage) pagesCollection
				.getPage(TabletFullScreenCallingOverlayPage.class);
	}

	/**
	 * Check whether full screen calling overlay is visible
	 * 
	 * @step. ^I see full screen calling overlay$
	 * 
	 * @throws Exception
	 */
	@When("I see (?:\\s*|the )full screen calling overlay$")
	public void WhenISeeOverlay() throws Exception {
		Assert.assertTrue("Full screen calling overlay is not visible",
				getFullScreenCallingOverlayPage().waitUntilVisible());
	}

	/**
	 * Accept an incoming call from lockscreen overlay
	 * 
	 * @step. ^I accept call on (?:\\s*|the )full screen calling overlay$
	 * 
	 * @throws Exception
	 */
	@When("^I accept call on (?:\\s*|the )full screen calling overlay$")
	public void IAcceptCall() throws Exception {
		getFullScreenCallingOverlayPage().acceptCall();
	}
}
