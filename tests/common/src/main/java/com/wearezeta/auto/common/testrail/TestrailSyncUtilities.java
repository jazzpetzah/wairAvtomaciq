package com.wearezeta.auto.common.testrail;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email_notifier.NotificationSender;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.RCTestcase;

import gherkin.formatter.model.Scenario;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TestrailSyncUtilities {
    private static final Logger log = ZetaLogger.getLog(TestrailSyncUtilities.class.getSimpleName());

    private static Optional<String> testrailProjectName = Optional.empty();
    private static Optional<String> testrailPlanName = Optional.empty();
    private static Optional<String> testrailRunName = Optional.empty();
    private static Optional<String> testrailRunConfigName = Optional.empty();

    static {
        try {
            testrailProjectName = CommonUtils.getTestrailProjectNameFromConfig(TestrailSyncUtilities.class);
            testrailPlanName = CommonUtils.getTestrailPlanNameFromConfig(TestrailSyncUtilities.class);
            testrailRunName = CommonUtils.getTestrailRunNameFromConfig(TestrailSyncUtilities.class);
            testrailRunConfigName = CommonUtils.getTestrailRunConfigNameFromConfig(TestrailSyncUtilities.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<String> rcTestsComment = Optional.empty();

    static {
        try {
            final Optional<String> rcTestsCommentPath =
                    CommonUtils.getRcTestsCommentPathFromCommonConfig(TestrailSyncUtilities.class);
            if (rcTestsCommentPath.isPresent()) {
                if (new File(rcTestsCommentPath.get()).exists()) {
                    rcTestsComment = Optional.of(new String(Files.readAllBytes(Paths.get(rcTestsCommentPath.get())),
                            Charset.forName("UTF-8")));
                } else {
                    log.error(String.format("Please make sure the file %s exists and is accessible",
                            rcTestsCommentPath.get()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isTestrailMutedSyncEnabled = false;

    static {
        try {
            isTestrailMutedSyncEnabled = CommonUtils.getSyncIsMuted(TestrailSyncUtilities.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isTestrailAutomatedSyncEnabled = false;

    static {
        try {
            isTestrailAutomatedSyncEnabled = CommonUtils.getSyncIsAutomated(TestrailSyncUtilities.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<String> rcNotificationsRecepients = Optional.empty();

    static {
        try {
            rcNotificationsRecepients = CommonUtils.getRCNotificationsRecepients(TestrailSyncUtilities.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<Long> testrailRunId = Optional.empty();

    private static void sendEmailNotification(String subject, String msg) {
        if (rcNotificationsRecepients.isPresent()) {
            try {
                NotificationSender.getInstance().send(
                        rcNotificationsRecepients.get(),
                        subject, msg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String IS_MUTED_PROPERTY = "custom_is_muted";

    private static void syncTestrailIsMutedState(String scenarioName, Set<String> normalizedTags,
                                                 TestrailExecutionStatus actualTestResult) {
        final List<String> actualIds = normalizedTags
                .stream()
                .filter(x -> x.startsWith(RCTestcase.TESTRAIL_ID_TAG_PREFIX)
                        && x.length() > RCTestcase.TESTRAIL_ID_TAG_PREFIX.length())
                .map(x -> x.substring(RCTestcase.TESTRAIL_ID_TAG_PREFIX.length(),
                        x.length())).collect(Collectors.toList());
        if (actualIds.isEmpty()) {
            final String warningMessage = String.format(
                    "Cannot change IsMuted state for the test case '%s' (tags: '%s'). " +
                            "No Testrail ids can be parsed.",
                    scenarioName, normalizedTags);
            log.warn(warningMessage);
            sendEmailNotification(String.format("No Testrail ids can be parsed from scenario '%s', (tags '%s')",
                    scenarioName, normalizedTags), warningMessage);
            return;
        }
        for (String caseId : actualIds) {
            switch (actualTestResult) {
                case Passed:
                    try {
                        log.info(String.format("Setting IsMuted property of test case #%s to FALSE",
                                caseId));
                        TestrailRESTWrapper.updateCustomCaseProperty(Long.parseLong(caseId), IS_MUTED_PROPERTY, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Failed:
                    try {
                        log.info(String.format("Setting IsMuted property of test case #%s to TRUE",
                                caseId));
                        TestrailRESTWrapper.updateCustomCaseProperty(Long.parseLong(caseId), IS_MUTED_PROPERTY, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private static final String STAGING_TAG = "@staging";
    private static final String IS_AUTOMATED_PROPERTY = "custom_is_automated";
    private static final String IS_STAGING_PROPERTY = "custom_is_staging";

    private static void syncTestrailIsAutomatedState(String scenarioName, Set<String> normalizedTags) {
        final List<String> actualIds = normalizedTags
                .stream()
                .filter(x -> x.startsWith(RCTestcase.TESTRAIL_ID_TAG_PREFIX)
                        && x.length() > RCTestcase.TESTRAIL_ID_TAG_PREFIX.length())
                .map(x -> x.substring(RCTestcase.TESTRAIL_ID_TAG_PREFIX.length(),
                        x.length())).collect(Collectors.toList());
        if (actualIds.isEmpty()) {
            final String warningMessage = String.format(
                    "Cannot change IsAutomated state for the test case '%s' (tags: '%s'). " +
                            "No Testrail ids can be parsed.",
                    scenarioName, normalizedTags);
            log.warn(warningMessage);
            sendEmailNotification(String
                    .format("No Testrail ids can be parsed from scenario '%s', (tags '%s')",
                            scenarioName, normalizedTags), warningMessage);
            return;
        }
        for (String caseId : actualIds) {
            log.info(String.format("Setting IsAutomated property of test case #%s to TRUE",
                    caseId));
            try {
                final Map<String, Object> props = new HashMap<>();
                if (normalizedTags.contains(STAGING_TAG)) {
                    props.put(IS_AUTOMATED_PROPERTY, false);
                    props.put(IS_STAGING_PROPERTY, true);
                } else {
                    props.put(IS_AUTOMATED_PROPERTY, true);
                    props.put(IS_STAGING_PROPERTY, false);
                }
                TestrailRESTWrapper.updateCustomCaseProperties(Long.parseLong(caseId), props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void syncCurrentTestResultWithTestrail(TestrailExecutionStatus actualTestResult,
                                                          Set<String> normalizedTags) throws Exception {
        final List<String> actualIds = normalizedTags
                .stream()
                .filter(x -> x.startsWith(RCTestcase.TESTRAIL_ID_TAG_PREFIX)
                        && x.length() > RCTestcase.TESTRAIL_ID_TAG_PREFIX.length())
                .map(x -> x.substring(RCTestcase.TESTRAIL_ID_TAG_PREFIX.length(),
                        x.length())).collect(Collectors.toList());
        if (actualIds.isEmpty()) {
            final String warningMessage = String.format("Cannot change execution status for a test case (tags: '%s'). " +
                    "No Testrail ids can be parsed.", normalizedTags);
            log.warn(warningMessage);
            final String notificationHeader = String
                    .format("ACHTUNG! An unknown RC test case has been executed in "
                                    + "project '%s', test plan '%s', run '%s (%s)'",
                            testrailProjectName.get(),
                            testrailPlanName.get(), testrailRunName.get(),
                            testrailRunConfigName.orElse("<No Config>"));
            sendEmailNotification(notificationHeader, warningMessage);
            return;
        }
        for (String tcId : actualIds) {
            TestrailExecutionStatus previousTestResult = TestrailExecutionStatus.Untested;
            try {
                previousTestResult =
                        TestrailRESTWrapper.getCurrentTestResult(testrailRunId.get(), Long.parseLong(tcId));
            } catch (TestrailRequestException e) {
                if (e.getReturnCode() == 400) {
                    // No such test case error
                    final String warningMessage = String
                            .format("It seems like there is no test case(s) # %s in "
                                            + "Testrail project '%s', plan '%s', run '%s (%s)'. "
                                            + "This could slow down the whole RC run. "
                                            + "Please double check .feature files whether the %s tag is properly set!",
                                    actualIds, testrailProjectName.get(),
                                    testrailPlanName.get(), testrailRunName.get(),
                                    testrailRunConfigName.orElse("<No Config>"),
                                    RCTestcase.RC_TAG);
                    log.warn(" --> " + warningMessage + "\n\n");
                    if (rcNotificationsRecepients.isPresent()) {
                        final String notificationHeader = String
                                .format("ACHTUNG! An extra RC test case has been executed in "
                                                + "project '%s', test plan '%s', run '%s (%s)'",
                                        testrailProjectName.get(),
                                        testrailPlanName.get(), testrailRunName.get(),
                                        testrailRunConfigName.orElse("<No Config>"));
                        NotificationSender.getInstance().send(
                                rcNotificationsRecepients.get(),
                                notificationHeader, warningMessage);
                    }
                } else {
                    throw e;
                }
            }
            log.info(String
                    .format(" --> Adding execution result '%s' to RC test case #%s (previous result was '%s'). "
                                    + "Project Name: '%s', Plan Name: '%s', Run Name: '%s (%s)')\n\n",
                            actualTestResult.toString(), tcId, previousTestResult.toString(),
                            testrailProjectName.get(),
                            testrailPlanName.get(), testrailRunName.get(),
                            testrailRunConfigName.orElse("<No Config>")));
            TestrailRESTWrapper.updateTestResult(testrailRunId.get(), Long.parseLong(tcId),
                    actualTestResult, rcTestsComment);
        }
    }

    public static void syncExecutedScenarioWithTestrail(Scenario scenario, TestrailExecutionStatus actualTestResult,
                                                        Set<String> normalizedTags) {
        syncExecutedScenarioWithTestrail(scenario.getName(), actualTestResult, normalizedTags);
    }
    
    public static void syncExecutedScenarioWithTestrail(String scenarioName, TestrailExecutionStatus actualTestResult,
                                                        Set<String> normalizedTags) {
        final boolean isTestrailRCRun = testrailProjectName.isPresent() && testrailProjectName.get().length() > 0
                && testrailPlanName.isPresent() && testrailPlanName.get().length() > 0
                && testrailRunName.isPresent() && testrailRunName.get().length() > 0;

        if (isTestrailRCRun && !testrailRunId.isPresent()) {
            try {
                final long projectId = TestrailRESTWrapper.getProjectId(testrailProjectName.get());
                final long planId = TestrailRESTWrapper.getTestPlanId(projectId, testrailPlanName.get());
                testrailRunId = Optional.of(TestrailRESTWrapper.getTestRunId(planId, testrailRunName.get(),
                        testrailRunConfigName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isTestrailRCRun && testrailRunId.isPresent()) {
            try {
                syncCurrentTestResultWithTestrail(actualTestResult, normalizedTags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isTestrailMutedSyncEnabled) {
            syncTestrailIsMutedState(scenarioName, normalizedTags,
                    actualTestResult);
        }
        if (isTestrailAutomatedSyncEnabled) {
            syncTestrailIsAutomatedState(scenarioName, normalizedTags);
        }
    }
}
