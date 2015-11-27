package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class OSXPagesCollection extends AbstractPagesCollection<OSXPage> {

	private static OSXPagesCollection instance = null;

	public synchronized static OSXPagesCollection getInstance() {
		if (instance == null) {
			instance = new OSXPagesCollection();
		}
		return instance;
	}

}
