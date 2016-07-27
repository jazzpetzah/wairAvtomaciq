package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.misc.ElementState;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.PersonalInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private PersonalInfoPage getPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(PersonalInfoPage.class);
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
        getPersonalInfoPage().tapOnScreenCenter();
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
        Assert.assertTrue("wire.com is not shown or website element has changed",
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
        getPersonalInfoPage().tapOnScreenCenter();
    }

    @When("^I tap (Camera|Camera Roll) button on personal screen$")
    public void ITapButton(String name) throws Exception {
        switch (name.toLowerCase()) {
            case "camera":
                getPersonalInfoPage().tapCameraButton();
                break;
            case "camera roll":
                getPersonalInfoPage().tapCameraRollButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    private ElementState previousProfilePictureScreenshot = new ElementState(
            () -> getPersonalInfoPage().takeScreenshot().
                    orElseThrow(() -> new IllegalStateException("Cannot take a screenshot of self profile page"))
    );

    /**
     * Take a screenshot of self profile page and save it into internal var
     *
     * @throws Exception
     * @step. ^I remember my current profile picture$
     */
    @When("^I remember my current profile picture$")
    public void IRememberMyProfilePicture() throws Exception {
        Assert.assertTrue("Profile page is not currently visible", getPersonalInfoPage().waitUntilVisible());
        previousProfilePictureScreenshot.remember();
    }


    @Then("I wait up to (\\d+) seconds? until my profile picture is changed")
    public void IWaitUntilProfilePictureIsChanged(int secondsTimeout) throws Exception {
        Assert.assertTrue("Profile page is not currently visible", getPersonalInfoPage().waitUntilVisible());
        if (previousProfilePictureScreenshot == null) {
            throw new IllegalStateException("Please take a screenshot of previous profile picture first");
        }
        final double minScore = 0.87;
        Assert.assertTrue("The previous and the current profile pictures seem to be the same",
                this.previousProfilePictureScreenshot.isChanged(secondsTimeout, minScore));
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
        getPersonalInfoPage().tapOnScreenCenter();
    }

    @When("I click on Settings button from the options menu")
    public void WhenIClickOnSettingsButtonFromOptionsMenu() throws Exception {
        getPersonalInfoPage().tapOnSettingsButton();
    }

    @Then("I see reset password page")
    public void ISeeResetPasswordPage() throws Exception {
        Assert.assertTrue("Change Password button is not shown",
                getPersonalInfoPage().isResetPasswordPageVisible());
    }

    /**
     * I change name in textfield
     *
     * @param newName new username in textfield
     * @throws Exception
     * @step. ^I change my name to (.*)
     */
    @When("^I change my name to (.*)")
    public void IChangeNameTo(String newName) throws Exception {
        getPersonalInfoPage().changeName(newName);
        usrMgr.getSelfUserOrThrowError().setName(newName);
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
        Assert.assertTrue("Customer support page has not been loaded",
                getPersonalInfoPage().isSupportWebPageVisible());
    }

    /**
     * Changes the accent color by clicking the color picker
     *
     * @param color should be one of - StrongBlue, StrongLimeGreen, BrightYellow,
     *              VividRed, BrightOrange, SoftPink, Violet
     * @throws Exception
     * @step. ^I set my accent color to (.*)
     */
    @When("^I set my accent color to (.*)")
    public void IChangeMyAccentColor(String color) throws Exception {
        getPersonalInfoPage().selectAccentColor(AccentColor.getByName(color));
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

    private ElementState colorPickerState = new ElementState(
            () -> getPersonalInfoPage().getPeoplePickerScreenshot()
    );

    private static final int COLOR_PICKER_STATE_CHANGE_TIMEOUT = 10;
    private static final double MIN_COLOR_PICKER_SIMILARITY_SCORE = 0.95;

    /**
     * Get and remember the screenshot of People Picker
     *
     * @throws Exception
     * @step. ^I remember the state of color picker$
     */
    @When("^I remember the state of color picker$")
    public void IRememberColorPickerState() throws Exception {
        colorPickerState.remember();
    }

    /**
     * Verify that color picker state has been changed
     *
     * @throws Exception
     * @step. ^I verify the state of color picker is changed$
     */
    @Then("^I verify the state of color picker is changed$")
    public void IVerifyColorPickerState() throws Exception {
        Assert.assertTrue("Color picker state has not been changed",
                colorPickerState.isChanged(COLOR_PICKER_STATE_CHANGE_TIMEOUT, MIN_COLOR_PICKER_SIMILARITY_SCORE));
    }
}

