package com.wearezeta.auto.common.testrail;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TestrailRESTWrapper {
    public static long getProjectId(String projectName) throws Exception {
        final JSONArray response = TestrailREST.getProjects();
        for (int i = 0; i < response.length(); i++) {
            if (response.getJSONObject(i).getString("name").equals(projectName)) {
                return response.getJSONObject(i).getLong("id");
            }
        }
        throw new IllegalArgumentException(String.format("Project '%s' cannot be found in Testrail",
                projectName));
    }

    public static long getTestPlanId(long projectId, String testPlanName) throws Exception {
        final JSONArray response = TestrailREST.getTestPlans(projectId);
        for (int i = 0; i < response.length(); i++) {
            if (response.getJSONObject(i).getString("name").equals(testPlanName)) {
                return response.getJSONObject(i).getLong("id");
            }
        }
        throw new IllegalArgumentException(String.format("Test plan '%s' cannot be found in Testrail",
                testPlanName));
    }

    private static boolean isConfigurationEqual(String expectedConfiguration, String actualConfiguration) {
        // Configuration name is comma-separated string
        final Set<String> normalizedExpectedConfig =
                Arrays.asList(expectedConfiguration.split(",")).stream().map(
                        String::trim).collect(Collectors.toSet());
        final Set<String> normalizedActualConfig =
                Arrays.asList(actualConfiguration.split(",")).stream().map(
                        String::trim).collect(Collectors.toSet());
        return normalizedExpectedConfig.equals(normalizedActualConfig);
    }

    public static long getTestRunId(long testPlanId, String testRunName, Optional<String> configurationName)
            throws Exception {
        final JSONObject response = TestrailREST.getTestPlan(testPlanId);
        if (!response.has("entries")) {
            throw new IllegalArgumentException(String.format("Test run '%s' cannot be found",
                    testRunName));
        }
        final JSONArray entries = response.getJSONArray("entries");
        for (int entryIdx = 0; entryIdx < entries.length(); entryIdx++) {
            if (entries.getJSONObject(entryIdx).has("runs")) {
                final JSONArray runs = entries.getJSONObject(entryIdx).getJSONArray("runs");
                for (int runIdx = 0; runIdx < runs.length(); runIdx++) {
                    if (runs.getJSONObject(runIdx).getString("name").equals(testRunName)) {
                        if (configurationName.isPresent()) {
                            if (runs.getJSONObject(runIdx).has("config") && isConfigurationEqual(
                                    configurationName.get(),
                                    runs.getJSONObject(runIdx).getString("config"))) {
                                return runs.getJSONObject(runIdx).getLong("id");
                            } else {
                                continue;
                            }
                        }
                        return runs.getJSONObject(runIdx).getLong("id");
                    }
                }
            }
        }
        throw new IllegalArgumentException(String.format("Test run '%s (%s)' cannot be found",
                testRunName, configurationName.orElse("<No Config>")));
    }

    /**
     * @param testRunId
     * @param caseId
     * @param newStatus this has to be never set to TestrailExecutionStatus.Untested
     *                  Otherwise API call will fail for sure
     * @param comment
     * @throws Exception
     */

    public static void updateTestResult(long testRunId, long caseId,
                                        TestrailExecutionStatus newStatus, Optional<String> comment)
            throws Exception {
        TestrailREST.addTestCaseResult(testRunId, caseId, newStatus.getId(), comment);
    }

    public static TestrailExecutionStatus getCurrentTestResult(long testRunId, long caseId)
            throws Exception {
        final JSONArray response = TestrailREST.getTestCaseResults(testRunId, caseId);
        if (response.length() == 0) {
            return TestrailExecutionStatus.Untested;
        } else {
            return TestrailExecutionStatus.getById(response.getJSONObject(0).getInt("status_id"));
        }
    }

    public static void updateCaseIsAutomated(long caseId, boolean newValue) throws Exception {
        final JSONObject requestBody = new JSONObject();
        requestBody.put("custom_is_automated", newValue);
        TestrailREST.updateCase(caseId, requestBody);
    }

    public static void updateCaseIsMuted(long caseId, boolean newValue) throws Exception {
        final JSONObject requestBody = new JSONObject();
        requestBody.put("custom_is_muted", newValue);
        TestrailREST.updateCase(caseId, requestBody);
    }
 }
