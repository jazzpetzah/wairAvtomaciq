package com.wearezeta.auto.android_tablet.common;

import com.wearezeta.auto.common.test_context.TestContextHolder;

public final class AndroidTabletTestContextHolder extends TestContextHolder<AndroidTabletTestContext> {
    private static AndroidTabletTestContextHolder instance = null;

    public static AndroidTabletTestContextHolder getInstance() {
        if (instance == null) {
            instance = new AndroidTabletTestContextHolder();
        }
        return instance;
    }

    private AndroidTabletTestContextHolder() {
    }
}
