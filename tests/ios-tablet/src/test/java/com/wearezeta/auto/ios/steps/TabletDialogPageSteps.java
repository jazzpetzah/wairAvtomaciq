package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletConversationViewPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

	private TabletConversationViewPage getTabletDialogPage() throws Exception {
		return pagesCollecton.getPage(TabletConversationViewPage.class);
	}
	
	/**
	 * Presses the add picture button on iPad to open a CameraRollPopoverPage
	 * 
	 * @step. ^I tap Add Picture button on iPad$
	 * @throws Exception
	 */
	@When("^I tap Add Picture button on iPad$")
	public void ITapAddPictureButton() throws Exception {
		getTabletDialogPage().tapAddPictureiPadButton();
	}

	/**
	 * Presses the conversation detail button on iPad to open a
	 * ConversationDetailPopoverPage
	 * 
	 * @step. ^I open conversation details on iPad$
	 * @throws Exception
	 */
	@When("^I open conversation details on iPad$")
	public void IOpenConversationDetailsOniPad() throws Exception {
		getTabletDialogPage().pressConversationDetailiPadButton();
	}
	
	/**
	 * Presses the conversation detail button on iPad to open a GroupConversationDetailPopoverPage
	 * @step. ^I open group conversation details on iPad$
	 * @throws Exception
	 */
	@When("^I open group conversation details on iPad$")
	public void IOpenGroupConversationDetailsOniPad() throws Exception {
		getTabletDialogPage().pressGroupConversationDetailiPadButton();
	}
}
