package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.GroupPeoplePopoverPage;
import com.wearezeta.auto.osx.pages.webapp.SingleUserPeoplePopoverPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.When;

public class PeopleViewSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verify that Single User Profile popover is visible or not
	 *
	 * @step. ^I( do not)? see Single User Profile popover$
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not present
	 * 
	 * @throws Exception
	 *
	 */
	@When("^I( do not)? see Single User Profile popover$")
	public void ISeeSingleUserPopup(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(SingleUserPeoplePopoverPage.class)
					.waitUntilVisibleOrThrowException();
		} else {
			webappPagesCollection.getPage(SingleUserPeoplePopoverPage.class)
					.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Verify that Group Participants popover is shown or not
	 *
	 * @step. ^I( do not)? see Group Participants popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist
	 * 
	 * @throws Exception
	 *
	 */
	@When("^I( do not)? see Group Participants popover$")
	public void ISeeUserProfilePopupPage(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
					.waitUntilVisibleOrThrowException();
		} else {
			webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
					.waitUntilNotVisibleOrThrowException();
		}
	}
}
