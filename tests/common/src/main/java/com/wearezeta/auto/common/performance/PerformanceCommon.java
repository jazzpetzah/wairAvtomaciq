package com.wearezeta.auto.common.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wearezeta.auto.common.wire_actors.SEBridge;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public final class PerformanceCommon {
    private final Logger logger = ZetaLogger.getLog(PerformanceCommon.class.getSimpleName());

    public Logger getLogger() {
        return this.logger;
    }

    private static final int MIN_WAIT_SECONDS = 2;
    private static final int MAX_WAIT_SECONDS = 5;

    private final ClientUsersManager usersManager;
    private final SEBridge devicesManager;

    public PerformanceCommon(ClientUsersManager usersManager, SEBridge devicesManager) {
        this.usersManager = usersManager;
        this.devicesManager = devicesManager;
    }

    private ClientUsersManager getUsersManager() {
        return this.usersManager;
    }

    private SEBridge getDevicesManager() {
        return this.devicesManager;
    }

    public void sendMultipleMessagesIntoConversation(String convoName, int msgsCount) throws Exception {
        convoName = getUsersManager().replaceAliasesOccurences(convoName, ClientUsersManager.FindBy.NAME_ALIAS);
        final String convo_id = BackendAPIWrappers.getConversationIdByName(getUsersManager().getSelfUserOrThrowError(),
                convoName);
        final List<String> msgsToSend = new ArrayList<>();
        for (int i = 0; i < msgsCount; i++) {
            msgsToSend.add(CommonUtils.generateGUID());
        }
        BackendAPIWrappers.sendConversationMessagesOtr(getUsersManager().findUserByNameOrNameAlias(convoName),
                convo_id, msgsToSend, getDevicesManager());
    }

    public interface PerformanceLoop {
        void run() throws Exception;
    }

    private static final Random rand = new Random();

    public void runPerformanceLoop(PerformanceLoop loop, final int timeoutMinutes) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            loop.run();

            final int sleepDurationSeconds = (MIN_WAIT_SECONDS + rand.nextInt(MAX_WAIT_SECONDS - MIN_WAIT_SECONDS + 1));
            getLogger().debug(String.format("Sleeping %s seconds", sleepDurationSeconds));
            Thread.sleep(sleepDurationSeconds * 1000);
            final long secondsLeft = timeoutMinutes * 60 - (System.currentTimeMillis() - millisecondsStarted) / 1000;
            if (secondsLeft > 0) {
                getLogger().info(String.format("Approximately %s second(s) left till the end of the perf test",
                        secondsLeft));
            }
        } while (System.currentTimeMillis() - millisecondsStarted < timeoutMinutes * 60000);
    }
}
