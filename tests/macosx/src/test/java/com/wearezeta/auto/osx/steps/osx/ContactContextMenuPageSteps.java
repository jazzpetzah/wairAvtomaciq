package com.wearezeta.auto.osx.steps.osx;

import org.apache.log4j.Logger;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

	private static final Logger LOG = ZetaLogger
			.getLog(ContactContextMenuPageSteps.class.getName());

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

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
