package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.ClientUsersManager;
import com.wearezeta.auto.user_management.ClientUsersManager.UserAliasType;
import com.wearezeta.auto.user_management.UserState;

import cucumber.api.java.en.*;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
	public void IEnterEmail(String email) {
		try {
			String realEmail = usrMgr.findUserByAlias(email,
					UserAliasType.EMAIL).getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchElementException e) {
			this.userToRegister.setEmail(email);
		}
		this.userToRegister.clearEmailAliases();
		this.userToRegister.addEmailAlias(email);
		PagesCollection.registrationPage.setEmail(this.userToRegister
				.getEmail());
	}

	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) {
		try {
			String realPassword = usrMgr.findUserByAlias(password,
					UserAliasType.PASSWORD).getPassword();
			this.userToRegister.setPassword(realPassword);
		} catch (NoSuchElementException e) {
			this.userToRegister.setPassword(password);
		}
		this.userToRegister.clearPasswordAliases();
		this.userToRegister.addPasswordAlias(password);
		PagesCollection.registrationPage.setPassword(this.userToRegister
				.getPassword());
	}

	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.createDefaultInstance().startMboxListener(
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
		BackendAPIWrappers.activateRegisteredUser(this.listener);
		this.userToRegister.setUserState(UserState.Created);
	}
}
