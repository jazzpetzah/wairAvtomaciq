package com.wearezeta.auto.common.zephyr;


import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email_notifier.NotificationSender;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.IRCTestcasesStorage;
import com.wearezeta.auto.common.rc.RCTestcase;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ZephyrSyncUtilities {
    private static final Logger log = ZetaLogger.getLog(ZephyrSyncUtilities.class
            .getSimpleName());

    private static Optional<IRCTestcasesStorage> storage = Optional.empty();
    private static Optional<ZephyrTestCycle> cycle = Optional.empty();

    private static synchronized void syncCurrentTestResultWithZephyr(Set<String> normalizedTags,
                                                                     ZephyrExecutionStatus actualTestResult)
            throws Exception {
        final ZephyrTestPhase phase = cycle.get().getPhaseByName(zephyrPhaseName.get());
        final List<ExecutedZephyrTestcase> rcTestCases = phase.getTestcases();
        boolean isAnyTestChanged = false;
        boolean isAnyTestFound = false;
        final List<String> actualIds = normalizedTags
                .stream()
                .filter(x -> x.startsWith(RCTestcase.ZEPHYR_ID_TAG_PREFIX)
                        && x.length() > RCTestcase.ZEPHYR_ID_TAG_PREFIX.length())
                .map(x -> x.substring(RCTestcase.ZEPHYR_ID_TAG_PREFIX.length(),
                        x.length())).collect(Collectors.toList());
        for (ExecutedZephyrTestcase rcTestCase : rcTestCases) {
            if (actualIds.contains(rcTestCase.getId())) {
                if (rcTestCase.getExecutionStatus() != actualTestResult) {
                    log.info(String
                            .format(" --> Changing execution result of RC test case #%s from '%s' to '%s' "
                                            + "(Cycle: '%s', Phase: '%s', Name: '%s')\n\n",
                                    rcTestCase.getId(), rcTestCase
                                            .getExecutionStatus().toString(),
                                    actualTestResult.toString(), cycle.get()
                                            .getName(), phase.getName(),
                                    rcTestCase.getName()));
                    rcTestCase.setExecutionStatus(actualTestResult);
                    if (jenkinsJobUrl.isPresent() && jenkinsJobUrl.get().length() > 0) {
                        rcTestCase.setExecutionComment(jenkinsJobUrl.get());
                    }
                    isAnyTestChanged = true;
                }
                isAnyTestFound = true;
            }
        }
        if (isAnyTestChanged) {
            ((ZephyrDB) storage.get()).syncPhaseResults(phase);
        } else {
            if (isAnyTestFound) {
                log.info(String
                        .format(" --> Execution result for RC test case(s) # %s has been already set "
                                        + "to '%s' and is still the same "
                                        + "(Cycle: '%s', Phase: '%s')\n\n", actualIds,
                                actualTestResult.toString(), cycle.get().getName(),
                                phase.getName()));
            } else {
                final String warningMessage = String
                        .format("It seems like there is no test case(s) # %s in Zephyr cycle '%s', phase '%s'. "
                                        + "This could slow down the whole RC run. "
                                        + "Please double check .feature files whether the %s tag is properly set!",
                                actualIds, cycle.get().getName(), phase.getName(),
                                RCTestcase.RC_TAG);
                log.warn(" --> " + warningMessage + "\n\n");
                final Optional<String> rcNotificationsRecepients = CommonUtils
                        .getRCNotificationsRecepients(ZephyrSyncUtilities.class);
                if (rcNotificationsRecepients.isPresent()) {
                    final String notificationHeader = String
                            .format("ACHTUNG! An extra RC test case has been executed in RC test cycle '%s', phase '%s'",
                                    cycle.get().getName(), phase.getName());
                    NotificationSender.getInstance().send(
                            rcNotificationsRecepients.get(),
                            notificationHeader, warningMessage);
                }
            }
        }
    }

    private static Optional<String> zephyrCycleName = Optional.empty();
    private static Optional<String> zephyrPhaseName = Optional.empty();
    static {
        try {
            zephyrCycleName = CommonUtils.getZephyrCycleNameFromConfig(ZephyrSyncUtilities.class);
            zephyrPhaseName = CommonUtils.getZephyrPhaseNameFromConfig(ZephyrSyncUtilities.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<String> jenkinsJobUrl = Optional.empty();
    static {
        try {
            jenkinsJobUrl = CommonUtils.getOptionalValueFromCommonConfig(ZephyrSyncUtilities.class,
                    "jenkinsJobUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncExecutedScenarioWithZephyr(ZephyrExecutionStatus actualTestResult,
                                                      Set<String> normalizedTags) {
        final boolean isZephyrRCRun = zephyrCycleName.isPresent() && zephyrCycleName.get().length() > 0
                && zephyrPhaseName.isPresent() && zephyrPhaseName.get().length() > 0;

        if (isZephyrRCRun && (!storage.isPresent() || !cycle.isPresent())) {
            try {
                storage = Optional.of(new ZephyrDB(
                        CommonUtils.getZephyrServerFromConfig(ZephyrSyncUtilities.class)));
                cycle = Optional.of(((ZephyrDB) storage.get()).getTestCycle(zephyrCycleName.get()));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        if (isZephyrRCRun) {
            // Commented out due to the request from WebApp team
            // if (normalizedTags.contains(RCTestcase.RC_TAG)) {
            try {
                syncCurrentTestResultWithZephyr(normalizedTags, actualTestResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
