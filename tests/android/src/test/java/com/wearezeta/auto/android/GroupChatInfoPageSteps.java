package com.wearezeta.auto.android;

import com.wearezeta.auto.android.pages.GroupChatPage;
import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {

	@When("^I swipe up group chat info page$")
	public void WhenISwipeUpGroupChatInfoPage() throws Throwable {	
		PagesCollection.groupChatInfoPage.swipeUp(500);
	}

	@When("^I swipe right on group chat info page$")
	public void WhenISwipeRightOnGroupChatInfoPage() throws Throwable {
		PagesCollection.groupChatPage =	(GroupChatPage) PagesCollection.groupChatInfoPage.swipeRight(500);
	}

	@When("^I press Leave conversation button$")
	public void WhenIPressLeaveConversationButton() throws Throwable {
		PagesCollection.groupChatInfoPage.pressLeaveConversationButton();
	}
	
	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Throwable {
		PagesCollection.groupChatInfoPage.pressConfirmButton();
	}
}
