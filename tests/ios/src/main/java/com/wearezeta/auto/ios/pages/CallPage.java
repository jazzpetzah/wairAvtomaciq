package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class CallPage extends IOSPage {

	public CallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

}
