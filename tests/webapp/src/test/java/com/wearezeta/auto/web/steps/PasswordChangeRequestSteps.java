package com.wearezeta.auto.web.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestSuccessfullPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordChangeRequestSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PasswordChangeRequestSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private Future<String> passwordChangeMessage = null;

	/**
	 * Verifies whether Password Change Request page is visible
	 * 
	 * @step. ^I see Password Change Request page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Password Change Request page$")
	public void ISeePasswordChangeRequestPage() throws Exception {
		Assert.assertTrue("Email field not visible", webappPagesCollection
				.getPage(PasswordChangeRequestPage.class).isEmailFieldVisible());
	}

	/**
	 * Types emails address into the corresponding field on Password Change
	 * Request page
	 * 
	 * @step. ^I enter email (\\S+) on Password Change Request page$
	 * 
	 * @param emailOrAlias
	 *            email address to type or alias
	 */
	@When("^I enter email (\\S+) on Password Change Request page$")
	public void IEnterEmail(String emailOrAlias) throws Exception {
		emailOrAlias = usrMgr.replaceAliasesOccurences(emailOrAlias,
				FindBy.EMAIL_ALIAS);
		webappPagesCollection.getPage(PasswordChangeRequestPage.class)
				.setEmail(emailOrAlias);
	}

	private static final int PASSWORD_MSG_TIMWOUT_SECONDS = 60;
	private static final long TIME_SENT_DELTA_MILLISECONDS = 2000;

	/**
	 * Start email listener for a particular mail box
	 * 
	 * @step. (.*) starts? listening for password change confirmation$
	 * 
	 * @param emailOrName
	 *            user name/email alias
	 * @throws Exception
	 */
	@And("(.*) starts? listening for password change confirmation$")
	public void IStartListeningForPasswordChangeEmail(String emailOrName)
			throws Exception {
		String email = null;
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(emailOrName).getEmail();
		} catch (NoSuchUserException e) {
			email = usrMgr.findUserByNameOrNameAlias(emailOrName).getEmail();
		}
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
		this.passwordChangeMessage = mbox.getMessage(expectedHeaders,
				PASSWORD_MSG_TIMWOUT_SECONDS, System.currentTimeMillis()
						- TIME_SENT_DELTA_MILLISECONDS);
	}

	/**
	 * Click Change Password button on Password Change Request page
	 * 
	 * @step. ^I click Change Password button on Password Change Request page$
	 * 
	 * @throws Exception
	 */
	@And("^I click Change Password button on Password Change Request page$")
	public void IClickChangePasswordButton() throws Exception {
		webappPagesCollection.getPage(PasswordChangeRequestPage.class)
				.clickChangePasswordButton();
	}

	/**
	 * Verifies whether Password Change Request Succeeded page is visible
	 * 
	 * @step. ^I see Password Change Request Succeeded page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Password Change Request Succeeded page$")
	public void ISeeRequestSucceededPage() throws Exception {
		assertThat(
				webappPagesCollection.getPage(
						PasswordChangeRequestSuccessfullPage.class)
						.isConfirmationTextVisible(), is(true));
	}

	/**
	 * Open Password Change link from the received email. This step requires
	 * email listener to be already started before. Otherwise it will throw
	 * RuntimeException
	 * 
	 * @step. ^I open Password Change link from the received email$
	 * 
	 * @throws Exception
	 */
	@When("^I open Password Change link from the received email$")
	public void IOpenPasswordChangeLinkFromEmail() throws Exception {
		if (this.passwordChangeMessage == null) {
			throw new RuntimeException(
					"Please call the corresponding step to initialize email listener first");
		}
		final PasswordResetMessage wrappedMsg = new PasswordResetMessage(
				this.passwordChangeMessage.get());
		webappPagesCollection.getPage(PasswordChangePage.class).setUrl(
				wrappedMsg.extractPasswordResetLink());
		webappPagesCollection.getPage(PasswordChangePage.class).navigateTo();
	}

}
