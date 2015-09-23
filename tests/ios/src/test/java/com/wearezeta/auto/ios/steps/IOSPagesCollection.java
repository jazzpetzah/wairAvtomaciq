package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.ios.pages.IOSPage;

public class IOSPagesCollection extends AbstractPagesCollection {

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

	@Override
	public IOSPage getCommonPage() throws Exception {
		return (IOSPage) super.getCommonPage();
	}

	@Override
	public void setFirstPage(BasePage page) {
		if (!(page instanceof IOSPage)) {
			throw new IllegalStateException(
				String.format(
					"Only instances of '%s' are allowed. '%s' instance has been provided instead",
					this.getClass().getSimpleName(), page.getClass()
					.getSimpleName()));
		}
		super.setFirstPage((IOSPage) page);
	}
}
