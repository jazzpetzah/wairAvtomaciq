package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.Map;
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
    
    private static final ScheduledThreadPoolExecutor PING_EXECUTOR = new ScheduledThreadPoolExecutor(1);
    static{
        PING_EXECUTOR.setRemoveOnCancelPolicy(true);
    }
    private static ScheduledFuture<?> RUNNING_PINGER;
    private static final Runnable PINGER = new Runnable() {
        @Override
        public void run() {
            for (Map.Entry<Platform, Future<? extends RemoteWebDriver>> entry : PlatformDrivers.getInstance().getDrivers().entrySet()) {
                try {
                    RemoteWebDriver driver = entry.getValue().get(1, TimeUnit.SECONDS);
                    log.debug(String.format("Pinging driver for \"%s\"",entry.getKey()));
                    driver.getPageSource();
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    log.warn(String.format("Could not ping driver: %s", ex.getMessage()));
                }
            }
        }
    };
    
    public static void startPinging() {
        if (RUNNING_PINGER == null) {
            log.debug("Scheduling pinger task");
            RUNNING_PINGER = PING_EXECUTOR.scheduleAtFixedRate(PINGER, 0, DRIVER_COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }else{
            log.warn("Driver pinger is already running - Please stop the driver pinger before starting it again");
        }
    }

    public static void stopPinging() {
        if (RUNNING_PINGER != null) {
            if (!RUNNING_PINGER.cancel(true)) {
                log.warn("Could not stop driver pinger");
            }
            RUNNING_PINGER = null;
        }else{
            log.warn("No pinger to stop");
        }
    }
}
