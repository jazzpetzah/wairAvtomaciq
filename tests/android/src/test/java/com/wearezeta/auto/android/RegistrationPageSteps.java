package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.mail.MessagingException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.UserCreationHelper;
import com.wearezeta.auto.user_management.UsersManager;
import com.wearezeta.auto.user_management.UsersManager.UserAliasType;
import com.wearezeta.auto.user_management.UserState;

import cucumber.api.java.en.*;

public class RegistrationPageSteps {
	private final UsersManager usrMgr = UsersManager.getInstance();

	private ClientUser userToRegister = null;

	private MBoxChangesListener listener;

	@When("^I press Camera button twice$")
	public void WhenIPressCameraButton() throws IOException,
			InterruptedException {
		PagesCollection.registrationPage.takePhoto();
		Thread.sleep(1000);
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
		try {
			this.userToRegister = usrMgr.findUserByNameAlias(name);
		} catch (NoSuchElementException e) {
			this.userToRegister = new ClientUser();
			this.userToRegister.setName(name);
			this.userToRegister.addNameAlias(name);
		}
		PagesCollection.registrationPage.setName(this.userToRegister.getName());
	}

	@When("^I enter email (.*)$")
	public void IEnterEmail(String email) throws IOException {
		Map<String, String> userCredentails = UserCreationHelper
				.generateUniqUserCredentials(UserCreationHelper.getMboxName());
		this.userToRegister
				.setEmail(userCredentails.values().iterator().next());
		this.userToRegister.setId(userCredentails.keySet().iterator().next());
		PagesCollection.registrationPage.setEmail(this.userToRegister
				.getEmail());
	}

	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) throws IOException,
			MessagingException, InterruptedException {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByAlias(password,
					UserAliasType.PASSWORD).getPassword());
		} catch (NoSuchElementException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		PagesCollection.registrationPage.setPassword(this.userToRegister
				.getPassword());
	}

	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = UserCreationHelper.getMboxInstance().startMboxListener(
				expectedHeaders);

		PagesCollection.registrationPage.createAccount();
	}

	@Then("^I see confirmation page$")
	public void ISeeConfirmationPage() {
		Assert.assertTrue(PagesCollection.registrationPage
				.isConfirmationVisible());
	}

	@Then("^I verify registration address$")
	public void IVerifyRegistrationAddress() throws Throwable {
		UserCreationHelper.activateRegisteredUser(this.listener);
		this.userToRegister.setUserState(UserState.Created);
		usrMgr.appendCustomUser(this.userToRegister);
	}
}
