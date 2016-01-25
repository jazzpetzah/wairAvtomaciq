package com.wearezeta.auto.ios.tools;

import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.CommonUtils;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class IOSSimulatorHelper {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 20; //seconds

    public IOSSimulatorHelper() {
        // TODO Auto-generated constructor stub
    }

    private static final int MAX_SWIPE_TIME_SECONDS = 40;

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";

    public static void swipeDown() throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' 0.8 0.25 0.8 0.85",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME)
        }).get(MAX_SWIPE_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public static void swipeRight() throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' 0.2 0.8 0.9 0.8",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME)
        }).get(MAX_SWIPE_TIME_SECONDS, TimeUnit.SECONDS);
    }
}
