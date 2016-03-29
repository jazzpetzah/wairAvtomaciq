package com.wearezeta.auto.common.email.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.email.MessagingUtils;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IMAPSMailbox implements ISupportsMessagesPolling {
    private static final Logger log = ZetaLogger.getLog(IMAPSMailbox.class.getSimpleName());

    private ISupportsMessagesPolling mailboxHandler = null;

    public Future<String> getMessage(Map<String, String> expectedHeaders, int timeoutSeconds) throws Exception {
        return getMessage(expectedHeaders, timeoutSeconds, 0);
    }

    public Future<String> getMessage(Map<String, String> expectedHeaders,
                                     int timeoutSeconds, long rejectMessagesBeforeTimestamp) throws Exception {
        return mailboxHandler.getMessage(expectedHeaders, timeoutSeconds, rejectMessagesBeforeTimestamp);
    }

    private static Map<String, IMAPSMailbox> instances = new HashMap<>();

    public static synchronized IMAPSMailbox getInstance(String mboxUserEmail, String mboxPassword) throws Exception {
        final String mboxUserName = MessagingUtils.normalizeEmail(mboxUserEmail);
        if (!instances.containsKey(mboxUserName)) {
            final String defaultHandler = CommonUtils.getValueFromCommonConfig(IMAPSMailbox.class, "mailboxHandlerType");
            ISupportsMessagesPolling handler;
            final IMAPSMailbox mboxInstance = new IMAPSMailbox();
            switch (defaultHandler) {
                case "REST":
                    handler = new RESTMBoxClientWrapper(mboxUserName);
                    if (handler.isAlive()) {
                        break;
                    } else {
                        log.error("REST listener seems to be dead. Falling back to the default listener...");
                    }
                case "JavaX":
                    handler = new JavaXMBox(mboxUserName, mboxPassword);
                    if (handler.isAlive()) {
                        break;
                    } else {
                        throw new IllegalStateException("JavaX mbox listener is dead");
                    }
                default:
                    throw new IllegalArgumentException(String.format("Unknown default message box handler '%s' is set",
                            defaultHandler));
            }
            mboxInstance.mailboxHandler = handler;
            instances.put(mboxUserName, mboxInstance);
            log.debug(String.format("Successfully created %s singleton", IMAPSMailbox.class.getSimpleName()));
        }
        return instances.get(mboxUserName);
    }

    private IMAPSMailbox() throws Exception {
    }

    @Override
    public void waitUntilMessagesCountReaches(String deliveredTo,
                                              int expectedMsgsCount, int timeoutSeconds) throws Exception {
        this.mailboxHandler.waitUntilMessagesCountReaches(deliveredTo, expectedMsgsCount, timeoutSeconds);
    }

    @Override
    public boolean isAlive() {
        return true;
    }
}
