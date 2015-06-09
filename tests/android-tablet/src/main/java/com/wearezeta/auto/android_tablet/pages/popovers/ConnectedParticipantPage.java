package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class ConnectedParticipantPage extends AbstractParticipantPage {
	public ConnectedParticipantPage(Future<ZetaAndroidDriver> lazyDriver,
			GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}
}
