package com.wearezeta.auto.android.steps;

import java.util.concurrent.Future;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.android.pages.registration.ProfilePicturePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.*;

public class RegistrationSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private RegistrationPage getRegistrationPage() throws Exception {
		return (RegistrationPage) pagesCollection
				.getPage(RegistrationPage.class);
	}

	private ProfilePicturePage getProfilePicturePage() throws Exception {
		return (ProfilePicturePage) pagesCollection
				.getPage(ProfilePicturePage.class);
	}

	private ClientUser userToRegister = null;

	public Future<String> activationMessage;

	/**
	 * Presses the camera button once to bring up the camera, and again to take
	 * a picture
	 * 
	 * @step. ^I press Camera button twice$
	 * @throws Exception
	 */
	@When("^I press Camera button twice$")
	public void WhenIPressCameraButton() throws Exception {
		getProfilePicturePage().clickCameraButton();
		Thread.sleep(2000);
		getProfilePicturePage().clickCameraButton();
	}

	/**
	 * Presses the camera button to bring up the camera app
	 * 
	 * @step. ^I press Picture button$
	 * @throws Exception
	 */
	@When("^I press Picture button$")
	public void WhenIPressPictureButton() throws Exception {
		getRegistrationPage().selectPicture();
	}

	/**
	 * Selects the first photo in the phone's stored photos
	 * 
	 * @step. ^I choose photo from album$
	 * @throws Exception
	 */
	@When("^I choose photo from album$")
	public void WhenIPressChoosePhoto() throws Exception {
		getRegistrationPage().selectFirstGalleryPhoto();
	}

	/**
	 * Presses the confirm button to confirm the selected picture
	 * 
	 * @step. ^I confirm selection$
	 * @throws Exception
	 */
	@When("^I confirm selection$")
	public void IConfirmSelection() throws Exception {
		getProfilePicturePage().confirmPicture();
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
		activationMessage = BackendAPIWrappers
				.initMessageListener(userToRegister);
		getRegistrationPage().createAccount();
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
		Assert.assertTrue(getRegistrationPage().isConfirmationVisible());
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
		BackendAPIWrappers.activateRegisteredUserByEmail(activationMessage);
		this.userToRegister.setUserState(UserState.Created);
		getRegistrationPage().continueRegistration();
	}

}
