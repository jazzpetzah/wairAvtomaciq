package com.wearezeta.auto.ios;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.ios.pages.CameraRollPage;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.user_management.UsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	private final UsersManager usrMgr = UsersManager.getInstance();

	BufferedImage referenceImage;

	@When("^I swipe up for options$")
	public void WhenISwipeUpForOptions() throws IOException, Throwable {
		PagesCollection.personalInfoPage.swipeUp(500);
	}

	@When("I tap to edit my name")
	public void ITapToEditName() {
		PagesCollection.personalInfoPage.tapOnEditNameField();
	}

	@When("I attempt to input an empty name and press return")
	public void EnterEmptyNameAndPressReturn() {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.pressEnterInNameField();
	}

	@When("I attempt to input an empty name and tap the screen")
	public void EnterEmptyNameAndTapScreen() {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("I see error message asking for more characters")
	public void ISeeErrorMessageForMoreCharacters() {
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

	@When("I click Sign out button from personal page")
	public void IClickSignOutButtonFromPersonalPage()
			throws MalformedURLException {
		PagesCollection.personalInfoPage.clickSignoutButton();
	}

	@When("^I tap on personal screen$")
	public void ITapOnPersonalScreen() throws InterruptedException {
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("^I press Camera button$")
	public void IPressCameraButton() throws InterruptedException, IOException {
		CameraRollPage page = PagesCollection.personalInfoPage
				.pressCameraButton();
		PagesCollection.cameraRollPage = (CameraRollPage) page;
	}

	@When("^I return to personal page$")
	public void IReturnToPersonalPage() throws Throwable {

		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(4000);
		referenceImage = PagesCollection.personalInfoPage.takeScreenshot();
		PagesCollection.personalInfoPage.tapOnPersonalPage();

	}

	@Then("^I see changed user picture (.*)$")
	public void ThenISeeChangedUserPicture(String filename) throws Throwable {

		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		System.out.print("SCORE: " + score);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.65, current = "
						+ score, score >= 0.65d);
	}

	@Then("I see profile image is same as template")
	public void ThenISeeProfileImageIsSameAsSelected(String filename)
			throws IOException {
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
	public void ISeePersonalPage() {
		PagesCollection.personalInfoPage.waitForSettingsButtonAppears();
	}

	@When("I see name (.*) on Personal page")
	public void ISeeMyNameOnPersonalPage(String name) {
		name = usrMgr.findUserByNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.personalInfoPage.getUserNameValue()
				.equals(name));
	}

	@When("I see email (.*) on Personal page")
	public void ISeeMyEmailOnPersonalPage(String name) {
		String email = usrMgr.findUserByNameAlias(name).getEmail();
		Assert.assertTrue(email.equals(PagesCollection.personalInfoPage
				.getUserEmailVaue()));
	}

	@When("I attempt to enter (.*) and press return")
	public void EnterUsernameAndPressReturn(String username) {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.enterNameInNamefield(username);
		PagesCollection.personalInfoPage.pressEnterInNameField();
	}

	@When("I attempt to enter (.*) and tap the screen")
	public void EnterUsernameAndTapScreen(String username) {
		PagesCollection.personalInfoPage.clearNameField();
		PagesCollection.personalInfoPage.enterNameInNamefield(username);
		PagesCollection.personalInfoPage.tapOnPersonalPage();
	}

	@When("I swipe right on the personal page")
	public void ISwipeRightOnPersonalPage() throws IOException {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.personalInfoPage
				.swipeRight(500);
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

}
