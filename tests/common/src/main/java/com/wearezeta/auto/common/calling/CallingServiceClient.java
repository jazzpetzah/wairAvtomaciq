package com.wearezeta.auto.common.calling;

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
		return CallingSericeREST.makeCall(
				userAs.getEmail(),
				userAs.getPassword(),
				BackendAPIWrappers.getConversationIdByName(userAs,
						userTo.getName()),
				CommonUtils.getBackendType(CallingServiceClient.class),
				callBackend).getString("id");
	}

	private static void waitForCallStatus(String callId,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString((String) CallingSericeREST
							.getCallStatus(callId).get("status"));
			if (currentStatus == expectedStatus) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
		throw new TimeoutException(
				String.format(
						"Timeout occured while waiting until outgoing call '%s' changes its status to '%s' within %s second(s) timeout",
						callId, expectedStatus, secondsTimeout));
	}

	public static String callToUser(ClientUser userAs, ClientUser userTo,
			CallingServiceBackend callBackend,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final String callId = callToUser(userAs, userTo, callBackend);
		waitForCallStatus(callId, expectedStatus, secondsTimeout);
		return callId;
	}

	public static void stopCall(String callId) throws Exception {
		CallingSericeREST.stopCall(callId);
	}

	public static void stopCall(String callId, int secondsTimeout)
			throws Exception {
		stopCall(callId);
		waitForCallStatus(callId, CallingServiceStatus.Inactive, secondsTimeout);
	}

	// TODO: mute/unmute

	private static void waitForInstanceStatus(String instanceId,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString((String) CallingSericeREST
							.getWaitingInstanceStatus(instanceId).get("status"));
			if (currentStatus == expectedStatus) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
		throw new TimeoutException(
				String.format(
						"Timeout occured while waiting until incoming call '%s' changes its status to '%s' within %s second(s) timeout",
						instanceId, expectedStatus, secondsTimeout));
	}

	public static String waitForCall(ClientUser userAs,
			CallingServiceBackend callBackend) throws Exception {
		return CallingSericeREST.makeWaitingInstance(userAs.getEmail(),
				userAs.getPassword(),
				CommonUtils.getBackendType(CallingServiceClient.class),
				callBackend).getString("id");
	}

	public static String waitForCall(ClientUser userAs,
			CallingServiceBackend callBackend,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final String instanceId = waitForCall(userAs, callBackend);
		waitForInstanceStatus(instanceId, expectedStatus, secondsTimeout);
		return instanceId;
	}

	public static void stopWaitingInstance(String instanceId) throws Exception {
		CallingSericeREST.stopWaitingInstance(instanceId);
	}

	public static void stopWaitingInstance(String instanceId, int secondsTimeout)
			throws Exception {
		stopWaitingInstance(instanceId);
		waitForInstanceStatus(instanceId, CallingServiceStatus.Inactive,
				secondsTimeout);
	}

	// TODO: mute/unmute
}
