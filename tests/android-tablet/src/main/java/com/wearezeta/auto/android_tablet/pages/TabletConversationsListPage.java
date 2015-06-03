package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletConversationsListPage extends AndroidTabletPage {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(TabletConversationsListPage.class.getSimpleName());

	public TabletConversationsListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private ContactListPage getContactListPage() {
		return (ContactListPage) this
				.getAndroidPageInstance(ContactListPage.class);
	}

	@Override
	public AndroidTabletPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		switch (direction) {
		case DOWN: {
			return new TabletPeoplePickerPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public void verifyConversationsListIsLoaded() throws Exception {
		getContactListPage().waitForConversationListLoad();
	}

	public TabletSelfProfilePage tapMyAvatar() throws Exception {
		getContactListPage().tapOnMyAvatar();
		return new TabletSelfProfilePage(this.getLazyDriver());
	}

}
