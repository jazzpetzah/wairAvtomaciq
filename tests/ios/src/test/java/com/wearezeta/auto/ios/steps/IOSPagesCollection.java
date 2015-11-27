package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.ios.pages.IOSPage;

public class IOSPagesCollection extends AbstractPagesCollection<IOSPage> {

	private static IOSPagesCollection instance = null;

	public synchronized static IOSPagesCollection getInstance() {
		if (instance == null) {
			instance = new IOSPagesCollection();
		}
		return instance;
	}

	private IOSPagesCollection() {
		super();
	}
}
