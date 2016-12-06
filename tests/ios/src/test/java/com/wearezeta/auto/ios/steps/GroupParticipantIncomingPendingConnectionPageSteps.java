package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.details_overlay.group.GroupPendingParticipantIncomingConnectionPage;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class GroupParticipantIncomingPendingConnectionPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupPendingParticipantIncomingConnectionPage getPage() throws Exception {
        return pagesCollection.getPage(GroupPendingParticipantIncomingConnectionPage.class);
    }

    /**
     * Verify user name/email presence on Group participant Pending incoming connection page
     *
     * @param shouldNotSee equals to null if the label should be visible
     * @param value        the actual value or alias
     * @param fieldType    either name or Address Book name or unique username
     * @throws Exception
     * @step. ^I (do not )?see (name|unique username|Address Book name|common friends count) (".*" |\s*)on Group participant Pending incoming connection page$
     */
    @Then("^I (do not )?see (name|unique username|Address Book name|common friends count) (\".*\" |\\s*)on Group participant Pending incoming connection page$")
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
                        getPage().isUserDetailInvisible(fieldType));
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

    /**
     * Verify button visibility on Group participant Pending incoming connection page
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      button name
     * @throws Exception
     * @step. ^I (do not )?see (Connect|X|Remove From Group) button on Group participant Pending incoming connection page$
     */
    @Then("^I (do not )?see (Connect|X|Remove From Group) " +
            "button on Group participant Pending incoming connection page$")
    public void ISeeButton(String shouldNotSee, String btnName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' button is expected to be visible", btnName),
                    getPage().isButtonVisible(btnName));
        } else {
            Assert.assertTrue(String.format("'%s' button is expected to be invisible", btnName),
                    getPage().isButtonInvisible(btnName));
        }
    }

    /**
     * Tap the corresponding button on Group participant Pending incoming connection page
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Connect|X|Remove From Group) button on Group participant Pending incoming connection page$
     */
    @When("^I tap (Connect|X|Remove From Group) " +
            "button on Group participant Pending incoming connection page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }
}
