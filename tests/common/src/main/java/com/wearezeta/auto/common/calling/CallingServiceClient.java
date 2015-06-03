package com.wearezeta.auto.common.calling;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.calling.models.CallingServiceStatus;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingServiceClient {

	// all timeout constants are in milliseconds
	private static final long POLLING_FREQUENCY_MILLISECONDS = 2000;

	public static String callToUser(ClientUser userAs, ClientUser userTo,
			CallingServiceBackend callBackend) throws Exception {
		return CallingServiceREST.makeCall(
				userAs.getEmail(),
				userAs.getPassword(),
				BackendAPIWrappers.getConversationIdByName(userAs,
						userTo.getName()),
				CommonUtils.getBackendType(CallingServiceClient.class),
				callBackend).getString("id");
	}

	private static void waitForCallStatuses(String callId,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString(CallingServiceREST.getCallStatus(callId)
							.getString("status"));
			if (CallingServiceStatus.isSubSetContains(expectedStatuses,
					currentStatus)) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
		throw new TimeoutException(
				String.format(
						"Timeout occured while waiting until outgoing call '%s' changes its status to '%s' after %s second(s)",
						callId, Arrays.toString(expectedStatuses),
						secondsTimeout));
	}

	public static String callToUser(ClientUser userAs, ClientUser userTo,
			CallingServiceBackend callBackend,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		final String callId = callToUser(userAs, userTo, callBackend);
		waitForCallStatuses(callId, expectedStatuses, secondsTimeout);
		return callId;
	}

	public static CallingServiceStatus getCallStatus(String instanceId)
			throws Exception {
		return CallingServiceStatus.fromString(CallingServiceREST.getCallStatus(
				instanceId).getString("status"));
	}

	public static void stopCall(String callId) throws Exception {
		CallingServiceREST.stopCall(callId);
	}

	public static void stopCall(String callId,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		stopCall(callId);
		waitForCallStatuses(callId, expectedStatuses, secondsTimeout);
	}

	// TODO: mute/unmute

	public static String startWaitingInstance(ClientUser userAs,
			CallingServiceBackend callBackend) throws Exception {
		return CallingServiceREST.makeWaitingInstance(userAs.getEmail(),
				userAs.getPassword(),
				CommonUtils.getBackendType(CallingServiceClient.class),
				callBackend).getString("id");
	}

	private static void waitForInstanceStatuses(String instanceId,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString(CallingServiceREST.getWaitingInstanceStatus(
							instanceId).getString("status"));
			if (CallingServiceStatus.isSubSetContains(expectedStatuses,
					currentStatus)) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
		throw new TimeoutException(
				String.format(
						"Timeout occured while waiting until incoming call '%s' changes its status to '%s' after %s second(s) ",
						instanceId, Arrays.toString(expectedStatuses),
						secondsTimeout));
	}

	public static String startWaitingInstance(ClientUser userAs,
			CallingServiceBackend callBackend,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		final String instanceId = startWaitingInstance(userAs, callBackend);
		waitForInstanceStatuses(instanceId, expectedStatuses, secondsTimeout);
		return instanceId;
	}

	public static void acceptNextIncomingCall(String instanceId)
			throws Exception {
		CallingServiceREST.acceptNextIncomingCall(instanceId);
	}

	public static void stopWaitingInstance(String instanceId) throws Exception {
		CallingServiceREST.stopWaitingInstance(instanceId);
	}

	public static void stopWaitingInstance(String instanceId,
			CallingServiceStatus[] expectedStatuses, int secondsTimeout)
			throws Exception {
		stopWaitingInstance(instanceId);
		waitForInstanceStatuses(instanceId, expectedStatuses, secondsTimeout);
	}

	public static CallingServiceStatus getWaitingInstanceStatus(
			String instanceId) throws Exception {
		return CallingServiceStatus.fromString(CallingServiceREST
				.getWaitingInstanceStatus(instanceId).getString("status"));
	}

	// TODO: mute/unmute
}
