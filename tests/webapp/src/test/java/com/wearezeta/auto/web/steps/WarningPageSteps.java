package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WarningPage;

import cucumber.api.java.en.Then;

public class WarningPageSteps {

	/**
	 * Verifies whether a warning is visible
	 * 
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I see a warning$
	 * 
	 * @throws AssertionError
	 *             if a warning is not currently visible
	 */
	@Then("^I( do not)? see a warning$")
	public void ISeeWarning(String doNot) throws Exception {
		PagesCollection.warningPage = (WarningPage) PagesCollection.loginPage
				.instantiatePage(WarningPage.class);
		if (doNot == null) {
			Assert.assertTrue("Warning is not visible",
					PagesCollection.warningPage.isVisible());
		} else {
			Assert.assertFalse("Warning is visible",
					PagesCollection.warningPage.isVisible());
		}
	}

	/**
	 * Closes the warning
	 *
	 * @throws java.lang.Exception
	 * @step. ^I close the warning$
	 *
	 */
	@Then("^I close the warning$")
	public void ICloseWarning() throws Exception {
		PagesCollection.warningPage.closeWarning();
	}

	/**
	 * Verifies presence of a link with the given caption.
	 *
	 * @param linkCaption
	 *            the caption of the link to look for
	 * @throws java.lang.Exception
	 * @step. ^I see \"(.*)\" link in warning$
	 *
	 */
	@Then("^I see \"(.*)\" link in warning$")
	public void ISeeLinkInMessage(String linkCaption) throws Exception {
		PagesCollection.warningPage.isLinkWithCaptionVisible(linkCaption);
	}

}
