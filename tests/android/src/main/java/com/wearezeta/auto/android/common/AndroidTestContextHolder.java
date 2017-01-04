package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.test_context.TestContextHolder;

public final class AndroidTestContextHolder extends TestContextHolder<AndroidTestContext> {
    private static AndroidTestContextHolder instance = null;

    public static AndroidTestContextHolder getInstance() {
        if (instance == null) {
            instance = new AndroidTestContextHolder();
        }
        return instance;
    }

    private AndroidTestContextHolder() {
    }
}
