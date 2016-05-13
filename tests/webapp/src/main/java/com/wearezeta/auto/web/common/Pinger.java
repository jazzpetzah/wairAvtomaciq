package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Pinger {

    public static final Logger log = ZetaLogger.getLog(Pinger.class.getSimpleName());

    private static final int DRIVER_COMMAND_TIMEOUT_SECONDS = 30;

    private final ScheduledThreadPoolExecutor PING_EXECUTOR = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> RUNNING_PINGER;
    private Future<? extends RemoteWebDriver> driver;
    private final Runnable PINGER = new Runnable() {
        @Override
        public void run() {
            try {
                log.debug("Pinging driver");
                driver.get(1, TimeUnit.SECONDS).getPageSource();
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                log.warn(String.format("Could not ping driver: %s", ex.getMessage()));
            }
        }
    };

    public Pinger(Future<? extends RemoteWebDriver> driver) {
        PING_EXECUTOR.setRemoveOnCancelPolicy(true);
        this.driver = driver;
    }

    public void startPinging() {
        if (RUNNING_PINGER == null) {
            log.debug("Scheduling pinger task");
            RUNNING_PINGER = PING_EXECUTOR.scheduleAtFixedRate(PINGER, 0, DRIVER_COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } else {
            log.warn("Driver pinger is already running - Please stop the driver pinger before starting it again");
        }
    }

    public void stopPinging() {
        if (RUNNING_PINGER != null) {
            if (!RUNNING_PINGER.cancel(true)) {
                log.warn("Could not stop driver pinger");
            }
            RUNNING_PINGER = null;
        } else {
            log.warn("No pinger to stop");
        }
    }
}
