package com.wearezeta.auto.sync.client.platform;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.listener.AndroidListener;
import com.wearezeta.auto.sync.client.sender.AndroidSender;

public class AndroidWireInstance extends WireInstance {

	private static final Logger log = ZetaLogger
			.getLog(AndroidWireInstance.class.getSimpleName());

	public AndroidWireInstance() {
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
	public void readPlatformSettings() {
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
	}

}
