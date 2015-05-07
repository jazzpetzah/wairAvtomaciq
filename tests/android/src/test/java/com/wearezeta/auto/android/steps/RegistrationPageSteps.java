package com.wearezeta.auto.android.steps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.*;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	public static Future<String> activationMessage;

	/**
	 * Presses the camera button once to bring up the camera, and again to take
	 * a picture
	 * 
	 * @step. ^I press Camera button twice$
	 * @throws Exception 
	 */
	@When("^I press Camera button twice$")
	public void WhenIPressCameraButton() throws Exception {
		PagesCollection.registrationPage.takePhoto();
		Thread.sleep(2000);
		PagesCollection.registrationPage.takePhoto();
	}

	/**
	 * Presses the camera button to bring up the camera app
	 * 
	 * @step. ^I press Picture button$
	 * 
	 * @throws IOException
	 */
	@When("^I press Picture button$")
	public void WhenIPressPictureButton() throws IOException {
		PagesCollection.registrationPage.selectPicture();
	}

	/**
	 * Selects the first photo in the phone's stored photos
	 * 
	 * @step. ^I choose photo from album$
	 * @throws Exception 
	 */
	@When("^I choose photo from album$")
	public void WhenIPressChoosePhoto() throws Exception {
		PagesCollection.registrationPage.chooseFirstPhoto();
	}

	/**
	 * Checks to see that a picture has been selected from either the gallery or
	 * the camera
	 * 
	 * @step. ^I See selected picture$
	 * 
	 * @throws Exception
	 */
	@When("^I See selected picture$")
	public void ISeeSelectedPicture() throws Exception {
		Assert.assertTrue(PagesCollection.registrationPage.isPictureSelected());
	}

	/**
	 * Presses the confirm button to confirm the selected picture
	 * 
	 * @step. ^I confirm selection$
	 * @throws Exception 
	 */
	@When("^I confirm selection$")
	public void IConfirmSelection() throws Exception {
		PagesCollection.registrationPage.confirmPicture();
	}

	/**
	 * Goes backwards one step in the registration process. This button is
	 * available at all steps of the process
	 * 
	 * @step. ^I press Registration back button$
	 * @throws Exception 
	 * 
	 */
	@When("^I press Registration back button$")
	public void IPressRegistrationBackButton() throws Exception {
		PagesCollection.registrationPage.pressBackButton();
	}

	/**
	 * Enters a name in the input field in the registration process
	 * 
	 * @step. ^I enter name (.*)$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I enter name (.*)$")
	public void IEnterName(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.addNameAlias(name);
		}
		PagesCollection.registrationPage.setName(this.userToRegister.getName());
	}

	/**
	 * Inputs an email address into the email input field (could these not be
	 * replaced by a general "input" step?)
	 * 
	 * @step. ^I enter email (.*)$
	 * 
	 * @param email
	 * @throws Exception
	 */
	@When("^I enter email (.*)$")
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
		}
		this.userToRegister.clearEmailAliases();
		this.userToRegister.addEmailAlias(email);
		PagesCollection.registrationPage.setEmail(this.userToRegister
				.getEmail());
	}

	/**
	 * Inputs a password into the password input field (could these not be
	 * replaced by a general "input" step?)
	 * 
	 * @step. ^I enter password (.*)$
	 * 
	 * @param password
	 * @throws Exception
	 */
	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) throws Exception {
		try {
			String realPassword = usrMgr.findUserByPasswordAlias(password)
					.getPassword();
			this.userToRegister.setPassword(realPassword);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setPassword(password);
		}
		this.userToRegister.clearPasswordAliases();
		this.userToRegister.addPasswordAlias(password);
		PagesCollection.registrationPage.setPassword(this.userToRegister
				.getPassword());
	}

	/**
	 * Submits all of the data given in the registration process by pressing the
	 * "create account" button Also waits at the inbox of the user to check to
	 * receive the verification email
	 * 
	 * @step. ^I submit registration data$
	 * 
	 * @throws Exception
	 */
	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		RegistrationPageSteps.activationMessage = IMAPSMailbox.getInstance()
				.getMessage(expectedHeaders,
						BackendAPIWrappers.UI_ACTIVATION_TIMEOUT);
		PagesCollection.registrationPage.createAccount();
	}

	/**
	 * Checks to see that the confirmation page exists once registration is
	 * finished. This is the page that tells the user to check their emails
	 * 
	 * @step. ^I see confirmation page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see confirmation page$")
	public void ISeeConfirmationPage() throws Exception {
		Assert.assertTrue(PagesCollection.registrationPage
				.isConfirmationVisible());
	}

	/**
	 * Uses the backend API to activate the registered user and then waits for
	 * the confirmation page to disappear (somehow, locator is not clear...)
	 * 
	 * @step. ^I verify registration address$
	 * 
	 * @throws Throwable
	 */
	@Then("^I verify registration address$")
	public void IVerifyRegistrationAddress() throws Throwable {
		BackendAPIWrappers
				.activateRegisteredUserByEmail(RegistrationPageSteps.activationMessage);
		this.userToRegister.setUserState(UserState.Created);
		PagesCollection.peoplePickerPage = PagesCollection.registrationPage
				.continueRegistration();
	}

}
