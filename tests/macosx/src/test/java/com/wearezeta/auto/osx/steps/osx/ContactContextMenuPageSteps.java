package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();
	@SuppressWarnings("unused")
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Click the block in context menu
	 *
	 * @step. ^I click block in context menu$
	 * @throws Exception
	 */
	@When("^I click block in context menu$")
	public void IClickBlockButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickBlock();
	}
}
