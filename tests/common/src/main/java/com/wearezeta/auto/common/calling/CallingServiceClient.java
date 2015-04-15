package com.wearezeta.auto.common.calling;

import java.util.concurrent.TimeoutException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.calling.models.CallingServiceStatus;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingServiceClient {

	private static final String URL_PROTOCOL = "http://";

	// all timeout constants are in milliseconds
	private static final long POLLING_FREQUENCY_MILLISECONDS = 2000;

	private CallingSericeREST callingApi;

	public CallingServiceClient(String host, String port) {
		this.callingApi = new CallingSericeREST(String.format("%s%s:%s",
				URL_PROTOCOL, host, port));
	}

	public String callToUser(ClientUser userAs, ClientUser userTo,
			CallingServiceBackend callBackend) throws Exception {
		return this.callingApi.makeCall(
				userAs.getEmail(),
				userAs.getPassword(),
				BackendAPIWrappers.getConversationIdByName(userAs,
						userTo.getName()),
				CommonUtils.getBackendType(getClass()), callBackend).getString(
				"id");
	}

	private void waitForCallStatus(String callId,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString((String) this.callingApi.getCallStatus(callId)
							.get("status"));
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

	public String callToUser(ClientUser userAs, ClientUser userTo,
			CallingServiceBackend callBackend,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final String callId = this.callToUser(userAs, userTo, callBackend);
		waitForCallStatus(callId, expectedStatus, secondsTimeout);
		return callId;
	}

	public void stopCall(String callId) throws Exception {
		this.callingApi.stopCall(callId);
	}

	public void stopCall(String callId, int secondsTimeout) throws Exception {
		this.callingApi.stopCall(callId);
		waitForCallStatus(callId, CallingServiceStatus.Inactive, secondsTimeout);
	}

	// TODO: mute/unmute

	private void waitForInstanceStatus(String instanceId,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final CallingServiceStatus currentStatus = CallingServiceStatus
					.fromString((String) this.callingApi
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

	public String waitForCall(ClientUser userAs,
			CallingServiceBackend callBackend) throws Exception {
		return this.callingApi.makeWaitingInstance(userAs.getEmail(),
				userAs.getPassword(), CommonUtils.getBackendType(getClass()),
				callBackend).getString("id");
	}

	public String waitForCall(ClientUser userAs,
			CallingServiceBackend callBackend,
			CallingServiceStatus expectedStatus, int secondsTimeout)
			throws Exception {
		final String instanceId = this.waitForCall(userAs, callBackend);
		waitForInstanceStatus(instanceId, expectedStatus, secondsTimeout);
		return instanceId;
	}

	public void stopWaitingInstance(String instanceId) throws Exception {
		this.callingApi.stopWaitingInstance(instanceId);
	}

	public void stopWaitingInstance(String instanceId, int secondsTimeout)
			throws Exception {
		this.callingApi.stopCall(instanceId);
		waitForCallStatus(instanceId, CallingServiceStatus.Inactive,
				secondsTimeout);
	}

	// TODO: mute/unmute
}
