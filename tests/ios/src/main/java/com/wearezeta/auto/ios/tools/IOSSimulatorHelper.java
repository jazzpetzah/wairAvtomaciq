package com.wearezeta.auto.ios.tools;

import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.CommonUtils;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class IOSSimulatorHelper {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 40; //seconds

    public IOSSimulatorHelper() {
    }

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";
    private static final String CLICK_SCRIPT_NAME = "clickInWindow.py";

    /**
     * @param startX 0 <= startX <= 1
     * @param startY 0 <= startY <= 1
     * @param endX   0 <= endX <= 1
     * @param endY   0 <= endY <= 1
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * @param startX         0 <= startX <= 1
     * @param startY         0 <= startY <= 1
     * @param endX           0 <= endX <= 1
     * @param endY           0 <= endY <= 1
     * @param durationMillis swipe duration in milliseconds
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY,
                             long durationMillis) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f %d",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY, durationMillis)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void swipeDown() throws Exception {
        swipe(0.8, 0.2, 0.8, 0.95);
    }

    public static void swipeRight() throws Exception {
        swipe(0.2, 0.8, 0.9, 0.8);
    }

    public static void clickAt(String relativeX, String relativeY) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %s %s",
                        getIOSToolsRoot(IOSSimulatorHelper.class), CLICK_SCRIPT_NAME, relativeX, relativeY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * Type a string using keyboard and avoiding autocorrection
     *
     * @param str the string to enter
     * @throws Exception
     */
    public static void typeString(String str, boolean useAutocompleteWorkaround) throws Exception {
        activateWindow();
        String[] script;
        if (useAutocompleteWorkaround) {
            // FIXME: Quote string for bash script
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "    /usr/bin/osascript -e 'tell application \"System Events\" to key code 53'",
                    "done"
            };
        } else {
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "done"
            };
        }
        CommonUtils.executeUIShellScript(script).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void typeStringAndPressEnter(String str, boolean useAutocompleteWorkaround) throws Exception {
        activateWindow();
        String[] script;
        if (useAutocompleteWorkaround) {
            // FIXME: Quote string for bash script
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "    /usr/bin/osascript -e 'tell application \"System Events\" to key code 53'",
                    "done",
                    "/usr/bin/osascript -e 'tell application \"System Events\" to keystroke return'",
                    "sleep 0.3"
            };
        } else {
            script = new String[]{
                    "str_to_enter='" + str + "'",
                    "for (( i=0; i<${#str_to_enter}; i++ )); do",
                    "    current_char=\"${str_to_enter:$i:1}\"",
                    "    /usr/bin/osascript \\",
                    "         -e \"tell application \\\"System Events\\\" to keystroke \\\"${current_char}\\\"\" \\",
                    "    sleep 0.3",
                    "done",
                    "/usr/bin/osascript -e 'tell application \"System Events\" to keystroke return'",
                    "sleep 0.3"
            };
        }
        CommonUtils.executeUIShellScript(script).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    private static void activateWindow() throws Exception {
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to tell application process \"Simulator\"",
                "set frontmost to false",
                "set frontmost to true",
                "end tell"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void switchToAppsList() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\"",
                "keystroke \"h\" using {command down, shift down}",
                "keystroke \"h\" using {command down, shift down}",
                "end tell"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void lock() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"l\" using {command down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void goHome() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void pressEnterKey() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke return"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void toggleSoftwareKeyboard() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"k\" using {command down}"
        }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }
}
