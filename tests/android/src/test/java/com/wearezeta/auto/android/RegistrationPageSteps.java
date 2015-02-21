package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.*;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ClientUser userToRegister = null;

	private MBoxChangesListener listener;

	@When("^I press Camera button twice$")
	public void WhenIPressCameraButton() throws IOException,
			InterruptedException {
		PagesCollection.registrationPage.takePhoto();
		Thread.sleep(2000);
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
	public void ISeeSelectedPicture() throws Exception {
		Assert.assertTrue(PagesCollection.registrationPage.isPictureSelected());
	}

	@When("^I confirm selection$")
	public void IConfirmSelection() throws IOException {
		PagesCollection.registrationPage.confirmPicture();
	}

	@When("^I enter name (.*)$")
	public void IEnterName(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.addNameAlias(name);
		}
		PagesCollection.registrationPage.setName(this.userToRegister.getName());
	}

	@When("^I enter email (.*)$")
	public void IEnterEmail(String email) throws Exception {
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setEmail(email);
		}
		this.userToRegister.clearEmailAliases();
		this.userToRegister.addEmailAlias(email);
		PagesCollection.registrationPage.setEmail(this.userToRegister
				.getEmail());
	}

	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) throws Exception {
		try {
			String realPassword = usrMgr.findUserByPasswordAlias(password)
					.getPassword();
			this.userToRegister.setPassword(realPassword);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setPassword(password);
		}
		this.userToRegister.clearPasswordAliases();
		this.userToRegister.addPasswordAlias(password);
		PagesCollection.registrationPage.setPassword(this.userToRegister
				.getPassword());
	}

	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		PagesCollection.registrationPage.createAccount();

		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.getInstance().startMboxListener(
				expectedHeaders);
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
		PagesCollection.contactListPage = PagesCollection.registrationPage.continueRegistration();
	}
	
	/**
	 * Activates user using browser by URL from mail
	 * 
	 * @step. ^I activate user by URL$
	 * 
	 * @throws Exception
	 */
	@Then("^I activate user by URL$")
	public void WhenIActivateUserByUrl() throws Exception{
		String link = BackendAPIWrappers.getUserActivationLink(this.listener);
		PagesCollection.peoplePickerPage = PagesCollection.registrationPage.activateByLink(link);
	}
}
