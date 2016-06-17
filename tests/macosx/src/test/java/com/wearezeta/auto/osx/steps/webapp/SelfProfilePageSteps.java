package com.wearezeta.auto.osx.steps.webapp;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.SettingsPage;
import com.wearezeta.auto.web.pages.SelfProfilePage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SelfProfilePageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    public SelfProfilePageSteps() {
    }
    

    /**
     * Checks the ... button on Self Profile page
     *
     * @step. ^I do not see the settings button on self profile page$
     * @throws Exception
     */
    @And("^I do not see the settings button on self profile page$")
    public void ISeeGearButton() throws Exception {
        Assert.assertTrue("Setting button visible", WebappPagesCollection
                .getInstance().getPage(SelfProfilePage.class)
                .isSettingsButtonVisible());
    }

    /**
     * Clicks the corresponding item from "gear" menu
     *
     * @step. ^I select (.*) menu item on self profile page$
     *
     * @param name the name of menu item
     * @throws Exception
     */
    @And("^I select (.*) menu item on self profile page$")
    public void ISelectGearMenuItem(String name) throws Exception {
        WebappPagesCollection.getInstance().getPage(SelfProfilePage.class)
                .selectGearMenuItem(name);
    }

    /**
     * Verifies that correct user name is shown on self profile page
     *
     * @step. ^I see user name on self profile page (.*)$
     *
     * @param name name of the user
     * @throws Exception
     */
    @And("^I see user name on self profile page (.*)$")
    public void ISeeUserNameOnSelfProfilePage(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        boolean nameCorrect = WebappPagesCollection.getInstance()
                .getPage(SelfProfilePage.class).checkNameInSelfProfile(name);
        Assert.assertTrue(nameCorrect);
    }

    /**
     * Verifies that correct phone number is shown on self profile page
     *
     * @step. ^I see user phone number on self profile page (.*)$
     *
     * @param phoneNumber phone number of the user
     * @throws Exception
     */
    @And("^I see user phone number on self profile page (.*)$")
    public void ISeeUserPhoneNumberOnSelfProfilePage(String phoneNumber)
            throws Exception {
        phoneNumber = usrMgr.replaceAliasesOccurences(phoneNumber,
                FindBy.PHONENUMBER_ALIAS);
        assertThat(
                WebappPagesCollection.getInstance()
                .getPage(SelfProfilePage.class).getUserPhoneNumber(),
                equalTo(phoneNumber));
    }

    /**
     * Verifies that correct user email is shown on self profile page
     *
     * @step. ^I see user email on self profile page (.*)$
     *
     * @param email email of the user
     *
     * @throws NoSuchUserException
     */
    @And("^I see user email on self profile page (.*)$")
    public void ISeeUserEmailOnSelfProfilePage(String email)
            throws NoSuchUserException, Exception {
        try {
            email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {

        }

        String actualEmail = WebappPagesCollection.getInstance()
                .getPage(SelfProfilePage.class).getUserMail();
        Assert.assertEquals(email, actualEmail);
    }

    /**
     * Set new username on self profile page
     *
     * @step. ^I change username to (.*)
     *
     * @param name new username string
     */
    @And("^I change username to (.*)")
    public void IChangeUserNameTo(String name) throws Exception {
        WebappPagesCollection.getInstance().getPage(SelfProfilePage.class).setUserName(name);
        usrMgr.getSelfUserOrThrowError().setName(name);
    }

    /**
     * Set accent color on self profile page
     *
     * @step. ^I set my accent color to (\\w+)$
     *
     * @param colorName one of these colors: StrongBlue, StrongLimeGreen, BrightYellow, VividRed, BrightOrange, SoftPink, Violet
     *
     * @throws Exception
     */
    @Then("^I set my accent color to (\\w+)$")
    public void ISetMyAccentColorTo(String colorName) throws Exception {
        WebappPagesCollection.getInstance().getPage(SelfProfilePage.class)
                .selectAccentColor(colorName);
    }

    /*
	 * Verify my accent color in color picker is equal to expected color
	 * 
	 * @step. ^I verify my accent color in color picker is set to (\\w+) color$
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
     */
    @Then("^I verify my accent color in color picker is set to (\\w+) color$")
    public void IVerifyMyAccentColor(String colorName) throws Exception {
        final int expectedColorId = AccentColor.getByName(colorName).getId();
        final int actualColorId = WebappPagesCollection.getInstance()
                .getPage(SelfProfilePage.class).getCurrentAccentColorId();
        Assert.assertTrue("my actual accent color is not set",
                actualColorId == expectedColorId);
    }

    /**
     * Click camera button on Self Profile page
     *
     * @step. ^I click camera button$
     *
     * @throws Exception
     */
    @And("^I click camera button$")
    public void IClickCameraButton() throws Exception {
        WebappPagesCollection.getInstance().getPage(SelfProfilePage.class)
                .clickCameraButton();
    }

    /**
     * Verify my avatar background color is set to expected color
     *
     * @step. ^I verify my avatar background color is set to (\\w+) color$
     *
     * @param colorName one of these colors: StrongBlue, StrongLimeGreen, BrightYellow, VividRed, BrightOrange, SoftPink, Violet
     *
     * @throws Exception
     */
    @Then("^I verify my avatar background color is set to (\\w+) color$")
    public void IVerifyMyAvatarColor(String colorName) throws Exception {
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor avatarColor = WebappPagesCollection.getInstance()
                .getPage(SelfProfilePage.class).getCurrentAvatarAccentColor();
        Assert.assertTrue("my avatar background accent color is not set",
                avatarColor == expectedColor);
    }

    /**
     * Verifies whether settings dialog is visible
     *
     * @step. ^I see Settings dialog$
     *
     * @throws AssertionError if settings dialog is not currently visible
     */
    @Then("^I see Settings dialog$")
    public void ISeeSetingsDialog() throws Exception {
        Assert.assertTrue(webappPagesCollection.getPage(SettingsPage.class)
                .isVisible());
    }

    @When("^I see the clear data dialog$")
    public void ISeeClearDataDialog() throws Exception {
        assertThat(webappPagesCollection.getPage(SelfProfilePage.class).isLogoutDialogShown(), is(true));
    }

    @When("^I enable checkbox to clear all data$")
    public void IEnableClearDataCheckbox() throws Exception {
        webappPagesCollection.getPage(SelfProfilePage.class).checkClearData();
    }

    @When("^I click Logout button on clear data dialog$")
    public void IClickLogoutOnClearDataDialog() throws Exception {
        webappPagesCollection.getPage(SelfProfilePage.class).clickLogoutButton();
    }

}
