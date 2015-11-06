package com.wearezeta.auto.win.pages.common;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWinDriver;

import com.wearezeta.auto.win.common.WinExecutionContext;
import com.wearezeta.auto.win.pages.win.WinPage;
import java.util.concurrent.Future;

public class MainMenuAndDockPage extends WinPage {

	public MainMenuAndDockPage(Future<ZetaWinDriver> osxDriver)
			throws Exception {
		super(osxDriver, WinExecutionContext.WIRE_APP_PATH);
	}

}
