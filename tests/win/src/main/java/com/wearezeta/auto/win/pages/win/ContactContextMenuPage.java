package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.driver.ZetaWinDriver;
import java.util.concurrent.Future;

import java.awt.Robot;

public class ContactContextMenuPage extends WinPage {

    private static final int CONTEXT_SILENCE_INDEX = 1;
    private static final int CONTEXT_ARCHIVE_INDEX = 2;
    private static final int CONTEXT_DELETE_INDEX = 3;
    private static final int CONTEXT_BLOCK_INDEX = 4;
    private static final int CONTEXT_LEAVE_INDEX = 4;

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public ContactContextMenuPage(Future<ZetaWinDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void clickArchive() throws Exception {
        selectByIndex(robot, CONTEXT_ARCHIVE_INDEX, 1000);
    }

    public void clickSilence() throws Exception {
        // https://www.youtube.com/watch?v=XFCU0dYgeeQ
        selectByIndex(robot, CONTEXT_SILENCE_INDEX, 1000);
    }

    public void clickNotify() throws Exception {
        selectByIndex(robot, CONTEXT_SILENCE_INDEX, 1000);
    }

    public void clickDelete() throws Exception {
        selectByIndex(robot, CONTEXT_DELETE_INDEX, 1000);
    }

    public void clickBlock() throws Exception {
        selectByIndex(robot, CONTEXT_BLOCK_INDEX, 1000);
    }

    public void clickLeave() throws Exception {
        selectByIndex(robot, CONTEXT_LEAVE_INDEX, 1000);
    }
}
