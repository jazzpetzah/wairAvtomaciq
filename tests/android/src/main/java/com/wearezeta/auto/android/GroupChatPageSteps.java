package com.wearezeta.auto.android;


import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class GroupChatPageSteps {

	@When("^I see group chat page with users (.*) (.*)$")
	public void WhenISeeGroupChatPage(String name1, String name2) throws Throwable {
		PagesCollection.groupChatPage.isGroupChatDialogVisible();
		if(name1.contains("aqaUser") && name2.contains("aqaUser")){
			PagesCollection.groupChatPage.isGroupChatDialogContainsNames(CommonUtils.getContactName(CommonUtils.contacts.firstKey())
					, CommonUtils.getContactName(CommonUtils.contacts.lastKey()));
		}
		else{
		    PagesCollection.groupChatPage.isGroupChatDialogContainsNames(name1, name2);
		}
	}
}
