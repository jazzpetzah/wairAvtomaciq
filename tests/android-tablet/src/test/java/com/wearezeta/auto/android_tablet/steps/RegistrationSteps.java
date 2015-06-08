package com.wearezeta.auto.android_tablet.steps;

import java.util.concurrent.Future;

import org.junit.Assert;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android_tablet.pages.registration.TabletRegisterConfirmationPage;
import com.wearezeta.auto.android_tablet.pages.registration.TabletRegistrationFormPage;
import com.wearezeta.auto.android_tablet.pages.registration.TabletTakeRegistrationPicturePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletRegistrationFormPage getRegistrationFormPage()
			throws Exception {
		return (TabletRegistrationFormPage) pagesCollection
				.getPage(TabletRegistrationFormPage.class);
	}

	private TabletRegisterConfirmationPage getRegisterConfirmationPage()
			throws Exception {
		return (TabletRegisterConfirmationPage) pagesCollection
				.getPage(TabletRegisterConfirmationPage.class);
	}

	private TabletTakeRegistrationPicturePage getTakeRegistrationPicturePage()
			throws Exception {
		return (TabletTakeRegistrationPicturePage) pagesCollection
				.getPage(TabletTakeRegistrationPicturePage.class);
	}

	private ClientUser userToRegister;
	{
		try {
			userToRegister = new ClientUser();
			ClientUsersManager
					.setClientUserAliases(
							userToRegister,
							new String[] { ClientUsersManager.NAME_ALIAS_TEMPLATE
									.apply(1) },
							new String[] { ClientUsersManager.PASSWORD_ALIAS_TEMPLATE
									.apply(1) },
							new String[] { ClientUsersManager.EMAIL_ALIAS_TEMPLATE
									.apply(1) },
							new String[] { ClientUsersManager.PHONE_NUMBER_ALIAS_TEMPLATE
									.apply(1) });
			usrMgr.appendCustomUser(userToRegister);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}
	private Future<String> registrationMessage;

	/**
	 * Enter the user name into the corresponding field on the registration form
	 * 
	 * @step. ^I enter (?:the |\\s*)registration name \"(.*)\"$
	 * 
	 * @param name
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^I enter (?:the |\\s*)registration name \"(.*)\"$")
	public void IEnterRegistrationName(String name) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			this.userToRegister.setName(name);
		}
		getRegistrationFormPage().enterName(name);
	}

	/**
	 * Enter the user email into the corresponding field on the registration
	 * form
	 * 
	 * @step. ^I enter (?:the |\\s*)registration email \"(.*)\"$
	 * 
	 * @param email
	 *            email address/alias
	 * @throws Exception
	 */
	@When("^I enter (?:the |\\s*)registration email \"(.*)\"$")
	public void IEnterRegistrationEmail(String email) throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			this.userToRegister.setEmail(email);
		}
		getRegistrationFormPage().enterEmail(email);
	}

	/**
	 * Enter the user password into the corresponding field on the registration
	 * form
	 * 
	 * @step. ^I enter (?:the |\\s*)registration password \"(.*)\"$
	 * 
	 * @param password
	 *            the password/alias
	 * @throws Exception
	 */
	@When("^I enter (?:the |\\s*)registration password \"(.*)\"$")
	public void IEnterRegistrationPassword(String password) throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
		}
		getRegistrationFormPage().enterPassword(password);
	}

	/**
	 * Starts the internal email listener to get the registration email
	 * 
	 * @step. ^I start listening for registration email$
	 * 
	 * @throws Exception
	 */
	@When("^I start listening for registration email$")
	public void IStartListeningForRegistrationEmail() throws Exception {
		registrationMessage = BackendAPIWrappers
				.initMessageListener(userToRegister);
	}

	/**
	 * Tap the corresponding button on the registration form to send the
	 * registration data
	 * 
	 * @step. ^I submit (?:the |\\s*)registration data$
	 * 
	 * @throws Exception
	 */
	@When("^I submit (?:the |\\s*)registration data$")
	public void ISubmitRegistrationData() throws Exception {
		getRegistrationFormPage().tapSubmitButton();
	}

	/**
	 * Wait until registration email with confirmation link is delivered and
	 * then activates the user using values from this email message
	 * 
	 * @step. ^I verify my registration via email$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify my registration via email$")
	public void IVerifyMyRegistrationData() throws Exception {
		BackendAPIWrappers.activateRegisteredUserByEmail(registrationMessage);
		userToRegister.setUserState(UserState.Created);
		usrMgr.setSelfUser(userToRegister);
	}

	/**
	 * Verify that registration data input form is visible
	 * 
	 * @step. ^I see (?:the |\\s*)[Rr]egistration form$
	 * 
	 * @throws Exception
	 */
	@When("^I see (?:the |\\s*)[Rr]egistration form$")
	public void IseeRegistrationForm() throws Exception {
		Assert.assertTrue(
				"The registration form is not visible after the timeout",
				getRegistrationFormPage().waitUntilVisible());
	}

	/**
	 * Verify whether the registration confirmation page is visible or not
	 * 
	 * @step. ^I (do not )?see (?:the |\\s*)[Cc]onfirmation page$"
	 * 
	 * @param shouldNotSee
	 *            equals to null if "do not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:the |\\s*)[Cc]onfirmation page$")
	public void ISeeConfirmationPage(String shouldNotSee) throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(
					"The confirmation is not visible after the timeout",
					getRegisterConfirmationPage().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The confirmation is still visible after the timeout",
					getRegisterConfirmationPage().waitUntilInvisible());
		}
	}

	/**
	 * Verify whether the particular email address is visible on the
	 * confirmation page
	 * 
	 * @step. ^I see the entered email on (?:the |\\s*)[Cc]onfirmation page$
	 * 
	 * @param expectedEmail
	 *            email address/alias
	 * @throws Exception
	 */
	@Then("^I see the entered email on (?:the |\\s*)[Cc]onfirmation page$")
	public void ISeeEmail() throws Exception {
		final String expectedEmail = userToRegister.getEmail();
		Assert.assertTrue(
				String.format(
						"The expected email address '%s' is not visible on the confirmation page",
						expectedEmail), getRegisterConfirmationPage()
						.waitUntilEmailIsVisible(expectedEmail));
	}

	/**
	 * Verify that Take Picture after registration page is visible
	 * 
	 * @step. ^I see (?:the |\\s*)[Tt]ake [Rr]egistration [Pp]icture page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see (?:the |\\s*)[Tt]ake [Rr]egistration [Pp]icture page$")
	public void ISeeRegistrationPicturePage() throws Exception {
		Assert.assertTrue(
				"The Take Picture after registration page has not been shown after timeout",
				getTakeRegistrationPicturePage().waitUntilVisible());
	}

	/**
	 * Tap Camera button on the Take Picture after registration page
	 * 
	 * @step. ^I tap Camera button on (?:the |\\s*)[Tt]ake [Rr]egistration
	 *        [Pp]icture page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Camera button on (?:the |\\s*)[Tt]ake [Rr]egistration [Pp]icture page$")
	public void ITapCameraButton() throws Exception {
		getTakeRegistrationPicturePage().tapCameraButton();
	}

	/**
	 * Tap Take picture button on the Take Picture after registration page
	 * 
	 * @step. ^I tap Take Picture button on (?:the |\\s*)[Tt]ake [Rr]egistration
	 *        [Pp]icture page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Take Picture button on (?:the |\\s*)[Tt]ake [Rr]egistration [Pp]icture page$")
	public void ITapTakePictureButton() throws Exception {
		getTakeRegistrationPicturePage().tapTakePictureButton();
	}

	/**
	 * Tap OK button on the Take Picture after registration page to confirm your
	 * photo selection
	 * 
	 * @step. ^I confirm my picture on (?:the |\\s*)[Tt]ake [Rr]egistration
	 *        [Pp]icture page$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm my picture on (?:the |\\s*)[Tt]ake [Rr]egistration [Pp]icture page$")
	public void IConfirmMyPicture() throws Exception {
		getTakeRegistrationPicturePage().tapOKButton();
	}
}
