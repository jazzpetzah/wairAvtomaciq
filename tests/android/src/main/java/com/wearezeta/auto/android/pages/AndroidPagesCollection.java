package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;

public final class AndroidPagesCollection extends AbstractPagesCollection {

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
	public AndroidPage getPage(Class<? extends BasePage> pageClass)
			throws Exception {
		return (AndroidPage) super.getPage(pageClass);
		// return (AndroidPage) super.getPageOrElseInstantiate(pageClass);
	}

	@Override
	public AndroidPage getPageOrElseInstantiate(
			Class<? extends BasePage> pageClass) throws Exception {
		return (AndroidPage) super.getPageOrElseInstantiate(pageClass);
	}

}
