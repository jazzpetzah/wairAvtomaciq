package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.UsersState;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {

	private String aqaName;

	private String aqaEmail;

	private String aqaPassword;

	boolean generateUsers = false;

	@When("^I press Camera button twice$")
	public void WhenIPressCameraButton() throws IOException {

		PagesCollection.registrationPage.takePhoto();
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
		Assert.assertTrue(PagesCollection.registrationPage.isConfirmationVisible());
	}

	@Then("^I verify registration address$")
	public void IVerifyRegistrationAddress() throws Throwable {

		CreateZetaUser.activateRegisteredUser(aqaEmail, 10, aqaEmail, aqaPassword);
	}

	@Then("^I press continue registration$")
	public void IPressContinue() throws Throwable {

		PagesCollection.instructionsPage = PagesCollection.registrationPage.continueRegistration();
		ClientUser myContact = new ClientUser(aqaEmail, aqaPassword, aqaName, UsersState.AllContactsConnected);
		CommonUtils.yourUsers = new ArrayList<ClientUser>();
		CommonUtils.yourUsers.add(myContact);
	}

}
