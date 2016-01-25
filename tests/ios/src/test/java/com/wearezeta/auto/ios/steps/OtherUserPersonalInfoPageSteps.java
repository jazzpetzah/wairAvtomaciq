package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

    private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage() throws Exception {
        return pagesCollecton.getPage(OtherUserPersonalInfoPage.class);
    }

    @When("^I see (.*) user profile page$")
    public void WhenISeeOtherUserProfilePage(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue(getOtherUserPersonalInfoPage().isOtherUserProfileNameVisible(name));
    }

    @When("^I press Add button$")
    public void WhenIPressAddButton() throws Exception {
        getOtherUserPersonalInfoPage().addContactToChat();
    }

    @When("^I click Remove$")
    public void IClickRemove() throws Exception {
        getOtherUserPersonalInfoPage().removeFromConversation();
    }

    @When("^I see warning message$")
    public void ISeeAreYouSure() throws Throwable {
        Assert.assertTrue(getOtherUserPersonalInfoPage().isRemoveFromConversationAlertVisible());
    }

    @When("^I confirm remove$")
    public void IConfirmRemove() throws Throwable {
        getOtherUserPersonalInfoPage().confirmRemove();
    }

    @When("I tap on start dialog button on other user profile page")
    public void ITapStartDialogOnOtherUserPage() throws Throwable {
        getOtherUserPersonalInfoPage().clickOnStartDialogButton();
    }

    /**
     * Close other user personal info page by click on close button
     *
     * @throws Exception
     * @step. ^I click close user profile page button$
     */
    @When("^I click close user profile page button$")
    public void ICloseUserProfileForDialog() throws Exception {
        getOtherUserPersonalInfoPage().clickCloseUserProfileButton();
    }

    /**
     * Opens the conversation details menu by clicking the according button
     *
     * @throws Exception
     * @step. ^I press conversation menu button$
     */
    @When("^I press conversation menu button$")
    public void IPressConversationMenuButton() throws Exception {
        getOtherUserPersonalInfoPage().openConversationMenu();
    }

    /**
     * Presses the silence button in the conversation detail menu
     *
     * @throws Exception
     * @step. ^I press menu silence button$
     */
    @When("^I press menu silence button$")
    public void IPressMenuSilenceButton() throws Exception {
        getOtherUserPersonalInfoPage().clickSilenceMenuButton();
    }

    /**
     * Presses the notify button in the conversation detail menu
     *
     * @throws Exception
     * @step. ^I press menu notify button$
     */
    @When("^I press menu notify button$")
    public void IPressMenuNotifyButton() throws Exception {
        getOtherUserPersonalInfoPage().clickNotifyMenuButton();
    }

    /**
     * Open ellipsis menu in conversation details
     *
     * @throws Exception
     * @step. ^I open ellipsis menu$
     */
    @When("^I open ellipsis menu$")
    public void IOpenEllipsisMenu() throws Exception {
        getOtherUserPersonalInfoPage().openEllipsisMenu();
    }

    /**
     * Click archive menu button in ellipsis menu
     *
     * @throws Exception
     * @step. ^I click archive menu button$
     */
    @When("^I click archive menu button$")
    public void IClickArchiveMenu() throws Exception {
        getOtherUserPersonalInfoPage().clickArchiveMenuButton();
    }

    /**
     * Click delete menu button in ellipsis menu
     *
     * @throws Exception
     * @step. ^I click delete menu button$
     */
    @When("^I click delete menu button$")
    public void IClickDeleteMenu() throws Exception {
        getOtherUserPersonalInfoPage().clickDeleteMenuButton();
    }

    /**
     * Click delete to confirm conversation content deletion
     *
     * @throws Exception
     * @step. ^I confirm delete conversation content$
     */
    @When("^I confirm delete conversation content$")
    public void IConfirmDelete() throws Exception {
        getOtherUserPersonalInfoPage().clickConfirmDeleteButton();
    }

    /**
     * Select Also Leave option on Delete conversation dialog
     *
     * @throws Exception
     * @step. ^I select Also Leave option on Delete conversation dialog$
     */
    @When("^I select Also Leave option on Delete conversation dialog$")
    public void ISelectAlsoLeaveOptionOnDeleteDialog() throws Exception {
        getOtherUserPersonalInfoPage().clickAlsoLeaveButton();
    }

    /**
     * Clicks on the Block button in the profile menu
     *
     * @throws Throwable
     * @step. ^I press menu Block button$
     */
    @When("^I press menu Block button$")
    public void IPressMenuBlockButton() throws Throwable {
        getOtherUserPersonalInfoPage().clickBlockMenuButton();
    }

    /**
     * Confirms the blocking alert by clicking block
     *
     * @throws Throwable
     * @step. ^I confirm blocking alert$
     */
    @When("^I confirm blocking alert$")
    public void IConfirmBlockingAlert() throws Throwable {
        getOtherUserPersonalInfoPage().clickBlockMenuButton();
    }

    /**
     * Click on cancel button
     *
     * @throws Exception
     * @step. I click Cancel button
     */
    @When("^I click Cancel button$")
    public void IClickCancelButton() throws Exception {
        getOtherUserPersonalInfoPage().clickCancelButton();
    }

    /**
     * Verify if conversation action menu is visible
     *
     * @throws Exception
     * @step. I see conversation action menu
     */
    @When("^I see conversation action menu$")
    public void ISeeConversationActionMenu() throws Exception {
        Assert.assertTrue("Conversation action menu is not visible",
                getOtherUserPersonalInfoPage().isActionMenuVisible());
    }

    /**
     * Verify user name on Other User Profile page
     *
     * @param user user name
     * @throws Exception
     * @step. ^I verify username (.*) on Other User Profile page is displayed and correct$
     */
    @When("^I verify username (.*) on Other User Profile page is displayed")
    public void IVerifyUserOtherUserProfilePage(String user) throws Exception {
        String username = usrMgr.findUserByNameOrNameAlias(user).getName();
        Assert.assertTrue(String.format("Use name '%s' is not visible", username),
                getOtherUserPersonalInfoPage().isUserNameVisible(username));
    }

    /**
     * Verify that user email on Other User Profile page is displayed and correct
     *
     * @param email user email
     * @param shouldNotBVisible equals to null if the email should be visible
     * @throws Exception
     * @step. ^I verify user email (.*) on Other User Profile page is correct and displayed$
     */
    @When("^I verify user email for (.*) on Other User Profile page is (not )?displayed$")
    public void IVerifyUserEmailOnOtherUserProfilePageIsDisplayedAndCorrect(String email,
                                                                            String shouldNotBVisible) throws Exception {
        email = usrMgr.findUserByNameOrNameAlias(email).getEmail();
        if (shouldNotBVisible == null) {
            Assert.assertTrue(String.format("Email '%s' is not visible", email),
                    getOtherUserPersonalInfoPage().isUserEmailVisible(email));
        } else {
            Assert.assertTrue(String.format("Email '%s' is displayed, but should be hidden", email),
                    getOtherUserPersonalInfoPage().isUserEmailNotVisible(email));
        }
    }

}
