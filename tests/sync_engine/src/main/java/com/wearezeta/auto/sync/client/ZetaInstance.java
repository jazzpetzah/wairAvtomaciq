package com.wearezeta.auto.sync.client;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

public class ZetaInstance {
	private static final Logger log = ZetaLogger.getLog(ZetaInstance.class.getSimpleName());
	
	private InstanceState state = InstanceState.CREATED;

	private ZetaSender sender;
	private ZetaListener listener;
	
	private ClientUser userInstance;
	
	private String platform;

	private boolean isEnabled = false;
	private boolean isSendUsingBackend = false;
	
	private long startupTimeMs;
	
	public ZetaInstance(String platform) {
		this.platform = platform;
		
		if (platform.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
			try {
				isEnabled = SyncEngineUtil
					.getAndroidClientEnabledFromConfig(ExecutionContext.class);
			} catch (IOException e) {
				isEnabled = true;
				log.warn("Failed to read property android.client.enabled from config file. Set to 'true' by default");
			}
			
			try {
				isSendUsingBackend = SyncEngineUtil
						.getAndroidBackendSenderFromConfig(this.getClass());
			} catch (IOException e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property android.backend.sender from config file. Set to 'false' by default");
				
			}
		} else if (platform.equals(CommonUtils.PLATFORM_NAME_IOS)) {
			try {
				isEnabled = SyncEngineUtil
					.getIosClientEnabledFromConfig(ExecutionContext.class);
			} catch (IOException e) {
				isEnabled = true;
				log.warn("Failed to read property ios.client.enabled from config file. Set to 'true' by default");
			}
			
			try {
				isSendUsingBackend = SyncEngineUtil
						.getIosBackendSenderFromConfig(this.getClass());
			} catch (IOException e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property ios.backend.sender from config file. Set to 'false' by default");
			}
		} else if (platform.equals(CommonUtils.PLATFORM_NAME_OSX)) {

			try {
				isEnabled = SyncEngineUtil
					.getOSXClientEnabledFromConfig(ExecutionContext.class);
			} catch (IOException e) {
				isEnabled = true;
				log.warn("Failed to read property osx.client.enabled from config file. Set to 'true' by default");
			}
			
			try {
				isSendUsingBackend = SyncEngineUtil
						.getOSXBackendSenderFromConfig(this.getClass());
			} catch (IOException e) {
				isSendUsingBackend = false;
				log.warn("Failed to read property osx.backend.sender from config file. Set to 'false' by default");
			}
		} else {
			state = InstanceState.ERROR_WRONG_PLATFORM;
		}
	}
	
	public void createSender() {
		sender = new ZetaSender(this, platform, ExecutionContext.messagesCount);
	}
	
	public void createListener() {
		listener = new ZetaListener(this, platform);
	}
	
	public ZetaSender sender() {
		return sender;
	}
	
	public ZetaListener listener() {
		return listener;
	}
	
	public InstanceState getState() {
		return state;
	}
	
	public void setState(InstanceState state) {
		this.state = state;
	}
	
	public void setIsSendUsingBackend(boolean value) {
		isSendUsingBackend = value;
	}
	
	public boolean getIsSendUsingBackend() {
		return isSendUsingBackend;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public ClientUser getUserInstance() {
		return userInstance;
	}

	public void setUserInstance(ClientUser userInstance) {
		this.userInstance = userInstance;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public long getStartupTimeMs() {
		return startupTimeMs;
	}

	public void setStartupTimeMs(long startupTimeMs) {
		this.startupTimeMs = startupTimeMs;
	}
}
