package com.wearezeta.auto.android.common.logging;

import java.util.List;
import java.util.Optional;

public abstract class LoggingProfile {
    public abstract Optional<List<String>> getExcludePatterns();
    public abstract Optional<List<String>> getIncludePatterns();
}

