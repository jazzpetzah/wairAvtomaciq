package com.wearezeta.auto.sync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.listener.WireListener;
import com.wearezeta.auto.sync.client.platform.AndroidWireInstance;
import com.wearezeta.auto.sync.client.platform.IOSWireInstance;
import com.wearezeta.auto.sync.client.platform.OSXWireInstance;
import com.wearezeta.auto.sync.client.sender.WireSender;

public abstract class WireInstance {

	private static final Logger log = ZetaLogger.getLog(WireInstance.class
			.getSimpleName());

	// settings
	protected boolean enabled = false;
	protected boolean sendToBackend = false;
	protected int messagesToSend = -1;
	private int messagesSendingInterval = 0;

	private Platform platform;

	// state
	private InstanceState state = InstanceState.CREATED;

	protected WireSender sender;
	protected WireListener listener;

	private ClientUser userInstance;

	// results
	private long startupTimeMs;
	@SuppressWarnings("unused")
	private long loginAndContactListLoadingTimeMs;
	@SuppressWarnings("unused")
	private long conversationLoadingTimeMs;
	private boolean isOrderCorrect;
	private ArrayList<MessageEntry> messagesListAfterTest;
	private BuildVersionInfo versionInfo;
	private ClientDeviceInfo deviceInfo;

	public WireInstance(Platform platform) {
		this.platform = platform;

		readPlatformSettings();

		try {
			messagesSendingInterval = SyncEngineUtil
					.getAcceptanceMaxSendingIntervalFromConfig(ExecutionContext.class);

		} catch (Exception e) {
			messagesSendingInterval = 0;
			log.warn("Failed to read property acceptance.max.sending.interval.sec "
					+ "from config file. Set to '0' by default");
		}

		try {
			messagesToSend = SyncEngineUtil
					.getClientMessagesCount(ExecutionContext.class);

		} catch (Exception e) {
			messagesToSend = -1;
			log.warn("Failed to read property acceptance.messages.count "
					+ "from config file. Set to '0' by default");
		}

		if (enabled) {
			createSender();
			createListener();
		}
	}

	public abstract void readPlatformSettings();

	public abstract void createSender();

	public abstract void createListener();

	public static WireInstance getInstance(Platform platform) {
		WireInstance instance = null;
		switch (platform) {
		case Android:
			instance = new AndroidWireInstance();
			break;
		case iOS:
			instance = new IOSWireInstance();
			break;
		case Mac:
			instance = new OSXWireInstance();
			break;
		default:
			throw new UnsupportedOperationException("Platform " + platform
					+ " is not supported by Sync Engine test.");
		}
		return instance;
	}

	public Platform platform() {
		return platform;
	}

	public WireSender sender() {
		return sender;
	}

	public WireListener listener() {
		return listener;
	}

	// getters and setters

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getStartupTimeMs() {
		return startupTimeMs;
	}

	public void setStartupTimeMs(long startupTimeMs) {
		this.startupTimeMs = startupTimeMs;
	}

	public InstanceState getState() {
		return state;
	}

	public void setState(InstanceState state) {
		this.state = state;
	}

	public boolean isSendToBackend() {
		return sendToBackend;
	}

	public void setSendToBackend(boolean sendToBackend) {
		this.sendToBackend = sendToBackend;
	}

	public int getMessagesToSend() {
		return messagesToSend;
	}

	public void setMessagesToSend(int messagesToSend) {
		this.messagesToSend = messagesToSend;
	}

	public int getMessagesSendingInterval() {
		return messagesSendingInterval;
	}

	public void setMessagesSendingInterval(int messagesSendingInterval) {
		this.messagesSendingInterval = messagesSendingInterval;
	}

	public ArrayList<MessageEntry> getMessagesListAfterTest() {
		return messagesListAfterTest;
	}

	public void setMessagesListAfterTest(
			ArrayList<MessageEntry> messagesListAfterTest) {
		this.messagesListAfterTest = messagesListAfterTest;
	}

	public ClientUser getUserInstance() {
		return userInstance;
	}

	public void setUserInstance(ClientUser userInstance) {
		this.userInstance = userInstance;
	}

	public boolean isOrderCorrect() {
		return isOrderCorrect;
	}

	public void setOrderCorrect(boolean isOrderCorrect) {
		this.isOrderCorrect = isOrderCorrect;
	}

	public BuildVersionInfo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(BuildVersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public ClientDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(ClientDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
