package com.wearezeta.auto.ios;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class GroupChatPageSteps {
	
	@When("^I see group chat page with users (.*) (.*)$")
	public void WhenISeeGroupChatPage(String name1, String name2) throws Throwable {
	    PagesCollection.groupChatPage.isGroupChatDialogVisible(name1, name2);
	}

}
