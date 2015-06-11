package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollTabletPopoverPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.TabletConversationDetailPopoverPage;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;
import com.wearezeta.auto.ios.pages.TabletDialogPage;

import cucumber.api.java.en.When;

public class TabletDialogPageSteps {
	
	public String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.";
	/**
	 * Presses the add picture button on iPad to open a CameraRollPopoverPage
	 * @step. ^I press Add Picture button on iPad$
	 * @throws Throwable
	 */
	@When("^I press Add Picture button on iPad$")
	public void IPressAddPictureButton() throws Throwable {
		TabletPagesCollection.tabletDialogPage = (TabletDialogPage) PagesCollection.loginPage
				.instantiatePage(TabletDialogPage.class);
		CameraRollTabletPopoverPage page = TabletPagesCollection.tabletDialogPage.pressAddPictureiPadButton();
		TabletPagesCollection.cameraRolliPadPopoverPage = (CameraRollTabletPopoverPage) page;
	}
	
	/**
	 * Presses the conversation detail button on iPad to open a ConversationDetailPopoverPage
	 * @step. ^I open conversation details on iPad$
	 * @throws Throwable
	 */
	@When("^I open conversation details on iPad$")
	public void IOpenConversationDetailsOniPad() throws Throwable {
		TabletPagesCollection.tabletDialogPage = (TabletDialogPage) PagesCollection.loginPage
				.instantiatePage(TabletDialogPage.class);
		TabletConversationDetailPopoverPage page = TabletPagesCollection.tabletDialogPage.pressConversationDetailiPadButton();
		TabletPagesCollection.tabletConversationDetailPopoverPage = (TabletConversationDetailPopoverPage) page;
	}
	
	
	
	@When("I type and send long message for tablet and media link (.*)")
	public void ITypeAndSendLongTextAndMediaLink(String link) throws Exception {
		PagesCollection.dialogPage.sendMessageUsingScript(longMessage);
		Thread.sleep(1000);
		PagesCollection.dialogPage.sendMessageUsingScript(link);
	}
	
	@When("I click play video button")
    public void IClickPlayButton() throws Exception{
        PagesCollection.dialogPage.clickOnPlayVideoButton();
    }
}
