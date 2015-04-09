package com.wearezeta.auto.common.calling;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingUtil {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CallingUtil.class
			.getSimpleName());

	private static String currentCallId = "";
	private static CallingServiceClient csc = null;
	static {
		try {
			csc = new CallingServiceClient(getCallingServiceHost(),
					getCallingServicePort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startCall(ClientUser caller, String conversationName)
			throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		String conversationId = BackendAPIWrappers.getConversationIdByName(
				caller, conversationName);
		currentCallId = csc.makeCall(email, password, conversationId,
				CommonUtils.getBackendType(CallingUtil.class),
				CallingServiceBackend.Autocall);
	}

	public static void stopCall() throws Exception {
		csc.stopCall(currentCallId);
	}

	public static void waitsForCallToAccept(ClientUser caller) throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		csc.waitToAcceptCall(email, password,
				CommonUtils.getBackendType(CallingUtil.class),
				CallingServiceBackend.Webdriver);
	}

	private static String getCallingServiceHost() throws Exception {
		return CommonUtils
				.getDefaultCallingServiceHostFromConfig(CallingUtil.class);
	}

	private static String getCallingServicePort() throws Exception {
		return CommonUtils
				.getDefaultCallingServicePortFromConfig(CallingUtil.class);
	}

}