package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;

import cucumber.api.java.en.When;

public class ConversationInfoPageSteps {
	@When("I choose user (.*) in Conversation info")
	public void WhenIChooseUserInConversationInfo(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getAppPathFromConfig(ConversationInfoPage.class)
				 ));
		CommonSteps.senderPages.getConversationInfoPage().selectUser(user);
		CommonSteps.senderPages.getConversationInfoPage().selectUserIfNotSelected(user);
	}
	
	@When("I remove selected user from conversation")
	public void WhenIRemoveSelectedUserFromConversation() {
		CommonSteps.senderPages.getConversationInfoPage().removeUser();
	}
	
	@When("I leave conversation")
	public void WhenILeaveConversation() {
		CommonSteps.senderPages.getConversationInfoPage().leaveConversation();
	}
}
