package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
	
	 private String aqaName;
	
	 private String aqaEmail;
	 
	 private String aqaPassword;
	 
	 boolean generateUsers = false;
	
	 @When("^I press Camera button$")
	 public void WhenIPressCameraButton() throws IOException {
		 
		 PagesCollection.registrationPage.takePhoto();
	 }
	 
	 @When("^I press Picture button$")
	 public void WhenIPressPictureButton() throws IOException {
		 
		 PagesCollection.registrationPage.selectPicture();
	 }
	 
	 @When("^I choose photo from album$")
	 public void WhenIPressChoosePhoto() throws IOException {
		 
		 PagesCollection.registrationPage.chooseFirstPhoto();
	 }

	 @When("^I See selected picture$")
	 public void ISeeSelectedPicture() throws IOException {
		 
		 Assert.assertTrue(PagesCollection.registrationPage.isPictureSelected());
	 }

	 @When("^I confirm selection$")
	 public void IConfirmSelection() throws IOException {
		 
		 PagesCollection.registrationPage.confirmPicture();
	 }
	 
	 @When("^I enter name (.*)$")
	 public void IEnterName(String name) throws IOException {
		 
		 if(name.equals(CommonUtils.YOUR_USER_1)) {
			 Map<String, String> map = CreateZetaUser.generateNextUser(CommonUtils.getDefaultEmailFromConfig(CommonUtils.class), CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class));
			 
			 aqaName = map.keySet().iterator().next();
			 
			 aqaEmail = map.get(aqaName);
			 
			 aqaPassword = CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class);
			 
			 generateUsers = true;
			 
			 PagesCollection.registrationPage.setName(aqaName);
		 }
		 
		 else {
			 aqaName = name;
			 PagesCollection.registrationPage.setName(name);
		 }
	 }
	 
	 @When("^I enter email (.*)$")
	 public void IEnterEmail(String email) throws IOException {
		 
		 if (email.equals(CommonUtils.YOUR_USER_1)) {
			 PagesCollection.registrationPage.setEmail(aqaEmail);
		 }
		 else {
			 aqaEmail = email;
			 PagesCollection.registrationPage.setEmail(email);
		 }
	 }
	 
	 @When("^I attempt to enter an email with spaces (.*)$")
	 public void IEnterEmailWithSpaces(String email) throws IOException{
		 	if (email.equals(CommonUtils.YOUR_USER_1)) {
			 email=aqaEmail;
		 }
		 	//what if email is less than 1 character?
		PagesCollection.registrationPage.setEmail(new StringBuilder(email).insert(email.length()-1,"          ").toString());
		 }
	 
	 @Then("^I verify no spaces are present in email$")
	 public void CheckForSpacesInEmail() throws IOException{
		 PagesCollection.registrationPage.typeEmail();
		 String realEmailText = PagesCollection.registrationPage.getEmailFieldValue();
		 String initialEmailText = PagesCollection.registrationPage.getEmail();
		 Assert.assertTrue(initialEmailText.replace(" ", "").equals(realEmailText));
	 }
	 
	 @When("^I attempt to enter emails with known incorrect formats$")
	 public void IEnterEmailWithIncorrectFormat() throws IOException{
		 //current design has basic email requirements: contains single @, contains a domain name with a dot + domain extension(min 2 characters)
		 String[] listOfInvalidEmails = {"abc.example.com","abc@example@.com","example@zeta","abc@example.","abc@example.c"};
		 //test fails because minimum 2 character domain extension is not implemented(allows for only 1)
		 PagesCollection.registrationPage.setListOfEmails(listOfInvalidEmails);
	 }
	 
	 @Then ("^I verify that the app does not let me continue$")
	 public void IVerifyIncorrectFormatMessage() throws IOException{
		 Assert.assertTrue(PagesCollection.registrationPage.typeAllInvalidEmails());
	 }
	 
	 @When("^I enter password (.*)$")
	 public void IEnterPassword(String password) throws IOException {
		 
		 if(password.equals(CommonUtils.YOUR_PASS)) {
			 PagesCollection.registrationPage.setPassword(CommonUtils.getDefaultPasswordFromConfig(CommonUtils.class));
		 }
		 else {
			 aqaPassword = password;
			 PagesCollection.registrationPage.setPassword(password);
		 }
	 }
	 
	 @When("^I submit registration data$")
	 public void ISubmitRegistrationData()
	 {
		 PagesCollection.registrationPage.createAccount();
	 }
	 
	 @Then("^I see confirmation page$")
	 public void ISeeConfirmationPage()
	 {
		 PagesCollection.registrationPage.catchLoginAlert();
		 Assert.assertTrue(PagesCollection.registrationPage.isConfirmationVisible());
	 }
	 
	 @Then("^I verify registration address$")
	 public void IVerifyRegistrationAddress() throws Throwable {
		 
		 Assert.assertTrue(CreateZetaUser.activateRegisteredUser(aqaEmail, 10, aqaEmail, aqaPassword));
	 }
	 
}
