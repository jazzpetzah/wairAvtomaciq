package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.webapp.PeoplePickerPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class PeoplePickerPageSteps {
	@SuppressWarnings("unused")
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

	/**
	 * Verifies a greater number of suggestion than the given one
	 *
	 * @step. ^I see more than (\\d+) suggestions? in people picker$
	 *
	 * @param count
	 *            the count to have greater number of suggestions
	 *
	 * @throws Exception
	 */
	@Then("^I see more than (\\d+) suggestions? in people picker$")
	public void ISeeMoreThanXSuggestionsInPeoplePicker(int count)
			throws Exception {
		assertThat("people suggestions",
				webappPagesCollection.getPage(PeoplePickerPage.class)
						.getNumberOfSuggestions(), greaterThan(count));
	}

}
