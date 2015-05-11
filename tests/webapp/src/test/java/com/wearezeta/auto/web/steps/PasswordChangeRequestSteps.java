package com.wearezeta.auto.web.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordChangeRequestSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PasswordChangeRequestSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final int VISIBILITY_TIMEOUT_SECONDS = 5;

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
		if (PagesCollection.passwordChangeRequestPage == null) {
			PagesCollection.passwordChangeRequestPage = (PasswordChangeRequestPage) PagesCollection.loginPage
					.instantiatePage(PasswordChangeRequestPage.class);
		}
		PagesCollection.passwordChangeRequestPage
				.waitUntilVisible(VISIBILITY_TIMEOUT_SECONDS);
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
	public void IEnterEmail(String emailOrAlias) {
		emailOrAlias = usrMgr.replaceAliasesOccurences(emailOrAlias,
				FindBy.EMAIL_ALIAS);
		PagesCollection.passwordChangeRequestPage.setEmail(emailOrAlias);
	}

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
		this.passwordChangeMessage = mbox.getMessage(expectedHeaders, 120);
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
		PagesCollection.passwordChangeRequestSuccessfullPage = PagesCollection.passwordChangeRequestPage
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
		PagesCollection.passwordChangeRequestSuccessfullPage
				.waitUntilVisible(VISIBILITY_TIMEOUT_SECONDS);
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
		PagesCollection.passwordChangePage = (PasswordChangePage) PagesCollection.loginPage
				.instantiatePage(PasswordChangePage.class);
		PagesCollection.passwordChangePage.setUrl(wrappedMsg
				.extractPasswordResetLink());
		PagesCollection.passwordChangePage.navigateTo();
	}

}
