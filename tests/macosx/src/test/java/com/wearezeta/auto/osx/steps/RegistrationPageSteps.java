package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
	private String aqaName;
	private String aqaEmail;
	private String aqaPassword;
	
	@When("I enter name (.*)")
	public void IEnterName(String name) throws IOException {
		if(name.equals(CommonUtils.YOUR_USER_1)) {
			Map<String, String> map =
					CreateZetaUser.generateNextUser(
							CommonUtils.getDefaultEmailFromConfig(CommonUtils.class),
							CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class));
			aqaName = map.keySet().iterator().next();
			aqaEmail = map.get(aqaName);
			aqaPassword = CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class);
			CommonSteps.senderPages.getRegistrationPage().enterName(aqaName);
		} else {
			aqaName = name;
			CommonSteps.senderPages.getRegistrationPage().enterName(name);
		}
	}
	
	@When("I enter email (.*)")
	public void IEnterEmail(String email) {
		if (email.equals(CommonUtils.YOUR_USER_1)) {
			CommonSteps.senderPages.getRegistrationPage().enterEmail(aqaEmail);
		} else {
			aqaEmail = email;
			CommonSteps.senderPages.getRegistrationPage().enterEmail(email);
		}
	}
	
	@When("I enter password (.*)")
	public void IEnterPassword(String password) throws IOException {
		if(password.equals(CommonUtils.YOUR_PASS)) {
			CommonSteps.senderPages.getRegistrationPage().enterPassword(
					CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class));
		} else {
			aqaPassword = password;
			CommonSteps.senderPages.getRegistrationPage().enterPassword(password);
		}
	}
	
	@When("I submit registration data")
	public void ISubmitRegistrationData() {
		CommonSteps.senderPages.getRegistrationPage().submitRegistration();
	}
	
	@Then("I see confirmation page")
	public void ISeeConfirmationPage() {
		Assert.assertTrue(CommonSteps.senderPages.getRegistrationPage().isConfirmationRequested());
	}
	 
	@Then("I verify registration address")
	public void IVerifyRegistrationAddress() throws Throwable {
		Assert.assertTrue(CreateZetaUser.activateRegisteredUser(aqaEmail, 10, aqaEmail, aqaPassword));
	}
	
	@When("I choose register using camera")
	public void IChooseRegisterUsingCamera() {
		CommonSteps.senderPages.getRegistrationPage().chooseToTakePicture();
	}
	
	@When("I take registration picture from camera")
	public void ITakeRegistrationPictureFromCamera() throws InterruptedException {
		CommonSteps.senderPages.getRegistrationPage().chooseToTakePicture();
		CommonSteps.senderPages.getRegistrationPage().acceptTakenPicture();
	}
	
	@When("I choose register with image")
	public void IChooseRegisterWithImage() {
		CommonSteps.senderPages.getRegistrationPage().chooseToPickImage();
	}
	
	@When("I take registration picture from image file (.*)")
	public void ITakeRegistrationPictureFromImageFile(String imageFile) throws MalformedURLException, IOException, InterruptedException {
		ChoosePicturePage choosePicturePage = new ChoosePicturePage(
				 CommonUtils.getUrlFromConfig(RegistrationPageSteps.class),
				 CommonUtils.getAppPathFromConfig(RegistrationPageSteps.class)
				 );
		Assert.assertTrue(choosePicturePage.isVisible());
		
		choosePicturePage.openImage(imageFile);

		CommonSteps.senderPages.getRegistrationPage().acceptTakenPicture();
	}
	
	@Then("I see contact list of registered user")
	public void ISeeContactListOfRegisteredUser() throws IOException {
		CommonSteps.senderPages.setContactListPage(new ContactListPage(
				CommonUtils.getUrlFromConfig(RegistrationPageSteps.class),
				CommonUtils.getAppPathFromConfig(RegistrationPageSteps.class)));
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.GivenISeeContactListWithName(aqaName);
	}
	
	public static final String[] INVALID_EMAILS = new String[] {
		"abc.example.com",
		"abc@example@.com",
		"example@zeta",
		"abc@example.",
		"abc@example.c"
	};
	
	public ArrayList<String> consideredValidEmails = new ArrayList<String>();
	
	@Then("I enter invalid emails")
	public void IEnterInvalidEmails() {
		RegistrationPage registrationPage = CommonSteps.senderPages.getRegistrationPage();
		for (String invalidEmail: INVALID_EMAILS) {
			registrationPage.enterEmail(invalidEmail);
			if (!registrationPage.isInvalidEmailMessageAppear()) {
				consideredValidEmails.add(invalidEmail);
			}
			registrationPage.enterEmail("");
		}
	}
	
	@Then("I see that all emails not accepted")
	public void ISeeThatAllEmailsNotAccepted() {
		Assert.assertTrue("Some of emails that should be invalid are considered valid. " +
				"Full list: " + Arrays.toString(INVALID_EMAILS) + "; " +
				"accepted: " + consideredValidEmails, consideredValidEmails.size() == 0);
		
	}
	
	@Then("I see that email invalid")
	public void ISeeThatEmailInvalid() {
		RegistrationPage registrationPage = CommonSteps.senderPages.getRegistrationPage();
		Assert.assertTrue("Email accepted but shouldn't be.", registrationPage.isInvalidEmailMessageAppear());
	}
	
	@Then("I see email (.*) without spaces")
	public void ISeeEmailWithoutSpaces(String email) {
		System.out.println(email);
		RegistrationPage registrationPage = CommonSteps.senderPages.getRegistrationPage();
		Assert.assertTrue(
				"It was accepted to enter spaces in email '"
						+ registrationPage.getEnteredEmail()
						+ "' but shouldn't be.",
				registrationPage.getEnteredEmail().equals(email.replaceAll(" ", "")));
	}
}
