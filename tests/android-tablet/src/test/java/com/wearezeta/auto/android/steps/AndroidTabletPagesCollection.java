package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.AndroidTabletPage;
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
		// return (AndroidTabletPage) super.getPageOrElseInstantiate(pageClass);
	}

	@Override
	public AndroidTabletPage getPageOrElseInstantiate(
			Class<? extends BasePage> pageClass) throws Exception {
		return (AndroidTabletPage) super.getPageOrElseInstantiate(pageClass);
	}

	@Override
	public void setPage(BasePage page) {
		if (!(page instanceof AndroidTabletPage)) {
			throw new IllegalStateException(
					String.format(
							"Only instances of '%s' are allowed. '%s' instance is provided instead",
							AndroidTabletPage.class.getSimpleName(), page
									.getClass().getSimpleName()));
		}
		super.setPage((AndroidTabletPage) page);
	}
}
