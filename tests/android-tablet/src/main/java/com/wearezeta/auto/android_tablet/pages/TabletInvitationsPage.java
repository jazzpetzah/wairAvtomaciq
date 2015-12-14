package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.InvitationsPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletInvitationsPage extends AndroidTabletPage {

	public TabletInvitationsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private InvitationsPage getInvitationsPage() throws Exception {
		return this.getAndroidPageInstance(InvitationsPage.class);
	}

	public boolean waitUntilUserNameIsVisible(String name) throws Exception {
		return getInvitationsPage().waitUntilUserNameIsVisible(name);
	}

	public boolean waitUntilUserNameIsInvisible(String name) throws Exception {
        return getInvitationsPage().waitUntilUserNameIsInvisible(name);
	}
}
