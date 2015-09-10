package com.wearezeta.auto.osx.pages.calling;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.osx.pages.OSXPage;

public abstract class CallPage extends OSXPage {

	public CallPage(Future<ZetaOSXDriver> lazyDriver,
			Future<ZetaWebAppDriver> secondaryDriver) throws Exception {
		super(lazyDriver, secondaryDriver);
	}
}
