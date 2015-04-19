package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.calling.CallingServiceClient;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.calling.models.CallingServiceStatus;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public final class CommonCallingSteps {
	public static final Logger log = ZetaLogger.getLog(CommonCallingSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final int CALL_START_TIMEOUT_SECONDS = 30;
	private static final int CALL_END_TIMEOUT_SECONDS = 30;
	private static final int INSTANCE_START_TIMEOUT_SECONDS = 30;
	private static final int INSTANCE_END_TIMEOUT_SECONDS = 30;
	private static final long POLLING_FREQUENCY_MILLISECONDS = 1000;

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

	private static List<CallingServiceStatus> statusesListToObject(
			String expectedStatuses) {
		List<CallingServiceStatus> result = new ArrayList<CallingServiceStatus>();
		String[] allStatuses = expectedStatuses.split(",");
		for (String status : allStatuses) {
			result.add(CallingServiceStatus.fromString(status.trim()));
		}
		return result;
	}

	public void UserXCallsToUserYUsingCallBackend(String userAsNameAlias,
			String userToNameAlias, String callingServiceBackend)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		final String callId = CallingServiceClient.callToUser(userAs, userTo,
				CallingServiceBackend.fromString(callingServiceBackend),
				new CallingServiceStatus[] { CallingServiceStatus.Waiting,
						CallingServiceStatus.Active },
				CALL_START_TIMEOUT_SECONDS);
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

	private void waitForExpectedCallStatuses(String callId,
			List<CallingServiceStatus> expectedStatuses, int secondsTimeout)
			throws Exception {
		long millisecondsStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
			final CallingServiceStatus currentStatus = CallingServiceClient
					.getCallStatus(callId);
			if (expectedStatuses.contains(currentStatus)) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		}
		throw new TimeoutException(
				String.format(
						"Call status has not been changed to '%s' after %s second(s) timeout",
						expectedStatuses, secondsTimeout));
	}

	private List<String> getCallIdsByParticipants(ClientUser userAs,
			ClientUser userTo) throws CallNotFoundException {
		final String callKey = makeKey(userAs, userTo);
		if (callsMapping.containsKey(callKey)) {
			return callsMapping.get(callKey);
		} else {
			throw new CallNotFoundException(String.format(
					"Please make a call from '%s' to '%s' first",
					userAs.getName(), userTo.getName()));
		}
	}

	public void UserXVerifesCallStatusToUserY(String userAsNameAlias,
			String userToNameAlias, String expectedStatuses, int secondsTimeout)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		waitForExpectedCallStatuses(getCallIdsByParticipants(userAs, userTo)
				.get(0), statusesListToObject(expectedStatuses), secondsTimeout);
	}

	public void UserXStopsCallsToUserY(String userAsNameAlias,
			String userToNameAlias) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		List<String> callIds = getCallIdsByParticipants(userAs, userTo);
		for (String callId : callIds) {
			CallingServiceClient
					.stopCall(
							callId,
							new CallingServiceStatus[] { CallingServiceStatus.Inactive },
							CALL_END_TIMEOUT_SECONDS);
		}
	}

	public void cleanupCalls() throws Exception {
		if (waitingInstancesMapping.size() > 0) {
			log.debug("Executing asynchronous cleanup of active calls leftovers...");
		}
		for (Map.Entry<String, List<String>> entry : callsMapping.entrySet()) {
			for (String callId : entry.getValue()) {
				CallingServiceClient.stopCall(callId);
			}
		}
		callsMapping.clear();
	}

	private List<String> getInstanceIdsByReceiver(ClientUser receiver)
			throws CallNotFoundException {
		final String instanceKey = makeKey(receiver);
		if (waitingInstancesMapping.containsKey(instanceKey)) {
			return waitingInstancesMapping.get(instanceKey);
		} else {
			throw new CallNotFoundException(String.format(
					"Please receive a call as user '%s' first",
					receiver.getName()));
		}
	}

	public void UserXStartsWaitingInstance(String userAsNameAlias,
			String callingServiceBackend) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		final String instanceId = CallingServiceClient.startWaitingInstance(
				userAs,
				CallingServiceBackend.fromString(callingServiceBackend),
				new CallingServiceStatus[] { CallingServiceStatus.Waiting },
				INSTANCE_START_TIMEOUT_SECONDS);
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

	public void UserXAcceptsNextIncomingCallAutomatically(String userAsNameAlias)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		CallingServiceClient.acceptNextIncomingCall(getInstanceIdsByReceiver(
				userAs).get(0));
	}

	private void waitForExpectedWaitingInstanceStatuses(String instanceId,
			List<CallingServiceStatus> expectedStatuses, int secondsTimeout)
			throws Exception {
		long millisecondsStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
			final CallingServiceStatus currentStatus = CallingServiceClient
					.getWaitingInstanceStatus(instanceId);
			if (expectedStatuses.contains(currentStatus)) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		}
		throw new TimeoutException(
				String.format(
						"Waiting instance status has not been changed to '%s' after %s second(s) timeout",
						expectedStatuses, secondsTimeout));
	}

	public void UserXVerifesWaitingInstanceStatus(String userAsNameAlias,
			String expectedStatuses, int secondsTimeout) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		waitForExpectedWaitingInstanceStatuses(getInstanceIdsByReceiver(userAs)
				.get(0), statusesListToObject(expectedStatuses), secondsTimeout);
	}

	public void UserXStopsIncomingCalls(String userAsNameAlias)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		for (String instanceId : getInstanceIdsByReceiver(userAs)) {
			CallingServiceClient
					.stopWaitingInstance(
							instanceId,
							new CallingServiceStatus[] { CallingServiceStatus.Inactive },
							INSTANCE_END_TIMEOUT_SECONDS);
		}
	}

	public void cleanupWaitingInstances() throws Exception {
		if (waitingInstancesMapping.size() > 0) {
			log.debug("Executing asynchronous cleanup of active waiting instances leftovers...");
		}
		for (Map.Entry<String, List<String>> entry : waitingInstancesMapping
				.entrySet()) {
			for (String instanceId : entry.getValue()) {
				CallingServiceClient.stopWaitingInstance(instanceId);
			}
		}
		waitingInstancesMapping.clear();
	}
}
