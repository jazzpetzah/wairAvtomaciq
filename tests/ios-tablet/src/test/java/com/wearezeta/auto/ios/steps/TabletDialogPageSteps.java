package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletConversationViewPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private TabletConversationViewPage getTabletDialogPage() throws Exception {
		return pagesCollection.getPage(TabletConversationViewPage.class);
	}
	
	/**
	 * Presses the conversation detail button on iPad to open a
	 * ConversationDetailPopoverPage
	 * 
	 * @step. ^I open (group )?conversation details on iPad$
	 * @throws Exception
	 */
	@When("^I open (group )?conversation details on iPad$")
	public void IOpenConversationDetailsOniPad(String isGroup) throws Exception {
		getTabletDialogPage().tapConversationDetailsIPadButton();
	}
}
