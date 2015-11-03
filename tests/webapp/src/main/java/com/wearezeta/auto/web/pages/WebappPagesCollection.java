package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class WebappPagesCollection extends AbstractPagesCollection<WebPage> {

	private static WebappPagesCollection instance = null;

	public synchronized static WebappPagesCollection getInstance() {
		if (instance == null) {
			instance = new WebappPagesCollection();
		}
		return instance;
	}

}
