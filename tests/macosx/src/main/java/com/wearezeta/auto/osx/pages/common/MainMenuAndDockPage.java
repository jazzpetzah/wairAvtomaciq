package com.wearezeta.auto.osx.pages.common;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.osx.OSXPage;
import java.util.concurrent.Future;

public class MainMenuAndDockPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(MainMenuAndDockPage.class.getSimpleName());

	public MainMenuAndDockPage(Future<ZetaOSXDriver> osxDriver)
			throws Exception {
		super(osxDriver, OSXExecutionContext.WIRE_APP_PATH);
	}

}
