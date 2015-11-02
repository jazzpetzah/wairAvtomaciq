package com.wearezeta.auto.android.common.logging;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class RegressionPassedLoggingProfile extends RegressionLoggingProfile {
    @Override
    public Optional<List<String>> getIncludePatterns() {
        return Optional.of(Arrays.asList(new String[]{" E/"}));
    }
}
