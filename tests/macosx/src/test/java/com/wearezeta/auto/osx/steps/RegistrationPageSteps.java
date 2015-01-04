package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Assert;

import com.wearezeta.auto.common.BackendAPIWrappers;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.ClientUsersManager;
import com.wearezeta.auto.user_management.ClientUsersManager.UserAliasType;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	private MBoxChangesListener listener;

	@When("I enter name (.*)")
	public void IEnterName(String name) throws IOException {
		try {
			this.userToRegister = usrMgr.findUserByNameAlias(name);
		} catch (NoSuchElementException e) {
			this.userToRegister = new ClientUser();
			this.userToRegister.setName(name);
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		CommonOSXSteps.senderPages.getRegistrationPage().enterName(
				this.userToRegister.getName());
	}

	@When("I enter email (.*)")
	public void IEnterEmail(String email) throws IOException {
		try {
			String realEmail = usrMgr.findUserByAlias(email,
					UserAliasType.EMAIL).getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchElementException e) {
			this.userToRegister.setEmail(email);
			this.userToRegister.clearEmailAliases();
			this.userToRegister.addEmailAlias(email);
		}
		CommonOSXSteps.senderPages.getRegistrationPage().enterEmail(
				this.userToRegister.getEmail());
	}

	@When("I enter password (.*)")
	public void IEnterPassword(String password) throws IOException {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByAlias(password,
					UserAliasType.PASSWORD).getPassword());
		} catch (NoSuchElementException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.clearPasswordAliases();
			this.userToRegister.addPasswordAlias(password);
		}
		CommonOSXSteps.senderPages.getRegistrationPage().enterPassword(
				this.userToRegister.getPassword());
	}

	@When("I submit registration data")
	public void ISubmitRegistrationData() throws Exception {
		CommonOSXSteps.senderPages.getRegistrationPage().submitRegistration();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.createDefaultInstance().startMboxListener(
				expectedHeaders);
	}

	@Then("I see confirmation page")
	public void ISeeConfirmationPage() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getRegistrationPage()
				.isConfirmationRequested());
	}

	@Then("I verify registration address")
	public void IVerifyRegistrationAddress() throws Exception {
		BackendAPIWrappers.activateRegisteredUser(this.listener);
	}

	@When("I choose register using camera")
	public void IChooseRegisterUsingCamera() {
		CommonOSXSteps.senderPages.getRegistrationPage().chooseToTakePicture();
	}

	@When("I take registration picture from camera")
	public void ITakeRegistrationPictureFromCamera()
			throws InterruptedException {
		CommonOSXSteps.senderPages.getRegistrationPage().chooseToTakePicture();
		CommonOSXSteps.senderPages.getRegistrationPage().acceptTakenPicture();
	}

	@When("I choose register with image")
	public void IChooseRegisterWithImage() {
		CommonOSXSteps.senderPages.getRegistrationPage().chooseToPickImage();
	}

	@When("I take registration picture from image file (.*)")
	public void ITakeRegistrationPictureFromImageFile(String imageFile)
			throws MalformedURLException, IOException, InterruptedException {
		ChoosePicturePage choosePicturePage = new ChoosePicturePage(
				CommonUtils
						.getOsxAppiumUrlFromConfig(RegistrationPageSteps.class),
				CommonUtils
						.getOsxApplicationPathFromConfig(RegistrationPageSteps.class));
		Assert.assertTrue(choosePicturePage.isVisible());

		choosePicturePage.openImage(imageFile);

		CommonOSXSteps.senderPages.getRegistrationPage().acceptTakenPicture();
	}

	@Then("I see contact list of registered user")
	public void ISeeContactListOfRegisteredUser() throws Exception {
		CommonOSXSteps.senderPages
				.setContactListPage(new ContactListPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(RegistrationPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(RegistrationPageSteps.class)));
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.ISeeMyNameInContactList(this.userToRegister.getName());
	}

	public static final String[] INVALID_EMAILS = new String[] {
			"abc.example.com", "abc@example@.com", "example@zeta",
			"abc@example." };

	public ArrayList<String> consideredValidEmails = new ArrayList<String>();

	@Then("I enter invalid emails")
	public void IEnterInvalidEmails() {
		RegistrationPage registrationPage = CommonOSXSteps.senderPages
				.getRegistrationPage();
		for (String invalidEmail : INVALID_EMAILS) {
			registrationPage.enterEmail(invalidEmail);
			if (!registrationPage.isInvalidEmailMessageAppear()) {
				consideredValidEmails.add(invalidEmail);
			}
			registrationPage.enterEmail("");
		}
	}

	@Then("I see that all emails not accepted")
	public void ISeeThatAllEmailsNotAccepted() {
		Assert.assertTrue(
				"Some of emails that should be invalid are considered valid. "
						+ "Full list: " + Arrays.toString(INVALID_EMAILS)
						+ "; " + "accepted: " + consideredValidEmails,
				consideredValidEmails.size() == 0);

	}

	@Then("I see that email invalid")
	public void ISeeThatEmailInvalid() {
		RegistrationPage registrationPage = CommonOSXSteps.senderPages
				.getRegistrationPage();
		Assert.assertTrue("Email accepted but shouldn't be.",
				registrationPage.isInvalidEmailMessageAppear());
	}

	@Then("I see email (.*) without spaces")
	public void ISeeEmailWithoutSpaces(String email) {
		RegistrationPage registrationPage = CommonOSXSteps.senderPages
				.getRegistrationPage();
		Assert.assertTrue(
				"It was accepted to enter spaces in email '"
						+ registrationPage.getEnteredEmail()
						+ "' but shouldn't be.",
				registrationPage.getEnteredEmail().equals(
						email.replaceAll(" ", "")));
	}
}
