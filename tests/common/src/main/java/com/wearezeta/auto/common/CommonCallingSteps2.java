package com.wearezeta.auto.common;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceCallException;
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
import java.util.HashMap;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.management.InstanceNotFoundException;
import javax.xml.bind.DatatypeConverter;

public final class CommonCallingSteps2 {

    public static final Logger LOG = ZetaLogger
            .getLog(CommonCallingSteps2.class.getName());

    private static final String CALL_BACKEND_VERSION_SEPARATOR = ":";

    private static final String ZCALL_DEFAULT_VERSION = "2.5.14";
    private String zcallVersion = ZCALL_DEFAULT_VERSION;

    public String getZcallVersion() {
        return zcallVersion;
    }

    public void setZcallVersion(String zcallVersion) {
        this.zcallVersion = zcallVersion;
    }

    private static final String AUTOCALL_DEFAULT_VERSION = "2.5.14";
    private String autocallVersion = AUTOCALL_DEFAULT_VERSION;

    public String getAutocallVersion() {
        return autocallVersion;
    }

    public void setAutocallVersion(String autocallVersion) {
        this.autocallVersion = autocallVersion;
    }

    private static final String FIREFOX_DEFAULT_VERSION = "46.0.1";
    private static final String CHROME_DEFAULT_VERSION = "51.0.2704.106";

    // Request timeout of 180 secs is set by callingservice, we add additional
    // 10 seconds on the client side to actually get a timeout response to
    // recocgnize a failed instances creation for retry mechanisms
    private static final int INSTANCE_START_TIMEOUT_SECONDS = 190;
    private static final int INSTANCE_DESTROY_TIMEOUT_SECONDS = 30;
    private static final int INSTANCE_CREATION_RETRIES = 3;
    private static final long POLLING_FREQUENCY_MILLISECONDS = 1000;
    private static CommonCallingSteps2 singleton = null;

    private ClientUsersManager usrMgr;
    private final CallingServiceClient client;
    private final Map<String, Instance> instanceMapping;
    private final Map<String, Call> callMapping;

    public synchronized static CommonCallingSteps2 getInstance() {
        if (singleton == null) {
            singleton = new CommonCallingSteps2(ClientUsersManager.getInstance());
        }
        return singleton;
    }

    /**
     * We break the singleton pattern here and make the constructor public to have multiple instances of this class for parallel
     * test executions. This means this class is not suitable as singleton and it should be changed to a non-singleton class. In
     * order to stay downward compatible we chose to just change the constructor.
     *
     * @return
     */
    public CommonCallingSteps2(ClientUsersManager usrMgr) {
        this.callMapping = new ConcurrentHashMap<>();
        this.instanceMapping = new ConcurrentHashMap<>();
        this.client = new CallingServiceClient();
        this.usrMgr = usrMgr;
    }

    public static class CallNotFoundException extends Exception {

        private static final long serialVersionUID = -2260765997668002031L;

        public CallNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Calls to a given conversation with given users. Instances are NOT automatically created.
     * <p>
     *
     * @param callerNames List of caller names who call to a conversation
     * @param conversationName the name of the conversation to call
     * @throws Exception
     */
    public void callToConversation(List<String> callerNames, String conversationName) throws Exception {
        for (String callerName : callerNames) {
            final ClientUser callerUser = usrMgr
                    .findUserByNameOrNameAlias(callerName);
            final String convId = getConversationId(callerUser, conversationName);
            final Instance instance = getInstance(callerUser);
            final Call call = client.callToUser(instance, convId);
            addCall(call, callerUser, convId);
        }
    }

    /**
     * Start video calls to a given conversation with given users. Instances are NOT automatically created.
     * <p>
     *
     * @param callerNames list of caller names
     * @param conversationName the name of the conversation to call
     * @throws Exception
     */
    public void startVideoCallToConversation(List<String> callerNames, String conversationName) throws Exception {
        for (String callerName : callerNames) {
            final ClientUser callerUser = usrMgr
                    .findUserByNameOrNameAlias(callerName);
            final String convId = getConversationId(callerUser, conversationName);
            final Instance instance = getInstance(callerUser);
            final Call call = client.videoCallToUser(instance, convId);
            addCall(call, callerUser, convId);
        }
    }

    /**
     * Verifies the status of a call from a calling instance in a given conversation from the view of a given user.
     * <p>
     *
     * @param callerNames the names of the callers
     * @param conversationName the name of the conversation to check
     * @param expectedStatuses the expected status
     * @param secondsTimeout timeout for checking the status
     * @throws Exception
     * @see com.wearezeta.auto.common.calling2.v1.model.CallStatus
     */
    public void verifyCallingStatus(List<String> callerNames, String conversationName,
            String expectedStatuses, int secondsTimeout) throws Exception {
        for (String callerName : callerNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
            final String convId = getConversationId(userAs, conversationName);
            waitForExpectedCallStatuses(getInstance(userAs),
                    getOutgoingCall(userAs, convId),
                    callStatusesListToObject(expectedStatuses), secondsTimeout);
        }
    }

    /**
     * Verifies current call status for a waiting instance.
     * <p>
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
            waitForExpectedCallStatuses(getInstance(userAs),
                    getIncomingCall(userAs),
                    callStatusesListToObject(expectedStatuses), secondsTimeout);
        }
    }

    /**
     * Starts one or more calling instances in parallel. This instance can be later used to call or to wait for a call. For
     * accepting an incoming call with such an instance use the method {@code CommonCallingSteps2#acceptNextCall}.
     * <p>
     *
     * @param calleeNames list of callee names
     * @param instanceType the {@code InstanceType} to call with as String
     * @throws Exception
     */
    public void startInstances(List<String> calleeNames, String instanceType) throws Exception {
        LOG.debug("Creating instances for " + Arrays.toString(calleeNames.toArray()));
        createInstances(calleeNames, instanceType);
    }

    private void createInstances(final List<String> calleeNames, String instanceType) throws InterruptedException,
            ExecutionException, NoSuchUserException, TimeoutException {
        Map<String, CompletableFuture<Instance>> createTasks = new HashMap<>(calleeNames.size());
        for (String calleeName : calleeNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
            createTasks.put(calleeName, CompletableFuture.supplyAsync(() -> {
                try {
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            final Instance instance = client.startInstance(userAs, convertTypeStringToTypeObject(instanceType),
                                    ZetaFormatter.getScenario());
                            addInstance(instance, userAs);
                            return instance;
                        } catch (CallingServiceInstanceException ex) {
                            throw new IllegalStateException(String.format("Could not start instance for user '%s'", 
                                    userAs.getName()), ex);
                        }
                    }).get(POLLING_FREQUENCY_MILLISECONDS, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    throw new IllegalStateException(String.format("Could not start instance for user '%s'", userAs.getName()),
                            ex);
                }
            }));
        }
        CompletableFuture.allOf(createTasks.values().toArray(new CompletableFuture[createTasks.size()])).get(
                INSTANCE_START_TIMEOUT_SECONDS * 2, TimeUnit.SECONDS);
    }

    /**
     * Calling this method on a waiting instance will force the instance to accept the next incoming call
     * <p>
     *
     * @param calleeNames list of names of the callees
     * @throws Exception
     */
    public void acceptNextCall(List<String> calleeNames) throws Exception {
        for (String calleeName : calleeNames) {
            final ClientUser userAs = usrMgr
                    .findUserByNameOrNameAlias(calleeName);
            final Call call = client
                    .acceptNextIncomingCall(getInstance(userAs));
            addCall(call, userAs);
        }
    }

    /**
     * Calling this method on a waiting instance will force the instance to accept the next incoming video call
     * <p>
     *
     * @param calleeNames list of names of the callees
     * @throws Exception
     */
    public void acceptNextVideoCall(List<String> calleeNames) throws Exception {
        for (String calleeName : calleeNames) {
            final ClientUser userAs = usrMgr
                    .findUserByNameOrNameAlias(calleeName);
            final Call call = client
                    .acceptNextIncomingVideoCall(getInstance(userAs));
            addCall(call, userAs);
        }
    }

    /**
     * Stops a call of a waiting instance.
     * <p>
     *
     * @param calleeNames the names of the callees
     * @throws Exception
     */
    public void stopIncomingCall(List<String> calleeNames) throws Exception {
        for (String calleeName : calleeNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
            client.stopCall(getInstance(userAs),
                    getIncomingCall(userAs));
        }
    }

    /**
     * Stops a call to a given conversation.
     * <p>
     *
     * @param callerNames the name of the caller
     * @param conversationName the name of the conversation to stop call to
     * @throws Exception
     */
    public void stopOutgoingCall(List<String> callerNames, String conversationName)
            throws Exception {
        for (String callerName : callerNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
            final String convId = getConversationId(userAs, conversationName);
            client.stopCall(getInstance(userAs),
                    getOutgoingCall(userAs, convId));
        }
    }

    public List<Flow> getFlows(String callerName)
            throws CallingServiceInstanceException, NoSuchUserException,
            InstanceNotFoundException {
        ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
        LOG.info("Get flows for user " + userAs.getEmail());
        return client.getFlows(getInstance(userAs));
    }

    public List<Call> getOutgoingCall(List<String> callerNames, String conversationName) throws NoSuchUserException, Exception {
        final List<Call> calls = new ArrayList<>();
        for (String callerName : callerNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
            final String convId = getConversationId(userAs, conversationName);
            calls.add(client.getCall(getInstance(userAs), getOutgoingCall(userAs, convId)));
        }
        return calls;
    }

    public List<Call> getIncomingCall(List<String> calleeNames) throws NoSuchUserException, Exception {
        final List<Call> calls = new ArrayList<>();
        for (String callerName : calleeNames) {
            ClientUser userAs = usrMgr.findUserByNameOrNameAlias(callerName);
            calls.add(client.getCall(getInstance(userAs), getIncomingCall(userAs)));
        }
        return calls;
    }

    public BufferedImage getScreenshot(ClientUser userAs) throws InstanceNotFoundException, IOException {
        final Instance instance = getInstance(userAs);
        String dataUrl = instance.getScreenshot();
        byte[] imagedata = DatatypeConverter.parseBase64Binary(dataUrl);
        return ImageIO.read(new ByteArrayInputStream(imagedata));
    }

    /**
     * Stops and terminates all instances and calls in parallel.
     *
     * @throws Exception
     */
    public synchronized void cleanup() throws Exception {
        if (instanceMapping.size() > 0) {
            LOG.debug("Executing parallel cleanup of call instance leftovers...");
        }
        final String callingServiceUrl = CommonUtils.getDefaultCallingServiceUrlFromConfig(CommonCallingSteps2.class);
        Map<Instance, CompletableFuture<Instance>> destroyTasks = new HashMap<>(instanceMapping.size());
        for (Map.Entry<String, Instance> entry : instanceMapping.entrySet()) {
            final Instance instance = entry.getValue();
            final String url = callingServiceUrl + "/api/v1/instance/" + instance.getId() + "/";
            LOG.debug("---BROWSER LOG FOR INSTANCE:\n" + instance + "\n"
                    + "<a href="+url+"/log>"+instance.getId()+" LOGS</a>"+ "\n"
                    + "<a href="+url+"/screenshots>"+instance.getId()+" SCREENSHOTS</a>");
            
            destroyTasks.put(instance, CompletableFuture.supplyAsync(() -> {
                try {
                    return client.stopInstance(instance);
                } catch (CallingServiceInstanceException ex) {
                    LOG.warn(String.format("Could not properly shut down instance '%s'", instance.getId()), ex);
                    return null;
                }
            }));
        }
        CompletableFuture.allOf(destroyTasks.values().toArray(new CompletableFuture[destroyTasks.size()]))
                .get(INSTANCE_DESTROY_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        LOG.debug("Destroyed all calling instances - Clearing calling instance map");
        instanceMapping.clear();
    }

    private void waitForExpectedCallStatuses(Instance instance, Call call,
            List<CallStatus> expectedStatuses, int secondsTimeout)
            throws Exception {
        long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
            final CallStatus currentStatus = client.getCall(instance,
                    call).getStatus();
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
                LOG.warn(
                        "Please use DESTROYED or NON_EXISTENT instead of READY to check the state of a call! READY will be removed in future versions.");
            }
            result.add(CallStatus.valueOf(clearedStatus));
        }
        return result;
    }

    private void addCall(Call call, ClientUser from) {
        final String key = makeKey(from);
        callMapping.put(key, call);
        LOG.info("Added waiting call from " + from.getName() + " with key " + key);
    }

    private void addCall(Call call, ClientUser from, String conversationId) {
        final String key = makeKey(from, conversationId);
        callMapping.put(key, call);
        LOG.info("Added call  from " + from.getName() + " with conversation ID " + conversationId + " with key " + key);
    }

    private void addInstance(Instance instance, ClientUser from) {
        final String key = makeKey(from);
        instanceMapping.put(key, instance);
        LOG.info("Added instance for user " + from.getName() + " with key " + key);
    }

    private void removeInstance(ClientUser from) {
        final String key = makeKey(from);
        instanceMapping.remove(key);
        LOG.info("Removed instance for user " + from.getName() + " with key " + key);
    }

    private synchronized Instance getInstance(ClientUser userAs)
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

    private synchronized Call getCurrentCall(Instance instance) throws CallingServiceInstanceException {
        return client.getCurrentCall(instance);
    }

    private synchronized Call getIncomingCall(ClientUser callee)
            throws CallNotFoundException {
        final String callKey = makeKey(callee);
        if (callMapping.containsKey(callKey)) {
            return callMapping.get(callKey);
        } else {
            throw new CallNotFoundException(String.format(
                    "Please wait for a call as '%s' first", callee.getName()));
        }
    }

    private synchronized Call getOutgoingCall(
            ClientUser caller, String conversationId)
            throws CallNotFoundException {
        final String callKey = makeKey(caller, conversationId);
        if (callMapping.containsKey(callKey)) {
            return callMapping.get(callKey);
        } else {
            throw new CallNotFoundException(String.format(
                    "Please make a call from '%s' to conversation '%s' first",
                    caller.getName(), conversationId));
        }
    }

    private VersionedInstanceType convertTypeStringToTypeObject(
            String instanceType) {
        instanceType = instanceType.toLowerCase();
        if (instanceType.contains(CALL_BACKEND_VERSION_SEPARATOR)) {
            final String[] versionedType = instanceType.split(CALL_BACKEND_VERSION_SEPARATOR);
            final String type = versionedType[0];
            final String version = versionedType[1];
            if (type == null || version == null) {
                throw new IllegalArgumentException("Could not parse instance type and/or version");
            }
            return new VersionedInstanceType(type, version);
        } else {
            switch (instanceType) {
                case "chrome":
                    return new VersionedInstanceType(instanceType, CHROME_DEFAULT_VERSION);
                case "firefox":
                    return new VersionedInstanceType(instanceType, FIREFOX_DEFAULT_VERSION);
                case "autocall":
                    return new VersionedInstanceType(instanceType, getAutocallVersion());
                case "zcall":
                    return new VersionedInstanceType(instanceType, getZcallVersion());
                default:
                    throw new IllegalArgumentException("Could not parse instance type and/or version");
            }
        }
    }

    private String getConversationId(ClientUser conversationOwner, String conversationName)
            throws NoSuchUserException, Exception {
        String convId;
        try {
            // get conv id from pure conv name
            convId = BackendAPIWrappers.getConversationIdByName(conversationOwner, conversationName);
        } catch (Exception e) {
            // get conv id from username
            final ClientUser convUser = usrMgr.findUserByNameOrNameAlias(conversationName);
            convId = BackendAPIWrappers.getConversationIdByName(conversationOwner,
                    convUser.getName());
        }
        return convId;
    }

    public void switchVideoOn(List<String> calleeNames) throws NoSuchUserException, InstanceNotFoundException,
            CallingServiceCallException, CallingServiceInstanceException {
        for (String calleeName : calleeNames) {
            final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
            Instance instance = getInstance(userAs);
            client.switchVideoOn(instance, getCurrentCall(instance));
        }
    }

    public void switchVideoOff(List<String> calleeNames) throws NoSuchUserException, InstanceNotFoundException,
            CallingServiceCallException, CallingServiceInstanceException {
        for (String calleeName : calleeNames) {
            final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(calleeName);
            Instance instance = getInstance(userAs);
            client.switchVideoOff(instance, getCurrentCall(instance));
        }
    }

}
