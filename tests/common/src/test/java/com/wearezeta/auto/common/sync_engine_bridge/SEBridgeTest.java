package com.wearezeta.auto.common.sync_engine_bridge;

import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import scala.collection.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SEBridgeTest {

    private static final ClientUsersManager USER_MANAGER = ClientUsersManager.getInstance();

    private SEBridge sut;

    @Before
    public void setUp() throws Exception {
        sut = new SEBridge();
    }

    @After
    public void tearDown() throws Exception {
        sut.reset();
    }

    @Test
    public void testGetDeviceIds() throws Exception {
        System.out.println("getDeviceIds");
        ClientUser me = createSelfUser();
        List<String> result = sut.getDeviceIds(me);
        assertEquals(Collections.emptyList(), result);
        sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        result = sut.getDeviceIds(me);
        assertTrue(result.size() == 1);
    }

    @Test
    public void testGetDeviceId() throws Exception {
        System.out.println("getDeviceId");
        ClientUser me = createSelfUser();
        sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        String result = sut.getDeviceId(me, "d0");
        assertNotNull(result);
        assertNotEquals("", result);
    }

    @Test
    public void testGetDeviceFingerprint() throws Exception {
        System.out.println("getDeviceFingerprint");
        ClientUser me = createSelfUser();
        sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        String result = sut.getDeviceFingerprint(me, "d0");
        assertNotNull(result);
        assertNotEquals("", result);
    }

    @Test
    public void testAddRemoteDeviceToAccount() throws Exception {
        System.out.println("addRemoteDeviceToAccount");
        ClientUser me = createSelfUser();
        List<String> result = sut.getDeviceIds(me);
        assertEquals(Collections.emptyList(), result);
        sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        result = sut.getDeviceIds(me);
        assertTrue(result.size() == 1);
    }

    @Test
    public void testProcessIsCleanedUpAfterFailedLogin() throws Exception {
        System.out.println("testProcessIsCleanedUpAfterFailedLogin");
        
        // reducing the pool size to force process reuse
        int oldPoolSize = SEBridge.MAX_PROCESS_NUM;
        SEBridge.MAX_PROCESS_NUM = 1;
        ClientUser me = createSelfUser();
        List<String> result = sut.getDeviceIds(me);
        assertEquals(Collections.emptyList(), result);
        
        me.setPassword("lala"); // make the login fail
        try {
            sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        me.setPassword("aqa123456!");
        
        result = sut.getDeviceIds(me);
        assertTrue(result.isEmpty());
        
        // processes don't forget device names so we have to choose another one :(
        sut.addRemoteDeviceToAccount(me, "d1", "dl1");
        result = sut.getDeviceIds(me);
        assertTrue(result.size() == 1);
        SEBridge.MAX_PROCESS_NUM = oldPoolSize;
    }

    @Test
    public void testGetConversationMessages() throws Exception {
        System.out.println("getConversationMessages");
        String convName = "gc";
        String deviceName = "d0";
        ClientUser me = createSelfUser();
        ClientUser two = createSecondUser();
        ClientUser three = createThirdUser();
        ClientUser four = createFourthUser();
        connect(me, two);
        connect(me, three);
        connect(me, four);
        createGroupConversation(me, Arrays.asList(two, three, four), convName);

        sut.addRemoteDeviceToAccount(me, deviceName, "dl0");
        ActorMessage.MessageInfo[] result = sut.getConversationMessages(me, getConversationIdByName(me, convName), deviceName);
        assertTrue(1 == result.length);
        assertTrue("STARTED_USING_DEVICE".equals(result[0].tpe().name()));
        sut.sendConversationMessage(me, getConversationIdByName(me, convName), "hello", deviceName);
        result = sut.getConversationMessages(me, getConversationIdByName(me, convName), deviceName);
        assertTrue(result.length == 2);
        assertTrue("TEXT".equals(result[1].tpe().name()));

        System.out.println(result[0].toString());
        System.out.println(result[1].toString());
        Iterator<Object> productIterator = result[1].productIterator();
        for (Iterator iterator = productIterator; iterator.hasNext();) {
            Object next = iterator.next();
            System.out.println(next);
        }
        // TODO no clue how to get the content of the message :(
    }

    /**
     * This test takes a lot of time
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void shouldCreate20Devices() throws Exception {
        System.out.println("shouldCreate20Devices");
        String convName = "gc";
        ClientUser me = createSelfUser();
        ClientUser two = createSecondUser();
        ClientUser three = createThirdUser();
        ClientUser four = createFourthUser();
        connect(me, two);
        connect(me, three);
        connect(me, four);
        createGroupConversation(me, Arrays.asList(two, three, four), convName);
        List<String> result;

        sut.addRemoteDeviceToAccount(me, "d0", "dl0");
        result = sut.getDeviceIds(me);
        assertTrue(result.size() == 1);

        for (int i = 0; i < 7; i++) {
            sut.addRemoteDeviceToAccount(two, "d" + i, "dl" + i);
            sut.addRemoteDeviceToAccount(three, "d" + i, "dl" + i);
            if (i != 5 && i != 6) {
                sut.addRemoteDeviceToAccount(four, "d" + i, "dl" + i);
            }
        }

        sut.sendConversationMessage(me, getConversationIdByName(me, convName), "Hello", "d0");
    }

//    @Test
//    public void testSendConversationMessage_3args() throws Exception {}
//
//    @Test
//    public void testSendConversationMessage_4args() throws Exception {}
//
//    @Test
//    public void testSendImage() throws Exception {}
//
//    @Test
//    public void testSendPing() throws Exception {}
//
//    @Test
//    public void testClearConversation() throws Exception {}
//
//    @Test
//    public void testMuteConversation() throws Exception {}
//
//    @Test
//    public void testUnmuteConversation() throws Exception {}
//
//    @Test
//    public void testSendFile() throws Exception {}
//
//    @Test
//    public void testDeleteMessage() throws Exception {}
//
//    @Test
//    public void testReset() throws Exception {}
    private static ClientUser createSelfUser() throws Exception {
        USER_MANAGER.createUsersOnBackend(5, RegistrationStrategy
                .getRegistrationStrategyForPlatform(Platform.Web));
        ClientUser me = USER_MANAGER.findUserByNameOrNameAlias("user1Name");
        USER_MANAGER.setSelfUser(me);
        return me;
    }

    private static ClientUser createSecondUser() throws Exception {
        return USER_MANAGER.findUserByNameOrNameAlias("user2Name");
    }

    private static ClientUser createThirdUser() throws Exception {
        return USER_MANAGER.findUserByNameOrNameAlias("user3Name");
    }

    private static ClientUser createFourthUser() throws Exception {
        return USER_MANAGER.findUserByNameOrNameAlias("user4Name");
    }

    private static ClientUser createFifthsUser() throws Exception {
        return USER_MANAGER.findUserByNameOrNameAlias("user5Name");
    }

    private static void connect(ClientUser one, ClientUser two) throws Exception {
        BackendAPIWrappers.autoTestSendRequest(one, two);
        BackendAPIWrappers.autoTestAcceptAllRequest(two);
    }

    private static String getConversationIdByName(ClientUser me, String name) throws Exception {
        return BackendAPIWrappers.getConversationIdByName(me, name);
    }

    private static void createGroupConversation(ClientUser me, List<ClientUser> others, String name) throws Exception {
        BackendAPIWrappers.createGroupConversation(me, others, name);
    }

}
