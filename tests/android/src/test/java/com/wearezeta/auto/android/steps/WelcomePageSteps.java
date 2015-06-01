package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

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
		Assert.assertTrue(PagesCollection.welcomePage.waitForInitialScreen());
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
		PagesCollection.emailSignInPage = PagesCollection.welcomePage
			.clickIHaveAnAccount();
	}
	
	/**
	 * 
	 * 
	 * @step. ^I input a new phone number (.*)$
	 * 
	 * @param phoneNumber
	 * @throws Exception
	 */
	@When("^I input a new phone number for user (.*)$")
	public void WhenIInputANewPhoneNumber(String name) throws Exception {
		PagesCollection.areaCodePage = PagesCollection.welcomePage.clickAreaCodeSelector();
		PagesCollection.welcomePage = PagesCollection.areaCodePage.selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
		
		if (this.userToRegister == null) {
			this.userToRegister = new ClientUser();
		}
		this.userToRegister.setName(name);
		this.userToRegister.clearNameAliases();
		this.userToRegister.addNameAlias(name);

		this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		String number = this.userToRegister.getPhoneNumber().toString();
		number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
		PagesCollection.welcomePage.inputPhoneNumber(number);
		
		PagesCollection.verificationPage = PagesCollection.welcomePage.clickConfirm();
	}
	
	@When("^I input the verification code$")
	public void IInputTheVerificationCode() throws Exception {
		PhoneNumber phoneNumber = this.userToRegister.getPhoneNumber();
		String verificationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(phoneNumber);
		PagesCollection.verificationPage.inputVerificationCode(verificationCode);
		PagesCollection.addNamePage = PagesCollection.verificationPage.clickConfirm();
	}
	
	@When("^I input my name$")
	public void IInputMyName() throws Exception {
		String name = this.userToRegister.getName();
		PagesCollection.addNamePage.inputName(name);
		PagesCollection.profilePicturePage = PagesCollection.addNamePage.clickConfirm();
	}
	
}
