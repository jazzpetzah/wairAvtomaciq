package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.Lifecycle;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeSuccessfullPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordChangeSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PasswordChangeSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr;
	private final WebappPagesCollection webappPagesCollection;

	private static final int VISIBILITY_TIMEOUT_SECONDS = 15;
        
        private final Lifecycle.TestContext context;

    public PasswordChangeSteps(Lifecycle.TestContext context) {
        this.context = context;
        this.usrMgr = context.getUserManager();
        this.webappPagesCollection = context.getPagesCollection();
    }

	/**
	 * Verifies whether Password Change page is visible
	 * 
	 * @step. ^I see Password Change page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Password Change page$")
	public void ISeePage() throws Exception {
		webappPagesCollection.getPage(PasswordChangePage.class)
				.waitUntilVisible(VISIBILITY_TIMEOUT_SECONDS);
	}

	/**
	 * Enter password string to the corresponding field on Password Change page
	 * 
	 * @step. ^I enter password (\\S+) on Password Change page$
	 * 
	 * @param passwordOrAlias
	 *            password string or an alias
	 */
	@When("^I enter password (\\S+) on Password Change page$")
	public void IEnterPassword(String passwordOrAlias) throws Exception {
		passwordOrAlias = usrMgr.replaceAliasesOccurences(passwordOrAlias,
				FindBy.PASSWORD_ALIAS);
		webappPagesCollection.getPage(PasswordChangePage.class).setNewPassword(
				passwordOrAlias);
	}

	/**
	 * Click Change Password button on Password Change page
	 * 
	 * @step. ^I click Change Password button on Password Change page$
	 * 
	 * @throws Exception
	 */
	@And("^I click Change Password button on Password Change page$")
	public void IClickChangePasswordButton() throws Exception {
		webappPagesCollection.getPage(PasswordChangePage.class)
				.clickChangePasswordButton();
	}

	/**
	 * Verify whether Password Change Succeeded page is visible
	 * 
	 * @step. ^I see Password Change Succeeded page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Password Change Succeeded page$")
	public void ISeePasswordChangeSucceeded() throws Exception {
		assertThat(
				webappPagesCollection.getPage(
						PasswordChangeSuccessfullPage.class)
						.isConfirmationTextVisible(), is(true));
	}
	
	@Then("^I dont see Password Change Succeeded page$")
	public void IDontSeePasswordChangeSucceeded() throws Exception {
		assertThat(
				webappPagesCollection.getPage(
						PasswordChangeSuccessfullPage.class)
						.isConfirmationTextVisible(), is(false));
	}
}
