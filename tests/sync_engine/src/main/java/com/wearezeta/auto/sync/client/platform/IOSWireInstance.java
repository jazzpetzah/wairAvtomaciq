package com.wearezeta.auto.sync.client.platform;

import java.util.Date;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.CommonIOSSteps;
import com.wearezeta.auto.ios.ContactListPageSteps;
import com.wearezeta.auto.ios.DialogPageSteps;
import com.wearezeta.auto.ios.LoginPageSteps;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.IOSListener;
import com.wearezeta.auto.sync.client.reporter.IOSInstanceReporter;
import com.wearezeta.auto.sync.client.sender.IOSSender;

public class IOSWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger.getLog(IOSWireInstance.class
			.getSimpleName());

	private String NAME_ALIAS = "user3Name";

	public IOSWireInstance() throws Exception {
		super(Platform.iOS);
	}

	@Override
	public void createSender() {
		sender = new IOSSender(this, messagesToSend);
	}

	@Override
	public void createListener() {
		listener = new IOSListener(this);
	}

	@Override
	public void readPlatformSettings() throws Exception {
		try {
			enabled = SyncEngineUtil
					.getIosClientEnabledFromConfig(ExecutionContext.class);
		} catch (Exception e) {
			enabled = true;
			log.warn("Failed to read property ios.client.enabled "
					+ "from config file. Set to 'true' by default");
		}

		try {
			sendToBackend = SyncEngineUtil.getIosBackendSenderFromConfig(this
					.getClass());
		} catch (Exception e) {
			sendToBackend = false;
			log.warn("Failed to read property ios.backend.sender "
					+ "from config file. Set to 'false' by default");
		}

		wirePath = CommonUtils.getIosApplicationPathFromConfig(this.getClass());

		appiumUrl = CommonUtils.getIosAppiumUrlFromConfig(this.getClass());

		try {
			appiumLogPath = IOSCommonUtils
					.getIosAppiumLogPathFromConfig(IOSWireInstance.class);
		} catch (Exception e) {
			appiumLogPath = null;
			log.warn("Failed to read property iosAppiumLogPath  from config file.");
		}
	}

	@Override
	public void closeAndClearImpl() throws Exception {
		PagesCollection.loginPage.close();
		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();
	}

	@Override
	public void startClientProcedureImpl() {
		if (PagesCollection.loginPage == null) {
			CommonIOSSteps iosSteps = new CommonIOSSteps();
			long startDate = new Date().getTime();
			try {
				iosSteps.setUpAcceptAlerts();
			} catch (Exception e) {
				log.debug("Failed to start iOS client. Error message: "
						+ e.getMessage());
				e.printStackTrace();
			}
			long endDate = new Date().getTime();
			try {
				startDate = SyncEngineUtil.readDateFromAppiumLog(appiumLogPath);
			} catch (Exception e) {
				log.error("Failed to read iOS application startup time from Appium log.\n"
						+ "Approximate value will be used. " + e.getMessage());
			}
			startupTime = endDate - startDate;
			log.debug("iOS application startup time: " + startupTime + "ms");
			try {
				com.wearezeta.auto.ios.pages.PagesCollection.loginPage
						.ignoreUpdate();
			} catch (Exception e) {
				log.debug("No update notification.");
			}
		}
	}

	@Override
	public void signInImpl(String userAlias, String email, String password)
			throws Throwable {
		LoginPageSteps iosLoginPageSteps = new LoginPageSteps();
		iosLoginPageSteps.GivenISignIn(email, password);
		ContactListPageSteps iosContactListPageSteps = new ContactListPageSteps();
		iosContactListPageSteps.GivenISeeContactListWithMyName(userAlias);
	}

	@Override
	public void openConversationImpl(String chatName) throws Exception {
		ContactListPageSteps iosContactListPageSteps = new ContactListPageSteps();
		DialogPageSteps iosDialogPageSteps = new DialogPageSteps();
		iosContactListPageSteps.WhenITapOnContactName(chatName);
		iosDialogPageSteps.WhenISeeDialogPage();
	}

	@Override
	public String NAME_ALIAS() {
		return NAME_ALIAS;
	}

	@Override
	public void createTestResults() {
		this.reporter = new IOSInstanceReporter(this);
	}
}
