package com.wearezeta.auto.ios;

import org.junit.Assert;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("I see sign in screen")
	public void GiveniSeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

	@Given("^I Sign in using login (.*) and password (.*)$")
	public void GivenISignIn(String login, String password) throws IOException,
			InterruptedException {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		PagesCollection.loginPage = (LoginPage) (PagesCollection.loginPage
				.signIn());
		PagesCollection.loginPage.setLogin(login);
		PagesCollection.loginPage.setPassword(password);
		PagesCollection.contactListPage = (ContactListPage) (PagesCollection.loginPage
				.login());

		Assert.assertNotNull("Login not passed",
				PagesCollection.contactListPage);
	}

	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws IOException {

		PagesCollection.loginPage.signIn();
	}

	@When("I press Login button")
	public void WhenIPressSignInButtonAgain() throws IOException {

		PagesCollection.contactListPage = (ContactListPage) (PagesCollection.loginPage
				.login());
	}

	@When("I attemt to press Login button")
	public void IAttemptToPressLoginButton() {
		PagesCollection.loginPage.clickLoginButton();
	}

	@When("I press Join button")
	public void WhenIPressJoinButton() throws IOException {
		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) throws IOException {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setLogin(login);
	}

	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws IOException {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setPassword(password);
	}

	@When("I fill in email input (.*)")
	public void IFillInEmailInput(String text) throws IOException {
		PagesCollection.loginPage.setLogin(text);
	}

	@Then("^I see login in screen$")
	public void ThenISeeLogInScreen() {
		Assert.assertTrue("I don't see login screen",
				PagesCollection.loginPage.isLoginButtonVisible());
	}

	@When("I tap and hold on Email input")
	public void ITapHoldEmailInput() {
		PagesCollection.loginPage.tapHoldEmailInput();
	}

	@When("I click on popup SelectAll item")
	public void IClickPopupSelectAll() {
		PagesCollection.loginPage.clickPopupSelectAllButton();
	}

	@When("I click on popup Copy item")
	public void IClickPopupCopy() {
		PagesCollection.loginPage.clickPopupCopyButton();
	}

	@When("I copy email input field content")
	public void ICopyEmailInputContent() {
		PagesCollection.loginPage.tapHoldEmailInput();
		PagesCollection.loginPage.clickPopupSelectAllButton();
		PagesCollection.loginPage.clickPopupCopyButton();
	}

	@When("I click on popup Paste item")
	public void IClickPopupPaste() {
		PagesCollection.loginPage.clickPopupPasteButton();
	}

	@When("^I press Terms of Service link$")
	public void IPressTermsOfServiceLink() throws Throwable {
		PagesCollection.loginPage.openTermsLink();
	}

	@Then("^I see the terms info page$")
	public void ISeeTheTermsInfoPage() throws Throwable {
		Assert.assertTrue("I don't see terms of service page",
				PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	@When("^I return to welcome page$")
	public void IReturnToWelcomePage() throws Throwable {
		PagesCollection.loginPage.closeTermsPrivacyController();
		Assert.assertTrue("I don't see login screen",
				PagesCollection.loginPage.isLoginButtonVisible());
	}

	@When("^I press Privacy Policy link$")
	public void IPressPrivacyPolicyLink() throws Throwable {
		PagesCollection.loginPage.openPrivacyLink();
	}

	@Then("^I see the privacy info page$")
	public void ISeeThePrivacyInfoPage() throws Throwable {
		Assert.assertTrue("I don't see privacy policy page",
				PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	@When("^I enter wrong email (.*)")
	public void IEnterWrongEmail(String wrongMail) throws IOException {
		PagesCollection.loginPage.setLogin(wrongMail);
		PagesCollection.loginPage.tapPasswordField();
	}

	@Then("^I see error with email notification$")
	public void ISeeErrorWithEmailNotification() {
		Assert.assertTrue("I don't see error mail notification",
				PagesCollection.loginPage.errorMailNotificationIsShown());
	}

	@Then("^I see no error notification$")
	public void ISeeNoErrorNotification() {
		Assert.assertFalse("I see error mail notification",
				PagesCollection.loginPage.errorMailNotificationIsNotShown());
	}

	@When("^I enter wrong password (.*)")
	public void IEnterWrongPassword(String wrongPassword) throws IOException {
		PagesCollection.loginPage.setPassword(wrongPassword);
	}

	@Then("^I see wrong credentials notification$")
	public void ISeeWrongCredentialsNotification() {
		Assert.assertTrue("I don't see wrong credentials notification",
				PagesCollection.loginPage.wrongCredentialsNotificationIsShown());
	}

}
