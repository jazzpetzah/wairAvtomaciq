package com.wearezeta.auto.sync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SEConstants;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.listener.WireListener;
import com.wearezeta.auto.sync.client.platform.AndroidWireInstance;
import com.wearezeta.auto.sync.client.platform.IOSWireInstance;
import com.wearezeta.auto.sync.client.platform.OSXWireInstance;
import com.wearezeta.auto.sync.client.sender.WireSender;

public abstract class WireInstance {

	private static final Logger log = ZetaLogger.getLog(WireInstance.class
			.getSimpleName());

	protected final ClientUsersManager usrMgr = ClientUsersManager
			.getInstance();

	// settings
	protected boolean enabled = false;
	protected boolean sendToBackend = false;
	protected int messagesToSend = -1;
	private int messagesSendingInterval = 0;

	private Platform platform;

	protected String wirePath;
	protected String appiumUrl;
	protected String appiumLogPath;

	// state
	private InstanceState state = InstanceState.CREATED;

	protected WireSender sender;
	protected WireListener listener;

	private ClientUser userInstance;

	// results
	protected long startupTime;
	private boolean orderCorrect;
	private ArrayList<MessageEntry> messagesListAfterTest;
	private BuildVersionInfo versionInfo;
	private ClientDeviceInfo deviceInfo;

	public WireInstance(Platform platform) throws Exception {
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

	public abstract String NAME_ALIAS();

	public abstract void readPlatformSettings() throws Exception;

	public abstract void createSender();

	public abstract void createListener();

	public Runnable startClientProcedure() {
		return new Runnable() {
			public void run() {
				if (enabled) {
					startClientProcedureImpl();
				} else {
					log.debug(String.format("%s: execution disabled. Skipped.",
							platform));
				}
			}
		};
	}

	public abstract void startClientProcedureImpl();

	public Runnable signInAndOpenConversation() {
		return new Runnable() {
			public void run() {
				if (enabled) {
					try {
						// resolve user email and password by aliases
						String email = usrMgr.findUserByNameOrNameAlias(
								NAME_ALIAS()).getEmail();
						String password = usrMgr.findUserByNameOrNameAlias(
								NAME_ALIAS()).getPassword();
						log.debug(String.format(
								"%s: Found user data %s:%s for alias %s",
								platform, email, password, NAME_ALIAS()));
						log.debug(String.format(
								"%s: Started sign in using credentials %s:%s",
								platform, email, password));
						signInImpl(NAME_ALIAS(), email, password);
					} catch (Throwable e) {
						log.fatal(String
								.format("%s: Client crashed during sign in procedure.\n%s",
										platform, e.getMessage()));
						e.printStackTrace();
						state = InstanceState.ERROR_CRASHED;
					}
					try {
						log.debug(String.format("%s: Open test conversation",
								platform));
						openConversationImpl(SEConstants.Common.TEST_CONVERSATION);
					} catch (Throwable e) {
						log.fatal(String
								.format("%s: Client crashed on opening conversation.\n%s",
										platform, e.getMessage()));
						e.printStackTrace();
						state = InstanceState.ERROR_CRASHED;
					}
					log.debug(String
							.format("%s: Client login finished %s",
									platform,
									(state == InstanceState.ERROR_CRASHED) ? "with errors."
											: "successfully."));
				}
			}
		};
	}

	public abstract void signInImpl(String userAlias, String email,
			String password) throws Throwable;

	public abstract void openConversationImpl(String chatName) throws Exception;

	public void closeAndClear() throws Exception {
		if (enabled) {
			closeAndClearImpl();
		}
	}

	public abstract void closeAndClearImpl() throws Exception;

	public static WireInstance getInstance(Platform platform) throws Exception {
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

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
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

	public boolean isOrderCorrect() {
		return orderCorrect;
	}

	public void setOrderCorrect(boolean orderCorrect) {
		this.orderCorrect = orderCorrect;
	}
}
