package com.wearezeta.auto.osx.pages.common;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;

import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.OSXPage;
import java.util.concurrent.Future;

public class MainMenuAndDockPage extends OSXPage {

	public MainMenuAndDockPage(Future<ZetaOSXDriver> osxDriver)
			throws Exception {
		super(osxDriver, OSXExecutionContext.WIRE_APP_PATH);
	}

}
