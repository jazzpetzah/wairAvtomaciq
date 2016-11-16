package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.awt.Robot;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;

public class EphemeralTimerButtonContextMenuPage extends WinPage {

    public static final Logger LOG = ZetaLogger.getLog(EphemeralTimerButtonContextMenuPage.class.getName());

    private static final int CONTEXT_EPH_TIMER_OFF_INDEX = 1;
    private static final int CONTEXT_EPH_TIMER_5_SECONDS_INDEX = 2;
    private static final int CONTEXT_EPH_TIMER_15_SECONDS_INDEX = 3;
    private static final int CONTEXT_EPH_TIMER_30_SECONDS_INDEX = 4;
    private static final int CONTEXT_EPH_TIMER_1_MINUTE_INDEX = 5;
    private static final int CONTEXT_EPH_TIMER_5_MINUTES_INDEX = 6;
    private static final int CONTEXT_EPH_TIMER_1_DAY_INDEX = 7;
    
    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public EphemeralTimerButtonContextMenuPage(Future<ZetaWinDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    /**
     * It's not possible to locate elements inside of the context menu thus we have to implement it this way :/
     *
     * @throws Exception
     */
    public void setEphemeralTimer(String timerName) throws Exception {
        switch(timerName){
            case "OFF":
                selectByIndex(robot, CONTEXT_EPH_TIMER_OFF_INDEX, 1000);
                break;
            case "5 SECONDS":
                selectByIndex(robot, CONTEXT_EPH_TIMER_5_SECONDS_INDEX, 1000);
                break;
            case "15 SECONDS":
                selectByIndex(robot, CONTEXT_EPH_TIMER_15_SECONDS_INDEX, 1000);
                break;
            case "30 SECONDS":
                selectByIndex(robot, CONTEXT_EPH_TIMER_30_SECONDS_INDEX, 1000);
                break;
            case "1 MINUTE":
                selectByIndex(robot, CONTEXT_EPH_TIMER_1_MINUTE_INDEX, 1000);
                break;
            case "5 MINUTES":
                selectByIndex(robot, CONTEXT_EPH_TIMER_5_MINUTES_INDEX, 1000);
                break;
            case "1 DAY":
                selectByIndex(robot, CONTEXT_EPH_TIMER_1_DAY_INDEX, 1000);
                break;    
        }
    }
}
