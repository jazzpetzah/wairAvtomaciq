package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.util.concurrent.Future;
import java.awt.Robot;

import org.apache.log4j.Logger;

public class ForeignMessageContextMenuPage extends WinPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ForeignMessageContextMenuPage.class.getName());

    private static final int CONTEXT_LIKE_INDEX = 1;
    private static final int CONTEXT_DELETE_INDEX = 2;

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public ForeignMessageContextMenuPage(Future<ZetaWinDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void clickLike() throws Exception {
        selectByIndex(robot, CONTEXT_LIKE_INDEX, 2000);
    }
    
    public void clickDelete() throws Exception {
        selectByIndex(robot, CONTEXT_DELETE_INDEX, 2000);
    }
    
}
