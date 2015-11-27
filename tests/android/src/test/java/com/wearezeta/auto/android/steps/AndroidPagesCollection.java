package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.AbstractPagesCollection;

final class AndroidPagesCollection extends AbstractPagesCollection<AndroidPage> {

	private static AndroidPagesCollection instance = null;

	public synchronized static AndroidPagesCollection getInstance() {
		if (instance == null) {
			instance = new AndroidPagesCollection();
		}
		return instance;
	}

	private AndroidPagesCollection() {
		super();
	}
}
