package com.wearezeta.auto.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.ZetaInstance;

public class ExecutionContext {
	private static final Logger log = ZetaLogger.getLog(ExecutionContext.class.getSimpleName());
	
	public static HashMap<String, ZetaInstance> clients = new HashMap<String, ZetaInstance>();

	public static int sendingInterval = 0;
	public static int messagesCount = -1;
	
	public static ArrayList<MessageEntry> sentMessages = new ArrayList<MessageEntry>();
	
	public static synchronized void addNewSentTextMessage(MessageEntry message) {
		sentMessages.add(message);
	}
	
	static {
		try {
			sendingInterval = SyncEngineUtil.getAcceptanceMaxSendingIntervalFromConfig(ExecutionContext.class);
			
		} catch (IOException e) {
			sendingInterval = 0;
			log.warn("Failed to read property acceptance.max.sending.interval.sec from config file. Set to '0' by default");
		}
		
		try {
			messagesCount = SyncEngineUtil.getClientMessagesCount(ExecutionContext.class);
			
		} catch (IOException e) {
			messagesCount = -1;
			log.warn("Failed to read property acceptance.messages.count from config file. Set to '0' by default");
		}
	}
	
	public static boolean isAndroidEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_ANDROID).isEnabled(); }
	public static boolean isIosEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_IOS).isEnabled(); }
	public static boolean isOsxEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_OSX).isEnabled(); }

	public static ZetaInstance androidZeta() { return clients.get(CommonUtils.PLATFORM_NAME_ANDROID); }
	public static ZetaInstance iosZeta() { return clients.get(CommonUtils.PLATFORM_NAME_IOS); }
	public static ZetaInstance osxZeta() { return clients.get(CommonUtils.PLATFORM_NAME_OSX); }
	
	
	public static boolean allInstancesFinishSending() {
		boolean result = true;
		for (ZetaInstance client: clients.values()) {
			result = result && (client.getState() == InstanceState.FINAL_LISTENING || !client.isEnabled());
		}
		return result;
	}

	public static boolean allClientsReadyForSending() {
		boolean result = true;
		for (ZetaInstance client: clients.values()) {
			result = result && (client.getState() == InstanceState.SENDING || !client.isEnabled());
		}
		return result;
	}
}
