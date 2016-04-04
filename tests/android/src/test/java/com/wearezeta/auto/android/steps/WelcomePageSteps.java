package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.registration.AddNamePage;
import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.PhoneNumberVerificationPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class WelcomePageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

	private WelcomePage getWelcomePage() throws Exception {
		return pagesCollection.getPage(WelcomePage.class);
	}

	private AreaCodePage getAreaCodePage() throws Exception {
		return pagesCollection.getPage(AreaCodePage.class);
	}

	private PhoneNumberVerificationPage getVerificationPage() throws Exception {
		return pagesCollection.getPage(PhoneNumberVerificationPage.class);
	}

	private AddNamePage getAddNamePage() throws Exception {
		return pagesCollection.getPage(AddNamePage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final String ERROR_CODE_ALERT_HEADER = "Invalid Code";
	private static final String ERROR_CODE_ALERT_MESSAGE = "Please enter a valid code.";

	private ClientUser userToRegister = null;

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue("Welcome page is not shown", getWelcomePage().waitForInitialScreen());
	}

	/**
	 * Press the "I have an account" button on the welcome page to switch to
	 * sign in using email address
	 * 
	 * @step. ^I switch to email sign in screen$
	 * @throws Exception
	 */
	@When("^I switch to email sign in screen$")
	public void ISwitchToEmailSignIn() throws Exception {
		getWelcomePage().tapSignInTab();
	}

	/**
	 * 
	 * 
	 * @step. ^I input a new phone number (.*)$
	 * 
	 * @param name
	 *            user name/alias
	 * @throws Exception
	 */
	@When("^I input a new phone number for user (.*)$")
	public void WhenIInputANewPhoneNumber(String name) throws Exception {
		getWelcomePage().clickAreaCodeSelector();
		getAreaCodePage().selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);

		this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		getWelcomePage().inputPhoneNumber(this.userToRegister.getPhoneNumber());
		getWelcomePage().clickConfirm();
	}

	/**
	 * Get the verification code from the backend, type it into the
	 * corresponding input field and confirm the input
	 * 
	 * @step. ^I input the verification code$
	 * 
	 * @throws Exception
	 */
	@When("^I input the verification code$")
	public void IInputTheVerificationCode() throws Exception {
		final PhoneNumber phoneNumber = this.userToRegister.getPhoneNumber();
		final String verificationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(phoneNumber);
		getVerificationPage().inputVerificationCode(verificationCode);
		getVerificationPage().clickConfirm();
	}

	/**
	 * Enter the user name on the registration form
	 * 
	 * @step. ^I input my name$
	 * 
	 * @throws Exception
	 */
	@When("^I input my name$")
	public void IInputMyName() throws Exception {
		final String name = this.userToRegister.getName();
		getAddNamePage().inputName(name);
		getAddNamePage().clickConfirm();
	}

	/**
	 * Inputs a random not correct code into the field
	 *
	 * @throws Exception
	 * @step. ^I input random activation code$
	 */
	@When("^I input random activation code$")
	public void IInputRandomActivationCode() throws Exception {
		final String randomVerificationCode = CommonUtils.generateRandomNumericString(6);
		getVerificationPage().inputVerificationCode(randomVerificationCode);
		getVerificationPage().clickConfirm();
	}

	/**
	 * Verifies the correct invalid code error alert
	 *
	 * @throws Exception
	 * @step. ^I see invalid code alert$
	 */
	@Then("^I see invalid code alert$")
	public void ISeeInvalidCodeAlert() throws Exception {
		Assert.assertEquals(ERROR_CODE_ALERT_HEADER, getVerificationPage().getErrorAlertHeader());
		Assert.assertEquals(ERROR_CODE_ALERT_MESSAGE, getVerificationPage().getErrorAlertMessage());
	}

}
