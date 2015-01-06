package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.GroupChatInfoPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Then("^I see group chat page with users (.*)$")
	public void ThenISeeGroupChatPage(String participantNameAliases)
			throws Throwable {
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			participantNames.add(usrMgr.findUserByNameAlias(nameAlias)
					.getName());
		}
		Assert.assertTrue(PagesCollection.groupChatPage
				.areRequiredContactsAddedToChat(participantNames));
	}

	@Then("^I see group chat page with 3 users (.*) (.*) (.*)$")
	public void ThenISeeGroupChatPage3Users(String name1, String name2,
			String name3) throws Throwable {
		Assert.assertTrue("Conversation page is not shown",
				PagesCollection.groupChatPage.isGroupChatPageVisible());
		name1 = usrMgr.findUserByNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameAlias(name3).getName();
		Thread.sleep(1000);// still have to wait some time for animation to
							// finish
		Assert.assertTrue(PagesCollection.groupChatPage
				.areRequired3ContactsAddedToChat(name1, name2, name3));
	}

	@When("^I swipe up on group chat page$")
	public void ISwipeUpOnGroupChatPage() throws Exception {
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage) PagesCollection.groupChatPage
				.swipeUp(1000);
	}

	@When("^I see the new conversation name displayed in in conversation$")
	public void IVerifyConversationNameInChat() throws IOException {
		Assert.assertTrue(PagesCollection.groupChatPage
				.isConversationChangedInChat());
	}

	@When("I see you renamed conversation to (.*) message shown in Group Chat")
	public void ISeeYouRenamedMessageInGroupChat(String name) {
		Assert.assertTrue(PagesCollection.groupChatPage
				.isYouRenamedConversationMessageVisible(name));
	}

	@Then("^I can see (.*) Have Left$")
	public void ICanSeeYouHaveLeft(String name) throws IOException {
		Assert.assertTrue(PagesCollection.groupChatPage.isYouHaveLeftVisible());
	}

	@Then("I see You Left message in group chat")
	public void ISeeYouLeftMessage() {
		Assert.assertTrue(PagesCollection.groupChatPage.isYouLeftMessageShown());
	}

	@Then("^I see that (.*) is not present on group chat page$")
	public void ISeeContactIsNotPresentOnGroupChatPage(String contact)
			throws InterruptedException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.groupChatPage
				.waitForContactToDisappear(contact));
	}

	@When("^I return to the chat list$")
	public void IReturnToChatList() throws IOException {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.groupChatPage
				.swipeRight(500);
	}

	@When("^I can see You Added (.*) message$")
	public void ICanSeeYouAddedContact(String contact) throws Throwable {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.groupChatPage
				.isYouAddedUserMessageShown(contact));
	}

	@When("I swipe down on group chat page")
	public void ISwipeDownOnGroupChat() throws Throwable {
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.groupChatPage
					.swipeDown(500);
		} else {
			PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.groupChatPage
					.swipeDownSimulator();
		}
	}

	@When("I see message in group chat (.*)")
	public void ISeeMessageInGroupChat(String message) {
		PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.iOSPage;
		Assert.assertTrue(PagesCollection.groupChatPage
				.isMessageShownInGroupChat(message));
	}

	@When("I swipe up on group chat page in simulator")
	public void ISwipeUpInGroupChatWithSimulator() throws Throwable {
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage) PagesCollection.groupChatPage
				.swipeUpSimulator();
	}

	@When("I swipe right on group chat page")
	public void ISwipeRightOnGroupChatPage() throws Throwable {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.groupChatPage
				.swipeRight(500);
	}

}
