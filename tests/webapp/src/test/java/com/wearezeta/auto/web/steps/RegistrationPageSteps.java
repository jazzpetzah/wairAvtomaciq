package com.wearezeta.auto.web.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RegistrationPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	private Future<String> activationMessage;

	public static final int maxCheckCnt = 2;

	private static final Logger LOG = ZetaLogger
			.getLog(RegistrationPageSteps.class.getName());

	/**
	 * Enter user name into registration form
	 * 
	 * @step. ^I enter user name (.*) on Registration page$
	 * 
	 * @param name
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^I enter user name (.*) on Registration page$")
	public void IEnterName(String name) throws Exception {
		WebappPagesCollection.registrationPage
				.waitForRegistrationPageToFullyLoad();
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
		WebappPagesCollection.registrationPage.enterName(this.userToRegister
				.getName());
	}

	/**
	 * Enter user email into registration form
	 * 
	 * @step. ^I enter user email (.*) on Registration page$
	 * 
	 * @param email
	 *            user email/alias
	 * @throws Exception
	 */
	@When("^I enter user email (.*) on Registration page$")
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
			WebappPagesCollection.registrationPage.enterEmail(email);
		} else {
			WebappPagesCollection.registrationPage
					.enterEmail(this.userToRegister.getEmail());
		}
	}

	/**
	 * Enter user password into registration form
	 * 
	 * @step. ^I enter user password \"(.*)\" on Registration page$
	 * 
	 * @param password
	 *            user password/alias
	 * @throws Exception
	 */
	@When("^I enter user password \"(.*)\" on Registration page$")
	public void IEnterPassword(String password) throws Exception {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
					password).getPassword());
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		WebappPagesCollection.registrationPage
				.enterPassword(this.userToRegister.getPassword());
	}

	/**
	 * Submit registration form
	 * 
	 * @step. ^I submit registration form$
	 * 
	 * @throws Exception
	 */
	@When("^I submit registration form$")
	public void ISubmitRegistration() throws Exception {
		WebappPagesCollection.registrationPage.submitRegistration();
	}

	/**
	 * Start monitoring thread for activation email. Please put this step BEFORE
	 * you submit the registration form
	 * 
	 * @step. ^I start activation email monitoring$
	 * 
	 * @throws Exception
	 */
	@When("^I start activation email monitoring$")
	public void IStartActivationEmailMonitoring() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
	}

	/**
	 * Verify whether email address, which is visible on email confirmation page
	 * is the same as the expected one
	 * 
	 * @step. ^I see email (.*) on [Vv]erification page$
	 * 
	 * @param email
	 *            expected email/alias
	 * @throws Exception
	 */
	@Then("^I see email (.*) on [Vv]erification page$")
	public void ISeeVerificationEmail(String email) throws Exception {
		email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		assertThat(
				WebappPagesCollection.registrationPage
						.getVerificationEmailAddress(),
				containsString(email));
	}

	/**
	 * Verify whether email address, which is visible on email pending page is
	 * the same as the expected one
	 * 
	 * @step. ^I see email (.*) on pending page$
	 * 
	 * @param email
	 *            expected email/alias
	 * @throws Exception
	 */
	@Then("^I see email (.*) on pending page$")
	public void ISeePendingEmail(String email) throws Exception {
		email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		assertThat(
				WebappPagesCollection.registrationPage.getPendingEmailAddress(),
				containsString(email));
	}

	/**
	 * Verify whether I see an error message on the verification page
	 *
	 * @step. ^I see error \"(.*)\" on [Vv]erification page$
	 *
	 * @param message
	 *            expected error message
	 * @throws NoSuchUserException
	 */
	@Then("^I see error \"(.*)\" on [Vv]erification page$")
	public void ISeeErrorMessageOnVerificationPage(String message)
			throws Throwable {
		assertThat(WebappPagesCollection.registrationPage.getErrorMessages(),
				hasItem(message));
	}

	/**
	 * Checks if a orange line is shown around the email field on the
	 * registration form
	 *
	 * @step. ^I verify that the email field on the registration form is( not)?
	 *        marked as error$
	 * @throws Exception
	 */
	@Then("^I verify that the email field on the registration form is( not)? marked as error$")
	public void ARedDotIsShownOnTheEmailField(String not) throws Exception {
		if (not == null) {
			assertThat("email field marked as error",
					WebappPagesCollection.registrationPage
							.isEmailFieldMarkedAsError());
		} else {
			assertThat("email field marked as valid",
					WebappPagesCollection.registrationPage
							.isEmailFieldMarkedAsValid());
		}
	}

	/**
	 * Checks if an icon is shown
	 * 
	 * @step. ^I verify that an envelope icon is shown$
	 * @throws Exception
	 */
	@Then("^I verify that an envelope icon is shown$")
	public void IVerifyThatAnEnvelopeIconIsShown() throws Exception {
		assertThat("Envelope icon not shown",
				WebappPagesCollection.registrationPage.isEnvelopeShown());
	}

	/**
	 * Activate newly registered user on the backend. Don't forget to call the
	 * 'I start activation email monitoring' step before this one
	 * 
	 * @step. ^I verify registration email$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify registration email$")
	public void IVerifyRegistrationEmail() throws Exception {
		BackendAPIWrappers
				.activateRegisteredUserByEmail(this.activationMessage);
		userToRegister.setUserState(UserState.Created);
	}

	/**
	 * Activates user using browser URL from activation email and sign him in to
	 * the app if the activation was successful. Don't forget to call the 'I
	 * start activation email monitoring' step before this one
	 * 
	 * @step. ^I activate user by URL$
	 * 
	 * @throws Exception
	 */
	@Then("^I activate user by URL$")
	public void WhenIActivateUserByUrl() throws Exception {
		final String link = BackendAPIWrappers
				.getUserActivationLink(this.activationMessage);
		LOG.info("Get activation link from " + link);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(link);
		HttpEntity entity = httpclient.execute(httpGet).getEntity();
		if (entity != null) {
			String content = EntityUtils.toString(entity);
			Pattern p = Pattern.compile("data-url=\"(.*?)\"");
			Matcher m = p.matcher(content);
			while(m.find()) {
			   String activationLink = m.group(1);
			   LOG.info("Activation link: " + activationLink);
			   httpGet = new HttpGet(activationLink);
			   httpclient.execute(httpGet);
			}
	    }

		this.userToRegister.setUserState(UserState.Created);
		// indexes in aliases start from 1
		final int userIndex = usrMgr.appendCustomUser(userToRegister) + 1;
		userToRegister.addEmailAlias(ClientUsersManager.EMAIL_ALIAS_TEMPLATE
				.apply(userIndex));
		userToRegister.addNameAlias(ClientUsersManager.NAME_ALIAS_TEMPLATE
				.apply(userIndex));
		userToRegister
				.addPasswordAlias(ClientUsersManager.PASSWORD_ALIAS_TEMPLATE
						.apply(userIndex));

		WebappPagesCollection.loginPage = WebappPagesCollection.registrationPage
				.instantiatePage(LoginPage.class);
		WebappPagesCollection.contactListPage = WebappPagesCollection.registrationPage
				.instantiatePage(ContactListPage.class);
		WebappPagesCollection.loginPage.waitForLogin();
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
		WebappPagesCollection.loginPage = WebappPagesCollection.registrationPage
				.switchToLoginPage();
	}
}
