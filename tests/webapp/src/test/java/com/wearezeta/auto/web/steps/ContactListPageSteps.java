package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("^I see my name (.*) in Contact list$")
	public void ISeeMyNameInContactList(String name) throws Exception {
		GivenISeeContactListWithName(name);
	}

	@Given("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactWithNameExists(name));
	}

	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.openConversation(contact, false);
		PagesCollection.conversationPage = new ConversationPage(
				CommonUtils
						.getWebAppAppiumUrlFromConfig(ContactListPageSteps.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(ContactListPageSteps.class));
	}
}
