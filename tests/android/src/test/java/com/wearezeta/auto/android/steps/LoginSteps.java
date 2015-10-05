package com.wearezeta.auto.android.steps;

import java.util.Random;

import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.android.pages.registration.PhoneNumberVerificationPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private final static int DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS = 60 * 2;

	private EmailSignInPage getEmailSignInPage() throws Exception {
		return pagesCollection.getPage(EmailSignInPage.class);
	}

	private WelcomePage getWelcomePage() throws Exception {
		return pagesCollection.getPage(WelcomePage.class);
	}

	private AreaCodePage getAreaCodePage() throws Exception {
		return pagesCollection.getPage(AreaCodePage.class);
	}

	private PhoneNumberVerificationPage getVerificationPage() throws Exception {
		return pagesCollection.getPage(PhoneNumberVerificationPage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Inputs the login details for the self user and then clicks the sign in
	 * button.
	 * 
	 * @step. ^I sign in using my email$
	 * 
	 * @throws Exception
	 */
	@Given("^I sign in using my email$")
	public void ISignInUsingMyEmail() throws Exception {
		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		assert getWelcomePage().waitForInitialScreen() : "The initial screen was not shown";
		getWelcomePage().tapIHaveAnAccount();
		try {
			getEmailSignInPage().setLogin(self.getEmail());
		} catch (NoSuchElementException e) {
			// FIXME: try again because sometimes tapping "I have account"
			// button fails without any reason
			getWelcomePage().tapIHaveAnAccount();
			getEmailSignInPage().setLogin(self.getEmail());
		}
		getEmailSignInPage().setPassword(self.getPassword());
		getEmailSignInPage().logIn(true, DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS);
	}

	/**
	 * Do sign in using phone number
	 * 
	 * @step. ^I sign in using my phone number$
	 * 
	 * @throws Exception
	 */
	@Given("^I sign in using my phone number$")
	public void ISignInUsingMyPhoneNumber() throws Exception {
		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		assert getWelcomePage().waitForInitialScreen() : "The initial screen was not shown";
		getWelcomePage().clickAreaCodeSelector();
		try {
			getAreaCodePage().selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
		} catch (NoSuchElementException e) {
			// FIXME: Sometimes the area code selector button is not clicked
			getWelcomePage().clickAreaCodeSelector();
			getAreaCodePage().selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
		}
		getWelcomePage().inputPhoneNumber(
				self.getPhoneNumber().toString()
						.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
		getWelcomePage().clickConfirm();
		final String verificationCode = BackendAPIWrappers
				.getLoginCodeByPhoneNumber(self.getPhoneNumber());
		getVerificationPage().inputVerificationCode(verificationCode);
		getVerificationPage().clickConfirm();
		assert getVerificationPage().waitUntilConfirmButtonDissapears() : "Phone number verification code input screen is still visible";
	}

	private static final int PHONE_NUMBER_LOGIN_THRESHOLD = 60;
	private static final Random random = new Random();

	/**
	 * Enter self user credentials into the corresponding fields on sign in
	 * screen and click Sign In button. Sometimes this step uses phone number to
	 * sign in and sometimes it uses email address
	 * 
	 * @step. ^I sign in using my email or phone number$
	 * 
	 * @throws Exception
	 */
	@Given("^I sign in using my email or phone number$")
	public void ISignInUsingMyEmailOrPhoneNumber() throws Exception {
		if (random.nextInt(100) < PHONE_NUMBER_LOGIN_THRESHOLD) {
			ISignInUsingMyPhoneNumber();
		} else {
			ISignInUsingMyEmail();
		}
	}

	/**
	 * Types an email address into the email login field
	 * 
	 * @step. ^I have entered login (.*)$
	 * 
	 * @param login
	 * @throws Exception
	 */
	@When("^I have entered login (.*)$")
	public void WhenIHaveEnteredLogin(String login) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getEmailSignInPage().setLogin(login);
	}

	/**
	 * Enters a password into the password login field
	 * 
	 * @step. ^I have entered password (.*)$
	 * 
	 * @param password
	 * @throws Exception
	 */
	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getEmailSignInPage().setPassword(password);
	}

	/**
	 * Presses the Log in Button underneath the email and password fields
	 * 
	 * @step. ^I press Log in button$
	 * 
	 * @throws Exception
	 */
	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {
		getEmailSignInPage().logIn(false, DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS);
	}

	/**
	 * Checks to see that the login error message contains the correct text
	 * After providing a false email address or password
	 * 
	 * @step. ^I see error message \"(.*)\"$
	 * @param expectedMsg
	 *            the expected error message
	 * 
	 * @throws Exception
	 */
	@Then("^I see error message \"(.*)\"$")
	public void ISeeErrorMessage(String expectedMsg) throws Exception {
		getEmailSignInPage().verifyErrorMessageText(expectedMsg);
	}
}
