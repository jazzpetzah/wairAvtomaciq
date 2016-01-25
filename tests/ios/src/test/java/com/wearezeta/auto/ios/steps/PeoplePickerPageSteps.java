package com.wearezeta.auto.ios.steps;

import java.awt.datatransfer.UnsupportedFlavorException;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

    private PeoplePickerPage getPeoplePickerPage() throws Exception {
        return pagesCollecton.getPage(PeoplePickerPage.class);
    }

    private ContactListPage getСontactListPage() throws Exception {
        return pagesCollecton.getPage(ContactListPage.class);
    }

    @When("^I see People picker page$")
    public void WhenISeePeoplePickerPage() throws Exception {
        Assert.assertTrue(getPeoplePickerPage().isPeoplePickerPageVisible());
    }

    @When("I see Upload contacts dialog")
    public void WhenISeeUploadContactsDialog() throws Exception {
        Assert.assertTrue("Upload dialog is not shown", getPeoplePickerPage()
                .isUploadDialogShown());
    }

    @When("I dont see Upload contacts dialog")
    public void WhenIDontSeeUploadContactsDialog() throws Exception {
        Assert.assertFalse("Upload dialog is shown", getPeoplePickerPage()
                .isUploadDialogShown());
    }

    @When("I click Continue button on Upload dialog")
    public void IClickContinueButtonOnUploadDialog() throws Exception {
        getPeoplePickerPage().clickContinueButton();
    }

    /**
     * Verifies that CONNECT label is visible
     *
     * @throws Exception
     * @step. I see CONNECT label
     */

    @When("I (dont )?see CONNECT label")
    public void ISeePeopleYouMayKnowLabel(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("CONNECT label is not visible",
                    getPeoplePickerPage().isPeopleYouMayKnowLabelVisible());
        } else {
            Assert.assertTrue("CONNECT label is visible",
                    getPeoplePickerPage().isPeopleYouMayKnowLabelInvisible());
        }
    }

    /**
     * Click maybe later to dismiss contacts import
     *
     * @throws Exception
     */
    @When("^I press maybe later button$")
    public void IPressMaybeLater() throws Exception {
        getPeoplePickerPage().clickMaybeLaterButton();
    }

    /**
     * A workaround for top people not loading immediately
     *
     * @throws Exception
     * @step. I re-enter the people picker if top people list is not there
     */

    @When("I re-enter the people picker if top people list is not there")
    public void IRetryPeoplePickerIfNotLoaded() throws Exception {
        for (int i = 0; i < 3; i++) {
            if (!getPeoplePickerPage().isTopPeopleLabelVisible()) {
                IClickCloseButtonDismissPeopleView();
                Thread.sleep(5000);
                getСontactListPage().openSearch();
            } else {
                break;
            }
        }
    }

    /**
     * A workaround for CONNECT label not loading immediately
     *
     * @throws Exception
     * @step. I re-enter the people picker if CONNECT label is not there
     */
    @When("I re-enter the people picker if CONNECT label is not there")
    public void IRetryPeoplePickerIfNoConnectLabel() throws Exception {
        for (int i = 0; i < 3; i++) {
            if (!getPeoplePickerPage().isConnectLabelVisible()) {
                IClickCloseButtonDismissPeopleView();
                Thread.sleep(5000);
                getСontactListPage().openSearch();
                // getPeoplePickerPage().closeShareContactsIfVisible();
            } else {
                break;
            }
        }
    }

    @When("^I tap on Search input on People picker page$")
    public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
        getPeoplePickerPage().tapOnPeoplePickerSearch();
    }

    @When("^I input in People picker search field (?:user|conversation) name (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserName(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        getPeoplePickerPage().fillTextInPeoplePickerSearch(name);
    }

    @When("^I input in People picker search field user email (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email)
            throws Exception {
        try {
            email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        // getPeoplePickerPage().closeShareContactsIfVisible();
        getPeoplePickerPage().fillTextInPeoplePickerSearch(email);
    }

    @When("^I see user (.*) found on People picker page$")
    public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        Assert.assertTrue(String.format("User '%s' is not presented on People picker page", contact),
                getPeoplePickerPage().getSearchResultsElement(contact).isPresent());
    }

    /**
     * Verify user is not found on people picker
     *
     * @param contact user name
     * @throws Exception
     * @step. ^I see that user (.*) is NOT found on People picker page$
     */
    @When("^I see that user (.*) is NOT found on People picker page$")
    public void WhenISeeUserNotFoundOnPeoplePickerPage(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        Assert.assertFalse("User: " + contact
                + " is presented on People picker page", getPeoplePickerPage().isElementNotFoundInSearch(contact));
    }

    /**
     * Verify that conversation is not presented in search results
     *
     * @param name conversation name to search
     * @throws Exception
     * @step. ^I see conversation (.*) is NOT presented in Search results$
     */
    @When("^I see conversation (.*) is NOT presented in Search results$")
    public void ISeeConversationIsNotFoundInSearchResult(String name)
            throws Exception {
        Assert.assertFalse("Conversation: " + name
                + " is presented in Search results", getPeoplePickerPage().isElementNotFoundInSearch(name));
    }

    /**
     * Verify that conversation is presented in search results
     *
     * @param name conversation name to search
     * @throws Exception
     * @step. ^I see conversation (.*) is presented in Search results$
     */
    @When("^I see conversation (.*) is presented in Search results$")
    public void ISeeConversationIsFoundInSearchResult(String name)
            throws Exception {
        Assert.assertTrue("Conversation: " + name
                + " is not presented in Search results", getPeoplePickerPage()
                .getSearchResultsElement(name).isPresent());
    }

    @When("^I search for user name (.*) and tap on it on People picker page$")
    public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(String contact) throws Throwable {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getPeoplePickerPage().fillTextInPeoplePickerSearch(contact);
        getPeoplePickerPage().selectElementInSearchResults(contact);
    }

    @When("^I don't see Add to conversation button$")
    public void WhenIDontSeeAddToConversationButton() throws Exception {
        Assert.assertTrue("Add to conversation button is visible",
                getPeoplePickerPage().addToConversationNotVisible());
    }

    @When("^I click on Go button$")
    public void WhenIClickOnGoButton() throws Exception {
        getPeoplePickerPage().clickOnGoButton();
    }

    @When("^I click clear button$")
    public void WhenIClickClearButton() throws Exception {
        getPeoplePickerPage().dismissPeoplePicker();
    }

    /**
     * Click on close button to dismiss Invite list
     *
     * @throws Exception
     * @step. ^I click close Invite list button$
     */
    @When("^I click close Invite list button$")
    public void WhenIClickCloseInviteListButton() throws Exception {
        getPeoplePickerPage().closeInviteList();
    }

    /**
     * Select pointed amount of contacts from top people in a row starting from
     * first
     *
     * @param numberOfTopConnections amount of contacts that should be selected
     * @throws Exception
     */
    @Then("I tap on first (.*) top connections")
    public void WhenITapOnTopConnections(int numberOfTopConnections)
            throws Exception {
        getPeoplePickerPage().tapNumberOfTopConnections(numberOfTopConnections);
    }

    /**
     * Tap on pointed amount of users from top people skipping pointed contact
     *
     * @param numberOfTopConnections number of top contacts to tap
     * @param contact                name of contact that shouldn't be tapped
     * @throws Exception
     */
    @Then("I tap on (.*) top connections but not (.*)")
    public void WhenITapOnTopConnectionsButNotUser(int numberOfTopConnections,
                                                   String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getPeoplePickerPage().tapNumberOfTopConnectionsButNotUser(
                numberOfTopConnections, contact);
    }

    /**
     * Tap on top connection contact avatar by pointed id order
     *
     * @param i contact order in top peoples
     * @throws Exception
     * @step. I tap on (\\d+)\\w+ top connection contact
     */
    @When("^I tap on (\\d+)\\w+ top connection contact$")
    public void IClickOnTopConnectionByOrder(int i) throws Exception {
        getPeoplePickerPage().tapOnTopConnectionAvatarByOrder(i);
    }

    @When("I see top people list on People picker page")
    public void ISeeTopPeopleListOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Top People label is not shown",
                getPeoplePickerPage().isTopPeopleLabelVisible());
    }

    @When("I see Create Conversation button on People picker page")
    public void ISeeCreateConversationButton() throws Exception {
        Assert.assertTrue("Create Conversation button is not visible.",
                getPeoplePickerPage().isCreateConversationButtonVisible());
    }

    /**
     * Click on Open button from Search to start conversation with single user
     *
     * @throws Exception
     * @step. ^I click open conversation button on People picker page$
     */
    @When("^I click open conversation button on People picker page$")
    public void IClickOpenConversationButtonOnPeoplePickerPage()
            throws Exception {
        getPeoplePickerPage().clickOpenConversationButton();
    }

    @When("I click Create Conversation button on People picker page")
    public void IClickCreateConversationButton() throws Throwable {
        if (getPeoplePickerPage().isCreateConversationButtonVisible()) {
            getPeoplePickerPage().clickCreateConversationButton();
        } else {
            WhenIClickOnGoButton();
        }
    }

    @When("I see user (.*) on People picker page is selected")
    public void ISeeUserIsSelectedOnPeoplePickerPage(String name)
            throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue(getPeoplePickerPage().isUserSelected(name));
    }

    @When("I see user (.*) on People picker page is NOT selected")
    public void ISeeUserIsNotSelectedOnPeoplePickerPage(String name)
            throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertFalse(getPeoplePickerPage().isUserSelected(name));
    }

    @When("I press backspace button")
    public void IPressBackspaceBtn() throws Exception {
        getPeoplePickerPage().hitDeleteButton();
    }

    /**
     * Click on conversation in search result with pointed name
     *
     * @param name conversation name
     * @throws Exception
     * @step. ^I tap on conversation (.*) in search result$
     */
    @When("^I tap on conversation (.*) in search result$")
    public void ITapOnConversationFromSearch(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getPeoplePickerPage().selectElementInSearchResults(name);
    }


    @When("^I click on Add to conversation button$")
    public void WhenIClickOnAddToConversationButton() throws Exception {
        if (getPeoplePickerPage().isKeyboardVisible()) {
            getPeoplePickerPage().clickOnGoButton();
        } else {
            getPeoplePickerPage().clickAddToConversationButton();
        }
    }

    @When("I click close button to dismiss people view")
    public void IClickCloseButtonDismissPeopleView() throws Exception {
        getPeoplePickerPage().tapOnPeoplePickerClearBtn();
    }

    /**
     * Unblocks a blocked user by clicking the unblock button
     *
     * @throws Exception
     * @step. I unblock user
     */
    @When("^I unblock user$")
    public void IUnblockUser() throws Exception {
        getPeoplePickerPage().unblockUser();
    }

    /**
     * Unblocks a blocked user by clicking the unblock button for iPad
     *
     * @throws Exception
     * @step. I unblock user on iPad
     */
    @When("^I unblock user on iPad$")
    public void IUnblockUserOniPad() throws Exception {
        getPeoplePickerPage().unblockUserOniPad();
    }

    /**
     * This step checks if the number of the selected contacts is correct.
     *
     * @param number expected number of contacts
     * @throws Exception
     * @step. ^I see that (\\d+) contacts are selected$
     */
    @Then("^I see that (\\d+) contacts are selected$")
    public void ISeeThatContactsAreSelected(int number) throws Exception {
        int numberOfSelectedTopPeople = getPeoplePickerPage()
                .getNumberOfSelectedTopPeople();
        Assert.assertEquals(
                "Expected selected contacts: " + number
                        + " but actual selected contacts: "
                        + numberOfSelectedTopPeople, number,
                numberOfSelectedTopPeople);
    }

    /**
     * Presses the Send An Invite button in the people picker. To invite people
     * via mail.
     *
     * @throws Exception
     * @step. ^I press the send an invite button$
     */
    @When("^I press the send an invite button$")
    public void IPressTheSendAnInviteButton() throws Exception {
        getPeoplePickerPage().tapSendInviteButton();
    }

    /**
     * Presses the Copy button on the Send Invitation pop up
     *
     * @throws Exception
     * @throws UnsupportedFlavorException
     * @step. ^I press the copy button$
     */
    @When("^I press the copy button$")
    public void IPressTheCopyButton() throws Exception {
        getPeoplePickerPage().tapSendInviteCopyButton();
    }

    /**
     * Presses the instant connect plus button
     *
     * @throws Exception
     * @step. ^I press the instant connect button$
     */
    @When("^I press the instant connect button$")
    public void IPressTheInstantConnectButton() throws Exception {
        getPeoplePickerPage().pressInstantConnectButton();
    }

    /**
     * Verify that Call action button is visible
     *
     * @throws Exception
     * @step. ^I see call action button on People picker page$
     */
    @When("^I see call action button on People picker page$")
    public void ISeeCallActionButtonOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Call action button is not visible",
                getPeoplePickerPage().isCallButtonVisible());
    }

    /**
     * Click on Call action button from Search to start call
     *
     * @throws Exception
     * @step. ^I click call action button on People picker page$
     */
    @When("^I click call action button on People picker page$")
    public void IClickCallActionButtonOnPeoplePickerPage() throws Exception {
        getPeoplePickerPage().clickCallButton();
    }

    /**
     * Verify that Send image action button is visible
     *
     * @throws Exception
     * @step. ^I see Send image action button on People picker page$
     */
    @When("^I see Send image action button on People picker page$")
    public void ISeeSendImageActionButtonOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Send image action button is not visible",
                getPeoplePickerPage().isSendImageButtonVisible());
    }

    /**
     * Click on Send image action button from Search to start call
     *
     * @throws Exception
     * @step. ^I click Send image action button on People picker page$
     */
    @When("^I click Send image action button on People picker page$")
    public void IClickSendImageActionButtonOnPeoplePickerPage()
            throws Exception {
        getPeoplePickerPage().clickSendImageButton();
    }

    /**
     * Verify if Open conversation button is visible
     *
     * @throws Exception
     * @step. ^I see open conversation action button on People picker page$
     */
    @When("^I see open conversation action button on People picker page$")
    public void ISeeOpenConversationActionButton() throws Exception {
        Assert.assertTrue("Open conversation button is not visible",
                getPeoplePickerPage().isOpenConversationButtonVisible());
    }

    /**
     * Verify if Open, Call and Send image action buttons are visible
     *
     * @throws Exception
     * @step. ^I see action buttons appeared on People picker page
     */
    @When("^I see action buttons appeared on People picker page$")
    public void ISeeActionButttonsAppearedOnPeoplePickerPage() throws Exception {
        ISeeOpenConversationActionButton();
        ISeeCallActionButtonOnPeoplePickerPage();
        ISeeSendImageActionButtonOnPeoplePickerPage();
    }

    /**
     * Verify that Open, Call and Send image action buttons are NOT visible
     *
     * @throws Exception
     * @step. ^I see action buttons disappeared on People picker page
     */
    @When("^I see action buttons disappeared on People picker page$")
    public void ISeeActionButttonsDisappearedOnPeoplePickerPage()
            throws Exception {
        Assert.assertFalse("Open conversation button is still visible",
                getPeoplePickerPage().isOpenConversationButtonVisible());
        Assert.assertFalse("Call action button is still visible",
                getPeoplePickerPage().isCallButtonVisible());
        Assert.assertFalse("Send image action button is still visible",
                getPeoplePickerPage().isSendImageButtonVisible());
    }

    /**
     * Opens conversation from the action button in people picker
     *
     * @throws Throwable
     * @step. ^I click open conversation action button on People picker page$
     */
    @When("^I click open conversation action button on People picker page$")
    public void IClickOpenConversationActionButtonOnPeoplePickerPage()
            throws Throwable {
        getPeoplePickerPage().clickOpenConversationButton();
    }
}
