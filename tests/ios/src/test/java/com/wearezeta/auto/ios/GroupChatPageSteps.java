package com.wearezeta.auto.ios;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	
	@Then("^I see group chat page with users (.*) (.*)$")
	public void ThenISeeGroupChatPage(String name1, String name2) throws Throwable {
		
		name1 = CommonUtils.retrieveRealUserContactPasswordValue(name1);
		name2 = CommonUtils.retrieveRealUserContactPasswordValue(name2);
	    PagesCollection.groupChatPage.areRequiredContactsAddedToChat(name1, name2);
	}

}
