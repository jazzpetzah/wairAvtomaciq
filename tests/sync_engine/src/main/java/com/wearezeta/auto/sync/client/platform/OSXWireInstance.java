package com.wearezeta.auto.sync.client.platform;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.steps.CommonOSXSteps;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.OSXListener;
import com.wearezeta.auto.sync.client.sender.OSXSender;

public class OSXWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger.getLog(OSXWireInstance.class
			.getSimpleName());

	public OSXWireInstance() throws Exception {
		super(Platform.Mac);
	}

	@Override
	public void createSender() {
		sender = new OSXSender(this, messagesToSend);
	}

	@Override
	public void createListener() {
		listener = new OSXListener(this);
	}

	@Override
	public void readPlatformSettings() throws Exception {
		try {
			enabled = SyncEngineUtil
					.getOSXClientEnabledFromConfig(ExecutionContext.class);
		} catch (Exception e) {
			enabled = true;
			log.warn("Failed to read property osx.client.enabled "
					+ "from config file. Set to 'true' by default");
		}

		try {
			sendToBackend = SyncEngineUtil.getOSXBackendSenderFromConfig(this
					.getClass());
		} catch (Exception e) {
			sendToBackend = false;
			log.warn("Failed to read property osx.backend.sender "
					+ "from config file. Set to 'false' by default");
		}

		wirePath = CommonUtils.getOsxApplicationPathFromConfig(this.getClass());

		appiumUrl = CommonUtils.getOsxAppiumUrlFromConfig(this.getClass());
	}

	@Override
	public void closeAndClearImpl() throws Exception {
		PagesCollection.loginPage.close();
		OSXPage.clearPagesCollection();
	}

	@Override
	public void startClientProcedureImpl() {
		CommonOSXSteps osxSteps = new CommonOSXSteps();
		try {
			osxSteps.setUp();
		} catch (Exception e) {
			log.debug("Failed to start OSX client. Error message: "
					+ e.getMessage());
			e.printStackTrace();
		}
		startupTime = osxSteps.startupTime;
		log.debug("OSX application startup time: " + startupTime + "ms");
	}

}
