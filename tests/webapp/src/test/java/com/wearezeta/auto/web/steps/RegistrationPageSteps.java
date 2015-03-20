package com.wearezeta.auto.web.steps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	private Future<Message> activationMessage;

	public static final int maxCheckCnt = 2;

	/**
	 * Enter user name into registration form
	 * 
	 * @step. ^I input user name (.*)
	 * 
	 * @param name
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^I input user name (.*)")
	public void IEnterName(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		PagesCollection.registrationPage.enterName(this.userToRegister
				.getName());
	}

	/**
	 * Enter user email into registration form
	 * 
	 * @step. ^I input user email (.*)
	 * 
	 * @param email
	 *            user email/alias
	 * @throws Exception
	 */
	@When("^I input user email (.*)")
	public void IEnterEmail(String email) throws Exception {
		boolean flag = false;
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			flag = true;
		}

		if (flag) {
			PagesCollection.registrationPage.enterEmail(email);
		} else {
			PagesCollection.registrationPage.enterEmail(this.userToRegister
					.getEmail());
		}
	}

	/**
	 * Enter user password into registration form
	 * 
	 * @step. ^I input user password (.*)
	 * 
	 * @param password
	 *            user password/alias
	 * @throws IOException
	 */
	@When("^I input user password (.*)")
	public void IEnterPassword(String password) throws IOException {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
					password).getPassword());
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		PagesCollection.registrationPage.enterPassword(this.userToRegister
				.getPassword());
	}

	/**
	 * Submit registration form
	 * 
	 * @step. ^I submit registration$
	 * 
	 * @throws MessagingException
	 * @throws InterruptedException
	 * @throws Exception
	 */
	@When("^I submit registration$")
	public void ISubmitRegistration() throws MessagingException,
			InterruptedException, Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.UI_ACTIVATION_TIMEOUT);
		PagesCollection.registrationPage.submitRegistration();
	}

	/**
	 * Verifiy whether email address, which is visible on email confirmation
	 * page is the same as the expected one
	 * 
	 * @step. ^I see email (.*) verification page$
	 * 
	 * @param email
	 *            expected email/alias
	 * @throws NoSuchUserException
	 */
	@Then("^I see email (.*) verification page$")
	public void ISeeVerificationEmail(String email) throws NoSuchUserException {
		email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		Assert.assertTrue(PagesCollection.registrationPage
				.isVerificationEmailCorrect(email));
	}

	/**
	 * Activate newly registered user on the backend
	 * 
	 * @step. ^I verify registration email$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify registration email$")
	public void IVerifyRegistrationEmail() throws Exception {
		BackendAPIWrappers.activateRegisteredUser(this.activationMessage);
		userToRegister.setUserState(UserState.Created);
	}

	/**
	 * Switch to Sign In page
	 * 
	 * @step. ^I switch to [Ss]ign [Ii]n page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to [Ss]ign [Ii]n page$")
	public void ISwitchToLoginPage() throws Exception {
		PagesCollection.loginPage = PagesCollection.registrationPage
				.switchToLoginPage();
	}
}
