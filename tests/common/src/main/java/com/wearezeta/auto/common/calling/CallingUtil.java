package com.wearezeta.auto.common.calling;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingUtil {

	private static final Logger log = ZetaLogger.getLog(CallingUtil.class
			.getSimpleName());

	public static String CALLING_UTIL_PATH = "";

	static {
		try {
			CALLING_UTIL_PATH = CommonUtils
					.getJenkinsProjectDir(CallingUtil.class);
		} catch (Exception e) {
		}
	}

	public static int currentCallPid;

	public static void startCall(ClientUser caller, String conversationName)
			throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		String conversationId = BackendAPIWrappers.getConversationIdByName(
				caller, conversationName);
		startCall(email, password, conversationId);
	}

	public static void startCall(String callerEmail, String callerPassword,
			String conversationId) throws Exception {
		String backendParam = "";
		switch (CommonUtils.getBackendType(CallingUtil.class)) {
		case "staging":
			backendParam = "-D";
			break;
		case "edge":
			backendParam = "-E";
			break;
		}
		String autocall = String.format("%sautocall -e %s -p %s -c %s %s",
				CALLING_UTIL_PATH, callerEmail, callerPassword, conversationId,
				backendParam);

		Process process = Runtime.getRuntime().exec(
				new String[] { "bash", "-c", autocall });
		Field pidField = process.getClass().getDeclaredField("pid");
		pidField.setAccessible(true);
		currentCallPid = (int) pidField.get(process);
		log.debug("Process started for cmdline " + autocall);
	}

	public static void stopCall() throws Exception {
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c",
				"kill -s SIGINT " + currentCallPid });
	}
	
	public static void waitsForCallToAccept(ClientUser caller)
			throws Exception {
		String email = caller.getEmail();
		String password = caller.getPassword();
		GoogleComputeEngine.createInstanceAndStartBlender("blender-for-" + caller.getId(), email, password);
	}

	public static void deleteAllBlenderInstances() throws IOException, GeneralSecurityException {
		GoogleComputeEngine.deleteAllInstancesWhereNameContains("blender-for-");
	}
}