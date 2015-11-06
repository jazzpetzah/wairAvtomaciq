package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class WinPagesCollection extends AbstractPagesCollection<WinPage> {

	private static WinPagesCollection instance = null;

	public synchronized static WinPagesCollection getInstance() {
		if (instance == null) {
			instance = new WinPagesCollection();
		}
		return instance;
	}

}
