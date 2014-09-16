package com.wearezeta.auto.ios;

import org.junit.Assert;

import java.io.IOException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @Given("^I Sign in using login (.*) and password (.*)$")
	 public void GivenISignIn(String login, String password) throws IOException, InterruptedException  {		
		 switch (login) {
		 case CommonUtils.YOUR_USER_1:
			 login = CommonUtils.yourUsers.get(0).getEmail();
			 break;
		 case CommonUtils.YOUR_USER_2:
			 login = CommonUtils.yourUsers.get(1).getEmail();
			 break;
		 case CommonUtils.YOUR_UNCONNECTED_USER:
			 login = CommonUtils.yourUsers.get(2).getEmail();
			 break;
		 case CommonUtils.CONTACT_1:
			 login = CommonUtils.contacts.get(0).getEmail();
			 break;
		 case CommonUtils.CONTACT_2:
			 login = CommonUtils.contacts.get(1).getEmail();
			 break;
		 case CommonUtils.CONTACT_3:
			 login = CommonUtils.contacts.get(2).getEmail();
			 break;
		 }
		 
		 password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		 PagesCollection.loginPage = (LoginPage)(PagesCollection.loginPage.signIn());
		 PagesCollection.loginPage.setLogin(login);
		 PagesCollection.loginPage.setPassword(password);
		 PagesCollection.contactListPage = (ContactListPage)(PagesCollection.loginPage.login());
		 
		 //Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	}
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {
		 
		 PagesCollection.loginPage.signIn();
	 }
	 
	 @When("I press Login button")
	 public void WhenIPressSignInButtonAgain() throws IOException {
		 
		 PagesCollection.contactListPage = (ContactListPage)(PagesCollection.loginPage.login());
	 }
	 
	 @When("I press Join button")
	 public void WhenIPressJoinButton() throws IOException {
		 
		 PagesCollection.registrationPage = PagesCollection.loginPage.join();
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String value) {
		 
		 if (value.equals(CommonUtils.YOUR_USER_1)) {
			 value = CommonUtils.yourUsers.get(0).getEmail();
		 }
		 PagesCollection.loginPage.setLogin(value);
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String value) {
		 
		 value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		 PagesCollection.loginPage.setPassword(value);
	 }	
	 
	 @When("I fill in email input (.*)")
	 public void IFillInEmailInput(String text){
		 PagesCollection.loginPage.setLogin(text);
	 }
	  
	 @Then("^I see login in screen$")
	 public void ThenISeeLogInScreen() {
		 Assert.assertTrue("I don't see login screen", PagesCollection.loginPage.isLoginButtonVisible());
	 }
	 
	 @When("I tap and hold on Email input")
	 public void ITapHoldEmailInput(){
		 PagesCollection.loginPage.tapHoldEmailInput();
	 }
	 
	 @When("I click on popup SelectAll item")
	 public void IClickPopupSelectAll(){
		 PagesCollection.loginPage.clickPopupSelectAllButton();
	 }
	 
	 @When("I click on popup Copy item")
	 public void IClickPopupCopy(){
		 PagesCollection.loginPage.clickPopupCopyButton();
	 }
	 
	 @When("I copy email input field content")
	 public void ICopyEmailInputContent(){
		 PagesCollection.loginPage.tapHoldEmailInput();
		 PagesCollection.loginPage.clickPopupSelectAllButton();
		 PagesCollection.loginPage.clickPopupCopyButton();
	 }
	 
	 @When("I click on popup Paste item")
	 public void IClickPopupPaste(){
		 PagesCollection.loginPage.clickPopupPasteButton();
	 }
	 
	 @When("^I press Terms of Service link$")
	 public void IPressTermsOfServiceLink() throws Throwable {
		 PagesCollection.loginPage.openTermsLink();
	 }

	 @Then("^I see the terms info page$")
	 public void ISeeTheTermsInfoPage() throws Throwable {
		 Assert.assertTrue("I don't see terms of service page", PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		//TODO:verify correct content as far as copywrite is in
	 }

	 @When("^I return to welcome page$")
	 public void IReturnToWelcomePage() throws Throwable {
		 PagesCollection.loginPage.closeTermsPrivacyController(); 
		 Assert.assertTrue("I don't see login screen", PagesCollection.loginPage.isLoginButtonVisible());
	 }

	 @When("^I press Privacy Policy link$")
	 public void IPressPrivacyPolicyLink() throws Throwable {
		 PagesCollection.loginPage.openPrivacyLink(); 
	 }

	 @Then("^I see the privacy info page$")
	 public void ISeeThePrivacyInfoPage() throws Throwable {
		 Assert.assertTrue("I don't see privacy policy page", PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		//TODO:verify correct content as far as copywrite is in
	 }
	 
	 @When("^I enter wrong email (.*)")
	 public void IEnterWrongEmail(String wrongMail){
		 PagesCollection.loginPage.setLogin(wrongMail);
		 PagesCollection.loginPage.tapPasswordField();
	 }
	 
	 @Then("^I see error with email notification$")
	 public void ISeeErrorWithEmailNotification(){
		 Assert.assertTrue("I don't see error mail notification", PagesCollection.loginPage.errorMailNotificationIsShown());
	 }

	 @Then("^I see no error notification$")
	 public void ISeeNoErrorNotification(){
		 Assert.assertFalse("I see error mail notification", PagesCollection.loginPage.errorMailNotificationIsNotShown());
	 }

	 @When("^I enter wrong password (.*)")
	 public void IEnterWrongPassword(String wrongPassword){
		 PagesCollection.loginPage.setPassword(wrongPassword);
	 }

	 @Then("^I see wrong credentials notification$")
	 public void ISeeWrongCredentialsNotification(){
		 Assert.assertTrue("I don't see wrong credentials notification", PagesCollection.loginPage.wrongCredentialsNotificationIsShown());
	 }


}
