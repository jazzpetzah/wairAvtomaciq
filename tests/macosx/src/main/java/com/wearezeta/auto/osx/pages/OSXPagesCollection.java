package com.wearezeta.auto.osx.pages;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;

public class OSXPagesCollection extends AbstractPagesCollection {

	private static OSXPagesCollection instance = null;

	public synchronized static OSXPagesCollection getInstance() {
		if (instance == null) {
			instance = new OSXPagesCollection();
		}
		return instance;
	}

	private OSXPagesCollection() {
		super();
	}

	@Override
	public OSXPage getCommonPage() throws Exception {
		return (OSXPage) super.getCommonPage();
	}

	@Override
	public void setFirstPage(BasePage page) {
		if (!(page instanceof OSXPage)) {
			throw new IllegalStateException(
					String.format(
							"Only instances of '%s' are allowed. '%s' instance has been provided instead",
							this.getClass().getSimpleName(), page.getClass()
									.getSimpleName()));
		}
		super.setFirstPage((OSXPage) page);
	}

}
