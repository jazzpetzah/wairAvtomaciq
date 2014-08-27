package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
	}
	
	public static PagesCollection senderPages;
	
	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;
	
	@Before
	public void setUp() throws Exception {
		boolean generateUsersFlag = Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class));
	
		if (isFirstRun) {
			isFirstRun = false;
			if (generateUsersFlag) {
				CommonUtils.generateUsers(3);
				Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
				TestPreparation.createContactLinks();
			} else {
				CommonUtils.usePrecreatedUsers();
			}
			isFirstRunPassed = true;
		}
		
		if (!isFirstRunPassed) {
			throw new Exception("Skipped due to error in users creation.");
		}
		
		String path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
		senderPages = new PagesCollection();
		
		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();
	}
	
	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();
	}
}
