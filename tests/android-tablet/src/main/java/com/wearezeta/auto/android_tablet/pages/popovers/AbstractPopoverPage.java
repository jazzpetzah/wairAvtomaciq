package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractPopoverPage extends AndroidTabletPage {
	private AbstractPopoverContainer container;

	protected AbstractPopoverContainer getContainer() {
		return this.container;
	}

	public AbstractPopoverPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver);
		this.container = container;
	}

}
