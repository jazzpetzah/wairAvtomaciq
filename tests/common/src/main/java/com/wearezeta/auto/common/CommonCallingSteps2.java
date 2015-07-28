package com.wearezeta.auto.common;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.calling2.v1.CallingServiceClient;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.CallStatus;
import com.wearezeta.auto.common.calling2.v1.model.Instance;
import com.wearezeta.auto.common.calling2.v1.model.InstanceStatus;
import com.wearezeta.auto.common.calling2.v1.model.InstanceType;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public final class CommonCallingSteps2 {
	public static final Logger log = ZetaLogger
			.getLog(CommonCallingSteps2.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final long POLLING_FREQUENCY_MILLISECONDS = 1000;

	private static CommonCallingSteps2 instance = null;

	private final Map<String, Instance> instanceMapping = new HashMap<>();
	private final Map<String, Call> callMapping = new HashMap<>();

	public synchronized static CommonCallingSteps2 getInstance() {
		if (instance == null) {
			instance = new CommonCallingSteps2();
		}
		return instance;
	}

	private CommonCallingSteps2() {
	}

	public static class CallNotFoundException extends Exception {
		private static final long serialVersionUID = -2260765997668002031L;

		public CallNotFoundException(String message) {
			super(message);
		}
	}

	public void UserXCallsToUserYUsingCallBackend(String userAsNameAlias,
			String userToNameAlias, String instanceType) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		final String convId = BackendAPIWrappers.getConversationIdByName(
				userAs, userTo.getName());

		final Instance instance = CallingServiceClient.startInstance(userAs,
				instanceTypeFix(instanceType));

		addInstance(instance, userAs);

		final Call call = CallingServiceClient.callToUser(instance, convId);

		addCall(call, userAs, userTo);

	}

	public void UserXVerifesCallStatusToUserY(String userAsNameAlias,
			String userToNameAlias, String expectedStatuses, int secondsTimeout)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		waitForExpectedCallStatuses(getInstanceByParticipant(userAs),
				getCallByParticipants(userAs, userTo),
				callStatusesListToObject(expectedStatuses), secondsTimeout);
	}

	public void UserXStopsCallsToUserY(String userAsNameAlias,
			String userToNameAlias) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		CallingServiceClient.stopCall(getInstanceByParticipant(userAs),
				getCallByParticipants(userAs, userTo));
	}

	public void UserXStartsWaitingInstance(String userAsNameAlias,
			String instanceType) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);

		final Instance instance = CallingServiceClient.startInstance(userAs,
				instanceTypeFix(instanceType));
		addInstance(instance, userAs);
	}

	public void UserXAcceptsNextIncomingCallAutomatically(String userAsNameAlias)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		Call call = CallingServiceClient
				.acceptNextIncomingCall(getInstanceByParticipant(userAs));
		addCall(call, userAs);
	}

	public void UserXVerifesWaitingInstanceStatus(String userAsNameAlias,
			String expectedStatuses, int secondsTimeout) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		waitForExpectedCallStatuses(getInstanceByParticipant(userAs),
				getWaitingCallByParticipant(userAs),
				callStatusesListToObject(expectedStatuses), secondsTimeout);
	}

	// TODO rename to 'stopAllWaitingCalls' if necessary
	public void UserXStopsIncomingCalls(String userAsNameAlias)
			throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
		CallingServiceClient.stopCall(getInstanceByParticipant(userAs),
				getWaitingCallByParticipant(userAs));
	}

	public void cleanup() throws Exception {
		if (instanceMapping.size() > 0) {
			log.debug("Executing asynchronous cleanup of call instance leftovers...");
		}
		for (Map.Entry<String, Instance> entry : instanceMapping.entrySet()) {
			// will also stop all related calls
			CallingServiceClient.stopInstance(entry.getValue());
		}
		instanceMapping.clear();
	}

	private void waitForExpectedCallStatuses(Instance instance, Call call,
			List<CallStatus> expectedStatuses, int secondsTimeout)
			throws Exception {
		long millisecondsStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
			final CallStatus currentStatus = CallingServiceClient
					.getCallStatus(instance, call);
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

	private static String makeKey(ClientUser from) {
		return from.getEmail();
	}

	private static String makeKey(ClientUser from, ClientUser... to) {
		StringBuilder key = new StringBuilder(from.getEmail());
		for (ClientUser user : to) {
			key.append(":").append(user.getEmail());
		}
		return key.toString();
	}

	private static List<CallStatus> callStatusesListToObject(
			String expectedStatuses) {
		List<CallStatus> result = new ArrayList<>();
		String[] allStatuses = expectedStatuses.split(",");
		for (String status : allStatuses) {
			String clearedStatus = status.trim().toUpperCase();
			if (clearedStatus.equals("READY")) {
				clearedStatus = "DESTROYED";
				// READY could mean DESTROYED or NON_EXISTENT so we add both
				result.add(CallStatus.NON_EXISTENT);
				log.warn("Please use DESTROYED or NON_EXISTENT instead of READY to check the state of a call! READY will be removed in future versions.");
			}
			result.add(CallStatus.valueOf(clearedStatus));
		}
		return result;
	}

	private static List<InstanceStatus> instanceStatusesListToObject(
			String expectedStatuses) {
		List<InstanceStatus> result = new ArrayList<>();
		String[] allStatuses = expectedStatuses.split(",");
		for (String status : allStatuses) {
			result.add(InstanceStatus.valueOf(status.trim()));
		}
		return result;
	}

	private void addCall(Call call, ClientUser from, ClientUser... to) {
		final String key = makeKey(from, to);
		callMapping.put(key, call);
	}

	private void addInstance(Instance instance, ClientUser from) {
		String key = makeKey(from);
		instanceMapping.put(key, instance);
	}

	private Instance getInstanceByParticipant(ClientUser userAs)
			throws CallNotFoundException {
		final String key = makeKey(userAs);
		if (instanceMapping.containsKey(key)) {

			return instanceMapping.get(key);
		} else {
			throw new CallNotFoundException(String.format(
					"Please create an instance for user '%s' first",
					userAs.getName()));
		}
	}

	private Call getWaitingCallByParticipant(ClientUser userAs)
			throws CallNotFoundException {
		final String callKey = makeKey(userAs);
		if (callMapping.containsKey(callKey)) {

			return callMapping.get(callKey);
		} else {
			throw new CallNotFoundException(String.format(
					"Please wait for a call as '%s' first", userAs.getName()));
		}
	}

	private Call getCallByParticipants(ClientUser userAs, ClientUser userTo)
			throws CallNotFoundException {
		final String callKey = makeKey(userAs, userTo);
		if (callMapping.containsKey(callKey)) {

			return callMapping.get(callKey);
		} else {
			throw new CallNotFoundException(String.format(
					"Please make a call from '%s' to '%s' first",
					userAs.getName(), userTo.getName()));
		}
	}

	/**
	 * Converts a string for the calling service instance type to an Enum. For
	 * compatibility reasons WEBDRIVER is changed to CHROME. WEBDRIVER was used
	 * as the callingservice didn't supported other browsers than CHROME.
	 *
	 * @param instanceType
	 * @return
	 */
	private InstanceType instanceTypeFix(String instanceType) {
		instanceType = instanceType.toUpperCase();
		if (instanceType.equals("WEBDRIVER")) {
			instanceType = "CHROME";
			log.warn("Please use CHROME or FIREFOX instead of WEBDRIVER as instance type for calling! WEBDRIVER will be removed in future versions.");
		}
		return InstanceType.valueOf(instanceType);
	}
}
