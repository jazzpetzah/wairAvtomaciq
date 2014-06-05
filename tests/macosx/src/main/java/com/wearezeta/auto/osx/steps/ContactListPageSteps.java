package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ConversationPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class ContactListPageSteps {
	@Given ("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws IOException {
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isContactWithNameExists(name));
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String value) {
		Assert.assertTrue("Login finished", CommonSteps.senderPages.getLoginPage().waitForLogin());
		Assert.assertTrue(CommonSteps.senderPages.getLoginPage().isLoginFinished(value));
	}
	
	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String value) throws MalformedURLException, IOException {
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().openConversation(value));
		CommonSteps.senderPages.setConversationPage(new ConversationPage(
				CommonUtils.getUrlFromConfig(ConversationPage.class),
				CommonUtils.getAppPathFromConfig(ConversationPage.class)));
	 }
}
