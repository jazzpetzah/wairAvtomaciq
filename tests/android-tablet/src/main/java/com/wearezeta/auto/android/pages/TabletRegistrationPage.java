package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegistrationPage extends RegistrationPage {

	public TabletRegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public TabletPersonalInfoPage initProfilePage() throws Exception {
		return new TabletPersonalInfoPage(getLazyDriver());
	}

}
