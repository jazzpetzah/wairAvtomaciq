package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
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
	 * Press the "I have an account" button on the welcome page. to switch to
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
	 * Opens the area code chooser by selecting the area code button in front of the
	 * phone number, and then selects the given area code
	 * 
	 * @step. ^I set the area code to (.*)$
	 * 
	 * @param areaCode
	 * @throws Exception
	 */
	@When("^I set the area code to (.*)$")
	public void WhenISetTheAreaCodeTo(String areaCode) throws Exception {
		PagesCollection.areaCodePage = PagesCollection.welcomePage.clickAreaCodeSelector();
		PagesCollection.welcomePage = PagesCollection.areaCodePage.selectAreaCode(areaCode);
	}
	
	/**
	 * 
	 * 
	 * @step. ^I input a new phone number (.*)$
	 * 
	 * @param phoneNumber
	 * @throws Exception
	 */
	@When("^I input a new phone number (.*)$")
	public void WhenIInputANewPhoneNumber(String phoneNumber) throws Exception {
		PagesCollection.welcomePage.inputPhoneNumber(phoneNumber);
	}
	
	@When("^I confirm the phone number$")
	public void IConfirmThePhoneNumber() throws Exception {
		PagesCollection.verificationPage = PagesCollection.welcomePage.clickConfirm();
	}
	
	@When("^I input the verification code$")
	public void IInputTheVerificationCode() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("+0", "0106151249");//usrMgr.f
		String verificationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(phoneNumber);
		PagesCollection.verificationPage.inputVerificationCode(verificationCode);
	}
	
	@When("^I confirm the verification code$")
	public void IConfirmTheVerificationCode() throws Exception {
		PagesCollection.addNamePage = PagesCollection.verificationPage.clickConfirm();
	}
	
	@When("^I input my name$")
	public void IInputMyName() throws Exception {
		String name = "testUser";
		PagesCollection.addNamePage.inputName(name);
	}
	
	@When("^I confirm my name$")
	public void IConfirmMyName() throws Exception {
		PagesCollection.profilePicturePage = PagesCollection.addNamePage.clickConfirm();
	}
	
}
