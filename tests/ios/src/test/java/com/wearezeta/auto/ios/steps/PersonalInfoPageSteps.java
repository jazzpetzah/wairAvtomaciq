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


    @When("I click on About button on personal page")
    public void WhenIClickOnAboutButtonOnPersonalPage() throws Exception {
        getPersonalInfoPage().clickOnAboutButton();
    }

    @When("^I tap on personal screen$")
    public void ITapOnPersonalScreen() throws Exception {
        getPersonalInfoPage().tapOnScreenCenter();
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

    /**
     * Tap on ADD EMAIL ADDRESS AND PASSWORD buttton
     *
     * @throws Exception
     * @step. ^I tap ADD EMAIL ADDRESS AND PASSWORD$
     */
    @When("^I tap ADD EMAIL ADDRESS AND PASSWORD$")
    public void ITapAddEmailAddress() throws Exception {
        getPersonalInfoPage().tapAddEmailAddressButton();
    }
}

