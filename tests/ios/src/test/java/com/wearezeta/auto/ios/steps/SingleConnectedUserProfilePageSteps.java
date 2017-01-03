package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import cucumber.api.java.en.Then;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.single.SingleConnectedUserProfilePage;

import cucumber.api.java.en.When;

public class SingleConnectedUserProfilePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SingleConnectedUserProfilePage getPage() throws Exception {
        return pagesCollection.getPage(SingleConnectedUserProfilePage.class);
    }

    /**
     * Tap the corresponding button on participant profile page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Create Group|X|Open Menu) button on Single user profile page$
     */
    @When("^I tap (Create Group|X|Open Menu) button on Single user profile page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }

    /**
     * Verify user details on Single user profile page
     *
     * @param shouldNotSee equals to null if the corresponding details should be visible
     * @param value        user name or unique username or Address Book name
     * @param fieldType    either name or email
     * @throws Exception
     * @step. ^I see (name|unique username|Address Book name|common friends count) (".*" |\s*)on Single user profile page$
     */
    @When("^I (do not )?see (name|unique username|Address Book name|common friends count) (\".*\" |\\s*)on Single user profile page$")
    public void ISeeLabel(String shouldNotSee, String fieldType, String value) throws Exception {
        value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.NAME_ALIAS);
        value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        if (shouldNotSee == null) {
            if (value.startsWith("\"")) {
                value = value.trim().replaceAll("^\"|\"$", "");
                Assert.assertTrue(String.format("'%s' field is expected to be visible", value),
                        getPage().isUserDetailVisible(fieldType, value));
            } else {
                Assert.assertTrue(String.format("'%s' field is expected to be visible", fieldType),
                        getPage().isUserDetailVisible(fieldType));
            }
        } else {
            if (value.startsWith("\"")) {
                value = value.trim().replaceAll("^\"|\"$", "");
                Assert.assertTrue(String.format("'%s' field is expected to be invisible", value),
                        getPage().isUserDetailInvisible(fieldType, value));
            } else {
                Assert.assertTrue(String.format("'%s' field is expected to be invisible", fieldType),
                        getPage().isUserDetailInvisible(fieldType));
            }
        }
    }

    private String userAddressBookName = null;

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
     * @step. ^I verify the previously remembered user name from Address Book is displayed on Single user profile page$
     */
    @Then("^I verify the previously remembered user name from Address Book is displayed on Single user profile page$")
    public void IVerifyUsersAddressBookNameOnOtherUserProfilePageIsDisplayed() throws Exception {
        if (userAddressBookName == null) {
            throw new IllegalStateException("Save the Address Book name of the user first!");
        }
        Assert.assertTrue(String.format("User Address Book name '%s' is not visible", userAddressBookName),
                getPage().isUserDetailVisible("address book name", userAddressBookName));
    }

    /**
     * Click on Devices button
     *
     * @param tabName either Devices or Details
     * @throws Exception
     * @step. ^I switch to (Devices|Details) tab$
     */
    @When("^I switch to (Devices|Details) tab on Single user profile page$")
    public void IChangeTab(String tabName) throws Exception {
        getPage().switchToTab(tabName);
    }

    /**
     * Verify whether the shield icon is visible on conversation details page
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon on Single user profile page$
     */
    @Then("^I (do not )?see shield icon on Single user profile page$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible on convo details page",
                    getPage().isShieldIconVisible());
        } else {
            Assert.assertTrue("The shield icon is still visible on convo details page",
                    getPage().isShieldIconNotVisible());
        }
    }

    private static final Timedelta PROFILE_PICTURE_CHANGE_TIMEOUT = Timedelta.fromSeconds(7);
    private static final double PROFILE_PICTURE_MAX_SCORE = 0.7;

    private final ElementState profilePictureState = new ElementState(
            () -> getPage().getProfilePictureScreenshot()
    );

    /**
     * Remember the current sate of user profile picture
     *
     * @throws Exception
     * @step. ^I remember user picture on Single user profile page$
     */
    @When("^I remember user picture on Single user profile page$")
    public void IRememberPicture() throws Exception {
        profilePictureState.remember();
    }

    /**
     * Verify whether user profile picture has been changed or not
     *
     * @param shouldNotBeChanged equals to null if the picture should stay the same
     * @throws Exception
     * @step. ^I see user picture is (not )?changed on Single user profile page$"
     */
    @Then("^I see user picture is (not )?changed on Single user profile page$")
    public void IVerifyPicture(String shouldNotBeChanged) throws Exception {
        if (shouldNotBeChanged == null) {
            Assert.assertTrue("User profile picture is still the same",
                    profilePictureState.isChanged(PROFILE_PICTURE_CHANGE_TIMEOUT, PROFILE_PICTURE_MAX_SCORE));
        } else {
            Assert.assertTrue("User profile picture is expected to be the same",
                    profilePictureState.isNotChanged(PROFILE_PICTURE_CHANGE_TIMEOUT, PROFILE_PICTURE_MAX_SCORE));
        }
    }
}
