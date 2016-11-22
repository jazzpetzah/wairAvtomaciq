package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.util.concurrent.Future;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

public class EphemeralTimerButtonContextMenuPage extends OSXPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(EphemeralTimerButtonContextMenuPage.class.getName());

    private static final int CONTEXT_EPH_TIMER_OFF_INDEX = 1;
    private static final int CONTEXT_EPH_TIMER_5_SECONDS_INDEX = 2;
    private static final int CONTEXT_EPH_TIMER_15_SECONDS_INDEX = 3;
    private static final int CONTEXT_EPH_TIMER_30_SECONDS_INDEX = 4;
    private static final int CONTEXT_EPH_TIMER_1_MINUTE_INDEX = 5;
    private static final int CONTEXT_EPH_TIMER_5_MINUTES_INDEX = 6;
    private static final int CONTEXT_EPH_TIMER_1_DAY_INDEX = 7;

    public EphemeralTimerButtonContextMenuPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
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
                selectByIndex(CONTEXT_EPH_TIMER_OFF_INDEX, 1000);
                break;
            case "5 SECONDS":
                selectByIndex(CONTEXT_EPH_TIMER_5_SECONDS_INDEX, 1000);
                break;
            case "15 SECONDS":
                selectByIndex(CONTEXT_EPH_TIMER_15_SECONDS_INDEX, 1000);
                break;
            case "30 SECONDS":
                selectByIndex(CONTEXT_EPH_TIMER_30_SECONDS_INDEX, 1000);
                break;
            case "1 MINUTE":
                selectByIndex(CONTEXT_EPH_TIMER_1_MINUTE_INDEX, 1000);
                break;
            case "5 MINUTES":
                selectByIndex(CONTEXT_EPH_TIMER_5_MINUTES_INDEX, 1000);
                break;
            case "1 DAY":
                selectByIndex(CONTEXT_EPH_TIMER_1_DAY_INDEX, 1000);
                break;    
        }
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
