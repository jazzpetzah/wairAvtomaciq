package com.wearezeta.auto.common.backend;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.List;

public class RemoteProcessIPC {

    private static DevicePool devicePool;

    public static void startDevices(int numDevices) throws Exception {
        devicePool = new DevicePool(numDevices);
    }

    public static void loginToSingleRemoteProcess(ClientUser user) throws Exception {
        List<Device> loggedInDevices = devicePool.getDevicesWithLoggedInUser(user);
        if (loggedInDevices.size() == 0) {
            devicePool.getNextFreeDevice().logInWithUser(user);
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
//        System.out.println("Sending message: " + message + " from user: " + userFrom + " to conversation: " + convId);
//        ActorRef remote = registeredDevices.get(userFrom.getName());
//
//        Future<Object> future = Patterns.ask(remote, new ActorMessage.SendText(new RConvId(convId), message), duration.toMillis());
//        Object resp = Await.result(future, duration);
//
//        System.out.println(resp.getClass());
    }
}