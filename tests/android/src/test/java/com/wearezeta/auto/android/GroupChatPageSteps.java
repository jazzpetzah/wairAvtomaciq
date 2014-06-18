package com.wearezeta.auto.android;


import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class GroupChatPageSteps {

	@When("^I see group chat page with users (.*) (.*)$")
	public void WhenISeeGroupChatPage(String name1, String name2) throws Throwable {
		PagesCollection.groupChatPage.isGroupChatDialogVisible();
		if(name1.contains(CommonUtils.CONTACT_1) && name2.contains(CommonUtils.CONTACT_2)){
			PagesCollection.groupChatPage.isGroupChatDialogContainsNames(CommonUtils.contacts.get(0).getName()
					, CommonUtils.contacts.get(1).getName());
		}
		else{
		    PagesCollection.groupChatPage.isGroupChatDialogContainsNames(name1, name2);
		}
	}
}
