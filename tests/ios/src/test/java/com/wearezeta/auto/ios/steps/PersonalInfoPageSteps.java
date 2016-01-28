package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PersonalInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection
            .getInstance();

    private PersonalInfoPage getPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(PersonalInfoPage.class);
    }

    BufferedImage referenceImage;

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
     * @throws Exception
     * @step. ^I attempt to enter an 80 char name$
     */
    @When("^I attempt to enter an 80 char name$")
    public void EnterTooLongName() throws Exception {
        getPersonalInfoPage().clearNameField();
        getPersonalInfoPage().attemptTooLongName();
    }

    /**
     * Verifies username is no more than 64 chars
     *
     * @throws Exception
     * @step. New name is only first 64 chars
     */
    @When("I verify my new name is only first 64 chars")
    public void NewNameIsMaxChars() throws Exception {
        Assert.assertTrue("Username is greater than 64 characters",
                getPersonalInfoPage().getSelfNameLength() >= 64);
    }

    @When("I see error message asking for more characters")
    public void ISeeErrorMessageForMoreCharacters() throws Exception {
        Assert.assertTrue("Error message is not shown", getPersonalInfoPage()
                .isTooShortNameErrorMessage());
    }

    @When("I click on Settings button on personal page")
    public void WhenIClickOnSettingsButtonOnPersonalPage() throws Exception {
        getPersonalInfoPage().clickOnSettingsButton();
    }

    @When("I see settings page")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getPersonalInfoPage().isSettingsPageVisible());
    }

    @When("I click on About button on personal page")
    public void WhenIClickOnAboutButtonOnPersonalPage() throws Exception {
        getPersonalInfoPage().clickOnAboutButton();
    }

    /**
     * Verifies the about page in settings is shown
     *
     * @throws Exception
     * @throws AssertionError about page is not shown
     * @step. ^I see About page
     */
    @Then("^I see About page$")
    public void ThenISeeAboutPage() throws Exception {
        Assert.assertTrue("About page not shown", getPersonalInfoPage()
                .isAboutPageVisible());
    }

    /**
     * Close About page
     *
     * @throws Exception
     * @step. I close About page
     */
    @When("^I close About page$")
    public void ICloseAboutPage() throws Exception {
        getPersonalInfoPage().clickAboutCloseButton();
    }

    /**
     * Verifies the wire.com button is shown
     *
     * @throws Exception
     * @throws AssertionError the wire.com button is not shown
     * @step. ^I see WireWebsiteButton$
     */
    @Then("^I see WireWebsiteButton$")
    public void ThenISeeWireWebsiteButton() throws Exception {
        Assert.assertTrue("wire.com button on \"about\" page is missing",
                getPersonalInfoPage().isWireWebsiteButtonVisible());
    }

    /**
     * Verifies the terms of use button is shown
     *
     * @throws Exception
     * @throws AssertionError the terms of use button is not shown
     * @step. ^I see TermsButton$
     */
    @Then("^I see TermsButton$")
    public void ThenISeeTermsButton() throws Exception {
        Assert.assertTrue("Terms of use button missing", getPersonalInfoPage()
                .isTermsButtonVisible());
    }

    /**
     * Verifies the privacy policy button is shown
     *
     * @throws Exception
     * @throws AssertionError the privacy policy button is not shown
     * @step. ^I see PrivacyPolicyButton$
     */
    @Then("^I see PrivacyPolicyButton$")
    public void ThenISeePrivacyPolicyButton() throws Exception {
        Assert.assertTrue("Privacy policy button missing",
                getPersonalInfoPage().isPrivacyPolicyButtonVisible());
    }

    /**
     * Verifies the build number text is shown
     *
     * @throws Exception
     * @throws AssertionError the build number info is not shown
     * @step. ^I see BuildNumberText$
     */
    @Then("^I see BuildNumberText$")
    public void ThenISeeBuildNumberText() throws Exception {
        Assert.assertTrue("Build number info not shown", getPersonalInfoPage()
                .isBuildNumberTextVisible());
    }

    /**
     * Opens the terms of use page from the about page
     *
     * @throws Exception
     * @step. ^I open TermsOfUsePage$
     */
    @When("^I open TermsOfUsePage$")
    public void IClickOnTermsOfUse() throws Exception {
        getPersonalInfoPage().openTermsOfUsePage();
    }

    /**
     * Opens the privacy policy page from the about page
     *
     * @throws Exception
     * @step. ^I open PrivacyPolicyPage$
     */
    @When("^I open PrivacyPolicyPage$")
    public void IClickOnPrivacyPolicy() throws Exception {
        getPersonalInfoPage().openPrivacyPolicyPage();
    }

    /**
     * Opens the wire.com website from the about page
     *
     * @throws Exception
     * @step. ^I open WireWebsite$
     */
    @When("^I open WireWebsite$")
    public void IClickOnWireWebsite() throws Exception {
        getPersonalInfoPage().openWireWebsite();
    }

    /**
     * Verifies that wire.com website is shown
     *
     * @throws Exception
     * @throws AssertionError the wire.com website is not shown
     * @step. ^I see WireWebsitePage$
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
     * @throws Exception
     * @step. ^I close legal page$
     */
    @When("^I close legal page$")
    public void IClickToCloseLegalPage() throws Exception {
        getPersonalInfoPage().closeLegalPage();
    }

    /**
     * Verifies the terms of use page is shown
     *
     * @throws Exception
     * @throws AssertionError the terms of use page is not shown
     * @step. ^I see TermsOfUsePage$
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
     * @throws Exception
     * @throws AssertionError the privacy policy page is not shown
     * @step. ^I see PrivacyPolicyPage$
     */
    @Then("^I see PrivacyPolicyPage$")
    public void ThenISeePrivacyPolicyPage() throws Exception {
        Assert.assertTrue(
                "Privacy Policy page is not visible or text element has changed",
                getPersonalInfoPage().isPrivacyPolicyPageVisible());
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

    /**
     * Verify that user name doesnt contains spaces
     *
     * @throws Exception
     * @step. ^I see user name doesnt contains spaces$
     */
    @When("^I see user name doesnt contains spaces$")
    public void ISeeUserNameNotContainSpaces() throws Exception {
        Assert.assertFalse("User name contains space chars",
                getPersonalInfoPage().isUserNameContainingSpaces());
    }

    @When("I see email (.*) on Personal page")
    public void ISeeMyEmailOnPersonalPage(String email) throws Exception {
        email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
        Assert.assertTrue(String.format("The email '%s' is not visible", email),
                getPersonalInfoPage().isEmailVisible(email));
    }

    @When("I attempt to enter (.*) and press return")
    public void EnterUsernameAndPressReturn(String username) throws Exception {
        getPersonalInfoPage().clearNameField();
        getPersonalInfoPage().enterNameInNameField(username);
        getPersonalInfoPage().pressEnterInNameField();
    }

    @When("I attempt to enter (.*) and tap the screen")
    public void EnterUsernameAndTapScreen(String username) throws Exception {
        getPersonalInfoPage().clearNameField();
        getPersonalInfoPage().enterNameInNameField(username);
        getPersonalInfoPage().tapOnPersonalPage();
    }

    /**
     * Attempt to change name using only spaces
     *
     * @step. I attempt to change name using only spaces
     */
    @When("I attempt to change name using only spaces")
    public void IEnterNameUsingOnlySpaces() throws Exception {
        getPersonalInfoPage().changeNameUsingOnlySpaces();
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
        Assert.assertTrue("Sound alerts page is not visible",
                getPersonalInfoPage().isSoundAlertsPageVisible());
    }

    @When("I verify that all is the default selected value")
    public void IVerifyAllIsDefaultValue() throws Exception {
        Assert.assertTrue("The selected value is different from the expected one",
                getPersonalInfoPage().isDefaultSoundValOne());
    }

    /**
     * I change name in textfield
     *
     * @param name new username in textfield
     * @throws AssertionError no such user exists
     * @step. ^I change name (.*) to (.*)$
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
     * @param name new username in textfield
     * @throws AssertionError no such user exists
     * @step. ^I see my new name (.*)$
     */
    @Then("^I see my new name (.*)$")
    public void ISeeMyNewName(String name) throws Throwable {
        String actualName = getPersonalInfoPage().getUserNameValue()
                .toLowerCase();
        Assert.assertTrue(actualName.contains(name.toLowerCase()));
    }

    /**
     * It clicks the Help button in the settings option menu
     *
     * @throws Exception
     * @step. ^I click on Help button from the options menu$
     */
    @When("^I click on Help button from the options menu$")
    public void IClickOnHelpButtonFromTheOptionsMenu() throws Exception {
        getPersonalInfoPage().clickOnHelpButton();
    }

    /**
     * Verifies that it sees the Support web page
     *
     * @throws Exception
     * @step. ^I see Support web page$
     */
    @Then("^I see Support web page$")
    public void ISeeSupportWebPage() throws Exception {
        Assert.assertTrue(getPersonalInfoPage().isSupportWebPageVisible());
    }

    /**
     * Changes the accent color by sliding to relevant one by coordinates at the
     * color picker
     *
     * @param startColor should be one of - StrongBlue, StrongLimeGreen, BrightYellow,
     *                   VividRed, BrightOrange, SoftPink, Violet
     * @param endColor   should be one of - StrongBlue, StrongLimeGreen, BrightYellow,
     *                   VividRed, BrightOrange, SoftPink, Violet
     * @throws Exception
     * @step. ^I slide my accent color via the colorpicker from (.*) to (.*)$
     */
    @When("^I slide my accent color via the colorpicker from (.*) to (.*)$")
    public void ISlideMyAccentColorViaTheColorpicker(String startColor,
                                                     String endColor) throws Exception {
        getPersonalInfoPage().swipeAccentColor(startColor, endColor);
    }

    /**
     * Switches the chathead preview on or off in settings
     *
     * @throws Exception
     * @step. ^I switch on or off the chathead preview$
     */
    @When("^I switch on or off the chathead preview$")
    public void ISwitchOnOrOffTheChatheadPreview() throws Exception {
        getPersonalInfoPage().switchChatheadsOnOff();
    }

    /**
     * Closes the settings by pressing back and done button
     *
     * @throws Exception
     * @step. ^I close the Settings$
     */
    @When("^I close the Settings$")
    public void ICloseTheSettings() throws Exception {
        getPersonalInfoPage().pressSettingsBackButton();
        getPersonalInfoPage().pressSettingsDoneButton();
    }

    /**
     * Close self profile by pressing X button
     *
     * @throws Exception
     * @step. ^I close self profile$
     */
    @When("^I close self profile$")
    public void ICloseSelfProfile() throws Exception {
        getPersonalInfoPage().closePersonalInfo();
    }

    /**
     * Verify Self profile page is opened
     *
     * @throws Exception
     * @step. I see self profile page
     */
    @When("I see self profile page")
    public void ISeeSelfProfilePage() throws Exception {
        Assert.assertTrue("Self profile page is not visible",
                getPersonalInfoPage().waitSelfProfileVisible());
    }

    /**
     * Clicks the Add Phone Number Button in self profile
     *
     * @throws Throwable
     * @step. ^I tap to add my phone number$
     */
    @When("^I tap to add my phone number$")
    public void ITapToAddMyPhoneNumber() throws Throwable {
        getPersonalInfoPage().clickAddPhoneNumberButton();
    }

    /**
     * Verifies that phone number is added to profile
     *
     * @throws Throwable
     * @step. ^I see phone number attached to profile$
     */
    @Then("^I see phone number attached to profile$")
    public void ISeePhoneNumberAttachedToProfile() throws Throwable {
        ClientUser self = usrMgr.getSelfUserOrThrowError();
        String number = self.getPhoneNumber().toString();
        Assert.assertTrue("Phone number did not get attached to the profile",
                getPersonalInfoPage().isPhoneNumberAttachedToProfile(number));
    }

    /**
     * Verifies that theme switcher button is shown on self profile page
     *
     * @throws Exception
     * @step. ^I see theme switcher button on self profile page$
     */
    @Then("^I see theme switcher button on self profile page$")
    public void ISeeThemeSwitcherButton() throws Exception {
        Assert.assertTrue("Theme switcher button is not visible",
                getPersonalInfoPage().isThemeSwitcherButtonVisible());
    }

    /**
     * Verifies that theme switcher button is NOT shown on self profile page
     *
     * @throws Exception
     * @step. ^I dont see theme switcher button on self profile page$
     */
    @Then("^I dont see theme switcher button on self profile page$")
    public void IDontSeeThemeSwitcherButton() throws Exception {
        Assert.assertFalse("Theme switcher button is visible",
                getPersonalInfoPage().isThemeSwitcherButtonVisible());
    }

    @When("^I click on Account Info on settings page$")
    public void IClickOnAccountInfoOnSettingsPage() throws Throwable {
        getPersonalInfoPage().clickAccountInfoButton();
    }

}
