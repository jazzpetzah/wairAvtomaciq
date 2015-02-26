package com.wearezeta.auto.sync.client.platform;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.IOSListener;
import com.wearezeta.auto.sync.client.sender.IOSSender;

public class IOSWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger.getLog(IOSWireInstance.class.getSimpleName());

	public IOSWireInstance() {
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
	public void readPlatformSettings() {
		try {
			enabled = SyncEngineUtil
					.getIosClientEnabledFromConfig(ExecutionContext.class);
		} catch (Exception e) {
			enabled = true;
			log.warn("Failed to read property ios.client.enabled "
					+ "from config file. Set to 'true' by default");
		}

		try {
			sendToBackend = SyncEngineUtil
					.getIosBackendSenderFromConfig(this.getClass());
		} catch (Exception e) {
			sendToBackend = false;
			log.warn("Failed to read property ios.backend.sender "
					+ "from config file. Set to 'false' by default");
		}
	}

}
