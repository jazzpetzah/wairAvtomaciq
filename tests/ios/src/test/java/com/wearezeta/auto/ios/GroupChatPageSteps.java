package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.GroupChatInfoPage;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
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
		
		PagesCollection.groupChatPage.isGroupChatPageVisible();
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage)PagesCollection.groupChatPage.swipeUp(1000);
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
}
