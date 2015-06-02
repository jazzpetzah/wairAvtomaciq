package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;

final class AndroidTabletPagesCollection extends AbstractPagesCollection {

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

	@Override
	public AndroidTabletPage getCommonPage() throws Exception {
		return (AndroidTabletPage) super.getCommonPage();
	}

	@Override
	public AndroidTabletPage getPage(Class<? extends BasePage> pageClass)
			throws Exception {
		return (AndroidTabletPage) super.getPage(pageClass);
	}

	@Override
	public void setFirstPage(BasePage page) {
		if (!(page instanceof AndroidTabletPage)) {
			throw new IllegalStateException(
					String.format(
							"Only instances of '%s' are allowed. '%s' instance is provided instead",
							this.getClass().getSimpleName(), page.getClass()
									.getSimpleName()));
		}
		super.setFirstPage((AndroidTabletPage) page);
	}
}
