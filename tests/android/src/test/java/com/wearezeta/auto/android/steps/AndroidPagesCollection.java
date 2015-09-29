package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;

final class AndroidPagesCollection extends AbstractPagesCollection {

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

	@Override
	public AndroidPage getCommonPage() throws Exception {
		return (AndroidPage) super.getCommonPage();
	}

	@Override
	public void setFirstPage(BasePage page) {
		if (!(page instanceof AndroidPage)) {
			throw new IllegalStateException(
				String.format(
					"Only instances of '%s' are allowed. '%s' instance has been provided instead",
					this.getClass().getSimpleName(), page.getClass()
					.getSimpleName()));
		}
		super.setFirstPage((AndroidPage) page);
	}
}
