package com.wearezeta.auto.web.steps;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.AccentColor;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.AccountPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;

public class AccountPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(AccountPageSteps.class.getSimpleName());

    private BufferedImage profileImage = null;
    private final WebAppTestContext context;
    private String rememberedUniqueUsername = null;

    public AccountPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I click logout in account preferences$")
    public void IClosePreferences() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).logout();
    }

    @When("^I see the clear data dialog$")
    public void ISeeClearDataDialog() throws Exception {
        assertThat(context.getPagesCollection().getPage(AccountPage.class).isLogoutDialogShown(), is(true));
    }

    @When("^I enable checkbox to clear all data$")
    public void IEnableClearDataCheckbox() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).checkClearDataInLogoutDialog();
    }

    @When("^I click logout button on clear data dialog$")
    public void IClickLogoutOnClearDataDialog() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).logoutInLogoutDialog();
    }

    @And("^I see name (.*) in account preferences$")
    public void ISeeNameOnSelfProfilePage(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        assertThat("Name", context.getPagesCollection().getPage(AccountPage.class).getName(), equalTo(name));
    }

    @And("^I see user phone number (.*) in account preferences$")
    public void ISeeUserPhoneNumberOnSelfProfilePage(String phoneNumber) throws Exception {
        phoneNumber = context.getUsersManager().
                replaceAliasesOccurences(phoneNumber, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        assertThat(context.getPagesCollection().getPage(AccountPage.class).getUserPhoneNumber(), equalTo(phoneNumber));
    }

    @And("^I see user email (.*) in account preferences$")
    public void ISeeUserEmailOnSelfProfilePage(String email) throws NoSuchUserException, Exception {
        try {
            email = context.getUsersManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {

        }
        String actualEmail = context.getPagesCollection().getPage(AccountPage.class).getUserMail();
        assertEquals(email, actualEmail);
    }

    @And("^I see unique username starts with (.*) in account preferences$")
    public void ISeeUniqueUsernameStartsWithOnSelfProfilePage(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertThat("Username in settings",
                context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername(), startsWith(name));
    }
    
    @And("^I (do not )?see unique username is the remembered one in account preferences$")
    public void ISeeRememberedUniqueUsernameOnSelfProfilePage(String not) throws Exception {
        if (not == null) {
            Assert.assertThat("Remembered username is NOT in settings",
                context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername(), equalTo(rememberedUniqueUsername));
        }else{
            Assert.assertThat("Remembered username is in settings",
                context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername(), not(equalTo(rememberedUniqueUsername)));
        }
    }

    @And("^I see unique username is (.*) in account preferences$")
    public void ISeeUniqueUsernameOnSelfProfilePage(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        Assert.assertThat("Username in settings",
                context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername(), equalTo(name.replaceAll(" ","")));
    }

    @When("^I remember the profile image on the account page$")
    public void IRememberSmallProfileImage() throws Exception {
        profileImage = context.getPagesCollection().getPage(AccountPage.class).getPicture();
    }

    @Then("^I verify that the profile image on the account page has( not)? changed$")
    public void IVerifyBackgroundImageHasChanged(String not) throws Exception {
        final int THRESHOLD = 100;

        if (not == null) {
            AccountPage contactListPage = context.getPagesCollection().getPage(AccountPage.class);

            Wait<AccountPage> wait = new FluentWait<>(contactListPage)
                    .withTimeout(15, TimeUnit.SECONDS)
                    .pollingEvery(5, TimeUnit.SECONDS)
                    .ignoring(AssertionError.class);

            wait.until(page -> {
                int actualMatch = THRESHOLD + 1;
                try {
                    actualMatch = ImageUtil.getMatches(page.getPicture(), profileImage);
                } catch (Exception e) {
                }
                assertThat("Image has not changed", actualMatch, lessThan(THRESHOLD));
                return actualMatch;
            });
        } else {
            BufferedImage actualPicture = context.getPagesCollection().getPage(AccountPage.class).getPicture();
            assertThat("Image has changed", ImageUtil.getMatches(actualPicture, profileImage), greaterThan(THRESHOLD));
        }
    }

    @And("^I change name to (.*)")
    public void IChangeNameTo(String name) throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).setName(name);
        context.getUsersManager().getSelfUserOrThrowError().setName(name);
    }

    @And("^I type (.*) into unique username field$")
    public void ITypeIntoUniqueUserNameField(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        context.getPagesCollection().getPage(AccountPage.class).setUniqueUsername(name);
    }

    @And("^I change unique username to (.*)")
    public void IChangeUniqueUserNameTo(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        context.getPagesCollection().getPage(AccountPage.class).setUniqueUsername(name);
    }
    
    @And("^I change my unique username to previously remembered unique username$")
    public void IChangeUniqueUserNameToRemembered() throws Exception {
        String rememberedUniqueUsername = context.getPagesCollection().getPage(AccountPage.class).getRememberedUniqueUsername();
        context.getPagesCollection().getPage(AccountPage.class).setUniqueUsername(rememberedUniqueUsername);
    }

    @Then("^I see error message for unique username saying (.*)")
    public void ISeeErrorMessageForUniqueUsername(String error) throws Exception {
        assertThat("Error does not match", context.getPagesCollection().getPage(AccountPage.class).getUniqueUsernameError(),
                is(error));
    }

    @Then("^I see hint message for unique username saying (.*)")
    public void ISeeHintMessageForUniqueUsername(String hint) throws Exception {
        assertThat("Hint does not match", context.getPagesCollection().getPage(AccountPage.class).getUniqueUsernameHint(), equalTo(hint));
    }

    @When("^I drop picture (.*) to account preferences$")
    public void IDropPicture(String pictureName) throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).dropPicture(pictureName);
    }

    @When("^I upload picture (.*) to account preferences$")
    public void IUploadPicture(String pictureName) throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).uploadPicture(pictureName);
    }

    @Then("^I set my accent color to (\\w+)$")
    public void ISetMyAccentColorTo(String colorName) throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).selectAccentColor(colorName);
    }

    @Then("^I verify my accent color in color picker is set to (\\w+) color$")
    public void IVerifyMyAccentColor(String colorName) throws Exception {
        final int expectedColorId = AccentColor.getByName(colorName).getId();
        final int actualColorId = context.getPagesCollection().getPage(AccountPage.class).getCurrentAccentColorId();
        assertThat("my actual accent color is not set", actualColorId, equalTo(expectedColorId));
    }

    @Then("^I verify my avatar background color is set to (\\w+) color$")
    public void IVerifyMyAvatarColor(String colorName) throws Exception {
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor avatarColor = context.getPagesCollection().getPage(AccountPage.class).getCurrentAvatarAccentColor();
        assertTrue("my avatar background accent color is not set", avatarColor == expectedColor);
    }

    @When("^I click delete account button on settings page$")
    public void IClickDeleteAccountButton() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).clickDeleteAccountButton();
    }

    @When("^I click cancel deletion button on settings page$")
    public void IClickCancelDeleteButton() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).clickCancelDeleteAccountButton();
    }

    @When("^I click send button to delete account$")
    public void IClickSendButton() throws Exception {
        context.getPagesCollection().getPage(AccountPage.class).clickConfirmDeleteAccountButton();
    }

    @Then("^I see unique username for (.*) in account preferences$")
    public void ICanSeeUniqueUsernameToUser(String userAlias) throws Exception {
        ClientUser user = context.getUsersManager().findUserBy(userAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        // username given. strict check for username
        String uniqueUsername = user.getUniqueUsername();
        assertThat(context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername(),
                equalTo(uniqueUsername));
    }
    @Then("I remember unique username of (.*)")
    public void RememberUniqueUsername(String nameAlias) throws Exception {
        ClientUser user = context.getUsersManager().findUserByNameOrNameAlias(nameAlias);
        rememberedUniqueUsername = user.getUniqueUsername();
    }
}
