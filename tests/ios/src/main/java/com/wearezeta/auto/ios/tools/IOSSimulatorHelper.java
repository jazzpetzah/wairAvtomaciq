package com.wearezeta.auto.ios.tools;

import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.CommonUtils;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class IOSSimulatorHelper {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 20; //seconds

    public IOSSimulatorHelper() {}

    private static final int MAX_SWIPE_TIME_SECONDS = 40;

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";
    private static final String CLICK_SCRIPT_NAME = "clickInWindow.py";

    /**
     *
     * @param startX 0 <= startX <= 1
     * @param startY 0 <= startY <= 1
     * @param endX 0 <= endX <= 1
     * @param endY 0 <= endY <= 1
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY) throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY)
        }).get(MAX_SWIPE_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public static void swipeDown() throws Exception {
        swipe(0.8, 0.25, 0.8, 0.85);
    }

    public static void swipeRight() throws Exception {
        swipe(0.2, 0.8, 0.9, 0.8);
    }

    public static void swipeUp() throws Exception {
        swipe(0.5, 0.7, 0.5, 0.1);
    }

    public static void clickAt(String relativeX, String relativeY) throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' %s %s",
                        getIOSToolsRoot(IOSSimulatorHelper.class), CLICK_SCRIPT_NAME, relativeX, relativeY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }
}
