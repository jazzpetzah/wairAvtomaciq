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
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.RegistrationPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;

public class RegistrationPageSteps {

	private ClientUser userToRegister = null;

	private Future<String> activationMessage;

	private Future<String> newDeviceMessage;

	public static final int maxCheckCnt = 2;

	private static final Logger LOG = ZetaLogger
			.getLog(RegistrationPageSteps.class.getName());
        
        private final TestContext context;
        
    public RegistrationPageSteps() {
        this.context = new TestContext();
    }

    public RegistrationPageSteps(TestContext context) {
        this.context = context;
    }

	@When("^I see Registration page$")
	public void ISeeRegistrationPage() throws Exception {
		context.getPagesCollection().getPage(RegistrationPage.class).waitForRegistrationPageToFullyLoad();
	}

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
		context.getPagesCollection().getPage(RegistrationPage.class)
				.waitForRegistrationPageToFullyLoad();
		try {
			this.userToRegister = context.getUserManager().findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		context.getPagesCollection().getPage(RegistrationPage.class).enterName(
				this.userToRegister.getName());
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
			String realEmail = context.getUserManager().findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			flag = true;
		}

		if (flag) {
			context.getPagesCollection().getPage(RegistrationPage.class).enterEmail(
					email);
		} else {
			context.getPagesCollection().getPage(RegistrationPage.class).enterEmail(
					this.userToRegister.getEmail());
		}
	}

	/**
	 * Verifies autofilled email of user
	 *
	 * @step. ^(.*) verifies email is correct on Registration page$
	 *
	 * @param usernameAlias
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^(.*) verifies email is correct on Registration page$")
	public void IVerifyEmail(String usernameAlias) throws Exception {
		String realEmail = context.getUserManager().findUserByNameOrNameAlias(usernameAlias)
				.getEmail();
		Assert.assertEquals("Entered email is wrong", realEmail,
				context.getPagesCollection().getPage(RegistrationPage.class)
						.getEnteredEmail());

	}

	/**
	 * Verifies autofilled username of user
	 *
	 * @step. ^(.*) verifies username is correct on Registration page$
	 *
	 * @param usernameAlias
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^(.*) verifies username is correct on Registration page$")
	public void IVerifyUsername(String usernameAlias) throws Exception {
		String realUsername = context.getUserManager().findUserByNameOrNameAlias(usernameAlias)
				.getName();
		Assert.assertEquals("Entered username is wrong", realUsername,
				context.getPagesCollection().getPage(RegistrationPage.class)
						.getEnteredName());

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
			ClientUser user = context.getUserManager().findUserByPasswordAlias(password);
			if (this.userToRegister == null) {
				this.userToRegister = user;
			}
			this.userToRegister.setPassword(user.getPassword());
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		context.getPagesCollection().getPage(RegistrationPage.class).enterPassword(
				this.userToRegister.getPassword());
	}

	/**
	 * Check terms of use checkbox
	 * 
	 * @step. ^I accept the Terms of Use$
	 * 
	 * @throws Exception
	 */
	@When("^I accept the Terms of Use$")
	public void IAcceptTermsOfUse() throws Exception {
		context.getPagesCollection().getPage(RegistrationPage.class)
				.acceptTermsOfUse();
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
		context.getPagesCollection().getPage(RegistrationPage.class)
				.submitRegistration();
	}

	/**
	 * Checks if Create Account button on the corresponding page is disabled
	 *
	 * @step. ^Create Account button is disabled$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^Create Account button is disabled$")
	public void CreateAccountButtonIsDisabled() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(RegistrationPage.class)
				.isCreateAccountButtonDisabled());
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
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance(userToRegister.getEmail(), userToRegister.getPassword())
				.getMessage(expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
	}

	@When("^(.*) starts? listening for new device mail$")
	public void IStartListeningForNewDeviceMail(String emailOrName) throws Exception {
		ClientUser user;
		try {
			user = context.getUserManager().findUserByEmailOrEmailAlias(emailOrName);
		} catch (NoSuchUserException e) {
			user = context.getUserManager().findUserByNameOrNameAlias(emailOrName);
		}
		IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put("Delivered-To", user.getEmail());
		this.newDeviceMessage = mbox.getMessage(expectedHeaders,
				BackendAPIWrappers.ACTIVATION_TIMEOUT, System.currentTimeMillis());
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
		email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
		assertThat(context.getPagesCollection().getPage(RegistrationPage.class)
				.getVerificationEmailAddress(), containsString(email));
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
		email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
		assertThat(context.getPagesCollection().getPage(RegistrationPage.class)
				.getPendingEmailAddress(), containsString(email));
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
		assertThat(context.getPagesCollection().getPage(RegistrationPage.class)
				.getErrorMessages(), hasItem(message));
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
			assertThat("email field marked as error", context.getPagesCollection()
					.getPage(RegistrationPage.class)
					.isEmailFieldMarkedAsError());
		} else {
			assertThat("email field marked as valid", context.getPagesCollection()
					.getPage(RegistrationPage.class)
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
				context.getPagesCollection().getPage(RegistrationPage.class)
						.isEnvelopeShown());
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
			while (m.find()) {
				String activationLink = m.group(1);
				LOG.info("Activation link: " + activationLink);
				httpGet = new HttpGet(activationLink);
				httpclient.execute(httpGet);
			}
		}

		// indexes in aliases start from 1
		final int userIndex = context.getUserManager().appendCustomUser(userToRegister) + 1;
		userToRegister.addEmailAlias(ClientUsersManager.EMAIL_ALIAS_TEMPLATE
				.apply(userIndex));
		userToRegister.addNameAlias(ClientUsersManager.NAME_ALIAS_TEMPLATE
				.apply(userIndex));
		userToRegister
				.addPasswordAlias(ClientUsersManager.PASSWORD_ALIAS_TEMPLATE
						.apply(userIndex));

		context.getPagesCollection().getPage(LoginPage.class).waitForLogin();
	}

	@Then("^I see verification mail in (.*) with (.*)$")
	public void ISeeVerificationMailInLanguage(String language, String message) throws Exception {
		final String content = BackendAPIWrappers
				.getMessageContent(this.activationMessage);
		switch (language) {
			case "de":
				assertThat("E-Mail is not German.", content, containsString(message));
				break;
			case "en":
				assertThat("E-Mail is not English.", content, containsString(message));
				break;
			default: break;
		}
	}

	@Then("^I see new device mail in (.*) with (.*)$")
	public void ISeeNewDeviceMailInLanguage(String language, String message) throws Exception {
		final String content = BackendAPIWrappers
				.getMessageContent(this.newDeviceMessage);
		switch (language) {
			case "de":
				assertThat("E-Mail is not German.", content, containsString(message));
				break;
			case "en":
				assertThat("E-Mail is not English.", content, containsString(message));
				break;
			default:
				break;
		}
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
		context.getPagesCollection().getPage(RegistrationPage.class).switchToLoginPage();
	}
        
	/**
	 * Clicks on Verify later button on Verification page
	 * 
	 * @step. ^I click on Verify later button on Verification page$
	 * 
	 * @throws Exception
	 */
	@Then("^I click on Verify later button on Verification page$")
	public void IClickVerifyLaterButton() throws Exception {
		context.getPagesCollection().getPage(RegistrationPage.class).clickVerifyLaterButton();
	}

	@Then("^I see intro about Wire when user got personal invite (.*)$")
	public void ISeeIntroWhenPersonalInvitation(String expectedText) throws Exception {
		final String shownText = context.getPagesCollection().getPage(RegistrationPage.class).getTextIntroWire();
		Assert.assertTrue(
				String.format("The actual login error '%s' is not equal to the expected one: '%s'", shownText, expectedText),
					shownText.equals(expectedText));
	}
}
