package com.wearezeta.auto.common;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.calling2.v1.CallingServiceClient;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceInstanceException;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.CallStatus;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.calling2.v1.model.Instance;
import com.wearezeta.auto.common.calling2.v1.model.VersionedInstanceType;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.management.InstanceNotFoundException;

public final class CommonCallingSteps2 {

	public static final Logger LOG = ZetaLogger
		.getLog(CommonCallingSteps2.class.getName());

	private static final String CALL_BACKEND_VERSION_SEPARATOR = ":";
	private static final String ZCALL_DEFAULT_VERSION = "1.12";
	private static final String AUTOCALL_DEFAULT_VERSION = "1.12";
	private static final String FIREFOX_DEFAULT_VERSION = "43.0.2";
	private static final String CHROME_DEFAULT_VERSION = "47.0.2526.73";

	// Request timeout of 180 secs is set by callingservice, we add additional
	// 10 seconds on the client side to actually get a timeout response to
	// recocgnize a failed instances creation for retry mechanisms
	private static final int INSTANCE_START_TIMEOUT_SECONDS = 190;
	private static final int INSTANCE_CREATION_RETRIES = 3;
	private static final long POLLING_FREQUENCY_MILLISECONDS = 1000;
	private static CommonCallingSteps2 singleton = null;

	private final ExecutorService executor;
	private final ClientUsersManager usrMgr;
	private final CallingServiceClient client;
	private final Map<String, Instance> instanceMapping;
	private final Map<String, Call> callMapping;

	public synchronized static CommonCallingSteps2 getInstance() {
		if (singleton == null) {
			singleton = new CommonCallingSteps2();
		}
		return singleton;
	}

	private CommonCallingSteps2() {
		this.callMapping = new ConcurrentHashMap<>();
		this.instanceMapping = new ConcurrentHashMap<>();
		this.client = new CallingServiceClient();
		this.usrMgr = ClientUsersManager.getInstance();
		this.executor = Executors.newCachedThreadPool();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				executor.shutdown();
				try {
					executor.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException ex) {
					LOG.log(null, ex);
				}
				if (!executor.isTerminated()) {
					LOG.warn("Could not finish all async calling cleanup tasks! Forcing executor shutdown ...");
					executor.shutdownNow();
				}
			}
		});
	}

	public static class CallNotFoundException extends Exception {

		private static final long serialVersionUID = -2260765997668002031L;

		public CallNotFoundException(String message) {
			super(message);
		}
	}

	/**
	 * Calls to a given conversation with a given user.
	 * <p>
	 * Migration: UserXCallsToUserYUsingCallBackend(String userAsNameAlias,
	 * String conversationName, String instanceType)
	 *
	 * @param callerName the name of the caller
	 * @param conversationName the name of the conversation to call
	 * @param instanceType the {@code InstanceType} to call with as String
	 * @throws Exception
	 * @see com.wearezeta.auto.common.calling2.v1.model.InstanceType
	 */
	public void callToConversation(String callerName, String conversationName,
		String instanceType) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);

		final String convId = getConversationId(userAs, conversationName);

		Instance instance = client.startInstance(userAs,
			convertTypeStringToTypeObject(instanceType),
			ZetaFormatter.getScenario());
		addInstance(instance, userAs);

		final Call call = client.callToUser(instance, convId);
		addCall(call, userAs, convId);
	}

	/**
	 * Verifies the status of a call from a calling instance in a given
	 * conversation from the view of a given user.
	 * <p>
	 * Migration:UserXVerifesCallStatusToUserY(String userAsNameAlias,String
	 * conversationName, String expectedStatuses, int secondsTimeout)
	 *
	 * @param callerName the name of the caller
	 * @param conversationName the name of the conversation to check
	 * @param expectedStatuses the expected status
	 * @param secondsTimeout timeout for checking the status
	 * @throws Exception
	 * @see com.wearezeta.auto.common.calling2.v1.model.CallStatus
	 */
	public void verifyCallingStatus(String callerName, String conversationName,
		String expectedStatuses, int secondsTimeout) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
		final String convId = getConversationId(userAs, conversationName);
		waitForExpectedCallStatuses(getInstanceByParticipant(userAs),
			getCallByParticipantAndConversationId(userAs, convId),
			callStatusesListToObject(expectedStatuses), secondsTimeout);
	}

	/**
	 * Verifies current call status for a waiting instance.
	 * <p>
	 * Migration: UserXVerifesWaitingInstanceStatus(String userAsNameAlias,
	 * String expectedStatuses, int secondsTimeout)
	 *
	 * @param calleeNames list of the names of the callees
	 * @param expectedStatuses the expected status
	 * @param secondsTimeout timeout for checking the status
	 * @throws Exception
	 * @see com.wearezeta.auto.common.calling2.v1.model.CallStatus
	 */
	public void verifyAcceptingCallStatus(List<String> calleeNames,
		String expectedStatuses, int secondsTimeout) throws Exception {
		for (String calleeName : calleeNames) {
			final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(calleeName);
			waitForExpectedCallStatuses(getInstanceByParticipant(userAs),
				getWaitingCallByParticipant(userAs),
				callStatusesListToObject(expectedStatuses), secondsTimeout);
		}
	}

	/**
	 * Stops a call to a given conversation.
	 * <p>
	 * Migration: UserXStopsCallsToUserY(String userAsNameAlias, String
	 * conversationName)
	 *
	 * @param callerName the name of the caller
	 * @param conversationName the name of the conversation to stop call to
	 * @throws Exception
	 */
	public void stopCall(String callerName, String conversationName)
		throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
		final String convId = getConversationId(userAs, conversationName);
		client.stopCall(getInstanceByParticipant(userAs),
			getCallByParticipantAndConversationId(userAs, convId));
	}

	/**
	 * Starts a calling instance that can wait for an incoming call and accept
	 * it. For accepting an incoming call with such an instance use the method
	 * {@code CommonCallingSteps2#acceptNextCall}.
	 * <p>
	 * Migration: UserXStartsWaitingInstance(String userAsNameAlias, String
	 * instanceType)
	 *
	 * @param calleeName the name of the callee
	 * @param instanceType the {@code InstanceType} to call with as String
	 * @throws Exception
	 * @see com.wearezeta.auto.common.calling2.v1.model.InstanceType
	 * @see #acceptNextCall(java.lang.String)
	 */
	@Deprecated
	public void startWaitingInstance(String calleeName, String instanceType)
		throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);

		final Instance instance = client.startInstance(userAs,
			convertTypeStringToTypeObject(instanceType),
			ZetaFormatter.getScenario());
		addInstance(instance, userAs);
	}

	/**
	 * Starts one or more calling instances in parallel that can wait for an
	 * incoming call and accept it. For accepting an incoming call with such an
	 * instance use the method {@code CommonCallingSteps2#acceptNextCall}.
	 * <p>
	 * Migration: UserXStartsWaitingInstance(String userAsNameAlias, String
	 * instanceType)
	 *
	 * @param calleeNames list of callee names
	 * @param instanceType the {@code InstanceType} to call with as String
	 * @throws Exception
	 * @see com.wearezeta.auto.common.calling2.v1.model.InstanceType
	 * @see #acceptNextCall(java.lang.String)
	 */
	public void startWaitingInstances(List<String> calleeNames,
		String instanceType) throws Exception {
		int creationRetries = INSTANCE_CREATION_RETRIES;
		List<String> callees = new ArrayList<>(calleeNames);
		Collections.copy(callees, calleeNames);

		do {
			LOG.debug(creationRetries + " retries left");
			LOG.debug("Creating instances for "
				+ Arrays.toString(callees.toArray()));
			callees = createInstances(callees, instanceType);
			creationRetries--;
		} while (!callees.isEmpty() && creationRetries > 0);
	}

	private List<String> createInstances(List<String> calleeNames,
		String instanceType) throws InterruptedException,
		ExecutionException, NoSuchUserException {
		Map<String, CompletableFuture<Instance>> createTasks = new HashMap<>(
			calleeNames.size());
		for (String calleeName : calleeNames) {
			ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
			createTasks.put(calleeName, CompletableFuture.supplyAsync(() -> {
				try {
					final Instance instance = client.startInstance(userAs,
						convertTypeStringToTypeObject(instanceType),
						ZetaFormatter.getScenario());
					addInstance(instance, userAs);
					addInstance(instance, userAs);
					return instance;
				} catch (CallingServiceInstanceException ex) {
					LOG.error(String.format(
						"Could not start instance for user '%s'",
						userAs.getName()), ex);
					return null;
				}
			}));
		}
		try {
			CompletableFuture.allOf(
				createTasks.values().toArray(
					new CompletableFuture[createTasks.size()])).get(
					INSTANCE_START_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			return Collections.EMPTY_LIST;
		} catch (TimeoutException e) {
			LOG.error(String.format(
				"Could not start all waiting instances in '%d' seconds",
				INSTANCE_START_TIMEOUT_SECONDS), e);
			List<String> calleesForRetry = new ArrayList<>();
			Instance instance;
			for (Map.Entry<String, CompletableFuture<Instance>> taskEntries : createTasks
				.entrySet()) {
				instance = taskEntries.getValue().getNow(null);
				if (instance == null) {
					calleesForRetry.add(taskEntries.getKey());
				}
			}
			return calleesForRetry;
		}
	}

	/**
	 * Calling this method on a waiting instance will force the instance to
	 * accept the next incoming call
	 * <p>
	 * Migration: UserXAcceptsNextIncomingCallAutomatically(String
	 * userAsNameAlias)
	 *
	 * @param calleeNames list of names of the callees
	 * @throws Exception
	 */
	public void acceptNextCall(List<String> calleeNames) throws Exception {
		for (String calleeName : calleeNames) {
			final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(calleeName);
			final Call call = client
				.acceptNextIncomingCall(getInstanceByParticipant(userAs));
			addCall(call, userAs);
		}
	}

	/**
	 * Stops a call of a waiting instance.
	 * <p>
	 * Migration: UserXStopsIncomingCalls(String userAsNameAlias)
	 *
	 * @param calleeName the name of the callee
	 * @throws Exception
	 */
	public void stopWaitingCall(String calleeName) throws Exception {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
		client.stopCall(getInstanceByParticipant(userAs),
			getWaitingCallByParticipant(userAs));
	}

	/**
	 * Stops and terminates all instances and calls asynchronously.
	 *
	 * @throws Exception
	 */
	public synchronized void cleanup() throws Exception {
		if (instanceMapping.size() > 0) {
			LOG.debug("Executing asynchronous cleanup of call instance leftovers...");
		}
		final String callingServiceUrl = CommonUtils
			.getDefaultCallingServiceUrlFromConfig(CommonCallingSteps2.class);
		for (Map.Entry<String, Instance> entry : instanceMapping.entrySet()) {
			final Instance instance = entry.getValue();
			LOG.debug("---BROWSER LOG FOR INSTANCE:\n" + instance + "\n"
				+ callingServiceUrl + "/api/v1/instance/"
				+ instance.getId() + "/log");
			CompletableFuture.runAsync(() -> {
				try {
					client.stopInstance(instance);
				} catch (CallingServiceInstanceException ex) {
					LOG.warn(String.format(
						"Could not properly shut down instance '%s'",
						instance.getId()), ex);
				}
			}, executor);
		}
		instanceMapping.clear();
	}

	private void waitForExpectedCallStatuses(Instance instance, Call call,
		List<CallStatus> expectedStatuses, int secondsTimeout)
		throws Exception {
		long millisecondsStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
			final CallStatus currentStatus = client.getCallStatus(instance,
				call);
			if (expectedStatuses.contains(currentStatus)) {
				return;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		}
		throw new TimeoutException(
			String.format(
				"Call status for instance '%s' has not been changed to '%s' after %s second(s) timeout",
				instance.getId(), expectedStatuses, secondsTimeout));
	}

	private static String makeKey(ClientUser from) {
		return from.getEmail();
	}

	private static String makeKey(ClientUser from, String conversationId) {
		StringBuilder key = new StringBuilder(makeKey(from));
		key.append(":").append(conversationId);
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
				LOG.warn("Please use DESTROYED or NON_EXISTENT instead of READY to check the state of a call! READY will be removed in future versions.");
			}
			result.add(CallStatus.valueOf(clearedStatus));
		}
		return result;
	}

	private void addCall(Call call, ClientUser from) {
		final String key = makeKey(from);
		callMapping.put(key, call);
		LOG.info("Added waiting call from " + from.getName() + " with key "
			+ key);
	}

	private void addCall(Call call, ClientUser from, String conversationId) {
		final String key = makeKey(from, conversationId);
		callMapping.put(key, call);
		LOG.info("Added call  from " + from.getName()
			+ " with conversation ID " + conversationId + " with key "
			+ key);
	}

	private void addInstance(Instance instance, ClientUser from) {
		String key = makeKey(from);
		instanceMapping.put(key, instance);
	}

	private void removeInstance(ClientUser from) {
		String key = makeKey(from);
		instanceMapping.remove(key);
	}

	private synchronized Instance getInstanceByParticipant(ClientUser userAs)
		throws InstanceNotFoundException {
		final String key = makeKey(userAs);
		if (instanceMapping.containsKey(key)) {
			return instanceMapping.get(key);
		} else {
			throw new InstanceNotFoundException(String.format(
				"Please create an instance for user '%s' first",
				userAs.getName()));
		}
	}

	private synchronized Call getWaitingCallByParticipant(ClientUser userAs)
		throws CallNotFoundException {
		final String callKey = makeKey(userAs);
		if (callMapping.containsKey(callKey)) {
			return callMapping.get(callKey);
		} else {
			throw new CallNotFoundException(String.format(
				"Please wait for a call as '%s' first", userAs.getName()));
		}
	}

	private synchronized Call getCallByParticipantAndConversationId(
		ClientUser userAs, String conversationId)
		throws CallNotFoundException {
		final String callKey = makeKey(userAs, conversationId);
		if (callMapping.containsKey(callKey)) {
			return callMapping.get(callKey);
		} else {
			throw new CallNotFoundException(String.format(
				"Please make a call from '%s' to conversation '%s' first",
				userAs.getName(), conversationId));
		}
	}

	private VersionedInstanceType convertTypeStringToTypeObject(
		String instanceType) {
		instanceType = instanceType.toLowerCase();
		if (instanceType.contains(CALL_BACKEND_VERSION_SEPARATOR)) {
			final String[] versionedType = instanceType
				.split(CALL_BACKEND_VERSION_SEPARATOR);
			final String type = versionedType[0];
			final String version = versionedType[1];
			if (type == null || version == null) {
				throw new IllegalArgumentException(
					"Could not parse instance type and/or version");
			}
			return new VersionedInstanceType(type, version);
		} else {
			switch (instanceType) {
				case "chrome":
					return new VersionedInstanceType(instanceType,
						CHROME_DEFAULT_VERSION);
				case "firefox":
					return new VersionedInstanceType(instanceType,
						FIREFOX_DEFAULT_VERSION);
				case "autocall":
					return new VersionedInstanceType(instanceType,
						AUTOCALL_DEFAULT_VERSION);
				case "zcall":
					return new VersionedInstanceType(instanceType,
						ZCALL_DEFAULT_VERSION);
				default:
					throw new IllegalArgumentException(
						"Could not parse instance type and/or version");
			}
		}
	}

	private String getConversationId(ClientUser userAs, String name)
		throws NoSuchUserException, Exception {
		String convId;
		try {
			// get conv id from pure conv name
			convId = BackendAPIWrappers.getConversationIdByName(userAs, name);
		} catch (Exception e) {
			// get conv id from username
			final ClientUser convUser = usrMgr.findUserByNameOrNameAlias(name);
			convId = BackendAPIWrappers.getConversationIdByName(userAs,
				convUser.getName());
		}
		return convId;
	}

	public List<Flow> getFlows(String callerName)
		throws CallingServiceInstanceException, NoSuchUserException, InstanceNotFoundException {
		ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
		LOG.info("Get flows for user " + userAs.getEmail());
		return client.getFlows(getInstanceByParticipant(userAs));
	}
}
