package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletLoginPage extends LoginPage {

	public TabletLoginPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public TabletPersonalInfoPage initProfilePage() throws Exception {
		return new TabletPersonalInfoPage(getLazyDriver());
	}

	public TabletContactListPage initContactListPage() throws Exception {
		return new TabletContactListPage(getLazyDriver());
	}

	public TabletRegistrationPage tabletJoin() throws Exception {
		signUpButton.click();
		return new TabletRegistrationPage(getLazyDriver());
	}

	public static void clearTabletPagesCollection()
			throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(TabletPagesCollection.class, AndroidPage.class);
	}
}
