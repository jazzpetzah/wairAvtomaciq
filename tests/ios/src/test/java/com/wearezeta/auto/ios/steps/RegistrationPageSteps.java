package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.LanguageUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.RegistrationPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private RegistrationPage getRegistrationPage() throws Exception {
		return (RegistrationPage) pagesCollecton
				.getPage(RegistrationPage.class);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
	}

	private ClientUser userToRegister = null;

	private Future<String> activationMessage;

	public static final int maxCheckCnt = 2;

	boolean generateUsers = false;
	public static BufferedImage basePhoto;
	BufferedImage templateImage;
	BufferedImage referenceImage;
	BufferedImage profileImage;

	@Then("I see Take or select photo label and smile")
	public void ISeeTakeOrSelectPhotoLabel() throws Exception {
		Assert.assertTrue(getRegistrationPage()
				.isTakeOrSelectPhotoLabelVisible());
		Assert.assertTrue(getRegistrationPage().isTakePhotoSmileDisplayed());
	}

	@Then("I don't see Take or select photo label and smile")
	public void IDontSeeTakeOrSelectPhotoLabel() throws Exception {
		Assert.assertFalse(getRegistrationPage()
				.isTakeOrSelectPhotoLabelVisible());
	}

	@When("^I press Camera button on Registration page$")
	public void WhenIPressCameraButtonOnRegistrationPage() throws Exception {
		getRegistrationPage().clickCameraButton();
	}

	@When("^I take photo by front camera$")
	public void WhenITakePhotoByFrontCamera() throws Exception {
		getRegistrationPage().takePhotoByFrontCamera();
	}

	@When("^I take photo by rear camera$")
	public void WhenITakePhotoByRearCamera() throws Exception {
		getRegistrationPage().takePhotoByRearCamera();
		basePhoto = getRegistrationPage().takeScreenshot().orElseThrow(
				AssertionError::new);
		Thread.sleep(3000);
	}

	@When("^I See photo taken$")
	public void ISeePhotoTaken() throws Exception {
		Assert.assertTrue(getRegistrationPage().isPictureSelected());
	}

	/**
	 * Verify Set picture page shown
	 * 
	 * @step. I see Set Picture page
	 * 
	 * @throws Exception
	 */
	@When("^I see Set Picture page$")
	public void ISeeSetPicturePage() throws Exception {
		getRegistrationPage().isSetPicturePageVisible();
	}

	@When("^I press Picture button$")
	public void WhenIPressPictureButton() throws Exception {
		getRegistrationPage().selectPicture();
	}

	@When("^I See selected picture$")
	public void ISeeSelectedPicture() throws Exception {
		templateImage = getRegistrationPage().takeScreenshot().orElseThrow(
				AssertionError::new);
		Assert.assertTrue(getRegistrationPage().isPictureSelected());
	}

	@When("I reject selected picture$")
	public void IRejectSelectedPicture() throws Exception {
		getRegistrationPage().cancelImageSelection();
	}

	@When("^I confirm selection$")
	public void IConfirmSelection() throws Exception {
		getRegistrationPage().confirmPicture();
	}

	@When("I click Back button")
	public void IClickBackButton() throws Exception {
		getRegistrationPage().tapBackButton();
	}

	@When("^I verify back button$")
	public void IVerifyBackButton() throws Exception {
		Assert.assertTrue("I don't see the back button", getRegistrationPage()
				.isBackButtonVisible());
	}

	@When("I click Forward Button")
	public void IClickForwardButton() throws Exception {
		getRegistrationPage().tapForwardButton();
	}

	@When("I see Vignette overlay")
	public void ISeeVignetteOverlay() throws Exception {
		Assert.assertTrue(getRegistrationPage().isVignetteOverlayVisible());
	}

	@When("I click Vignette overlay")
	public void IClickVignetteOverlay() throws Exception {
		getRegistrationPage().clickVignetteLayer();
	}

	@When("I dismiss Vignette overlay")
	public void IDismissVignetteOverlay() throws Exception {
		getRegistrationPage().dismissVignetteBakground();
	}

	@When("I don't see Vignette overlay")
	public void IDontSeeVignetteOverlay() throws Exception {
		Assert.assertFalse(getRegistrationPage().isVignetteOverlayVisible());
	}

	@When("I see full color mode")
	public void ISeeColorMode() throws Exception {
		Assert.assertTrue(getRegistrationPage().isColorModeVisible());
	}

	@When("I click close full color mode button")
	public void IClickCloseColorModeButton() throws Exception {
		getRegistrationPage().tapCloseColorModeButton();
	}

	@When("I see Registration name input")
	public void ISeeRegistrationNameInput() throws Exception {
		Assert.assertTrue(getRegistrationPage().isNameLabelVisible());
	}

	/**
	 * Input fake phone number for given user
	 * 
	 * @param name
	 *            User name alias
	 * @throws Exception
	 */
	@When("^I enter phone number for user (.*)$")
	public void IEnterPhoneNumber(String name) throws Exception {
		if (this.userToRegister == null) {
			this.userToRegister = new ClientUser();
		}
		this.userToRegister.setName(name);
		this.userToRegister.clearNameAliases();
		this.userToRegister.addNameAlias(name);

		this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		String number = this.userToRegister.getPhoneNumber().toString();
		number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
		getRegistrationPage().selectCodeAndInputPhoneNumber(number,
				PhoneNumber.WIRE_COUNTRY_PREFIX);
	}

	/**
	 * Input phone number of allready registered user
	 * 
	 * @step. ^I input phone number of already registered user (.*)$
	 * 
	 * @param name
	 *            username
	 * @throws Exception
	 */
	@When("^I input phone number of already registered user (.*)$")
	public void IInputPhoneNumberOfRegisteredUser(String name) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String number = user.getPhoneNumber().toString();
		number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
		getRegistrationPage().selectCodeAndInputPhoneNumber(number,
				PhoneNumber.WIRE_COUNTRY_PREFIX);
	}

	/**
	 * Input in sign in by phone number page a random phone number
	 * 
	 * @step. ^I enter random phone number$
	 * 
	 * @throws Exception
	 */
	@When("^I enter random phone number$")
	public void IEnterRandomePhoneNumber() throws Exception {
		getRegistrationPage().inputPhoneNumber(
				CommonUtils.generateRandomXdigits(7));
	}

	/**
	 * Input in phone number field page phone number with code
	 * 
	 * @step. ^I input phone number (.*) with code (.*)$
	 * @param number
	 *            phone number
	 * @param code
	 *            country code
	 * @throws Exception
	 */
	@When("^I input phone number (.*) with code (.*)$")
	public void IInputPhoneNumber(String number, String code) throws Exception {
		getRegistrationPage().selectCodeAndInputPhoneNumber(number, code);
	}

	/**
	 * Input in phone number field page an invalid phone number
	 * 
	 * @step. ^I enter invalid phone number$
	 * 
	 * @throws Exception
	 */
	@When("^I enter invalid phone number$")
	public void IEnterInvalidPhoneNumber() throws Exception {
		getRegistrationPage().inputPhoneNumber(
				CommonUtils.generateRandomXdigits(11));
	}

	/**
	 * Input in phone number field page a random X digits
	 * 
	 * @step. ^I enter (.*) digits phone number
	 * 
	 * @throws Exception
	 */
	@When("^I enter (.*) digits phone number$")
	public void IEnterXDigitesPhoneNumber(int x) throws Exception {
		getRegistrationPage().inputPhoneNumber(
				CommonUtils.generateRandomXdigits(x));
	}

	/**
	 * Click on I AGREE button to accept terms of service
	 * 
	 * @throws Exception
	 */
	@When("^I accept terms of service$")
	public void IAcceptTermsOfService() throws Exception {
		getRegistrationPage().clickAgreeButton();
	}

	/**
	 * Verify Term Of Use page shown
	 * 
	 * @step. ^I see Term Of Use page$
	 * 
	 * @throws Exception
	 */
	@When("^I see Term Of Use page$")
	public void ISeeTermOfUsePage() throws Exception {
		Assert.assertTrue(getRegistrationPage().isTermOfUsePageVisible());
	}

	/**
	 * Input activation code generated for fake phone number
	 * 
	 * @throws Exception
	 */
	@When("^I enter activation code$")
	public void IEnterActivationCode() throws Exception {
		String code = BackendAPIWrappers
				.getActivationCodeByPhoneNumber(this.userToRegister
						.getPhoneNumber());
		getRegistrationPage().inputActivationCode(code);
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
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		getRegistrationPage().setName(this.userToRegister.getName());
	}

	@When("^I enter a username which is at most (\\d+) characters long from (\\w+) alphabet$")
	public void IEnterNameWithCharacterLimit(int charactersLimit,
			String alphabetName) throws Throwable {
		String nameToType = LanguageUtils.generateRandomString(charactersLimit,
				alphabetName).replace('\\', '|');
		IEnterName(nameToType);
		getRegistrationPage().inputName();
	}

	@When("^I input name (.*) and hit Enter$")
	public void IInputNameAndHitEnter(String name) throws Exception {
		IEnterName(name);
		getRegistrationPage().inputName();
	}

	/**
	 * Fill in name field username with leading and trailing spaces
	 * 
	 * @step. ^I fill in name (.*) with leading and trailing spaces and hit
	 *        Enter$
	 * 
	 * @param name
	 *            username
	 * @throws Exception
	 */
	@When("^I fill in name (.*) with leading and trailing spaces and hit Enter$")
	public void IInputNameWithSpacesAndHitEnter(String name) throws Exception {
		getRegistrationPage().setName("  " + name + "  ");
		getRegistrationPage().inputName();
	}

	/**
	 * Fill in name field username with leading and trailing spaces on iPad
	 * 
	 * @step. ^I fill in name (.*) with leading and trailing spaces on iPad
	 * 
	 * @param name
	 *            username
	 * @throws Exception
	 */
	@When("^I fill in name (.*) with leading and trailing spaces on iPad$")
	public void IInputNameWithSpacesOnIpad(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.clearNameAliases();
			this.userToRegister.addNameAlias(name);
		}
		getRegistrationPage().setName("  " + userToRegister.getName() + "  ");
	}

	@Then("^I verify that my username is at most (\\d+) characters long$")
	public void IVerifyUsernameLength(int charactersLimit) throws Exception {
		String realUserName = getRegistrationPage().getUsernameFieldValue();
		int usernameLength = LanguageUtils.getUnicodeStringAsCharList(
				realUserName).size();
		Assert.assertTrue(charactersLimit >= usernameLength);
	}

	@When("^I enter email (.*)$")
	public void IEnterEmail(String email) throws Exception {
		boolean flag = false;
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			flag = true;
		}

		if (flag) {
			getRegistrationPage().setEmail(email + "\n");
		} else {
			getRegistrationPage().setEmail(
					this.userToRegister.getEmail() + "\n");
		}
	}

	/**
	 * Step that should be used for iOS registration tests before starting
	 * monitoring email
	 * 
	 * @step. ^My user email is (.*)$
	 * 
	 * @param email
	 *            user email
	 * @throws Exception
	 */
	@When("^My user email is (.*)$")
	public void UserToRegisterEmailSettinTo(String email) throws Exception {
		try {
			String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
					.getEmail();
			this.userToRegister.setEmail(realEmail);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
		}
	}

	@When("^I input email (.*) and hit Enter$")
	public void IInputEmailAndHitEnter(String email) throws Exception {
		IEnterEmail(email);
	}

	@When("^I attempt to enter an email with spaces (.*)$")
	public void IEnterEmailWithSpaces(String email) throws Exception {
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
		getRegistrationPage().setEmail(
				new StringBuilder(this.userToRegister.getEmail()).insert(
						email.length() - 1, "          ").toString());
	}

	@When("^I enter an incorrect email (.*)$")
	public void IEnterIncorrectEmail(String email) throws Exception {
		getRegistrationPage().setEmail(email);
	}

	@When("I clear email input field on Registration page")
	public void IClearEmailInputRegistration() throws Exception {
		getRegistrationPage().clearEmailInput();
	}

	@Then("^I verify no spaces are present in email$")
	public void CheckForSpacesInEmail() throws Exception {
		String realEmailText = getRegistrationPage().getEmailFieldValue();
		String initialEmailText = getRegistrationPage().getEmail();
		Assert.assertTrue(initialEmailText.replace(" ", "").equals(
				realEmailText));
	}

	@When("^I attempt to enter emails with known incorrect formats$")
	public void IEnterEmailWithIncorrectFormat() throws Exception {
		// current design has basic email requirements: contains single @,
		// contains a domain name with a dot + domain extension(min 2
		// characters)
		String[] listOfInvalidEmails = { "abc.example.com", "abc@example@.com",
				"example@zeta", "abc@example."/* , "abc@example.c" */};
		// test fails because minimum 2 character domain extension is not
		// implemented(allows for only 1)
		getRegistrationPage().setListOfEmails(listOfInvalidEmails);
	}

	@Then("^I verify that the app does not let me continue$")
	public void IVerifyIncorrectFormatMessage() throws Exception {
		Assert.assertTrue(getRegistrationPage().typeAllInvalidEmails());
	}

	@When("^I enter password (.*)$")
	public void IEnterPassword(String password) throws Exception {
		try {
			this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
					password).getPassword());
		} catch (NoSuchUserException e) {
			this.userToRegister.setPassword(password);
			this.userToRegister.addPasswordAlias(password);
		}
		getRegistrationPage().setPassword(this.userToRegister.getPassword());
	}

	@When("^I input password (.*) and hit Enter$")
	public void IInputPasswordAndHitEnter(String password) throws Exception {
		IEnterPassword(password);
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
		getRegistrationPage().inputPassword();
	}

	@When("I click Create Account Button")
	public void IClickCreateAccountButton() throws Exception {
		getRegistrationPage().clickCreateAccountButton();
	}

	@Then("Contact list loads with only my name (.*)")
	public void ContactListLoadsWithOnlyMyName(String name) throws Throwable {
		name = this.userToRegister.getName();
		getContactListPage().waitForContactListToLoad();
		Assert.assertTrue(
				"My username is not displayed first",
				getContactListPage().isMyUserNameDisplayedFirstInContactList(
						name));
	}

	@Then("I see Create Account button disabled")
	public void ISeeCreateAccountButtonDisabled() throws Exception {
		Assert.assertFalse(getRegistrationPage().isCreateAccountEnabled());
	}

	@Then("^I navigate throughout the registration pages and see my input$")
	public void NavigateAndVerifyInput() throws Exception {
		getRegistrationPage().verifyUserInputIsPresent(
				this.userToRegister.getName(), this.userToRegister.getEmail());
	}

	@When("I navigate from password screen back to Welcome screen")
	public void NaviateFromPassScreenToWelcomeScreen() throws Exception {
		getRegistrationPage().navigateToWelcomePage();
	}

	/**
	 * Navigates from any page in the registration process, back to the welcome
	 * page
	 * 
	 * @step. I navigate back to welcome page
	 * @throws Exception
	 */
	@When("^I navigate back to welcome page")
	public void INavigateToWelcomePage() throws Exception {
		getRegistrationPage().navigateToWelcomePage();
	}

	@When("I input user data")
	public void IInputUserData() throws Exception {
		getRegistrationPage().typeInRegistrationData();
	}

	@When("^I submit registration data$")
	public void ISubmitRegistrationData() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
		getRegistrationPage().createAccount();
	}

	/**
	 * Verifies that mailbox contains at least X emails for the current
	 * recipient
	 * 
	 * @step. ^I confirm that inbox contains (\\d+) emails? for current
	 *        recipient$
	 * 
	 * @param expectedCnt
	 *            expected messages count
	 * @throws Exception
	 */
	@Then("^I confirm that inbox contains (\\d+) emails? for current recipient$")
	public void VerifyRecipientsCount(int expectedCnt) throws Exception {
		String expectedRecipient = this.userToRegister.getEmail();
		getRegistrationPage().waitUntilEmailsCountReachesExpectedValue(
				expectedCnt, expectedRecipient,
				BackendAPIWrappers.ACTIVATION_TIMEOUT);
	}

	@Then("^I resend verification email$")
	public void IResendVerificationEmail() throws Exception {
		getRegistrationPage().reSendEmail();
	}

	@When("^I see error page$")
	public void ISeeErrorPage() throws Exception {
		Assert.assertTrue(getRegistrationPage().confirmErrorPage());
	}

	@Then("^I return to the email page from error page$")
	public void IReturntoEmailPageFromErrorPage() throws Exception {
		getRegistrationPage().backToEmailPageFromErrorPage();
	}

	@Then("^I see confirmation page$")
	public void ISeeConfirmationPage() throws Exception {
		Assert.assertTrue(getRegistrationPage().isConfirmationShown());
	}

	/**
	 * Start monitoring thread for activation email. Please put this step BEFORE
	 * you submit the registration form
	 * 
	 * @step. ^I start activation email monitoring$
	 * 
	 * @throws Exception
	 */
	@When("^I start activation email monitoring$")
	public void IStartActivationEmailMonitoring() throws Exception {
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.activationMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
	}

	@Then("^I verify registration address$")
	public void IVerifyRegistrationAddress() throws Exception {
		BackendAPIWrappers
				.activateRegisteredUserByEmail(this.activationMessage);
		userToRegister.setUserState(UserState.Created);
	}

	@When("I don't see Next button")
	public void IDontSeeNextButton() throws Exception {
		Assert.assertFalse(getRegistrationPage().isNextButtonPresented());
	}

	@When("^I see selected image set as background$")
	public void ISeeSelectedImageSetAsBackground() throws Throwable {
		templateImage = getRegistrationPage().takeScreenshot().orElseThrow(
				AssertionError::new);
	}

	@When("^I see photo set as background$")
	public void ISeePhotoSetAsBackground() throws Throwable {
		referenceImage = getRegistrationPage().takeScreenshot().orElseThrow(
				AssertionError::new);
	}

	@Then("^I see photo is set as profile background$")
	public void ISeePhotoSetAsProfileBackground() throws Throwable {
		profileImage = getRegistrationPage().takeScreenshot().orElseThrow(
				AssertionError::new);
	}

	@Then("I see photo image is correct")
	public void ISeeProfileImageIsCorrect() {
		double score = ImageUtil.getOverlapScore(referenceImage, profileImage);
		Assert.assertTrue(
				"Images do not look same. Expected score >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	@Then("I see background image is replaced")
	public void ISeeBackgroundImageIsReplaced() {
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		System.out.println("SCORE: " + score);
		Assert.assertTrue(
				"Images look same. Expected score <= 0.25, current = " + score,
				score <= 0.25d);
	}

	/**
	 * Verifies that the email verification reminder on the login page is
	 * displayed
	 * 
	 * @step. I see email verification reminder
	 * @throws Exception
	 */
	@Then("^I see email verification reminder$")
	public void ISeeEmailVerificationReminder() throws Exception {
		Assert.assertTrue(getRegistrationPage().isEmailVerifPromptVisible());
	}

	/**
	 * Verifies whether the notification invalid code is shown
	 * 
	 * @step. ^I see invalid code alert$
	 * 
	 * @throws Exception
	 */
	@Then("^I see invalid code alert$")
	public void ISeeInvalidEmailAlert() throws Exception {
		Assert.assertTrue("I don't see invalid code alert",
				getRegistrationPage().isInvalidCodeAlertShown());
	}

	/**
	 * Verifies that spaces in name field are trimmed
	 * 
	 * @step. ^I verify name input do not contains spaces$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify name input do not contains spaces$")
	public void IVerifyNameDoNotContainSpaces() throws Exception {
		Assert.assertFalse("Username field contains spaces",
				getRegistrationPage().userNameContainSpaces());
	}

	/**
	 * Presses the Choose own picture button on sign up
	 *
	 * @throws Exception
	 * @step. ^I press Choose Own Picture button$
	 */
	@When("^I press Choose Own Picture button$")
	public void IPressChooseOwnPictureButton() throws Exception {
		getRegistrationPage().clickChooseOwnPicButton();
	}

	/**
	 * Presses on Alert Choose Photo button
	 *
	 * @throws Exception
	 * @step. ^I press Choose Photo button$
	 */
	@When("^I press Choose Photo button$")
	public void IPressChoosePhotoButton() throws Exception {
		getRegistrationPage().clickChoosePhotoButton();
	}

}
