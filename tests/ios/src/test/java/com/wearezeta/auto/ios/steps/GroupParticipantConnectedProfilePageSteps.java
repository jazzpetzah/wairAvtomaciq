package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.group.GroupConnectedParticipantProfilePage;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupParticipantConnectedProfilePageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupConnectedParticipantProfilePage getPage() throws Exception {
        return pagesCollection.getPage(GroupConnectedParticipantProfilePage.class);
    }

    /**
     * Tap the corresponding button on participant profile page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Remove From Conversation|Open Conversation|X|Open Menu) button on Group participant profile page$
     */
    @When("^I tap (Remove From Conversation|Open Conversation|X|Open Menu) button on Group participant profile page$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }

    /**
     * Verify user details on participant profile page
     *
     * @param shouldNotSee equals to null if the label should be visible
     * @param value user name or unique username or Address Book name
     * @param fieldType either name or unique username or Address Book name
     * @throws Exception
     * @step. ^I (do not )?see (name|unique username|Address Book name) (".*" |\s*)on Group participant profile page$
     */
    @When("^I (do not )?see (name|unique username|Address Book name) (\".*\" |\\s*)on Group participant profile page$")
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
     * Click on Devices button
     *
     * @param tabName either Devices or Details
     * @throws Exception
     * @step. ^I switch to (Devices|Details) tab$
     */
    @When("^I switch to (Devices|Details) tab on Group participant profile page$")
    public void IChangeTab(String tabName) throws Exception {
        getPage().switchToTab(tabName);
    }
}
