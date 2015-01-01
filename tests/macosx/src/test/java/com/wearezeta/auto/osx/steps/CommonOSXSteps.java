package com.wearezeta.auto.osx.steps;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonOSXSteps extends CommonSteps{
	public CommonOSXSteps() {
		super();
	}

	public static final Logger log = ZetaLogger.getLog(CommonOSXSteps.class
			.getSimpleName());

	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static PagesCollection senderPages;

	@Before("@performance")
	public void setUpPerformance() throws Exception, UriBuilderException,
			IOException, MessagingException, JSONException,
			BackendRequestException, InterruptedException {
		usrMgr.generatePerformanceUser();

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonOSXSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();
	}

	@Before("~@performance")
	public void setUp() throws Exception {

		OSXCommonUtils.deleteZClientLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();

		OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
				.getClass()));

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonOSXSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();

		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils.getBackendType(this
				.getClass()))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			senderPages.getMainMenuPage().quitZClient();
			OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
					.getClass()));
			senderPages.getLoginPage().startApp();
		}
	}

	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();

		// workaround for stuck on Send picture test
		OSXCommonUtils.killWireIfStuck();
	}
}
