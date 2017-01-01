package com.wearezeta.auto.android.steps.details_overlay.group;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.details_overlay.group.GroupInfoPage;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupInfoPageSteps {
    private GroupInfoPage getGroupInfoPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(GroupInfoPage.class);
    }

    /**
     * Renames a group conversation by first tapping on the participants header
     * and then sending the message to the text input field
     *
     * @param newConversationName the new conversation name
     * @throws Exception
     * @step. ^I rename group conversation to (.*) on Group info page$
     */
    @When("^I rename group conversation to (.*) on Group info page$")
    public void IRenameGroupConversationTo(String newConversationName) throws Exception {
        getGroupInfoPage().tapOnParticipantsHeader();
        getGroupInfoPage().renameGroupChat(newConversationName);
    }

    /**
     * Tap on all buttons on Group info page
     *
     * @param buttonName could be add people/X/open menu
     * @throws Exception
     * @step. ^I tap (add people|open menu) button on Group info page$
     */
    @When("^I tap (add people|open menu) button on Group info page$")
    public void ITapAddContactButton(String buttonName) throws Exception {
        getGroupInfoPage().tapButton(buttonName);
    }

    /**
     * Taps on a contact from the group details page (seems out of place)
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on contact (.*) on Group info page$
     */
    @When("^I tap on contact (.*) on Group info page$")
    public void ITapOnContact(String contact) throws Exception {
        try {
            contact = AndroidTestContextHolder.getInstance().getTestContext().getUserManager()
                    .findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getGroupInfoPage().tapOnParticipant(contact);
    }

    /**
     * Check that any UI content of Group info page is showed
     *
     * @throws Exception
     * @step. ^I see Group info page$
     */
    @Then("^I see Group info page$")
    public void ISeeCorrectParticipantPage() throws Exception {
        Assert.assertTrue(getGroupInfoPage().waitUntilPageVisible());
    }

    /**
     * Checks to see that the conversation name is what is expected -outofplace
     *
     * @param name expected conversation name
     * @throws Exception
     * @step. ^I see that the conversation name is (.*) on Group info page$
     */
    @Then("^I see the conversation name is (.*) on Group info page$")
    public void IVerifyCorrectConversationName(String name) throws Exception {
        Assert.assertEquals(getGroupInfoPage().getConversationName(), name);
    }

    /**
     * Checks to see that correct avatars for given users appear
     *
     * @param contacts one or more contacts separated by comma
     * @throws Exception
     * @step. ^I see the( verified)? participant avatars? for (.*) on Group info page$
     */
    @Then("^I see the( verified)? participant avatars? for (.*) on Group info page")
    public void ISeeCorrectParticipantAvatars(String checkVerifiedContact, String contacts) throws Exception {
        for (String userName : AndroidTestContextHolder.getInstance().getTestContext().getUserManager()
                .splitAliases(contacts)) {
            userName = AndroidTestContextHolder.getInstance().getTestContext().getUserManager()
                    .findUserByNameOrNameAlias(userName).getName();
            if (checkVerifiedContact == null) {
                Assert.assertTrue(String.format("The avatar for '%s' is not visible", userName),
                        getGroupInfoPage().waitUntilParticipantAvatarVisible(userName));
            } else {
                Assert.assertTrue(String.format("The verified avatar for '%s' is not visible", userName),
                        getGroupInfoPage().waitUntilVerifiedParticipantAvatarVisible(userName));
            }
        }
    }

    /**
     * Verify the participant is or is not visible
     *
     * @param shouldNotSee equals null means the participant should be visible
     * @param userName     the user name
     * @throws Exception
     * @step. ^I( do not)? see participant (.*) on Group info page$
     */
    @Then("^I( do not)? see participant (.*) on Group info page$")
    public void ISeeContact(String shouldNotSee, String userName) throws Exception {
        userName = AndroidTestContextHolder.getInstance().getTestContext().getUserManager()
                .findUserByNameOrNameAlias(userName).getName();
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Participant %s is invisible", userName),
                    getGroupInfoPage().waitUntilParticipantVisible(userName));
        } else {
            Assert.assertTrue(String.format("Participant %s is still visible", userName),
                    getGroupInfoPage().waitUntilParticipantInvisible(userName));
        }
    }

    /**
     * Checks to see that the correct number of users appears in the group name
     *
     * @param countOfParticipants int value indicate how many participants in group
     * @param type                could be people or person
     * @throws Exception
     * @step. ^I see "(\d+) (people|person)" in sub header on Group info page$
     */
    @Then("^I see \"(\\d+) (people|person)\" in sub header on Group info page$")
    public void IVerifyParticipantNumber(int countOfParticipants, String type) throws Exception {
        String expectHeader = String.format("%d %s", countOfParticipants, type);
        Assert.assertTrue(String.format("Cannot find the Sub Hader contains '%s'",expectHeader),
                getGroupInfoPage().waitUntilSubHeaderVisible(expectHeader));
    }
}
