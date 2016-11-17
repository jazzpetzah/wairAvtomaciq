package com.wearezeta.auto.win.pages.webapp;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Future;


import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import org.apache.log4j.Logger;

public class ContactListPage extends com.wearezeta.auto.web.pages.ContactListPage {

    private static final Logger LOG = ZetaLogger.getLog(ContactListPage.class.getSimpleName());

    // TODO hide behind driver impl
    private final Robot robot = new Robot();


    public ContactListPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void pressShortCutToMute() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutToArchive() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForNextConv() throws Exception {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void pressShortCutForPrevConv() throws Exception {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void pressShortCutToSearch() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

}
