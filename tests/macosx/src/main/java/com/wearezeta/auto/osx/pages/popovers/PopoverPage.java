package com.wearezeta.auto.osx.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.pages.OSXPage;

public abstract class PopoverPage extends OSXPage {

	public PopoverPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

}
