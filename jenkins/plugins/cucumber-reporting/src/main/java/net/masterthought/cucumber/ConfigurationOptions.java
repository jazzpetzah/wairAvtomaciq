package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Artifact;

import java.io.File;
import java.util.Map;

public class ConfigurationOptions {

    public static boolean skippedFailsBuildValue;
    public static boolean allSkippedFailsBuildValue;
    public static boolean pendingFailsBuildValue;
    public static boolean undefinedFailsBuildValue;
    public static boolean artifactsEnabledValue;
    public static Map<String, Artifact> artifactConfiguration;

    private ConfigurationOptions() {
        throw new AssertionError();
    }

    public static void setSkippedFailsBuild(boolean skippedFailsBuild) {
        skippedFailsBuildValue = skippedFailsBuild;
    }
    public static void setAllSkippedFailsBuild(boolean allSkippedFailsBuild) {
        allSkippedFailsBuildValue = allSkippedFailsBuild;
    }
    public static void setPendingFailsBuild(boolean pendingFailsBuild) { pendingFailsBuildValue = pendingFailsBuild; }
    public static void setUndefinedFailsBuild(boolean undefinedFailsBuild) { undefinedFailsBuildValue = undefinedFailsBuild; }

    public static void setArtifactsEnabled(boolean artifactsEnabled) {
        artifactsEnabledValue = artifactsEnabled;
    }

    public static void setArtifactConfiguration(Map<String, Artifact> configuration) {
        artifactConfiguration = configuration;
    }

    public static boolean skippedFailsBuild() {
        return skippedFailsBuildValue;
    }
    public static boolean allSkippedFailsBuild() { return allSkippedFailsBuildValue; }

    public static boolean undefinedFailsBuild() {
        return undefinedFailsBuildValue;
    }

    public static boolean pendingFailsBuild() { return pendingFailsBuildValue; }

    public static boolean artifactsEnabled() {
        return artifactsEnabledValue;
    }

    public static Map<String, Artifact> artifactConfig() {
        return artifactConfiguration;
    }
}
