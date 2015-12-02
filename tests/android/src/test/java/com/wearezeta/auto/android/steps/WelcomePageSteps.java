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

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class WelcomePageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

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

	private ClientUser userToRegister = null;

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue("Welcome page is not shown", getWelcomePage()
				.waitForInitialScreen());
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
		getWelcomePage().tapIHaveAnAccount();
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

		if (this.userToRegister == null) {
			this.userToRegister = new ClientUser();
		}
		this.userToRegister.setName(name);
		this.userToRegister.clearNameAliases();
		this.userToRegister.addNameAlias(name);

		this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		String number = this.userToRegister.getPhoneNumber().toString();
		number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
		getWelcomePage().inputPhoneNumber(number);
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
		final String verificationCode = BackendAPIWrappers
				.getActivationCodeByPhoneNumber(phoneNumber);
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

	@When("^I input random activation code$")
	public void i_input_random_activation_code() throws Throwable {

	}

	@Then("^I see invalid code alert$")
	public void i_see_invalid_code_alert() throws Throwable {

	}

}
