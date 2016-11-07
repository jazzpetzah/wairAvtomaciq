package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonSteps;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.group.GroupInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;
import java.util.stream.Collectors;

public class GroupInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupInfoPage getGroupInfoPage() throws Exception {
        return pagesCollection.getPage(GroupInfoPage.class);
    }

    @When("^I change group conversation name to \"(.*)\" on Group info page$")
    public void IChangeConversationNameTo(String name) throws Exception {
        getGroupInfoPage().setGroupChatName(name);
    }

    /**
     * Tap the corresponding button on group info page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Add People|X|Open Menu|Also Leave) (?:checkbox|button) on Group info page$
     */
    @When("^I tap (Add People|X|Open Menu|Also Leave) (?:checkbox|button) on Group info page$")
    public void ITapButton(String btnName) throws Exception {
        getGroupInfoPage().tapButton(btnName);
    }

    /**
     * Try to input conversations name with given length
     *
     * @param length length of the conversation's name
     * @throws Exception
     * @step. ^I try to change group conversation name to random with length (\\d+) on Group info page$"
     */
    @When("^I try to change group conversation name to random with length (\\d+) on Group info page$")
    public void IChangeConversationNameToRandom(int length) throws Exception {
        String name = CommonUtils.generateRandomString(length);
        getGroupInfoPage().setGroupChatName(name);
    }

    @Then("^I see that the conversation name contains users? (.*)$")
    public void IVerifyCorrectConversationName(String nameAliases) throws Exception {
        final List<String> expectedNames = CommonSteps.splitAliases(nameAliases).stream().
                map(x -> usrMgr.replaceAliasesOccurences(x, ClientUsersManager.FindBy.NAME_ALIAS)).
                collect(Collectors.toList());
        Assert.assertTrue(String.format("Group conversation name does not contain all the aliases from '%s'",
                nameAliases),
                getGroupInfoPage().isCorrectConversationName(expectedNames));
    }

    @Then("^I see conversation name \"(.*)\" on Group info page$")
    public void ISeeCorrectConversationName(String expectedName) throws Exception {
        Assert.assertTrue(String.format("Group conversation name is not equal to '%s'", expectedName),
                getGroupInfoPage().isGroupNameEqualTo(expectedName));
    }

    @When("^I select participant (.*) on Group info page$")
    public void ISelectParticipant(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getGroupInfoPage().selectParticipant(name);
    }

    @Then("^I see \"(\\d+) people\" label on Group info page$")
    public void ISeeThatConversationHasNumberPeople(int number) throws Exception {
        Assert.assertTrue(String.format("The actual number of people in the chat is not the same as expected number %s",
                number), getGroupInfoPage().isNumberOfPeopleEquals(number));
    }

    @When("^I see (\\d+) participants? avatars? on Group info page$")
    public void ISeeNumberParticipantsAvatars(int expectedCount) throws Exception {
        final int actual = getGroupInfoPage().getParticipantsAvatarsCount();
        Assert.assertTrue(String.format(
                "Actual number of avatars (%s) is not the same as expected (%s)", actual, expectedCount),
                actual == expectedCount);
    }

    /**
     * Verifies whether a contact is present on Group chat info page
     *
     * @param contact username
     * @throws Exception
     * @step. ^I (do not )?see participant name (.*) on Group info page$
     */
    @Then("^I (do not )?see participant name (.*) on Group info page$")
    public void ISeeContactInGroupInfo(String shouldNotSee, String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("User '%s' should be visible", contact),
                    getGroupInfoPage().isParticipantVisible(contact));
        } else {
            Assert.assertTrue(String.format("User '%s' should not be visible", contact),
                    getGroupInfoPage().isParticipantInvisible(contact));
        }
    }

    /**
     * Verify current length of group name
     *
     * @param expectedLength the expected number of chars in group name
     * @throws Exception
     * @step. ^I see the length of group conversation name equals to (\d+) on Group info page$
     */
    @Then("^I see the length of group conversation name equals to (\\d+) on Group info page$")
    public void IVerifyNameLength(int expectedLength) throws Exception {
        final int actualLength = getGroupInfoPage().getGroupNameLength();
        Assert.assertTrue(String.format("The actual group name length %d is not equal to the expected length %d",
                actualLength, expectedLength), actualLength == expectedLength);
    }
}
