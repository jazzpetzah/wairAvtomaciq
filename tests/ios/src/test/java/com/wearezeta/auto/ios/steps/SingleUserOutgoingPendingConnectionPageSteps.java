package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.details_overlay.single.SinglePendingUserOutgoingConnectionPage;

import cucumber.api.java.en.When;

public class SingleUserOutgoingPendingConnectionPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SinglePendingUserOutgoingConnectionPage getPage() throws Exception {
        return pagesCollection.getPage(SinglePendingUserOutgoingConnectionPage.class);
    }

    /**
     * Verify user details presence on Single user Pending outgoing connection page
     *
     * @param shouldNotSee equals to null if the label should be visible
     * @param value        the actual value or alias
     * @param fieldType    either unique username or Address Book name or name
     * @throws Exception
     * @step. ^I (do not )?see (email|name) (".*" |\s*)on Single user Pending outgoing connection page$"
     */
    @Then("^I (do not )?see (unique username|Address Book name|name) (\".*\" |\\s*)on Single user Pending outgoing connection page$")
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
                Assert.assertTrue(String.format("'%s' field is expected to be invisible", value),
                        getPage().isUserDetailInvisible(fieldType));
            }
        }
    }

    /**
     * Verify button visibility on Single user Pending outgoing connection page
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      button name
     * @throws Exception
     * @step. ^I (do not )?see (Cancel Request|Connect) button on Single user Pending outgoing connection page$
     */
    @Then("^I (do not )?see (Cancel Request|Connect) button on Single user Pending outgoing connection page$")
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
     * Tap the corresponding button on Single user Pending incoming connection page
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Cancel Request|Connect) button on Single user Pending incoming connection page$
     */
    @When("^I tap (Cancel Request|Connect) button on Single user Pending outgoing connection page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }
}
