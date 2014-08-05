package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.GroupChatPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {

	public static final String GROUP_CHAT_NAME = "TempTestChat";

	@When("^I tap on group chat contact (.*)$")
	public void WhenITapOnGroupChatContact(String contact) throws Throwable {
		PagesCollection.otherUserPersonalInfoPage = PagesCollection.groupChatInfoPage.tapOnContact(contact);
	}
	
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

	@Then("^I see that the conversation name is (.*)$")
	public void IVerifyCorrectConversationName(String name) throws IOException{
		Assert.assertEquals(PagesCollection.groupChatInfoPage.getConversationName(), name);
	}
	
	@Then("^I see the correct participant (.*) and (.*) avatars$")
	public void ISeeCorrectParticipantAvatars(String contact1, String contact2) throws IOException{
		Assert.assertTrue(PagesCollection.groupChatInfoPage.isParticipantAvatars(CommonUtils.retrieveRealUserContactPasswordValue(contact1),
				CommonUtils.retrieveRealUserContactPasswordValue(contact2)));
	}
	
	@Then("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants) throws IOException{
		Assert.assertEquals(PagesCollection.groupChatInfoPage.getSubHeader(), realNumberOfParticipants + " people");
		}
	
	@Then("^I do not see (.*) on group chat info page$")
	public void ThenIDoNotSeeOnGroupChatInfoPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    Assert.assertFalse(PagesCollection.groupChatInfoPage.isContactExists(contact));
	}
	
	@Then("^I return to group chat page$")
	public void ThenIReturnToGroupChatPage() throws Throwable {
		PagesCollection.groupChatPage =	(GroupChatPage) PagesCollection.groupChatInfoPage.tabBackButton();
	}
}
