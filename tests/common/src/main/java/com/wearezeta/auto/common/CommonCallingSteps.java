package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.common.calling.CallingServiceClient;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.calling.models.CallingServiceStatus;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public final class CommonCallingSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static CommonCallingSteps instance = null;

	private Map<String, List<String>> callsMapping = new HashMap<String, List<String>>();
	private Map<String, List<String>> waitingInstancesMapping = new HashMap<String, List<String>>();

	public synchronized static CommonCallingSteps getInstance() {
		if (instance == null) {
			instance = new CommonCallingSteps();
		}
		return instance;
	}

	private CommonCallingSteps() {
	}

	private static String makeKey(ClientUser user1, ClientUser user2) {
		return String.format("%s:%s", user1.getEmail(), user2.getEmail());
	}

	private static String makeKey(ClientUser user1) {
		return user1.getEmail();
	}

	public static class CallNotFoundException extends Exception {
		private static final long serialVersionUID = -2260765997668002031L;

		public CallNotFoundException(String message) {
			super(message);
		}
	}

	public void UserXCallsToUserYUsingCallBackend(String userAsNameAlias,
			String userToNameAlias, String callingServiceBackend)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		final String callId = CallingServiceClient.callToUser(userAs, userTo,
				CallingServiceBackend.fromString(callingServiceBackend));
		final String callKey = makeKey(userAs, userTo);
		if (callsMapping.containsKey(callKey)) {
			List<String> callIds = callsMapping.get(callKey);
			callIds.add(callId);
		} else {
			List<String> callIds = new ArrayList<String>();
			callIds.add(callId);
			callsMapping.put(callKey, callIds);
		}
	}

	public void UserXStopsCallsToUserY(String userAsNameAlias,
			String userToNameAlias) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		final String callKey = makeKey(userAs, userTo);
		if (callsMapping.containsKey(callKey)) {
			List<String> callIds = callsMapping.get(callKey);
			for (String callId : callIds) {
				CallingServiceClient.stopCall(callId);
			}
		} else {
			throw new CallNotFoundException(String.format(
					"Please make a call from '%s' to '%s' first",
					userAs.getName(), userTo.getName()));
		}
	}

	public void UserXWaitsForACall(String userAsNameAlias,
			String callingServiceBackend, int secondsTimeout) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		final String instanceId = CallingServiceClient.waitForCall(userAs,
				CallingServiceBackend.fromString(callingServiceBackend),
				CallingServiceStatus.Waiting, secondsTimeout);
		final String instanceKey = makeKey(userAs);
		if (waitingInstancesMapping.containsKey(instanceKey)) {
			List<String> instanceIds = waitingInstancesMapping.get(instanceKey);
			instanceIds.add(instanceId);
		} else {
			List<String> instanceIds = new ArrayList<String>();
			instanceIds.add(instanceId);
			waitingInstancesMapping.put(instanceKey, instanceIds);
		}
	}

	public void UserXStopsIncomingCalls(String userAsNameAlias,
			int secondsTimeout) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		final String instanceKey = makeKey(userAs);
		if (waitingInstancesMapping.containsKey(instanceKey)) {
			List<String> instanceIds = waitingInstancesMapping.get(instanceKey);
			for (String instanceId : instanceIds) {
				CallingServiceClient.stopWaitingInstance(instanceId,
						secondsTimeout);
			}
		} else {
			throw new CallNotFoundException(String.format(
					"Please receive a call as user '%s' first",
					userAs.getName()));
		}
	}
}
