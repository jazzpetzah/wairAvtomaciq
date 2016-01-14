package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletFirstTimeOverlay;
import cucumber.api.java.en.And;

public class FirstTimeOverlaySteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletFirstTimeOverlay getFirstTimeOverlay() throws Exception {
		return pagesCollection.getPage(TabletFirstTimeOverlay.class);
	}

	/**
	 * Accept the First Time overlay as soon as it is visible
	 *
	 * @step. ^I accept First Time overlay as soon as it is visible$
	 *
	 * @throws Exception
	 */
	@And("^I accept First Time overlay as soon as it is visible$")
	public void IAcceptTheOverLayWhenItIsVisible() throws Exception {
		if (getFirstTimeOverlay().isVisible()) {
			getFirstTimeOverlay().tapGotItButton();
		}
	}
}
