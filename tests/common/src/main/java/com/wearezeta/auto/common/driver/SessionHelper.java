package com.wearezeta.auto.common.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.log.ZetaLogger;

final class SessionHelper {
    private static final Logger log = ZetaLogger.getLog(SessionHelper.class.getSimpleName());

    private static final int MAX_FIND_ELEMENT_COMMAND_DURATION = 60 * 3; // seconds

    private volatile boolean isSessionLost = false;
    private ZetaDriver wrappedDriver;

    public SessionHelper(ZetaDriver wrappedDriver) {
        this.wrappedDriver = wrappedDriver;
    }

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    public List<WebElement> wrappedFindElements(Function<By, List<WebElement>> f, By by) {
        if (!isSessionLost) {
            final Callable<List<WebElement>> task = () -> f.apply(by);
            final Future<List<WebElement>> future = executor.submit(task);
            try {
                return future.get(MAX_FIND_ELEMENT_COMMAND_DURATION, TimeUnit.SECONDS);
            } catch (Exception e) {
                setSessionLost(true);
                Throwables.propagate(e);
            }
        }
        return new ArrayList<>();
    }

    public WebElement wrappedFindElement(Function<By, WebElement> f, By by) {
        if (!isSessionLost) {
            final Callable<WebElement> task = () -> f.apply(by);
            final Future<WebElement> future = executor.submit(task);
            try {
                return future.get(MAX_FIND_ELEMENT_COMMAND_DURATION, TimeUnit.SECONDS);
            } catch (Exception e) {
                setSessionLost(true);
                Throwables.propagate(e);
            }
        }
        return null;
    }

    public void wrappedClose(IVoidMethod f) {
        if (isSessionLost) {
            return;
        }
        try {
            f.call();
        } catch (Exception e) {
            setSessionLost(true);
            Throwables.propagate(e);
        }
    }

    public void wrappedQuit(IVoidMethod f) {
        if (isSessionLost) {
            return;
        }
        try {
            f.call();
        } catch (Exception e) {
            setSessionLost(true);
            Throwables.propagate(e);
        }
    }

    public boolean isSessionLost() {
        return isSessionLost;
    }

    private void setSessionLost(boolean isSessionLost) {
        log.info(String.format("Setting isSessionLost to %s", isSessionLost));
        this.isSessionLost = isSessionLost;
        if (isSessionLost && this.wrappedDriver instanceof ZetaIOSDriver) {
            try {
                AppiumServerTools.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
