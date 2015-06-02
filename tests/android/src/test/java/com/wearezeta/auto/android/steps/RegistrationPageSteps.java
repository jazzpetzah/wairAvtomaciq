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
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.*;

public class RegistrationPageSteps {
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
		PagesCollection.profilePicturePage.clickCameraButton();
		Thread.sleep(2000);
		PagesCollection.profilePicturePage.clickCameraButton();
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
		PagesCollection.registrationPage.selectFirstGalleryPhoto();
	}

	/**
	 * Presses the confirm button to confirm the selected picture
	 * 
	 * @step. ^I confirm selection$
	 * @throws Exception
	 */
	@When("^I confirm selection$")
	public void IConfirmSelection() throws Exception {
		PagesCollection.contactListPage = PagesCollection.profilePicturePage.confirmPicture();
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
		PagesCollection.registrationPage.createAccount();
		// FIXME: activation message should be received in another step!
		RegistrationPageSteps.activationMessage = IMAPSMailbox.getInstance()
				.getMessage(expectedHeaders,
						BackendAPIWrappers.UI_ACTIVATION_TIMEOUT);
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
