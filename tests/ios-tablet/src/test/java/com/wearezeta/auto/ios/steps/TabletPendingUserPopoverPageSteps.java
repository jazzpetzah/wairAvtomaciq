package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;
import com.wearezeta.auto.ios.pages.TabletPendingUserPopoverPage;

import cucumber.api.java.en.When;

public class TabletPendingUserPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verifying pending profile popover on iPad
	 * 
	 * @step ^I see (.*) user pending profile popover on iPad$
	 * 
	 * @param user
	 * 			usern from Examples
	 * 
	 * @throws NoSuchUserException
	 * @throws Exception
	 */
	@When("^I see (.*) user pending profile popover on iPad$")
	public void IseeUserPendingPopoverOnIpad(String user)
			throws NoSuchUserException, Exception {
		if (TabletPagesCollection.tabletPendingUserPopoverPage == null) {
			TabletPagesCollection.tabletPendingUserPopoverPage = (TabletPendingUserPopoverPage) PagesCollection.loginPage
					.instantiatePage(TabletPendingUserPopoverPage.class);
		}
		Assert.assertTrue("User name is not displayed",
				TabletPagesCollection.tabletPendingUserPopoverPage
						.isUserNameDisplayed(usrMgr.findUserByNameOrNameAlias(
								user).getName()));
		Assert.assertTrue("Pending label is not displayed",
				TabletPagesCollection.tabletPendingUserPopoverPage
						.isPendingLabelVisible());
	}

}
