package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.util.concurrent.Future;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

public class MessageContextMenuPage extends WinPage {

    public static final Logger LOG = ZetaLogger.getLog(MessageContextMenuPage.class.getName());

    private static final int CONTEXT_DELETE_INDEX = 1;
    private static final int CONTEXT_EDIT_INDEX = 1;
    private static final int CONTEXT_DELETE_EVERYWHERE_INDEX = 3;

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public MessageContextMenuPage(Future<ZetaWinDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void clickDelete() throws Exception {
        selectByIndex(CONTEXT_DELETE_INDEX, 2000);
    }
    
    public void clickEdit() throws Exception {
        selectByIndex(CONTEXT_EDIT_INDEX, 2000);
    }
    
    public void clickDeleteEverywhere() throws Exception {
        selectByIndex(CONTEXT_DELETE_EVERYWHERE_INDEX, 2000);
    }

    private void selectByIndex(int index, long wait) throws InterruptedException {
        Thread.sleep(wait);
        for (int i = 0; i < index; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            Thread.sleep(wait);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
    }
}
