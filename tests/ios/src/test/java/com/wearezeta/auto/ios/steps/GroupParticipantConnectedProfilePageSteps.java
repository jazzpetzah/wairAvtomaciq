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
     * Verify user name/email on participant profile page
     *
     * @param value user name or email
     * @param fieldType either name or email
     * @throws Exception
     * @step. ^I see (.*) (name|email) on Group participant profile page$
     */
    @When("^I (do not )?see (.*) (name|email) on Group participant profile page$")
    public void IVerifyUserOtherUserProfilePage(String shouldNotSee, String value, String fieldType) throws Exception {
        boolean result;
        switch (fieldType) {
            case "name":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.NAME_ALIAS);
                if (shouldNotSee == null) {
                    result = getPage().isNameVisible(value);
                } else {
                    result = getPage().isNameInvisible(value);
                }
                break;
            case "email":
                value = usrMgr.replaceAliasesOccurences(value, ClientUsersManager.FindBy.EMAIL_ALIAS);
                if (shouldNotSee == null) {
                    result = getPage().isEmailVisible(value);
                } else {
                    result = getPage().isEmailInvisible(value);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown field type '%s'", fieldType));
        }
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' field is expected to be visible", value), result);
        } else {
            Assert.assertTrue(String.format("'%s' field is expected to be invisible", value), result);
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
