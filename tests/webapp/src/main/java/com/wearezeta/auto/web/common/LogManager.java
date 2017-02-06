package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.IOUtils;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogManager {

    public static final Logger LOG = ZetaLogger.getLog(LogManager.class.getSimpleName());

    private final WebAppTestContext context;
    private final List<LogEntry> BROWSER_LOG = new ArrayList<>();
    private boolean firefoxLogScriptApplied = false;

    public LogManager(WebAppTestContext context) {
        this.context = context;
    }

    public void applyFirefoxLoggerScript() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        final String SAVE_CONSOLE_SCRIPT = IOUtils.readFully(this.getClass().getResourceAsStream("/scripts/save_firefox_console.js"));
        context.getDriver().executeScript(SAVE_CONSOLE_SCRIPT);
        firefoxLogScriptApplied = true;
    }

    @SuppressWarnings("unchecked")
    public List<LogEntry> getBrowserLog() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        if (firefoxLogScriptApplied) {
            final String GET_CONSOLE_SCRIPT = IOUtils.readFully(this.getClass().getResourceAsStream("/scripts/get_firefox_console.js"));
            List<String> logs = (List<String>) context.getDriver().executeScript(GET_CONSOLE_SCRIPT, new Object[]{});
            List<LogEntry> logEntries = logs.stream().map((log)->new LogEntry(Level.INFO, System.currentTimeMillis(), log)).collect(Collectors.toList());
            BROWSER_LOG.clear();
            BROWSER_LOG.addAll(logEntries);
        } else {
            BROWSER_LOG.addAll(IteratorUtils.toList((Iterator<LogEntry>) context.getDriver().manage().logs().get(LogType.BROWSER).
                iterator()));
        }
        return BROWSER_LOG;
    }

    public Optional<LogEntry> searchUntilLogEntryAppears(String searchString, long sinceTimeMillis, int timeoutSeconds,
            int sleepMillis) throws Exception {
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
                            } catch (InterruptedException | ExecutionException | IOException | java.util.concurrent.TimeoutException ex) {
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

    public void printBrowserLog() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        LOG.debug("BROWSER CONSOLE LOGS:");
        getBrowserLog().forEach((logEntry) -> {
            LOG.debug(logEntry.getMessage().replaceAll("^.*\"z\\.", "z\\."));
        });
        LOG.debug("--- END OF LOG ---");
    }

}
