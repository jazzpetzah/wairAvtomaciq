package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PreferencesAccountPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Assert;

public class PreferencesAccountPageSteps {

    private final TestContext context;

    public PreferencesAccountPageSteps() {
        this.context = new TestContext();
    }

    public PreferencesAccountPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I click logout in account preferences$")
    public void IClosePreferences() throws Exception {
        context.getPagesCollection().getPage(PreferencesAccountPage.class).logout();
    }

    @When("^I see the clear data dialog$")
    public void ISeeClearDataDialog() throws Exception {
        assertThat(context.getPagesCollection().getPage(PreferencesAccountPage.class).isLogoutDialogShown(), is(true));
    }

    @When("^I enable checkbox to clear all data$")
    public void IEnableClearDataCheckbox() throws Exception {
        context.getPagesCollection().getPage(PreferencesAccountPage.class).checkClearDataInLogoutDialog();
    }

    @When("^I click logout button on clear data dialog$")
    public void IClickLogoutOnClearDataDialog() throws Exception {
        context.getPagesCollection().getPage(PreferencesAccountPage.class).logoutInLogoutDialog();
    }

    @And("^I see username (.*) in account preferences$")
    public void ISeeUserNameOnSelfProfilePage(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        boolean nameCorrect = context.getPagesCollection().getPage(PreferencesAccountPage.class).checkNameInSelfProfile(name);
        Assert.assertTrue(nameCorrect);
    }

    @And("^I see user phone number (.*) in account preferences$")
    public void ISeeUserPhoneNumberOnSelfProfilePage(String phoneNumber) throws Exception {
        phoneNumber = context.getUserManager().
                replaceAliasesOccurences(phoneNumber, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        assertThat(context.getPagesCollection().getPage(PreferencesAccountPage.class).getUserPhoneNumber(), equalTo(phoneNumber));
    }

    @And("^I see user email (.*) in account preferences$")
    public void ISeeUserEmailOnSelfProfilePage(String email) throws NoSuchUserException, Exception {
        try {
            email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {

        }
        String actualEmail = context.getPagesCollection().getPage(PreferencesAccountPage.class).getUserMail();
        Assert.assertEquals(email, actualEmail);
    }
    
    @And("^I change username to (.*)")
    public void IChangeUserNameTo(String name) throws Exception {
        context.getPagesCollection().getPage(PreferencesAccountPage.class).setUserName(name);
        context.getUserManager().getSelfUserOrThrowError().setName(name);
    }

}
