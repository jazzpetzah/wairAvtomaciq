package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.AbstractPagesCollection;

final class AndroidTabletPagesCollection extends
		AbstractPagesCollection<AndroidTabletPage> {

	private static AndroidTabletPagesCollection instance = null;

	public synchronized static AndroidTabletPagesCollection getInstance() {
		if (instance == null) {
			instance = new AndroidTabletPagesCollection();
		}
		return instance;
	}

	private AndroidTabletPagesCollection() {
		super();
	}
}
