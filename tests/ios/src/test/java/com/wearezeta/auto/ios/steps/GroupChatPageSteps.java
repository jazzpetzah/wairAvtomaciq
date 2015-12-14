package com.wearezeta.auto.ios.steps;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private GroupChatPage getGroupChatPage() throws Exception {
		return (GroupChatPage) pagesCollecton.getPage(GroupChatPage.class);
	}

	@Then("^I see group chat page with users (.*)$")
	public void ThenISeeGroupChatPage(String participantNameAliases)
			throws Exception {
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			String name = usrMgr.findUserByNameOrNameAlias(nameAlias).getName();
			if (name.indexOf(" ") != -1) {
				name = name.substring(0, name.indexOf(" "));
			}
			participantNames.add(name);
		}
		Assert.assertTrue(getGroupChatPage().areRequiredContactsAddedToChat(
				participantNames));
	}

	/**
	 * Verifies that group chat is empty and has only system message
	 * 
	 * @param participantNameAliases
	 *            user names comma separated
	 * @throws Exception
	 */
	@Then("^I see empty group chat page with users (.*) with only system message$")
	public void ISeeGroupChatPageWithUsersAndOnlySystemMessage(
			String participantNameAliases) throws Exception {
		DialogPageSteps dialog = new DialogPageSteps();
		dialog.ISeeOnlyXAmountOfMessages(1);
		ThenISeeGroupChatPage(participantNameAliases);
	}

	@Then("^I see group chat page with 3 users (.*) (.*) (.*)$")
	public void ThenISeeGroupChatPage3Users(String name1, String name2,
			String name3) throws Throwable {
		Assert.assertTrue("Conversation page is not shown", getGroupChatPage()
				.isGroupChatPageVisible());
		name1 = usrMgr.findUserByNameOrNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameOrNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameOrNameAlias(name3).getName();
		Thread.sleep(1000);// still have to wait some time for animation to
							// finish
		Assert.assertTrue(getGroupChatPage().areRequired3ContactsAddedToChat(
				name1, name2, name3));
	}

	/**
	 * Click open conversation details button in group chat
	 * 
	 * @step. ^I open group conversation details$
	 * 
	 * @throws Exception
	 *             if group chat info page was not created
	 */
	@When("^I open group conversation details$")
	public void IOpenConversationDetails() throws Exception {
		getGroupChatPage().openConversationDetailsClick();
	}

	@When("^I swipe up on group chat page$")
	public void ISwipeUpOnGroupChatPage() throws Exception {
		getGroupChatPage().swipeUp(1000);
	}

	@When("^I see the new conversation name displayed in in conversation$")
	public void IVerifyConversationNameInChat() throws Exception {
		Assert.assertTrue(getGroupChatPage().isConversationChangedInChat());
	}

	@When("I see you renamed conversation to (.*) message shown in Group Chat")
	public void ISeeYouRenamedMessageInGroupChat(String name) throws Exception {
		Assert.assertTrue(getGroupChatPage()
				.isYouRenamedConversationMessageVisible(name));
	}

	@Then("^I can see (.*) Have Left$")
	public void ICanSeeYouHaveLeft(String name) throws Exception {
		Assert.assertTrue(getGroupChatPage().isYouHaveLeftVisible());
	}

	@Then("I see You Left message in group chat")
	public void ISeeYouLeftMessage() throws Exception {
		Assert.assertTrue(getGroupChatPage().isYouLeftMessageShown());
	}

	@When("^I return to the chat list$")
	public void IReturnToChatList() throws Exception {
		getGroupChatPage().returnToContactList();
	}

	@When("^I can see You Added (.*) message$")
	public void ICanSeeYouAddedContact(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(getGroupChatPage()
				.isYouAddedUserMessageShown(contact));
	}

	@When("I swipe down on group chat page")
	public void ISwipeDownOnGroupChat() throws Throwable {
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			getGroupChatPage().swipeDown(500);
		} else {
			getGroupChatPage().swipeDownSimulator();
		}
	}

	@When("I see message in group chat (.*)")
	public void ISeeMessageInGroupChat(String message) throws Exception {
		Assert.assertTrue(getGroupChatPage().isMessageShownInGroupChat(message));
	}

	@When("I swipe up on group chat page in simulator")
	public void ISwipeUpInGroupChatWithSimulator() throws Throwable {
		getGroupChatPage().swipeUpSimulator();
	}

	@When("I swipe right on group chat page")
	public void ISwipeRightOnGroupChatPage() throws Throwable {
		getGroupChatPage().swipeRight(1000);
	}

}
