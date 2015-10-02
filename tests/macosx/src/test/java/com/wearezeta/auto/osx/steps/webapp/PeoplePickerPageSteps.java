package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.webapp.PeoplePickerPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verifies the presence of the People Picker
	 *
	 * @step. ^I see [Pp]eople [Pp]icker$
	 *
	 * @throws Exception
	 */
	@When("^I see [Pp]eople [Pp]icker$")
	public void ISeePeoplePicker() throws Exception {
		webappPagesCollection.getPage(PeoplePickerPage.class).isVisible();
	}
}
