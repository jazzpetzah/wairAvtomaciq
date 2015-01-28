package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ConversationPageSteps.class.getSimpleName());

	private String randomMessage;

	@When("I write random message")
	public void WhenIWriteRandomMessage() {
		randomMessage = CommonUtils.generateGUID();
		IWriteMessage(randomMessage);
	}

	@When("I write message (.*)")
	public void IWriteMessage(String message) {
		PagesCollection.conversationPage.writeNewMessage(message);
	}

	@When("I send message")
	public void WhenISendMessage() {
		PagesCollection.conversationPage.sendNewMessage();
	}

	@Then("I see random message in conversation$")
	public void ThenISeeRandomMessageInConversation() {
		Assert.assertTrue(PagesCollection.conversationPage
				.isMessageSent(randomMessage));
	}
}
