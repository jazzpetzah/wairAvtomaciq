package com.wearezeta.auto.sync;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.client.platform.AndroidWireInstance;
import com.wearezeta.auto.sync.client.platform.IOSWireInstance;
import com.wearezeta.auto.sync.client.platform.OSXWireInstance;
import com.wearezeta.auto.sync.report.ReportData;

public class ExecutionContext {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(ExecutionContext.class
			.getSimpleName());

	public static HashMap<Platform, WireInstance> clients = new HashMap<Platform, WireInstance>();

	public static LinkedHashMap<String, MessageEntry> sentMessages = new LinkedHashMap<String, MessageEntry>();

	public static synchronized void addNewSentTextMessage(MessageEntry message,
			boolean checkTime) {
		sentMessages.put(message.messageContent, message);

	}

	public static boolean isAndroidEnabled() {
		return clients.get(Platform.Android).isEnabled();
	}

	public static boolean isIosEnabled() {
		return clients.get(Platform.iOS).isEnabled();
	}

	public static boolean isOsxEnabled() {
		return clients.get(Platform.Mac).isEnabled();
	}

	public static AndroidWireInstance androidZeta() {
		return (AndroidWireInstance) clients.get(Platform.Android);
	}

	public static IOSWireInstance iosZeta() {
		return (IOSWireInstance) clients.get(Platform.iOS);
	}

	public static OSXWireInstance osxZeta() {
		return (OSXWireInstance) clients.get(Platform.Mac);
	}

	public static boolean allInstancesFinishSending() {
		boolean result = true;
		for (WireInstance client : clients.values()) {
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
		for (WireInstance client : clients.values()) {
			result = result
					&& (client.getState() == InstanceState.SENDING || !client
							.isEnabled());
		}
		return result;
	}

	public static boolean isPlatformMessagesOrderCorrect(Platform platform) {
		return clients.get(platform).isOrderCorrect();
	}

	public static ReportData report = new ReportData();
}
