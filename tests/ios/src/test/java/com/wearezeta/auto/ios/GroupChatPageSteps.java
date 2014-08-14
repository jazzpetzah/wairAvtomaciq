package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.GroupChatInfoPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	
	@Then("^I see group chat page with users (.*) (.*)$")
	public void ThenISeeGroupChatPage(String name1, String name2) throws Throwable {
		
		Thread.sleep(2000);
		name1 = CommonUtils.retrieveRealUserContactPasswordValue(name1);
		name2 = CommonUtils.retrieveRealUserContactPasswordValue(name2);
	    PagesCollection.groupChatPage.areRequiredContactsAddedToChat(name1, name2);
	}
	
	@When("^I swipe up on group chat page$")
	public void ISwipeUpOnGroupChatPage() throws IOException, InterruptedException{
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage)PagesCollection.groupChatPage.swipeUp(500);
	}
	
	@When("^I see the new conversation name displayed in in conversation$")
	public void IVerifyConversationNameInChat() throws IOException{
		Assert.assertTrue(PagesCollection.groupChatPage.isConversationChangedInChat());
	}
	
	@Then("^I can see (.*) Have Left$")
	public void ICanSeeYouHaveLeft(String name) throws IOException{
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue(PagesCollection.groupChatPage.isYouHaveLeftVisible(name));
	}

	@Then("^I see that (.*) is not present on group chat page$")
	public void ISeeContactIsNotPresentOnGroupChatPage(String contact) throws InterruptedException {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue(PagesCollection.groupChatPage.waitForContactToDisappear(contact));
	}
	
	@When("^I return to the chat list$")
	public void IReturnToChatList() throws IOException{
		PagesCollection.contactListPage = (ContactListPage)PagesCollection.groupChatPage.swipeRight(1000);
		System.out.println("swiped right");
	}
	
	@When("^I can see (.*) Added (.*)$")
	public void ICanSeeUserAddedContact(String user, String contact) throws Throwable {
		
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue(PagesCollection.groupChatPage.isUserAddedContactVisible(user,contact));
	}
	
}
