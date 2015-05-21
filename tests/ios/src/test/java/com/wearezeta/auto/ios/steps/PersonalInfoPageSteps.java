package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.CameraRollPage;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	BufferedImage referenceImage;

	@When("^I swipe up for options$")
	public void WhenISwipeUpForOptions() throws IOException, Throwable {
		PagesCollection.personalInfoPage.swipeUp(500);
	}

	@When("I tap to edit my name")
	public void ITapToEditName() throws Exception {
		PagesCollection.personalInfoPage.tapOnEditNameField();
	}

	@When("I attempt to input an empty name and press return")
	public void EnterEmptyNameAndPressReturn() throws Exception {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.pressEnterInNameField();
	}

	@When("I attempt to input an empty name and tap the screen")
	public void EnterEmptyNameAndTapScreen() {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("I see error message asking for more characters")
	public void ISeeErrorMessageForMoreCharacters() throws Exception {
		Assert.assertTrue("Error message is not shown",
				PagesCollection.personalInfoPage.isTooShortNameErrorMessage());
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButtonByText(buttonName);
	}

	@When("I click on Settings button on personal page")
	public void WhenIClickOnSettingsButtonOnPersonalPage() {
		PagesCollection.personalInfoPage.clickOnSettingsButton();
	}

	@When("I see settings page")
	public void ISeeSettingsPage() {
		PagesCollection.personalInfoPage.isSettingsPageVisible();
	}

	@When("I click on About button on personal page")
	public void WhenIClickOnAboutButtonOnPersonalPage() {
		PagesCollection.personalInfoPage.clickOnAboutButton();
	}



	@Then("I see About page")
	public void ThenISeeAboutPAge() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isAboutPageVisible());
	}

	@Then("I see that the About page is colored (.*)")
	public void AboutPageIsColor(String color) throws Exception {
		//only takes violet color
		Assert.assertTrue(PagesCollection.personalInfoPage.isAboutPageCertainColor(color));
	}
	
	@Then("I see WireWebsiteButton")
	public void ThenISeeWireWebsiteButton() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isWireWebsiteButtonVisible());
	}

	@Then("I see TermsButton")
	public void ThenISeeTermsButton() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isTermsButtonVisible());
	}

	@Then("I see PrivacyPolicyButton")
	public void ThenISeePrivacyPolicyButton() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isPrivacyPolicyButtonVisible());
	}

	@Then("I see BuildNumberText")
	public void ThenISeeBuildNumberText() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isBuildNumberTextVisible());
	}

	@When("I open TermsOfUsePage")
	public void IClickOnTermsOfUse() {
		PagesCollection.personalInfoPage.openTermsOfUsePage();
	}

	@When("I open PrivacyPolicyPage")
	public void IClickOnPrivacyPolicy() {
		PagesCollection.personalInfoPage.openPrivacyPolicyPage();
	}

	@When("I open WireWebsite")
	public void IClickOnWireWebsite() {
		PagesCollection.personalInfoPage.openWireWebsite();
	}

	@Then("I see WireWebsitePage")
	public void ThenISeeWireWebsite() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isWireWebsitePageVisible());
	}
	
	@When("I close legal page")
	public void IClickToCloseLegalPage() {
		PagesCollection.personalInfoPage.closeLegalPage();
	}

	@Then("I see TermsOfUsePage")
	public void ThenISeeTermsOfUsePage() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isTermsOfUsePageVisible());
	}

	@Then("I see PrivacyPolicyPage")
	public void ThenISeePrivacyPolicyPage() {
		Assert.assertTrue(PagesCollection.personalInfoPage.isPrivacyPolicyPageVisible());
	}




	@When("I click Sign out button from personal page")
	public void IClickSignOutButtonFromPersonalPage() throws Exception {
		PagesCollection.personalInfoPage.clickSignoutButton();
	}

	@When("^I tap on personal screen$")
	public void ITapOnPersonalScreen() throws InterruptedException {
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("^I press Camera button$")
	public void IPressCameraButton() throws Exception {
		CameraRollPage page = PagesCollection.personalInfoPage
				.pressCameraButton();
		PagesCollection.cameraRollPage = (CameraRollPage) page;
	}

	@When("^I return to personal page$")
	public void IReturnToPersonalPage() throws Throwable {
		Thread.sleep(4000);//wait for picture to load on simulator
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);//wait for picture to load on simulator
		referenceImage = PagesCollection.personalInfoPage.takeScreenshot();
		PagesCollection.personalInfoPage.tapOnPersonalPage();

	}

	@Then("^I see changed user picture (.*)$")
	public void ThenISeeChangedUserPicture(String filename) throws Throwable {

		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.65, current = "
						+ score, score >= 0.65d);
	}

	@Then("I see profile image is same as template")
	public void ThenISeeProfileImageIsSameAsSelected(String filename)
			throws Exception {
		BufferedImage profileImage = PagesCollection.personalInfoPage
				.takeScreenshot();
		double score = ImageUtil.getOverlapScore(
				RegistrationPageSteps.basePhoto, profileImage);
		System.out.println("SCORE: " + score);
		Assert.assertTrue(
				"Images are differen. Expected score >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	@When("I see Personal page")
	public void ISeePersonalPage() throws Exception {
		PagesCollection.personalInfoPage.waitForSettingsButtonAppears();
	}

	@When("I see name (.*) on Personal page")
	public void ISeeMyNameOnPersonalPage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.personalInfoPage.getUserNameValue()
				.equals(name));
	}

	@When("I see email (.*) on Personal page")
	public void ISeeMyEmailOnPersonalPage(String email) throws Exception {
		email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		Assert.assertTrue(email.equals(PagesCollection.personalInfoPage
				.getUserEmailVaue()));
	}

	@When("I attempt to enter (.*) and press return")
	public void EnterUsernameAndPressReturn(String username) throws Exception {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.enterNameInNamefield(username);
		PagesCollection.personalInfoPage.pressEnterInNameField();
	}

	@When("I attempt to enter (.*) and tap the screen")
	public void EnterUsernameAndTapScreen(String username) throws Exception {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.enterNameInNamefield(username);
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("I swipe right on the personal page")
	public void ISwipeRightOnPersonalPage() throws Exception {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.personalInfoPage
				.swipeRight(1000);
	}

	@When("I click on Settings button from the options menu")
	public void WhenIClickOnSettingsButtonFromOptionsMenu() {
		PagesCollection.personalInfoPage.tapOnSettingsButton();
	}

	@When("I click on Change Password button in Settings")
	public void WhenIClickOnChangePasswordButtonFromSettings() {
		PagesCollection.personalInfoPage.clickChangePasswordButton();
	}

	@Then("I see reset password page")
	public void ISeeResetPasswordPage() {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isResetPasswordPageVisible());
	}

	@When("I tap on Sound Alerts")
	public void ITapOnSoundAlerts() {
		PagesCollection.personalInfoPage.enterSoundAlertSettings();
	}

	@When("I see the Sound alerts page")
	public void ISeeSoundAlertsPage() {
		PagesCollection.personalInfoPage.isSoundAlertsPageVisible();
	}

	@When("I verify that all is the default selected value")
	public void IVerifyAllIsDefaultValue() {
		PagesCollection.personalInfoPage.isDefaultSoundValOne();
	}

	/**
	 * I change name in textfield
	 * 
	 * @step. ^I change name (.*) to (.*)$
	 * 
	 * @param name
	 *            new username in textfield
	 * 
	 * @throws AssertionError
	 *             no such user exists
	 * 
	 */
	@When("^I change name (.*) to (.*)$")
	public void IChangeNameTo(String name, String newName) throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.changeName(newName);
		usrMgr.getSelfUser().setName(newName);
	}

	/**
	 * Verifies name in text field is changed
	 * 
	 * @step. ^I see my new name (.*)$
	 * 
	 * @param name
	 *            new username in textfield
	 * 
	 * @throws AssertionError
	 *             no such user exists
	 * 
	 */
	@Then("^I see my new name (.*)$")
	public void ISeeMyNewName(String name) throws Throwable {
		Assert.assertTrue(name.equals(PagesCollection.personalInfoPage
				.getUserNameValue()));
	}

	/**
	 * It clicks the Help button in the settings option menu
	 * 
	 * @step. ^I click on Help button from the options menu$
	 */
	@When("^I click on Help button from the options menu$")
	public void IClickOnHelpButtonFromTheOptionsMenu() {
		PagesCollection.personalInfoPage.clickOnHelpButton();
	}

	/**
	 * Verifies that it sees the Support web page
	 * 
	 * @step. ^I see Support web page$
	 */
	@Then("^I see Support web page$")
	public void ISeeSupportWebPage() {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isSupportWebPageVisible());
	}

	/**
	 * Changes the accent color by pickick one by coordinates at the color
	 * picker
	 * 
	 * @step. ^I change my accent color via the colorpicker$
	 */
	@When("^I change my accent color via the colorpicker$")
	public void IChangeMyAccentColorViaTheColorpicker() {
		PagesCollection.personalInfoPage.changeAccentColor();
	}

	/**
	 * Switches the chathead preview on or off in settings
	 * 
	 * @step. ^I switch on or off the chathead preview$
	 */
	@When("^I switch on or off the chathead preview$")
	public void ISwitchOnOrOffTheChatheadPreview() {
		PagesCollection.personalInfoPage.switchChatheadsOnOff();
	}

	/**
	 * Closes the settings by pressing back and done button
	 * 
	 * @step. ^I close the Settings$
	 */
	@When("^I close the Settings$")
	public void ICloseTheSettings() {
		PagesCollection.personalInfoPage.pressSettingsBackButton();
		PagesCollection.personalInfoPage.pressSettingsDoneButton();
	}

}
