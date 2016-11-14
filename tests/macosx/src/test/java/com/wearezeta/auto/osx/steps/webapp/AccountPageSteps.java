package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.AccountPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AccountPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public AccountPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I click logout in account preferences$")
    public void IClosePreferences() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).logout();
    }
    
    @When("^I do not see logout in account preferences$")
    public void IDoNotSeeLogout() throws Exception {
        assertTrue("Logout should NOT be visible", webContext.getPagesCollection().getPage(AccountPage.class).isLogoutInvisible());
    }

    @When("^I see the clear data dialog$")
    public void ISeeClearDataDialog() throws Exception {
        assertThat(webContext.getPagesCollection().getPage(AccountPage.class).isLogoutDialogShown(), is(true));
    }

    @When("^I enable checkbox to clear all data$")
    public void IEnableClearDataCheckbox() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).checkClearDataInLogoutDialog();
    }

    @When("^I click logout button on clear data dialog$")
    public void IClickLogoutOnClearDataDialog() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).logoutInLogoutDialog();
    }

    @And("^I see username (.*) in account preferences$")
    public void ISeeUserNameOnSelfProfilePage(String name) throws Exception {
        name = webContext.getUserManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        assertThat("Username", webContext.getPagesCollection().getPage(AccountPage.class).getUserName(), equalTo(name));
    }

    @And("^I see user phone number (.*) in account preferences$")
    public void ISeeUserPhoneNumberOnSelfProfilePage(String phoneNumber) throws Exception {
        phoneNumber = webContext.getUserManager().
                replaceAliasesOccurences(phoneNumber, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        assertThat(webContext.getPagesCollection().getPage(AccountPage.class).getUserPhoneNumber(), equalTo(phoneNumber));
    }

    @And("^I see user email (.*) in account preferences$")
    public void ISeeUserEmailOnSelfProfilePage(String email) throws NoSuchUserException, Exception {
        try {
            email = webContext.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {

        }
        String actualEmail = webContext.getPagesCollection().getPage(AccountPage.class).getUserMail();
        assertEquals(email, actualEmail);
    }

    @And("^I change username to (.*)")
    public void IChangeUserNameTo(String name) throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).setUserName(name);
        webContext.getUserManager().getSelfUserOrThrowError().setName(name);
    }

    @When("^I drop picture (.*) to account preferences$")
    public void IDropPicture(String pictureName) throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).dropPicture(pictureName);
    }

    @When("^I upload picture (.*) to account preferences$")
    public void IUploadPicture(String pictureName) throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).uploadPicture(pictureName);
    }

    @Then("^I set my accent color to (\\w+)$")
    public void ISetMyAccentColorTo(String colorName) throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).selectAccentColor(colorName);
    }

    @Then("^I verify my accent color in color picker is set to (\\w+) color$")
    public void IVerifyMyAccentColor(String colorName) throws Exception {
        final int expectedColorId = AccentColor.getByName(colorName).getId();
        final int actualColorId = webContext.getPagesCollection().getPage(AccountPage.class).getCurrentAccentColorId();
        assertTrue("my actual accent color is not set", actualColorId == expectedColorId);
    }

    @Then("^I verify my avatar background color is set to (\\w+) color$")
    public void IVerifyMyAvatarColor(String colorName) throws Exception {
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor avatarColor = webContext.getPagesCollection().getPage(AccountPage.class).getCurrentAvatarAccentColor();
        assertTrue("my avatar background accent color is not set", avatarColor == expectedColor);
    }

    @When("^I click delete account button on settings page$")
    public void IClickDeleteAccountButton() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).clickDeleteAccountButton();
    }

    @When("^I click cancel deletion button on settings page$")
    public void IClickCancelDeleteButton() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).clickCancelDeleteAccountButton();
    }

    @When("^I click send button to delete account$")
    public void IClickSendButton() throws Exception {
        webContext.getPagesCollection().getPage(AccountPage.class).clickConfirmDeleteAccountButton();
    }
}
