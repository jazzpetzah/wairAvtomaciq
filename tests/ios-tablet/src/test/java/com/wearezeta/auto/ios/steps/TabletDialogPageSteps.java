package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollTabletPopoverPage;
import com.wearezeta.auto.ios.pages.TabletConversationDetailPopoverPage;
import com.wearezeta.auto.ios.pages.TabletGroupConversationDetailPopoverPage;
import com.wearezeta.auto.ios.pages.TabletDialogPage;
import com.wearezeta.auto.ios.pages.TabletLoginPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletDialogPage getTabletDialogPage() throws Exception {
		return (TabletDialogPage) pagesCollecton
				.getPage(TabletDialogPage.class);
	}
	
	private TabletLoginPage getTabletLoginPage() throws Exception {
		return (TabletLoginPage) pagesCollecton.getPage(TabletLoginPage.class);
	}
	
	private TabletGroupConversationDetailPopoverPage getTabletGroupConversationDetailPopoverPage()
			throws Exception {
		return (TabletGroupConversationDetailPopoverPage) pagesCollecton
				.getPage(TabletGroupConversationDetailPopoverPage.class);
	}

	/**
	 * Presses the add picture button on iPad to open a CameraRollPopoverPage
	 * 
	 * @step. ^I press Add Picture button on iPad$
	 * @throws Throwable
	 */
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		getTabletDialogPage().pressAddPictureiPadButton();
	}

	/**
	 * Presses the conversation detail button on iPad to open a
	 * ConversationDetailPopoverPage
	 * 
	 * @step. ^I open conversation details on iPad$
	 * @throws Throwable
	 */
	@When("^I open conversation details on iPad$")
	public void IOpenConversationDetailsOniPad() throws Throwable {
		getTabletDialogPage().pressConversationDetailiPadButton();
	}
	
	/**
	 * Presses the conversation detail button on iPad to open a GroupConversationDetailPopoverPage
	 * @step. ^I open group conversation details on iPad$
	 * @throws Throwable
	 */
	@When("^I open group conversation details on iPad$")
	public void IOpenGroupConversationDetailsOniPad() throws Throwable {
//		TabletPagesCollection.tabletDialogPage = (TabletDialogPage) PagesCollection.loginPage
//				.instantiatePage(TabletDialogPage.class);
		getTabletLoginPage().instantiatePage(TabletDialogPage.class);
		TabletGroupConversationDetailPopoverPage page = getTabletDialogPage().pressGroupConversationDetailiPadButton();
//		TabletPagesCollection.tabletGroupConversationDetailPopoverPage = (TabletGroupConversationDetailPopoverPage) page;
		getTabletGroupConversationDetailPopoverPage();
	}
	
	

}
