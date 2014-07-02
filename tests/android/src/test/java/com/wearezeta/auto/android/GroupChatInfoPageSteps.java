package com.wearezeta.auto.android;

import com.wearezeta.auto.android.pages.GroupChatPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {

	public static final String GROUP_CHAT_NAME = "TempTestChat";

	@When("^I swipe up group chat info page$")
	public void WhenISwipeUpGroupChatInfoPage() throws Throwable {	
		PagesCollection.groupChatInfoPage.swipeUp(500);
	}

	@When("^I swipe right on group chat info page$")
	public void WhenISwipeRightOnGroupChatInfoPage() throws Throwable {
		PagesCollection.groupChatPage =	(GroupChatPage) PagesCollection.groupChatInfoPage.swipeRight(500);
	}

	@When("^I press back on group chat info page$")
	public void WhenIPressBackOnGroupChatInfoPage() throws Throwable {
		PagesCollection.groupChatPage =	(GroupChatPage) PagesCollection.groupChatInfoPage.tabBackButton();
	}
	
	@When("^I press Leave conversation button$")
	public void WhenIPressLeaveConversationButton() throws Throwable {
		PagesCollection.groupChatInfoPage.pressLeaveConversationButton();
	}

	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Throwable {
		PagesCollection.groupChatInfoPage.pressConfirmButton();
	}

	@When("^I select contact (.*)$")
	public void WhenISelectContact(String name) throws Throwable {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage = PagesCollection.groupChatInfoPage.selectContactByName(name);
	}

}
