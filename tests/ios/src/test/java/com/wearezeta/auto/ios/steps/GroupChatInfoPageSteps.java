package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.GroupChatInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

    private GroupChatInfoPage getGroupChatInfoPage() throws Exception {
        return (GroupChatInfoPage) pagesCollecton.getPage(GroupChatInfoPage.class);
    }

    private ContactListPage getContactListPage() throws Exception {
        return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
    }

    @When("^I press leave converstation button$")
    public void IPressLeaveConverstationButton() throws Exception {
        getGroupChatInfoPage().leaveConversation();
    }

    @When("I change group conversation name to (.*)")
    public void IChangeConversationNameTo(String name) throws Exception {
        getGroupChatInfoPage().setGroupChatName(name);
    }

    /**
     * Try to input empty conversation name
     * 
     * @step. ^I try to change group conversation name to empty$
     * @throws Exception
     */
    @When("^I try to change group conversation name to empty$")
    public void IChangeConversationNameToEmpty() throws Exception {
        getGroupChatInfoPage().setGroupChatName("");
    }

    /**
     * Try to input conversations name with given length
     * 
     * @step. ^I try to change group conversation name to random with length (.*)$"
     * @param length length of the conversation's name
     * @throws Exception
     */
    @When("^I try to change group conversation name to random with length (.*)$")
    public void IChangeConversationNameToRandom(int length) throws Exception {
        String name = CommonUtils.generateRandomString(length);
        getGroupChatInfoPage().setGroupChatName(name);
    }

    @Then("^I see that the conversation name is correct with (.*) and (.*)$")
    public void IVerifyCorrectConversationName(String contact1, String contact2) throws Exception {
        Assert.assertTrue(getGroupChatInfoPage().isCorrectConversationName(contact1, contact2));
    }

    @When("I see correct conversation name (.*)")
    public void ISeeCorrectConversationName(String name) throws Exception {
        Assert.assertEquals(getGroupChatInfoPage().getGroupChatName(), name);
    }

    @When("^I see the correct participant (.*) avatar$")
    public void IVerifyCorrectParticipantAvatars(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(getGroupChatInfoPage().areParticipantAvatarCorrect(contact));
    }

    @When("^I exit the group info page$")
    // may require reworking when the UI changes
    public void IExitGroupInfoPage() throws Exception {
        getGroupChatInfoPage().exitGroupInfoPage();
    }

    @When("^I see leave conversation alert$")
    public void ISeeLeaveConversationAlert() throws Throwable {
        Assert.assertTrue(getGroupChatInfoPage().isLeaveConversationAlertVisible());
    }

    @Then("^I press leave$")
    public void IPressLeave() throws Throwable {
        getGroupChatInfoPage().confirmLeaveConversation();
        getContactListPage().waitForContactListToLoad();
    }

    @When("^I select contact (.*)$")
    public void ISelectContact(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getGroupChatInfoPage().selectContactByName(name);
    }

    @When("I tap on not connected contact (.*)")
    public void ITapOnNotConnectedContact(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getGroupChatInfoPage().selectNotConnectedUser(name);
    }

    @Then("^I see that conversation has (.*) people$")
    public void ISeeThatConversationHasNumberPeople(int number) throws Throwable {
        int actualNumberOfPeople = getGroupChatInfoPage().numberOfPeopleInConversation();
        Assert.assertTrue("Actual number of people in chat (" + actualNumberOfPeople + ") is not the same as expected ("
            + number + ")", actualNumberOfPeople == number);
    }

    @When("^I see (.*) participants avatars$")
    public void ISeeNumberParticipantsAvatars(int number) throws Throwable {
        int actual = getGroupChatInfoPage().numberOfParticipantsAvatars();
        Assert.assertTrue("Actual number of avatars (" + actual + ") is not the same as expected (" + number + ")",
            actual == number);
    }

    @When("I tap on add button on group chat info page")
    public void ITapAddButtonOnGroupChatInfoPage() throws Exception {
        getGroupChatInfoPage().clickOnAddButton();
    }

    /**
     * Click on continue button on share history warning dialog
     * 
     * @step. I click on Continue button on share history warning
     * 
     * @throws Throwable
     */
    @When("I click on Continue button on share history warning")
    public void IClickOnContinueButtonInAddPeopleToGroupChatDialog() throws Throwable {
        getGroupChatInfoPage().clickOnAddDialogContinueButton();
    }

    @When("I add to existing group chat contact (.*)")
    public void IAddToExistingChatContact(String contact) throws Throwable {
        ITapAddButtonOnGroupChatInfoPage();
        IClickOnContinueButtonInAddPeopleToGroupChatDialog();

        PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
        pickerSteps.WhenISeePeoplePickerPage();
        pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact);
        pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact);
        pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact);
        pickerSteps.WhenIClickOnAddToConversationButton();
    }

    /**
     * Verifies that contact is not presented on Group chat info page
     * 
     * @step. ^I see that (.*) is not present on group chat info page$
     * 
     * @param contact username
     * @throws Exception
     */
    @Then("^I see that (.*) is not present on group chat info page$")
    public void ISeeContactIsNotPresentOnGroupChatPage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(getGroupChatInfoPage().waitForContactToDisappear(contact));
    }
}
