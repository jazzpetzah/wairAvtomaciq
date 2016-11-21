package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogManager {

    public static final Logger LOG = ZetaLogger.getLog(LogManager.class.getSimpleName());

    private final TestContext context;
    private final List<LogEntry> BROWSER_LOG = new ArrayList<>();

    public LogManager(TestContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public List<LogEntry> getBrowserLog() throws InterruptedException, ExecutionException, TimeoutException {
        BROWSER_LOG.addAll(IteratorUtils.toList((Iterator<LogEntry>) context.getDriver().manage().logs().get(LogType.BROWSER).
                iterator()));
        return BROWSER_LOG;
    }

    public Optional<LogEntry> searchUntilLogEntryAppears(String searchString, long sinceTimeMillis, int timeoutSeconds, int sleepMillis) throws
            Exception {
        try {
            return Optional.ofNullable(new WebDriverWait(context.getDriver(), timeoutSeconds, sleepMillis)
                    .until(new ExpectedCondition<LogEntry>() {
                        private long lastCheckedTimeStamp = sinceTimeMillis;

                        @Override
                        public LogEntry apply(WebDriver unusedDriver) {
                            LogEntry found;
                            try {
                                found = getBrowserLog().stream()
                                        .filter((entry) -> {
                                            return entry.getTimestamp() >= lastCheckedTimeStamp;
                                        })
                                        .filter((entry) -> {
                                            LOG.debug(entry);
                                            lastCheckedTimeStamp = entry.getTimestamp();
                                            return entry.getMessage().contains(searchString);
                                        })
                                        .findAny()
                                        .orElse(null);
                            } catch (InterruptedException | ExecutionException | java.util.concurrent.TimeoutException ex) {
                                LOG.warn(ex);
                                return null;
                            }
                            return found;
                        }
                    }));
        } catch (org.openqa.selenium.TimeoutException e) {
            return Optional.empty();
        }
    }

    public void printBrowserLog() throws InterruptedException, ExecutionException, TimeoutException {
        LOG.debug("BROWSER CONSOLE LOGS:");
        getBrowserLog().forEach((logEntry) -> {
            LOG.debug(logEntry.getMessage().replaceAll("^.*z\\.", "z\\."));
        });
        LOG.debug("--- END OF LOG ---");
    }

}
