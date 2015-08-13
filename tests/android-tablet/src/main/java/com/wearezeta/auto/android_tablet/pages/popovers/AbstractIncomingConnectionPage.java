package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractIncomingConnectionPage extends AbstractConnectionPage {

	public AbstractIncomingConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

}
