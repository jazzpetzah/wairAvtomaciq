package com.wearezeta.auto.android.common.logging;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public abstract class RegressionLoggingProfile extends LoggingProfile {
    @Override
    public Optional<List<String>> getExcludePatterns() {
        return Optional.of(Arrays.asList("/SELENDROID",
                "/ResourceType", "/AlarmManager",
                "/WifiStateMachine", "/SFPerfTracer",
                "/LockPatternUtilsCache"));
    }

    @Override
    public Optional<List<String>> getIncludePatterns() {
        return Optional.empty();
    }
}
