package com.wearezeta.auto.win.pages.webapp;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class PreferencesPage extends
        com.wearezeta.auto.web.pages.PreferencesPage {

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public PreferencesPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void pressShortCutForPreferences() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}
