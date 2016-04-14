package com.wearezeta.auto.ios.steps;

import java.util.List;

import cucumber.api.java.en.Then;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(OtherUserPersonalInfoPage.class);
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

    @When("^I confirm remove$")
    public void IConfirmRemove() throws Exception {
        getOtherUserPersonalInfoPage().confirmRemove();
    }

    @When("I tap on start dialog button on other user profile page")
    public void ITapStartDialogOnOtherUserPage() throws Exception {
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
     * @param email             user email
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

    /**
     * Click on Devices button
     *
     * @param tabName either Devices or Details
     * @throws Exception
     * @step. ^I switch to (Devices|Details) tab$
     */
    @When("^I switch to (Devices|Details) tab$")
    public void IChangeTab(String tabName) throws Exception {
        getOtherUserPersonalInfoPage().switchToTab(tabName);
    }

    /**
     * Checks the number of devices in participant devices tab
     *
     * @param expectedNumDevices Expected number of devices
     * @throws Exception
     * @step. ^I see (\d+) devices shown in participant devices tab$
     * tab$
     */
    @When("^I see (\\d+) devices shown in participant devices tab$")
    public void ISeeDevicesShownInDevicesTab(int expectedNumDevices) throws Exception {
        int numDevices = getOtherUserPersonalInfoPage().getParticipantDevicesCount();
        Assert.assertTrue("The expected number of devices: " + expectedNumDevices +
                " is not equals to actual count: " + numDevices, expectedNumDevices == numDevices);
    }

    /**
     * Open the details page of corresponding device on conversation details page
     *
     * @param deviceIndex the device index. Starts from 1
     * @throws Exception
     * @step. ^I open details page of device number (\d+)$
     */
    @When("^I open details page of device number (\\d+)$")
    public void IOpenDeviceDetails(int deviceIndex) throws Exception {
        getOtherUserPersonalInfoPage().openDeviceDetailsPage(deviceIndex);
    }

    /**
     * Verify whether the shield icon is visible on conversation details page
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon on conversation details page$
     */
    @Then("^I (do not )?see shield icon on conversation details page$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible on convo details page",
                    getOtherUserPersonalInfoPage().isShieldIconVisible());
        } else {
            Assert.assertTrue("The shield icon is still visible on convo details page",
                    getOtherUserPersonalInfoPage().isShieldIconNotVisible());
        }
    }
    
    /**
     * Verify all device with correct IDs are presented on participant devices tab
     * 
     * @step. ^I see user (.*) devices? IDs? (?:is|are) presented on participant devices tab$
     * @param name username
     * @throws Exception
     */
    @Then("^I see user (.*) devices? IDs? (?:is|are) presented on participant devices tab$")
    public void ISeeUserDeveceIDPresentedOnDetailsPage(String name) throws Exception {
        List<String> deviceIDs = CommonSteps.getInstance().GetDevicesIDsForUser(name);
        for (String id : deviceIDs) {
            Assert.assertTrue(String.format("Device ID '%s' is not visible", id), getOtherUserPersonalInfoPage()
                .isUserDeviceIdVisible(id));
        }
    }

    /**
     * Tap the corresponding link on user details page. Since we cannot detect the exact link position
     * we just assume this link is located at the bottom left corner of the container text block.
     *
     * @step. ^I tap "(.*)" link in user details$
     * @param expectedLink the full text of the link to be clicked
     * @throws Exception
     */
    @When("^I tap \"(.*)\" link in user details$")
    public void ITapLink(String expectedLink) throws Exception {
        getOtherUserPersonalInfoPage().tapLink(expectedLink);
    }


}
