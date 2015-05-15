package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletLoginPage extends LoginPage {
	
	public TabletLoginPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

}
