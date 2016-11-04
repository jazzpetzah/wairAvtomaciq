package com.wearezeta.auto.ios.steps;

import java.util.List;

import cucumber.api.java.en.Then;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ParticipantProfilePage;

import cucumber.api.java.en.When;

public class ParticipantProfilePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ParticipantProfilePage getParticipantProfilePage() throws Exception {
        return pagesCollection.getPage(ParticipantProfilePage.class);
    }

    @When("^I see (.*) user profile page$")
    public void WhenISeeOtherUserProfilePage(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue(getParticipantProfilePage().isOtherUserProfileNameVisible(name));
    }

    /**
     * Tap the corresponding button on participant profile page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Create Group|Remove From Conversation|Confirm Removal|Confirm Deletion|X|Open Conversation|Open Menu)
     * button on Participant profile page$
     */
    @When("^I tap (Create Group|Remove From Conversation|Confirm Removal|Confirm Deletion|X|Open Conversation|Open Menu) " +
            "button on Participant profile page$")
    public void ITapButton(String btnName) throws Exception {
        getParticipantProfilePage().tapButton(btnName);
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
                getParticipantProfilePage().isActionMenuVisible());
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
                getParticipantProfilePage().isUserNameVisible(username));
    }

    private String userAddressBookName;

    /**
     * Remembers the name of the user how he is saved in the Address Book
     *
     * @param addressbookName name of user in Address Book
     * @throws Exception
     * @step. ^I remember the name of user (.*) in Address Book$
     */
    @When("^I remember the name of user (.*) in Address Book$")
    public void IRememberTheUsersAddressBookName(String addressbookName) throws Exception {
        userAddressBookName = usrMgr.replaceAliasesOccurences(addressbookName, ClientUsersManager.FindBy.NAME_ALIAS);
    }

    /**
     * Verifies that the Address Book name of the user is displayed
     *
     * @throws Exception
     * @step. ^I verify the previously remembered user name from Address Book is displayed on Other User Profile page$
     */
    @Then("^I verify the previously remembered user name from Address Book is displayed on Other User Profile page$")
    public void IVerifyUsersAddressBookNameOnOtherUserProfilePageIsDisplayed() throws Exception {
        if (userAddressBookName == null) {
            throw new IllegalStateException("Save the Address Book name of the user first!");
        }
        Assert.assertTrue(String.format("User Address Book name '%s' is not visible", userAddressBookName),
                getParticipantProfilePage().isUserAddressBookNameVisible(userAddressBookName));
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
                    getParticipantProfilePage().isUserEmailVisible(email));
        } else {
            Assert.assertTrue(String.format("Email '%s' is displayed, but should be hidden", email),
                    getParticipantProfilePage().isUserEmailNotVisible(email));
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
        getParticipantProfilePage().switchToTab(tabName);
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
        Assert.assertTrue(
                String.format("The expected number of devices: %s is not equals to actual count", expectedNumDevices),
                getParticipantProfilePage().isParticipantDevicesCountEqualTo(expectedNumDevices)
        );
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
        getParticipantProfilePage().openDeviceDetailsPage(deviceIndex);
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
                    getParticipantProfilePage().isShieldIconVisible());
        } else {
            Assert.assertTrue("The shield icon is still visible on convo details page",
                    getParticipantProfilePage().isShieldIconNotVisible());
        }
    }

    /**
     * Verify all device with correct IDs are presented on participant devices tab
     *
     * @param name username
     * @throws Exception
     * @step. ^I see user (.*) devices? IDs? (?:is|are) presented on participant devices tab$
     */
    @Then("^I see user (.*) devices? IDs? (?:is|are) presented on participant devices tab$")
    public void ISeeUserDeveceIDPresentedOnDetailsPage(String name) throws Exception {
        List<String> deviceIDs = CommonSteps.getInstance().GetDevicesIDsForUser(name);
        for (String id : deviceIDs) {
            Assert.assertTrue(String.format("Device ID '%s' is not visible", id), getParticipantProfilePage()
                    .isUserDeviceIdVisible(id));
        }
    }

    /**
     * Tap the corresponding link on user details page. Since we cannot detect the exact link position
     * we just assume this link is located at the bottom left corner of the container text block.
     *
     * @param expectedLink the full text of the link to be clicked
     * @throws Exception
     * @step. ^I tap "(.*)" link in user details$
     */
    @When("^I tap \"(.*)\" link in user details$")
    public void ITapLink(String expectedLink) throws Exception {
        getParticipantProfilePage().tapLink(expectedLink);
    }
}
