package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	/**
	 * Enters name into registration Name fields
	 * 
	 * @step. I enter name (.*)
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I enter name (.*)")
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
	 * Enters email into registration Email field
	 * 
	 * @step. I enter email (.*)
	 * 
	 * @param email
	 *            user email string
	 * 
	 * @throws Exception
	 */
	@When("I enter email (.*)")
	public void IEnterEmail(String email) throws Exception {
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setEmail(email);
			this.userToRegister.clearEmailAliases();
			this.userToRegister.addEmailAlias(email);
		}
		PagesCollection.registrationPage.enterEmail(this.userToRegister
				.getEmail());
	}

	/**
	 * Enters password into registration Password field
	 * 
	 * @step. I enter password (.*)
	 * 
	 * @param password
	 *            user password string
	 * 
	 * @throws Exception
	 */
	@When("I enter password (.*)")
	public void IEnterPassword(String password) throws Exception {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
					password).getPassword());
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setPassword(password);
			this.userToRegister.clearPasswordAliases();
			this.userToRegister.addPasswordAlias(password);
		}
		PagesCollection.registrationPage.enterPassword(this.userToRegister
				.getPassword());
	}

	/**
	 * Clicks on Create Account button and submits all entered registration
	 * data.
	 * 
	 * @step. I submit registration data
	 * 
	 * @throws Exception
	 */
	@When("I submit registration data")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		PagesCollection.registrationPage.setActivationMessage(IMAPSMailbox
				.getInstance().getMessage(expectedHeaders,
						BackendAPIWrappers.UI_ACTIVATION_TIMEOUT));

		PagesCollection.registrationPage.submitRegistration();
	}

	/**
	 * Checks that e-mail confirmation page appers after Create Account button
	 * clicked
	 * 
	 * @step. I see confirmation page
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if confirmation page did not appear
	 */
	@Then("I see confirmation page")
	public void ISeeConfirmationPage() throws Exception {
		Assert.assertTrue(PagesCollection.registrationPage
				.isConfirmationRequested());
	}

	/**
	 * Verifies registration address (using data from mail headers)
	 * 
	 * @step. I verify registration address
	 * 
	 * @throws Exception
	 */
	@Then("I verify registration address")
	public void IVerifyRegistrationAddress() throws Exception {
		BackendAPIWrappers
				.activateRegisteredUser(PagesCollection.registrationPage
						.getActivationMessage());
	}

	/**
	 * Chooses to take user picture using camera
	 * 
	 * @step. I choose register using camera
	 */
	@When("I choose register using camera")
	public void IChooseRegisterUsingCamera() {
		PagesCollection.registrationPage.chooseToTakePicture();
	}

	/**
	 * Takes picture using camera
	 * 
	 * @step. I take registration picture from camera
	 * 
	 * @throws InterruptedException
	 */
	@When("I take registration picture from camera")
	public void ITakeRegistrationPictureFromCamera()
			throws InterruptedException {
		PagesCollection.registrationPage.chooseToTakePicture();
		PagesCollection.registrationPage.acceptTakenPicture();
	}

	/**
	 * Chooses to take user picture by selecting existing image from file system
	 * 
	 * @step. I choose register with image
	 */
	@When("I choose register with image")
	public void IChooseRegisterWithImage() {
		PagesCollection.registrationPage.chooseToPickImage();
	}

	/**
	 * Takes picture from selected image file on file system
	 * 
	 * @step. I take registration picture from image file (.*)
	 * 
	 * @param imageFile
	 *            name of file to be chosen (should be placed in Documents
	 *            folder)
	 * 
	 * @throws Exception
	 */
	@When("I take registration picture from image file (.*)")
	public void ITakeRegistrationPictureFromImageFile(String imageFile)
			throws Exception {
		ChoosePicturePage choosePicturePage = new ChoosePicturePage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
		Assert.assertTrue(choosePicturePage.isVisible());

		choosePicturePage.openImage(imageFile);

		PagesCollection.registrationPage.acceptTakenPicture();
	}

	/**
	 * Checks that Contact list appears with registered user name
	 * 
	 * @step. I see contact list of registered user
	 * 
	 * @throws Exception
	 */
	@Then("I see contact list of registered user")
	public void ISeeContactListOfRegisteredUser() throws Exception {
		PagesCollection.contactListPage = new ContactListPage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.ISeeMyNameInContactList(this.userToRegister.getName());
	}

	/**
	 * Checks that Self Profile appears with registered user name
	 * 
	 * @step. I see self profile of registered user
	 * 
	 * @throws Exception
	 */
	@Then("I see self profile of registered user")
	public void ISeeSelfProfileOfRegisteredUser() throws Exception {
		PagesCollection.userProfilePage = new UserProfilePage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
		UserProfilePageSteps upSteps = new UserProfilePageSteps();
		upSteps.ISeeNameInUserProfile(this.userToRegister.getName());
	}

	public static final String[] INVALID_EMAILS = new String[] {
			"abc.example.com", "abc@example@.com", "example@zeta",
			"abc@example." };

	public ArrayList<String> consideredValidEmails = new ArrayList<String>();

	/**
	 * Tries to enter list of invalid emails and gathers error messages
	 * 
	 * @step. I enter invalid emails
	 */
	@Then("I enter invalid emails")
	public void IEnterInvalidEmails() {
		for (String invalidEmail : INVALID_EMAILS) {
			PagesCollection.registrationPage.enterEmail(invalidEmail);
			if (!PagesCollection.registrationPage.isInvalidEmailMessageAppear()) {
				consideredValidEmails.add(invalidEmail);
			}
			PagesCollection.registrationPage.enterEmail("");
		}
	}

	/**
	 * Checks that all entered invalid emails cause error messages to appear
	 * 
	 * @step. I see that all emails not accepted
	 * 
	 * @throws AssertionError
	 *             if some of invalid emails did not failed to be entered
	 */
	@Then("I see that all emails not accepted")
	public void ISeeThatAllEmailsNotAccepted() {
		Assert.assertTrue(
				"Some of emails that should be invalid are considered valid. "
						+ "Full list: " + Arrays.toString(INVALID_EMAILS)
						+ "; " + "accepted: " + consideredValidEmails,
				consideredValidEmails.size() == 0);

	}

	/**
	 * Checks that entered email caused error message to appear
	 * 
	 * @step. I see that email invalid
	 * 
	 * @throws AssertionError
	 *             if no error message when email entered
	 */
	@Then("I see that email invalid")
	public void ISeeThatEmailInvalid() {
		Assert.assertTrue("Email accepted but shouldn't be.",
				PagesCollection.registrationPage.isInvalidEmailMessageAppear());
	}

	/**
	 * Checks that there is no spaces in email input field when entering email
	 * with spaces
	 * 
	 * @step. I see email (.*) without spaces
	 * 
	 * @param email
	 *            user email string
	 * 
	 * @throws AssertionError
	 *             if spaces appear in email input field
	 */
	@Then("I see email (.*) without spaces")
	public void ISeeEmailWithoutSpaces(String email) {
		Assert.assertTrue("It was accepted to enter spaces in email '"
				+ PagesCollection.registrationPage.getEnteredEmail()
				+ "' but shouldn't be.", PagesCollection.registrationPage
				.getEnteredEmail().equals(email.replaceAll(" ", "")));
	}

	/**
	 * Opens activation link in browser and stores response message
	 * 
	 * @step. ^I open activation link in browser$
	 * 
	 * @throws Exception
	 */
	@When("^I open activation link in browser$")
	public void IOpenActivationLinkInBrowser() throws Exception {
		PagesCollection.registrationPage.activateUserFromBrowser();
	}

	/**
	 * Checks that response message from activation using browser says that user
	 * activated successfully
	 * 
	 * @step. ^I see that user activated$
	 * 
	 * @throws AssertionError
	 *             if activation response different from expected on success
	 */
	@When("^I see that user activated$")
	public void ISeeUserActivated() {
		Assert.assertTrue(PagesCollection.registrationPage.isUserActivated());
	}
}
