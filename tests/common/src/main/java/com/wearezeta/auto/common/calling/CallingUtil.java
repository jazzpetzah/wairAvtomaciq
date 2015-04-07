package com.wearezeta.auto.common.calling;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingUtil {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CallingUtil.class
			.getSimpleName());

	public static void startCall(ClientUser caller, String conversationName)
			throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		String conversationId = BackendAPIWrappers.getConversationIdByName(
				caller, conversationName);
		CallingServiceClient csc = new CallingServiceClient("192.168.10.44", "21980");
		csc.makeCall(email, password, conversationId, "staging", "autocall");
	}

	public static void stopCall() throws Exception {
		CallingServiceClient csc = new CallingServiceClient("192.168.10.44", "21980");
		csc.stopCall("");
	}
	
	public static void waitsForCallToAccept(ClientUser caller)
			throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		CallingServiceClient csc = new CallingServiceClient("192.168.10.44", "21980");
		csc.waitToAcceptCall(email, password, "staging", "webdriver");
	}

}