package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends PeoplePickerPage{

	public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public TabletConnectToPage initConnectToPage() throws Exception {
		return new TabletConnectToPage(getLazyDriver());
	}
}
