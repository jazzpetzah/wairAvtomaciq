package com.wearezeta.auto.ios.common;

import com.wearezeta.auto.common.test_context.TestContextHolder;

public final class IOSTestContextHolder extends TestContextHolder<IOSTestContext> {
    private static IOSTestContextHolder instance = null;

    public static IOSTestContextHolder getInstance() {
        if (instance == null) {
            instance = new IOSTestContextHolder();
        }
        return instance;
    }

    private IOSTestContextHolder() {
    }
}
