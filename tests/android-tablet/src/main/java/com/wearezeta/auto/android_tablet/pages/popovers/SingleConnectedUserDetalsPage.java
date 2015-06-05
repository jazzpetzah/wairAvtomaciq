package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SingleConnectedUserDetalsPage extends AbstractConversationDetailsPage {

	public SingleConnectedUserDetalsPage(Future<ZetaAndroidDriver> lazyDriver,
			SingleUserPopover container) throws Exception {
		super(lazyDriver, container);
	}

}
