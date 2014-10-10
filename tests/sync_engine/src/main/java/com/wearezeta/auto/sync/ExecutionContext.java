package com.wearezeta.auto.sync;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.ZetaInstance;

import com.wearezeta.auto.sync.report.ReportData;

public class ExecutionContext {
	private static final Logger log = ZetaLogger.getLog(ExecutionContext.class.getSimpleName());
	
	public static HashMap<String, ZetaInstance> clients = new HashMap<String, ZetaInstance>();

	public static LinkedHashMap<String, MessageEntry> sentMessages = new LinkedHashMap<String, MessageEntry>();
	
	public static synchronized void addNewSentTextMessage(MessageEntry message) {
		sentMessages.put(message.messageContent, message);
	}
	
	public static boolean isAndroidEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_ANDROID).isEnabled(); }
	public static boolean isIosEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_IOS).isEnabled(); }
	public static boolean isOsxEnabled() { return clients.get(CommonUtils.PLATFORM_NAME_OSX).isEnabled(); }

	public static ZetaInstance androidZeta() { return clients.get(CommonUtils.PLATFORM_NAME_ANDROID); }
	public static ZetaInstance iosZeta() { return clients.get(CommonUtils.PLATFORM_NAME_IOS); }
	public static ZetaInstance osxZeta() { return clients.get(CommonUtils.PLATFORM_NAME_OSX); }

	public static boolean allInstancesFinishSending() {
		boolean result = true;
		for (ZetaInstance client : clients.values()) {
			if (client.isEnabled()) {
				result = (result && (client.getState() == InstanceState.FINAL_LISTENING
						|| client.getState() == InstanceState.FINISHED || client
						.getState() == InstanceState.ERROR_CRASHED));
			}
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
	
	public static boolean messagesOrderCorrect() {
		return androidZeta().isOrderCorrect()
				&& osxZeta().isOrderCorrect()
				&& iosZeta().isOrderCorrect();
	}
	
	public static ReportData report = new ReportData();
}
