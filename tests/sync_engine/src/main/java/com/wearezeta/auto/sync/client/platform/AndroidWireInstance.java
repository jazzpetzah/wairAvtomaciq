package com.wearezeta.auto.sync.client.platform;

import java.util.Date;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.steps.CommonAndroidSteps;
import com.wearezeta.auto.android.steps.ContactListPageSteps;
import com.wearezeta.auto.android.steps.DialogPageSteps;
import com.wearezeta.auto.android.steps.LoginPageSteps;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.AndroidListener;
import com.wearezeta.auto.sync.client.reporter.AndroidInstanceReporter;
import com.wearezeta.auto.sync.client.sender.AndroidSender;

public class AndroidWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger
			.getLog(AndroidWireInstance.class.getSimpleName());

	private String NAME_ALIAS = "user2Name";

	public AndroidWireInstance() throws Exception {
		super(Platform.Android);
	}

	@Override
	public void createSender() {
		sender = new AndroidSender(this, messagesToSend);

	}

	@Override
	public void createListener() {
		listener = new AndroidListener(this);
	}

	@Override
	public void readPlatformSettings() throws Exception {
		try {
			enabled = SyncEngineUtil
					.getAndroidClientEnabledFromConfig(ExecutionContext.class);
		} catch (Exception e) {
			enabled = true;
			log.warn("Failed to read property android.client.enabled "
					+ "from config file. Set to 'true' by default");
		}

		try {
			sendToBackend = SyncEngineUtil
					.getAndroidBackendSenderFromConfig(this.getClass());
		} catch (Exception e) {
			sendToBackend = false;
			log.warn("Failed to read property android.backend.sender "
					+ "from config file. Set to 'false' by default");
		}

		wirePath = CommonUtils.getAndroidApplicationPathFromConfig(this
				.getClass());

		appiumUrl = CommonUtils.getAndroidAppiumUrlFromConfig(this.getClass());

		try {
			appiumLogPath = AndroidCommonUtils
					.getAndroidAppiumLogPathFromConfig(AndroidWireInstance.class);
		} catch (Exception e) {
			appiumLogPath = null;
			log.warn("Failed to read property androidAppiumLogPath from config file.");
		}
	}

	@Override
	public void closeAndClearImpl() throws Exception {
		if (PagesCollection.loginPage != null)
			PagesCollection.loginPage.close();
		AndroidPage.clearPagesCollection();
	}

	private void disableHints() {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			log.warn("Failed to disable hints on Android client.\n"
					+ e.getMessage());
		}
	}

	@Override
	public void startClientProcedureImpl() {
		disableHints();
		if (PagesCollection.loginPage == null) {
			CommonAndroidSteps androidSteps = new CommonAndroidSteps();
			long startDate = new Date().getTime();
			long endDate = 0;
			try {
				androidSteps.commonBefore();
				final Future<ZetaAndroidDriver> lazyDriver = androidSteps
						.resetAndroidDriver(appiumUrl, wirePath, false,
								this.getClass());
				PagesCollection.loginPage = new LoginPage(lazyDriver);
				endDate = new Date().getTime();
				ZetaFormatter.setLazyDriver(lazyDriver);
			} catch (Exception e) {
				log.debug("Failed to start Android client. Error message: "
						+ e.getMessage());
				e.printStackTrace();
			}

			try {
				startDate = SyncEngineUtil.readDateFromAppiumLog(appiumLogPath);
			} catch (Exception e) {
				log.error("Failed to read Android application startup time from Appium log.\n"
						+ "Approximate value will be used. " + e.getMessage());
			}
			reporter.setStartupTime(endDate - startDate);
		}
	}

	@Override
	public void signInImpl(String userAlias, String email, String password)
			throws Throwable {
		LoginPageSteps androidLoginPageSteps = new LoginPageSteps();
		androidLoginPageSteps.GivenISignIn(email, password);
		ContactListPageSteps androidContactListPageSteps = new ContactListPageSteps();
		androidContactListPageSteps.GivenISeeContactList();
	}

	@Override
	public void openConversationImpl(String chatName) throws Exception {
		ContactListPageSteps androidContactListPageSteps = new ContactListPageSteps();
		DialogPageSteps androidDialogPageSteps = new DialogPageSteps();
		androidContactListPageSteps.WhenITapOnContactName(chatName);
		androidDialogPageSteps.WhenISeeDialogPage();
	}

	@Override
	public String NAME_ALIAS() {
		return NAME_ALIAS;
	}

	@Override
	public void createReporter() {
		this.reporter = new AndroidInstanceReporter(this);
	}
}
