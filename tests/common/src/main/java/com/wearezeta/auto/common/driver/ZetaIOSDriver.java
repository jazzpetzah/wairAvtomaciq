package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class ZetaIOSDriver extends IOSDriver<WebElement> implements ZetaDriver {
    private static final Logger log = ZetaLogger.getLog(ZetaIOSDriver.class.getSimpleName());

    private volatile boolean isSessionLost = false;

    public ZetaIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    public boolean isSessionLost() {
        return this.isSessionLost;
    }

    private void setSessionLost(boolean isSessionLost) {
        if (isSessionLost != this.isSessionLost) {
            log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
        }
        if (isSessionLost && !this.isSessionLost) {
            try {
                AppiumServerTools.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.isSessionLost = isSessionLost;
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        if (this.isSessionLost()) {
            // TODO: Take a full-screen Max OS screenshot if driverCommand.equals(DriverCommand.SCREENSHOT)
            log.warn(String.format("Appium session is dead. Skipping execution of '%s' command...", driverCommand));
            return null;
        }
        final Callable<Response> task = () -> super.execute(driverCommand, parameters);
        final Future<Response> future = executor.submit(task);
        try {
            return future.get(MAX_COMMAND_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if ((e.getCause() instanceof UnreachableBrowserException) ||
                        (e.getCause() instanceof SessionNotFoundException)) {
                    setSessionLost(true);
                }
                Throwables.propagate(e.getCause());
            } else {
                setSessionLost(true);
                Throwables.propagate(e);
            }
        }
        // This should never happen
        return super.execute(driverCommand, parameters);
    }

}
