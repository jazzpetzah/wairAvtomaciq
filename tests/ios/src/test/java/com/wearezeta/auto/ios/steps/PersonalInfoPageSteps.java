package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PersonalInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private PersonalInfoPage getPersonalInfoPage() throws Exception {
		return (PersonalInfoPage) pagesCollecton
				.getPage(PersonalInfoPage.class);
	}

	BufferedImage referenceImage;

	@When("^I swipe up for options$")
	public void WhenISwipeUpForOptions() throws IOException, Throwable {
		getPersonalInfoPage().swipeUp(500);
	}

	@When("I tap to edit my name")
	public void ITapToEditName() throws Exception {
		getPersonalInfoPage().tapOnEditNameField();
	}

	@When("I attempt to input an empty name and press return")
	public void EnterEmptyNameAndPressReturn() throws Exception {
		getPersonalInfoPage().clearNameField();
		getPersonalInfoPage().pressEnterInNameField();
	}

	@When("I attempt to input an empty name and tap the screen")
	public void EnterEmptyNameAndTapScreen() throws Exception {
		getPersonalInfoPage().clearNameField();
		getPersonalInfoPage().tapOnPersonalPage();
	}

	/**
	 * Enters an 80 char username
	 * 
	 * @step. ^I attempt to enter an 80 char name$
	 * @throws Exception
	 * 
	 */
	@When("^I attempt to enter an 80 char name$")
	public void EnterTooLongName() throws Exception {
		getPersonalInfoPage().clearNameField();
		getPersonalInfoPage().attemptTooLongName();
	}

	/**
	 * Verifies username is no more than 64 chars
	 * 
	 * @step. New name is only first 64 chars
	 * @throws Exception
	 * 
	 */
	@When("I verify my new name is only first 64 chars")
	public void NewNameIsMaxChars() throws Exception {
		Assert.assertTrue("Username is greater than 64 characters",
				getPersonalInfoPage().nameIsMaxChars() >= 64);
	}

	@When("I see error message asking for more characters")
	public void ISeeErrorMessageForMoreCharacters() throws Exception {
		Assert.assertTrue("Error message is not shown", getPersonalInfoPage()
				.isTooShortNameErrorMessage());
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		getPersonalInfoPage().tapOptionsButtonByText(buttonName);
	}

	@When("I click on Settings button on personal page")
	public void WhenIClickOnSettingsButtonOnPersonalPage() throws Exception {
		getPersonalInfoPage().clickOnSettingsButton();
	}

	@When("I see settings page")
	public void ISeeSettingsPage() throws Exception {
		getPersonalInfoPage().isSettingsPageVisible();
	}

	@When("I click on About button on personal page")
	public void WhenIClickOnAboutButtonOnPersonalPage() throws Exception {
		getPersonalInfoPage().clickOnAboutButton();
	}

	/**
	 * Verifies the about page in settings is shown
	 * 
	 * @step. ^I see About page
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             about page is not shown
	 */
	@Then("^I see About page$")
	public void ThenISeeAboutPage() throws Exception {
		Assert.assertTrue("About page not shown", getPersonalInfoPage()
				.isAboutPageVisible());
	}

	/**
	 * Close About page
	 * 
	 * @step. I close About page
	 * 
	 * @throws Exception
	 */
	@When("^I close About page$")
	public void ICloseAboutPage() throws Exception {
		getPersonalInfoPage().clickAboutCloseButton();
	}

	/**
	 * Verifies the about page is Violet
	 * 
	 * @step. ^I see that the About page is colored (.*)$
	 * 
	 * @param color
	 *            the color the about page should be (Violet)
	 * 
	 * @throws AssertionError
	 *             the about page is not Violet
	 */
	@Then("^I see that the About page is colored (.*)$")
	public void AboutPageIsColor(String color) throws Exception {
		// only takes violet color
		Assert.assertTrue("About page is not Violet", getPersonalInfoPage()
				.isAboutPageCertainColor(color));
	}

	/**
	 * Verifies the wire.com button is shown
	 * 
	 * @step. ^I see WireWebsiteButton$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the wire.com button is not shown
	 */
	@Then("^I see WireWebsiteButton$")
	public void ThenISeeWireWebsiteButton() throws Exception {
		Assert.assertTrue("wire.com button on \"about\" page is missing",
				getPersonalInfoPage().isWireWebsiteButtonVisible());
	}

	/**
	 * Verifies the terms of use button is shown
	 * 
	 * @step. ^I see TermsButton$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the terms of use button is not shown
	 */
	@Then("^I see TermsButton$")
	public void ThenISeeTermsButton() throws Exception {
		Assert.assertTrue("Terms of use button missing", getPersonalInfoPage()
				.isTermsButtonVisible());
	}

	/**
	 * Verifies the privacy policy button is shown
	 * 
	 * @step. ^I see PrivacyPolicyButton$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the privacy policy button is not shown
	 */
	@Then("^I see PrivacyPolicyButton$")
	public void ThenISeePrivacyPolicyButton() throws Exception {
		Assert.assertTrue("Privacy policy button missing",
				getPersonalInfoPage().isPrivacyPolicyButtonVisible());
	}

	/**
	 * Verifies the build number text is shown
	 * 
	 * @step. ^I see BuildNumberText$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the build number info is not shown
	 */
	@Then("^I see BuildNumberText$")
	public void ThenISeeBuildNumberText() throws Exception {
		Assert.assertTrue("Build number info not shown", getPersonalInfoPage()
				.isBuildNumberTextVisible());
	}

	/**
	 * Opens the terms of use page from the about page
	 * 
	 * @step. ^I open TermsOfUsePage$
	 * @throws Exception
	 */
	@When("^I open TermsOfUsePage$")
	public void IClickOnTermsOfUse() throws Exception {
		getPersonalInfoPage().openTermsOfUsePage();
	}

	/**
	 * Opens the privacy policy page from the about page
	 * 
	 * @step. ^I open PrivacyPolicyPage$
	 * @throws Exception
	 */
	@When("^I open PrivacyPolicyPage$")
	public void IClickOnPrivacyPolicy() throws Exception {
		getPersonalInfoPage().openPrivacyPolicyPage();
	}

	/**
	 * Opens the wire.com website from the about page
	 * 
	 * @step. ^I open WireWebsite$
	 * @throws Exception
	 */
	@When("^I open WireWebsite$")
	public void IClickOnWireWebsite() throws Exception {
		getPersonalInfoPage().openWireWebsite();
	}

	/**
	 * Verifies that wire.com website is shown
	 * 
	 * @step. ^I see WireWebsitePage$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the wire.com website is not shown
	 */
	@Then("^I see WireWebsitePage$")
	public void ThenISeeWireWebsite() throws Exception {
		Assert.assertTrue(
				"wire.com is not shown or website element has changed",
				getPersonalInfoPage().isWireWebsitePageVisible());
	}

	/**
	 * Closes a legal page from the about page
	 * 
	 * @step. ^I close legal page$
	 * @throws Exception
	 */
	@When("^I close legal page$")
	public void IClickToCloseLegalPage() throws Exception {
		getPersonalInfoPage().closeLegalPage();
	}

	/**
	 * Verifies the terms of use page is shown
	 * 
	 * @step. ^I see TermsOfUsePage$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the terms of use page is not shown
	 */
	@Then("^I see TermsOfUsePage$")
	public void ThenISeeTermsOfUsePage() throws Exception {
		Assert.assertTrue(
				"Terms of use page not visible or text element has changed",
				getPersonalInfoPage().isTermsOfUsePageVisible());
	}

	/**
	 * Verifies the privacy policy page is shown
	 * 
	 * @step. ^I see PrivacyPolicyPage$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             the privacy policy page is not shown
	 */
	@Then("^I see PrivacyPolicyPage$")
	public void ThenISeePrivacyPolicyPage() throws Exception {
		Assert.assertTrue(
				"Privacy Policy page is not visible or text element has changed",
				getPersonalInfoPage().isPrivacyPolicyPageVisible());
	}

	@When("I click Sign out button from personal page")
	public void IClickSignOutButtonFromPersonalPage() throws Exception {
		getPersonalInfoPage().clickSignoutButton();
	}

	@When("^I tap on personal screen$")
	public void ITapOnPersonalScreen() throws Exception {
		getPersonalInfoPage().tapOnPersonalPage();
	}

	@When("^I press Camera button$")
	public void IPressCameraButton() throws Exception {
		getPersonalInfoPage().pressCameraButton();
	}

	@When("^I return to personal page$")
	public void IReturnToPersonalPage() throws Throwable {
		Thread.sleep(5000);// wait for picture to load on simulator
		getPersonalInfoPage().tapOnPersonalPage();
		Thread.sleep(2000);// wait for picture to load on simulator
		getPersonalInfoPage().tapOnPersonalPage();
		Thread.sleep(2000);
		getPersonalInfoPage().tapOnPersonalPage();
		referenceImage = getPersonalInfoPage().takeScreenshot().orElseThrow(
				AssertionError::new);
		getPersonalInfoPage().tapOnPersonalPage();
	}

	@Then("^I see changed user picture (.*)$")
	public void ThenISeeChangedUserPicture(String filename) throws Throwable {
		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.65, current = "
						+ score, score >= 0.65d);
	}

	@Then("I see profile image is same as template")
	public void ThenISeeProfileImageIsSameAsSelected(String filename)
			throws Exception {
		BufferedImage profileImage = getPersonalInfoPage().takeScreenshot()
				.orElseThrow(AssertionError::new);
		double score = ImageUtil.getOverlapScore(
				RegistrationPageSteps.basePhoto, profileImage,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);

		Assert.assertTrue(
				"Images are differen. Expected score >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	@When("I see Personal page")
	public void ISeePersonalPage() throws Exception {
		getPersonalInfoPage().waitForSettingsButtonAppears();
	}

	@When("I see name (.*) on Personal page")
	public void ISeeMyNameOnPersonalPage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(getPersonalInfoPage().getUserNameValue().equals(name));
	}

	@When("I see email (.*) on Personal page")
	public void ISeeMyEmailOnPersonalPage(String email) throws Exception {
		email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		Assert.assertTrue(email
				.equals(getPersonalInfoPage().getUserEmailVaue()));
	}

	@When("I attempt to enter (.*) and press return")
	public void EnterUsernameAndPressReturn(String username) throws Exception {
		getPersonalInfoPage().clearNameField();
		getPersonalInfoPage().enterNameInNamefield(username);
		getPersonalInfoPage().pressEnterInNameField();
	}

	@When("I attempt to enter (.*) and tap the screen")
	public void EnterUsernameAndTapScreen(String username) throws Exception {
		getPersonalInfoPage().clearNameField();
		getPersonalInfoPage().enterNameInNamefield(username);
		getPersonalInfoPage().tapOnPersonalPage();
	}

	/**
	 * Attempt to change name using only spaces
	 * 
	 * @step. I attempt to change name using only spaces
	 * 
	 */
	@When("I attempt to change name using only spaces")
	public void IEnterNameUsingOnlySpaces() throws Exception {
		getPersonalInfoPage().changeNameUsingOnlySpaces();
	}

	@When("I swipe right on the personal page")
	public void ISwipeRightOnPersonalPage() throws Exception {
		getPersonalInfoPage().swipeRight(1000);
	}

	@When("I click on Settings button from the options menu")
	public void WhenIClickOnSettingsButtonFromOptionsMenu() throws Exception {
		getPersonalInfoPage().tapOnSettingsButton();
	}

	@When("I click on Change Password button in Settings")
	public void WhenIClickOnChangePasswordButtonFromSettings() throws Exception {
		getPersonalInfoPage().clickChangePasswordButton();
	}

	@Then("I see reset password page")
	public void ISeeResetPasswordPage() throws Exception {
		Assert.assertTrue("Change Password button is not shown",
				getPersonalInfoPage().isResetPasswordPageVisible());
	}

	@When("I tap on Sound Alerts")
	public void ITapOnSoundAlerts() throws Exception {
		getPersonalInfoPage().enterSoundAlertSettings();
	}

	@When("I see the Sound alerts page")
	public void ISeeSoundAlertsPage() throws Exception {
		getPersonalInfoPage().isSoundAlertsPageVisible();
	}

	@When("I verify that all is the default selected value")
	public void IVerifyAllIsDefaultValue() throws Exception {
		getPersonalInfoPage().isDefaultSoundValOne();
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
		getPersonalInfoPage().changeName(newName);
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
		String actualName = getPersonalInfoPage().getUserNameValue();
		Assert.assertTrue(actualName.contains(name));
	}

	/**
	 * It clicks the Help button in the settings option menu
	 * 
	 * @step. ^I click on Help button from the options menu$
	 * @throws Exception
	 */
	@When("^I click on Help button from the options menu$")
	public void IClickOnHelpButtonFromTheOptionsMenu() throws Exception {
		getPersonalInfoPage().clickOnHelpButton();
	}

	/**
	 * Verifies that it sees the Support web page
	 * 
	 * @step. ^I see Support web page$
	 * @throws Exception
	 */
	@Then("^I see Support web page$")
	public void ISeeSupportWebPage() throws Exception {
		Assert.assertTrue(getPersonalInfoPage().isSupportWebPageVisible());
	}

	/**
	 * Changes the accent color by pickick one by coordinates at the color
	 * picker
	 * 
	 * @step. ^I change my accent color via the colorpicker$
	 * @throws Exception
	 */
	@When("^I change my accent color via the colorpicker$")
	public void IChangeMyAccentColorViaTheColorpicker() throws Exception {
		getPersonalInfoPage().changeAccentColor();
	}

	/**
	 * Changes the accent color by pickick relevant one by coordinates at the
	 * color picker
	 * 
	 * @step. ^I set my accent color via the colorpicker to (.*)$
	 * 
	 * @param String
	 *            color - should be StrongBlue, StrongLimeGreen, BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * @throws Exception
	 */
	@When("^I set my accent color via the colorpicker to (.*)$")
	public void ISetMyAccentColorViaTheColorpicker(String color)
			throws Exception {
		getPersonalInfoPage().setAccentColor(color);
	}
	
	/**
	 * Changes the accent color by sliding to relevant one by coordinates at the
	 * color picker
	 * 
	 * @step. ^I slide my accent color via the colorpicker from (.*) to (.*)$
	 * 
	 * @param String
	 *            color - should be StrongBlue, StrongLimeGreen, BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * @throws Exception
	 */
	@When("^I slide my accent color via the colorpicker from (.*) to (.*)$")
	public void ISlideMyAccentColorViaTheColorpicker(String startColor, String endColor)
			throws Exception {
		getPersonalInfoPage().swipeAccentColor(startColor, endColor);
	}

	/**
	 * Switches the chathead preview on or off in settings
	 * 
	 * @step. ^I switch on or off the chathead preview$
	 * @throws Exception
	 */
	@When("^I switch on or off the chathead preview$")
	public void ISwitchOnOrOffTheChatheadPreview() throws Exception {
		getPersonalInfoPage().switchChatheadsOnOff();
	}

	/**
	 * Closes the settings by pressing back and done button
	 * 
	 * @step. ^I close the Settings$
	 * @throws Exception
	 */
	@When("^I close the Settings$")
	public void ICloseTheSettings() throws Exception {
		getPersonalInfoPage().pressSettingsBackButton();
		getPersonalInfoPage().pressSettingsDoneButton();
	}

	/**
	 * Close self profile by pressing X button
	 * 
	 * @step. ^I close self profile$
	 * @throws Exception
	 */
	@When("^I close self profile$")
	public void ICloseSelfProfile() throws Exception {
		getPersonalInfoPage().closePersonalInfo();
	}

	/**
	 * Verify Self profile page is opened
	 * 
	 * @step. I see self profile page
	 * 
	 * @throws Exception
	 */
	@When("I see self profile page")
	public void ISeeSelfProfilePage() throws Exception {
		Assert.assertTrue("Self profile page is not visible",
				getPersonalInfoPage().waitSelfProfileVisible());
	}

}
