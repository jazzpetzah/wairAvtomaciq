package com.wearezeta.auto.common.calling2.v1;

import java.util.List;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceCallException;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceInstanceException;
import com.wearezeta.auto.common.calling2.v1.model.BackendType;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Instance;
import com.wearezeta.auto.common.calling2.v1.model.InstanceStatus;
import com.wearezeta.auto.common.calling2.v1.model.CallRequest;
import com.wearezeta.auto.common.calling2.v1.model.InstanceRequest;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.calling2.v1.model.VersionedInstanceType;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingServiceClient {

    private static final org.apache.log4j.Logger LOG = ZetaLogger
            .getLog(CallingServiceClient.class.getSimpleName());

    private static final String API_VERSION = "1";
    private static final boolean TRACE = false;
    private final InstanceResource INSTANCE_RESOURCE = new InstanceResource(
            getApiRoot(), API_VERSION, TRACE);

    private final CallResource CALL_RESOURCE = new CallResource(getApiRoot(),
            API_VERSION, TRACE);

    public Instance startInstance(ClientUser userAs,
                                  VersionedInstanceType instanceType, String name)
            throws CallingServiceInstanceException {
        InstanceRequest instanceRequest = new InstanceRequest(
                userAs.getEmail(), userAs.getPassword(), getBackendType(),
                instanceType, name);
        return INSTANCE_RESOURCE.createInstance(instanceRequest);
    }

    public Instance stopInstance(Instance instance)
            throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.destroyInstance(instance);
    }

    public InstanceStatus getInstanceStatus(Instance instance)
            throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.getInstance(instance).getStatus();
    }

    public Call acceptNextIncomingCall(Instance instance)
            throws CallingServiceCallException {
        CallRequest callRequest = new CallRequest();

        return CALL_RESOURCE.acceptNext(instance, callRequest);
    }

    public Call acceptNextIncomingVideoCall(Instance instance)
            throws CallingServiceCallException {
        CallRequest callRequest = new CallRequest();

        return CALL_RESOURCE.acceptNextVideo(instance, callRequest);
    }

    public Call callToUser(Instance instance, String convId)
            throws CallingServiceCallException {
        CallRequest callRequest = new CallRequest(convId);
        return CALL_RESOURCE.start(instance, callRequest);
    }

    public Call videoCallToUser(Instance instance, String convId)
            throws CallingServiceCallException {
        CallRequest callRequest = new CallRequest(convId);
        return CALL_RESOURCE.startVideo(instance, callRequest);
    }

    public Call getCall(Instance instance, Call call)
            throws CallingServiceCallException {
        return CALL_RESOURCE.getCall(instance, call);
    }

    public Call stopCall(Instance instance, Call call)
            throws CallingServiceCallException {
        return CALL_RESOURCE.stop(instance, call);
    }
    
    public Call declineCall(Instance instance, String convId)
            throws CallingServiceCallException {
        CallRequest callRequest = new CallRequest(convId);
        return CALL_RESOURCE.decline(instance, callRequest);
    }

    // TODO: mute/unmute/listen/speak
    private static BackendType getBackendType() {
        try {
            return BackendType.valueOf(System.getProperty("com.wire.calling.env", "staging").toUpperCase());
        } catch (Exception ex) {
            LOG.warn("Can't get backend type", ex);
            return BackendType.STAGING;
        }
    }

    private static String getApiRoot() {
        try {
            return CommonUtils
                    .getDefaultCallingServiceUrlFromConfig(CallResource.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Flow> getFlows(Instance instance)
            throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.getFlows(instance);
    }

    public String getLog(Instance instance)
            throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.getLog(instance);
    }

    public Call switchVideoOn(Instance instance, Call call)
            throws CallingServiceCallException {
        return CALL_RESOURCE.switchVideoOn(instance, call);
    }

    public Call switchVideoOff(Instance instance, Call call)
            throws CallingServiceCallException {
        return CALL_RESOURCE.switchVideoOff(instance, call);
    }

    public Call getCurrentCall(Instance instance) throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.getInstance(instance).getCurrentCall();
    }

    public String getScreenshot(Instance instance) throws CallingServiceInstanceException {
        return INSTANCE_RESOURCE.getInstance(instance).getScreenshot();
    }
}
