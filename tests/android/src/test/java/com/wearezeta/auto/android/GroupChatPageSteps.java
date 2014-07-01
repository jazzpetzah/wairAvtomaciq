package com.wearezeta.auto.android;


import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.GroupChatInfoPage;
import com.wearezeta.auto.android.pages.GroupChatPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	
	public static final String userRemovedMessage = "YOU REMOVED ";
	
	@When("^I swipe left on group dialog page$")
	public void WhenISwipeLeftOnGroupDialogPage() throws Throwable {
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage) PagesCollection.groupChatPage.swipeLeft(500);
	}
	
	@When("^I swipe right on group dialog page$")
	public void WhenISwipeRightOnGroupDialogPage() throws Throwable {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.groupChatPage.swipeRight(500);
	}
	
	@Then("^I see group chat page with users (.*) (.*)$")
	public void ThenISeeGroupChatPage(String name1, String name2) throws Throwable {
		PagesCollection.groupChatPage.isGroupChatDialogVisible();
		if(name1.contains(CommonUtils.CONTACT_1) && name2.contains(CommonUtils.CONTACT_2)){
			PagesCollection.groupChatPage.isGroupChatDialogContainsNames(CommonUtils.contacts.get(0).getName()
					, CommonUtils.contacts.get(1).getName());
		}
		else{
		    PagesCollection.groupChatPage.isGroupChatDialogContainsNames(name1, name2);
		}
	}
	
	@Then("^I see message that I left chat$")
	public void ThenISeeMessageThatILeftChat() throws Throwable {
		Assert.assertTrue(PagesCollection.groupChatPage.isMessageExists(GroupChatPage.I_LEFT_CHAT_MESSAGE));
	}
	
	@Then("^I see that (.*) is not present on group chat page$")
	public void I_see_that_aqaContact_is_not_present_on_group_chat_page(String user) throws Throwable {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		String message = userRemovedMessage + user.toUpperCase();
		Assert.assertTrue(PagesCollection.groupChatPage.isMessageExists(message));
	}
}
