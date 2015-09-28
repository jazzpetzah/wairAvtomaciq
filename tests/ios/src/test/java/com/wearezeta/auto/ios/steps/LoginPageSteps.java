package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.RegistrationPage;

import cucumber.api.java.en.*;

/**
 * Contains steps to work with Login/Welcome page
 *
 */
public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private LoginPage getLoginPage() throws Exception {
		return (LoginPage) pagesCollecton.getPage(LoginPage.class);
	}

	private RegistrationPage getRegistrationPage() throws Exception {
		return (RegistrationPage) pagesCollecton
				.getPage(RegistrationPage.class);
	}

	private Future<String> activationMessage;
	private static final String stagingURLForgot = "https://staging-website.zinfra.io/forgot/";

	/**
	 * Verifies whether sign in screen is the current screen
	 * 
	 * @step. I see sign in screen
	 * @throws Exception
	 */
	@Given("I see sign in screen")
	public void GiveniSeeSignInScreen() throws Exception {
		Assert.assertNotNull(getLoginPage().isVisible());
	}

	/**
	 * Enter user email and password into corresponding fields on sign in screen
	 * then taps "Sign In" button
	 * 
	 * @step. ^I sign in using my email$
	 * 
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("^I sign in using my email$")
	public void GivenISignInUsingEmail() throws Exception {
		Assert.assertNotNull(getLoginPage().isVisible());
		getLoginPage().signIn();

		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		emailLoginSequence(self.getEmail(), self.getPassword());
	}

	private void emailLoginSequence(String login, String password)
			throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		getLoginPage().setLogin(login);
		getLoginPage().setPassword(password);
		getLoginPage().login();
	}

	private void phoneLoginSequence(final PhoneNumber number) throws Exception {
		getLoginPage().clickPhoneLogin();

		getRegistrationPage().selectCodeAndInputPhoneNumber(
				number.toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""),
				PhoneNumber.WIRE_COUNTRY_PREFIX);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(number);

		getRegistrationPage().inputActivationCode(code);

		getLoginPage().waitForLoginToFinish();
	}

	/**
	 * Enter verification code for specified user
	 * 
	 * @step. ^I enter verification code for user (.*)$
	 * 
	 * @param name
	 *            name of user
	 * @throws Exception
	 */
	@When("^I enter verification code for user (.*)$")
	public void IEnterVerificationCodeForUser(String name) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		getRegistrationPage().inputActivationCode(code);
	}

	/**
	 * Inputs not valid verification code
	 * 
	 * @step. ^I enter random verification code$
	 * 
	 * @throws Exception
	 */
	@When("^I enter random verification code$")
	public void IEnterRandomVerificationCode() throws Exception {
		getRegistrationPage().inputRandomActivationCode();
	}

	/**
	 * Sends new verification code for specified user and enter previous one
	 * 
	 * @step. ^I enter verification code for user (.*)$
	 * 
	 * @param name
	 *            name of user
	 * @throws Exception
	 */
	@When("^I enter previous verification code for user (.*)$")
	public void IEnterPreviousVerificationCodeForUser(String name)
			throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		getRegistrationPage().clickResendCodeButton();
		getRegistrationPage().inputActivationCode(code);
	}

	/**
	 * Click on RESEND button to send new verification code
	 * 
	 * @step. ^I tap RESEND code button$"
	 * 
	 * @throws Exception
	 */
	@When("^I tap RESEND code button$")
	public void ITapResendCodeButton() throws Exception {
		getRegistrationPage().clickResendCodeButton();
	}

	/**
	 * Verify if PHONE SIGN IN button is visible
	 * 
	 * @step. ^I see PHONE SIGN IN button$
	 * 
	 * @throws Exception
	 */
	@When("^I see PHONE SIGN IN button$")
	public void ISeePhoneSignInButton() throws Exception {
		Assert.assertTrue("PHONE SIGN IN button is not visible", getLoginPage()
				.isPhoneSignInButtonVisible());
	}

	/**
	 * Tap on PHONE SIGN IN button is visible
	 * 
	 * @step. I tap on PHONE SIGN IN button
	 * 
	 * @throws Exception
	 */
	@When("I tap on PHONE SIGN IN button")
	public void ITapPhoneSignInButton() throws Exception {
		getLoginPage().clickPhoneLogin();
	}

	/**
	 * Verify country picker button presented
	 * 
	 * @step. ^I see country picker button on Sign in screen$
	 * 
	 * @throws Exception
	 */
	@When("^I see country picker button on Sign in screen$")
	public void ISeeCountryPickerButton() throws Exception {
		Assert.assertTrue("Country picker button is not visible",
				getLoginPage().isCountryPickerButttonVisible());
	}

	/**
	 * Verify verification code page shown
	 * 
	 * @step. ^I see verification code page$
	 * 
	 * @throws Exception
	 */
	@When("^I see verification code page$")
	public void ISeeVerificationCodePage() throws Exception {
		Assert.assertTrue(getRegistrationPage().isVerificationCodePageVisible());
	}

	/**
	 * Verify set email/password suggesstion page is shown
	 * 
	 * @step. ^I see set email/password suggesstion page$
	 * 
	 * @throws Exception
	 */
	@When("^I see set email/password suggesstion page$")
	public void ISeeSetEmailPassSuggestionPage() throws Exception {
		Assert.assertTrue(getLoginPage().isSetEmailPasswordSuggestionVisible());
	}

	/**
	 * Sign in with email/password (20%) or phone number (80%)
	 * 
	 * @step. ^I sign in using my email or phone number$
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("^I sign in using my email or phone number$")
	public void GivenISignInUsingEmailOrPhone() throws Exception {
		Assert.assertNotNull(getLoginPage().isVisible());
		getLoginPage().signIn();

		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		if (CommonUtils.trueInPercents(80)) {
			try {
				phoneLoginSequence(self.getPhoneNumber());
			} catch (BackendRequestException ex) {
				getLoginPage().switchToEmailLogin();
				emailLoginSequence(self.getEmail(), self.getPassword());
			}
		} else {
			emailLoginSequence(self.getEmail(), self.getPassword());
		}
	}

	/**
	 * Taps Sign In button on the corresponding screen and verifies whether an
	 * account is signed in properly
	 * 
	 * @step. I press Sign in button
	 * @throws Exception
	 */
	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws Exception {
		getLoginPage().signIn();
	}

	/**
	 * Tap I HAVE AN ACCOUNT button
	 * 
	 * @step. I tap I HAVE AN ACCOUNT button
	 * 
	 * @throws Exception
	 */
	@When("I tap I HAVE AN ACCOUNT button")
	public void ITapHaveAnAccount() throws Exception {
		WhenIPressSignInButton();
	}

	/**
	 * Taps Login button on the corresponding screen
	 * 
	 * @step. I press Login button
	 * 
	 * @throws IOException
	 */
	@When("I press Login button")
	public void WhenIPressSignInButtonAgain() throws Exception {
		getLoginPage().login();
	}

	/**
	 * Taps Login button on the corresponding screen
	 * 
	 * @step. I attempt to press Login button
	 * @throws Exception
	 */
	@When("I attempt to press Login button")
	public void IAttemptToPressLoginButton() throws Exception {
		getLoginPage().clickLoginButton();
	}

	/**
	 * Types login string into the corresponding input field on sign in page
	 * 
	 * @step. I have entered login (.*)
	 * 
	 * @param login
	 *            login string (usually it is user email)
	 * @throws IOException
	 */
	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getLoginPage().setLogin(login);
	}

	/**
	 * Types password string into the corresponding input field on sign in page
	 * 
	 * @step. I have entered password (.*)
	 * 
	 * @param password
	 *            password string
	 * @throws IOException
	 */
	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getLoginPage().setPassword(password);
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. I fill in email input (.*)
	 * 
	 * @param text
	 *            a string to type
	 */
	@When("I fill in email input (.*)")
	public void IFillInEmailInput(String text) throws Exception {
		getLoginPage().setLogin(text);
	}

	/**
	 * Verifies whether login screen is the current screen
	 * 
	 * @step. ^I see login in screen$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if current screen is not login screen
	 */
	@Then("^I see login in screen$")
	public void ThenISeeLogInScreen() throws Exception {
		Assert.assertTrue("I don't see login screen", getLoginPage()
				.isLoginButtonVisible());
	}

	/**
	 * Tap and holds the "finger" for a while on email input field
	 * 
	 * @step. I tap and hold on Email input
	 * @throws Exception
	 */
	@When("I tap and hold on Email input")
	public void ITapHoldEmailInput() throws Exception {
		getLoginPage().tapHoldEmailInput();
	}

	/**
	 * Taps "Select All" item in popup menu of an input field
	 * 
	 * @step. I click on popup SelectAll item
	 * @throws Exception
	 */
	@When("I click on popup SelectAll item")
	public void IClickPopupSelectAll() throws Exception {
		getLoginPage().clickPopupSelectAllButton();
	}

	/**
	 * Taps "Copy" item in popup menu of an input field
	 * 
	 * @step. I click on popup Copy item
	 * @throws Exception
	 */
	@When("I click on popup Copy item")
	public void IClickPopupCopy() throws Exception {
		getLoginPage().clickPopupCopyButton();
	}

	/**
	 * Copies the content of Email input field into clipboard
	 * 
	 * @step. I copy email input field content
	 * @throws Exception
	 */
	@When("I copy email input field content")
	public void ICopyEmailInputContent() throws Exception {
		getLoginPage().tapHoldEmailInput();
		getLoginPage().clickPopupSelectAllButton();
		getLoginPage().clickPopupCopyButton();
	}

	/**
	 * Taps "Paste" item in popup menu of an input field
	 * 
	 * @step. I click on popup Paste item
	 * @throws Exception
	 */
	@When("I click on popup Paste item")
	public void IClickPopupPaste() throws Exception {
		getLoginPage().clickPopupPasteButton();
	}

	/**
	 * Taps "Terms of Service" link on Welcome screen
	 * 
	 * @step. ^I press Terms of Service link$
	 */
	@When("^I press Terms of Service link$")
	public void IPressTermsOfServiceLink() throws Throwable {
		getLoginPage().openTermsLink();
	}

	/**
	 * Verifies whether the current page is "Terms and Conditions" page
	 * 
	 * @step. ^I see the terms info page$
	 * @throws AssertionError
	 *             if the current page is not "Terms and Conditions" page
	 */
	@Then("^I see the terms info page$")
	public void ISeeTheTermsInfoPage() throws Throwable {
		Assert.assertTrue("I don't see terms of service page", getLoginPage()
				.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	/**
	 * Closes "Terms and Conditions" page to return back to Welcome page
	 * 
	 * @step. ^I return to welcome page$
	 * @throws AssertionError
	 *             if the current page is not "Welcome" page
	 */
	@When("^I return to welcome page$")
	public void IReturnToWelcomePage() throws Throwable {
		getLoginPage().closeTermsPrivacyController();
		Assert.assertTrue("I don't see login screen", getLoginPage()
				.isLoginButtonVisible());
	}

	/**
	 * Taps "Privacy Policy" link on Welcome page
	 * 
	 * @step. ^I press Privacy Policy link$
	 */
	@When("^I press Privacy Policy link$")
	public void IPressPrivacyPolicyLink() throws Throwable {
		getLoginPage().openPrivacyLink();
	}

	/**
	 * Verifies whether the current page is "Privacy Info" page
	 * 
	 * @step. ^I see the privacy info page$
	 * 
	 * @throws AssertionError
	 *             if the current page differs from "Privacy Info" page
	 */
	@Then("^I see the privacy info page$")
	public void ISeeThePrivacyInfoPage() throws Throwable {
		Assert.assertTrue("I don't see privacy policy page", getLoginPage()
				.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	/**
	 * Enters given text into email input field and taps password field
	 * 
	 * @step. ^I enter wrong email (.*)
	 * @param wrongMail
	 *            text to enter into email input field
	 */
	@When("^I enter wrong email (.*)")
	public void IEnterWrongEmail(String wrongMail) throws Exception {
		getLoginPage().setLogin(wrongMail);
		getLoginPage().tapPasswordField();
	}

	/**
	 * Verifies whether error message about email field is visible
	 * 
	 * @step. ^I see error with email notification$
	 * @throws Exception
	 * @throws AssertionError
	 *             if error notification is not visible
	 */
	@Then("^I see error with email notification$")
	public void ISeeErrorWithEmailNotification() throws Exception {
		Assert.assertTrue("I don't see error mail notification", getLoginPage()
				.errorMailNotificationIsShown());
	}

	/**
	 * Verifies whether error message about email field is NOT visible
	 * 
	 * @step. ^I see no error notification$
	 * @throws Exception
	 * @throws AssertionError
	 *             if error notification is visible
	 */
	@Then("^I see no error notification$")
	public void ISeeNoErrorNotification() throws Exception {
		Assert.assertFalse("I see error mail notification", getLoginPage()
				.errorMailNotificationIsNotShown());
	}

	/**
	 * Enters a text into password input field
	 * 
	 * @step. ^I enter wrong password (.*)
	 */
	@When("^I enter wrong password (.*)")
	public void IEnterWrongPassword(String wrongPassword) throws Exception {
		getLoginPage().setPassword(wrongPassword);
	}

	/**
	 * Verifies whether the notification about wrong credentials exists on the
	 * current screen
	 * 
	 * @step. ^I see wrong credentials notification$
	 * @throws Exception
	 */
	@Then("^I see wrong credentials notification$")
	public void ISeeWrongCredentialsNotification() throws Exception {
		Assert.assertTrue("I don't see wrong credentials notification",
				getLoginPage().wrongCredentialsNotificationIsShown());
	}

	/**
	 * Verifies whether the notification Resend avialble in 10 min is shown
	 * 
	 * @step. ^I see Resend will be possible after 10 min aleart$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Resend will be possible after 10 min aleart$")
	public void ISeeResendIn10minAlert() throws Exception {
		Assert.assertTrue("I don't see Resend in 10 min alert", getLoginPage()
				.isResendIn10minAlertVisible());
	}

	/**
	 * Verifies whether the notification invalid phone number shown
	 * 
	 * @step. ^I see invalid phone number alert$
	 * 
	 * @throws Exception
	 */
	@Then("^I see invalid phone number alert$")
	public void ISeeInvalidPhoneNumberAlert() throws Exception {
		Assert.assertTrue("I don't see invalid phone number alert",
				getLoginPage().isInvalidPhoneNumberAlertShown());
	}

	/**
	 * Clicks on the Forgot/Change password button on the Sign In screen
	 * 
	 * @step. ^I click on Change Password button on SignIn$
	 * @throws Exception
	 */
	@When("^I click on Change Password button on SignIn$")
	public void IClickOnChangePasswordButtonOnSignIn() throws Exception {
		getLoginPage().tapChangePasswordButton();

	}

	@When("^I change URL to staging$")
	public void IChangeURLToStaging() throws Exception {
		getLoginPage().changeURLInBrowser(stagingURLForgot);
	}

	/**
	 * Types the mail into the field, where change password link should be send
	 * to
	 * 
	 * @step. ^I type in email (.*) to change password$
	 * @param email
	 * @throws Exception
	 */
	@When("^I type in email (.*) to change password$")
	public void ITypeInEmailToChangePassword(String email) throws Exception {
		email = usrMgr.replaceAliasesOccurences(email, FindBy.EMAIL_ALIAS);
		getLoginPage().tapEmailFieldToChangePassword(email);

		// activate the user, to get access to the mails
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", email);
		expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME,
				PasswordResetMessage.MESSAGE_PURPOSE);
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
	}

	/**
	 * Presses the change password button in the safari webview
	 * 
	 * @step. ^I press Change Password button in browser$
	 * @throws Exception
	 *
	 */
	@When("^I press Change Password button in browser$")
	public void IPressChangePasswordButtonInBrowser() throws Exception {
		getLoginPage().tapChangePasswordButtonInWebView();
	}

	/**
	 * Copies the link in the email and types it into the safari search field
	 * 
	 * @step. ^I copy link from email and past it into Safari
	 * @throws Exception
	 */
	@When("^I copy link from email and past it into Safari$")
	public void ICopyLinkFromEmailAndPastItIntoSafari() throws Exception {
		String link = BackendAPIWrappers
				.getPasswordResetLink(this.activationMessage);
		getLoginPage().changeURLInBrowser(link);
	}

	/**
	 * Types the new password into the password field
	 * 
	 * @step. ^I type in new password (.*)$
	 * @param newPassword
	 *            that gets set as new password by typing it into the field
	 * @throws Exception
	 */
	@When("^I type in new password (.*)$")
	public void ITypeInNewPassword(String newPassword) throws Exception {
		usrMgr.getSelfUserOrThrowError().setPassword(newPassword);
		getLoginPage().tapPasswordFieldToChangePassword(newPassword);
	}

	/**
	 * Returns in Simulator back to Wire App
	 * 
	 * @step. ^Return to Wire app$
	 * @throws Exception
	 * 
	 */
	@When("^Return to Wire app$")
	public void ReturnToWireApp() throws Exception {
		getLoginPage().pressSimulatorHomeButton();
	}

}
