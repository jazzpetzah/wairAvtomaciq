package com.wearezeta.auto.osx.pages.common;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.OSXPage;
import java.util.concurrent.Future;

public class MainMenuAndDockPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(MainMenuAndDockPage.class.getSimpleName());

	public MainMenuAndDockPage(Future<ZetaOSXDriver> osxDriver,
			Future<ZetaWebAppDriver> webDriver) throws Exception {
		super(osxDriver, webDriver, OSXExecutionContext.WIRE_APP_PATH);
	}

}
