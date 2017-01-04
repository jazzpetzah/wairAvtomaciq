package com.wearezeta.auto.android_tablet.steps.details_overlay.group;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.group.TabletGroupInfoPopover;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupInfoPopoverSteps {
    private TabletGroupInfoPopover getGroupInfoPopover() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletGroupInfoPopover.class);
    }

    /**
     * Renames a group conversation by first tapping on the participants header
     * and then sending the message to the text input field
     *
     * @param newConversationName the new conversation name
     * @throws Exception
     * @step. ^I rename group conversation to (.*) on Group info popover$
     */
    @When("^I rename group conversation to (.*) on Group info popover")
    public void IRenameGroupConversationTo(String newConversationName) throws Exception {
        getGroupInfoPopover().tapOnParticipantsHeader();
        getGroupInfoPopover().renameGroupChat(newConversationName);
    }

    /**
     * Tap on all buttons on Group info page
     *
     * @param buttonName could be add people/X/open menu
     * @throws Exception
     * @step. ^I tap (add people|open menu) button on Group info popover$
     */
    @When("^I tap (add people|open menu) button on Group info popover")
    public void ITapAddContactButton(String buttonName) throws Exception {
        getGroupInfoPopover().tapButton(buttonName);
    }

    /**
     * Taps on a contact from the group details page (seems out of place)
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on contact (.*) on Group info popover$
     */
    @When("^I tap on contact (.*) on Group info popover")
    public void ITapOnContact(String contact) throws Exception {
        try {
            contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getGroupInfoPopover().tapOnParticipant(contact);
    }

    /**
     * Check that any UI content of Group info page is showed
     *
     * @param shouldNotSee equals null means the Group info popover should be visible
     * @throws Exception
     * @step. ^I( do not)? see Group info popover$
     */
    @Then("^I( do not)? see Group info popover$")
    public void ISeeCorrectParticipantPage(String shouldNotSee) throws Exception {
        if(shouldNotSee == null) {
            Assert.assertTrue("The Group info popover is invisible",getGroupInfoPopover().waitUntilPopoverVisible());
        } else {
            Assert.assertTrue("The Group info popover is visible", getGroupInfoPopover().waitUntilPopoverInvisible());
        }
    }

    /**
     * Checks to see that the conversation name is what is expected -outofplace
     *
     * @param name expected conversation name
     * @throws Exception
     * @step. ^I see that the conversation name is (.*) on Group info popover$
     */
    @Then("^I see the conversation name is (.*) on Group info popover")
    public void IVerifyCorrectConversationName(String name) throws Exception {
        Assert.assertEquals(getGroupInfoPopover().getConversationName(), name);
    }

    /**
     * Checks to see that correct avatars for given users appear
     *
     * @param contacts one or more contacts separated by comma
     * @throws Exception
     * @step. ^I see the( verified)? participant avatars? for (.*) on Group info popover$
     */
    @Then("^I see the( verified)? participant avatars? for (.*) on Group info popover")
    public void ISeeCorrectParticipantAvatars(String checkVerifiedContact, String contacts) throws Exception {
        for (String userName : AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(contacts)) {
            userName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(userName).getName();
            if (checkVerifiedContact == null) {
                Assert.assertTrue(String.format("The avatar for '%s' is not visible", userName),
                        getGroupInfoPopover().waitUntilParticipantAvatarVisible(userName));
            } else {
                Assert.assertTrue(String.format("The verified avatar for '%s' is not visible", userName),
                        getGroupInfoPopover().waitUntilVerifiedParticipantAvatarVisible(userName));
            }
        }
    }

    /**
     * Verify the participant is or is not visible
     *
     * @param shouldNotSee equals null means the participant should be visible
     * @param userName     the user name
     * @throws Exception
     * @step. ^I( do not)? see participant (.*) on Group info popover$
     */
    @Then("^I( do not)? see participant (.*) on Group info popover")
    public void ISeeContact(String shouldNotSee, String userName) throws Exception {
        userName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(userName).getName();
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Participant %s is invisible", userName),
                    getGroupInfoPopover().waitUntilParticipantVisible(userName));
        } else {
            Assert.assertTrue(String.format("Participant %s is still visible", userName),
                    getGroupInfoPopover().waitUntilParticipantInvisible(userName));
        }
    }

    /**
     * Checks to see that the correct number of users appears in the group name
     *
     * @param countOfParticipants int value indicate how many participants in group
     * @param type                could be people or person
     * @throws Exception
     * @step. ^I see "(\d+) (people|person)" in sub header on Group info popover$
     */
    @Then("^I see \"(\\d+) (people|person)\" in sub header on Group info popover")
    public void IVerifyParticipantNumber(int countOfParticipants, String type) throws Exception {
        String expectHeader = String.format("%d %s", countOfParticipants, type);
        Assert.assertTrue(String.format("Cannot find the Sub Hader contains '%s'", expectHeader),
                getGroupInfoPopover().waitUntilSubHeaderVisible(expectHeader));
    }
}
