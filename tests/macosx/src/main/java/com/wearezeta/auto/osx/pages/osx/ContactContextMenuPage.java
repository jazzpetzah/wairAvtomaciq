package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.util.concurrent.Future;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

public class ContactContextMenuPage extends OSXPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ContactContextMenuPage.class.getName());

    private static final int CONTEXT_SILENCE_INDEX = 1;
    private static final int CONTEXT_ARCHIVE_INDEX = 2;
    private static final int CONTEXT_DELETE_INDEX = 3;
    private static final int CONTEXT_BLOCK_INDEX = 4;
    private static final int CONTEXT_LEAVE_INDEX = 4;

    public ContactContextMenuPage(Future<ZetaOSXDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void clickArchive() throws Exception {
        selectByIndex(CONTEXT_ARCHIVE_INDEX, 1000);
    }

    public void clickSilence() throws Exception {
        // https://www.youtube.com/watch?v=XFCU0dYgeeQ
        selectByIndex(CONTEXT_SILENCE_INDEX, 1000);
    }

    public void clickNotify() throws Exception {
        selectByIndex(CONTEXT_SILENCE_INDEX, 1000);
    }

    public void clickDelete() throws Exception {
        selectByIndex(CONTEXT_DELETE_INDEX, 1000);
    }

    public void clickBlock() throws Exception {
        selectByIndex(CONTEXT_BLOCK_INDEX, 1000);
    }

    public void clickLeave() throws Exception {
        selectByIndex(CONTEXT_LEAVE_INDEX, 1000);
    }

    private void selectByIndex(int index, long wait) throws Exception {
        Thread.sleep(wait);
        for (int i = 0; i < index; i++) {
            this.getDriver().getRobot().keyPress(KeyEvent.VK_DOWN);
            Thread.sleep(wait);
        }
        this.getDriver().getRobot().keyPress(KeyEvent.VK_ENTER);
    }
}
