package com.wearezeta.common.process;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class ProcessHandler {
    
    private static final Logger LOG = ZetaLogger.getLog(ProcessHandler.class.getName());
    private static final ScheduledExecutorService POOL = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors());
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{POOL.shutdownNow();}));
    }
    private Process process = null;
    private final String[] cmd;
    private final List<String> output = new CopyOnWriteArrayList<>();
    private Future mergedOutputMonitor = null;
    
    
    public ProcessHandler(String[] cmd) {
        Objects.requireNonNull(cmd);
        this.cmd = cmd;
    }
    
    public ProcessHandler startProcess(long timeout, TimeUnit unit) throws IOException, InterruptedException {
        startProcess();
        if(!process.waitFor(timeout, unit)){
            LOG.warn("Process timeout exceeded - stopping process");
        }
        stopProcess();
        return this;
    }
    
    public ProcessHandler startProcess() throws IOException, InterruptedException {
        if (process == null || !process.isAlive()) {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);//merge error and input steam into one stream
            LOG.debug(String.format("Starting process '%s'", Arrays.toString(cmd)));
            process = builder.start();
            mergedOutputMonitor = createOutputLogger(process.getInputStream());
        } else {
            throw new IllegalStateException("Process is already running");
        }
        return this;
    }
    
    public ProcessHandler stopProcess() {
        LOG.debug(String.format("Stopping process '%s'", Arrays.toString(cmd)));
        if (process.isAlive()) {
            try {
                int retryCount = 3;
                do {
                    retryCount--;
                    process.destroy();
                    process.waitFor(5, TimeUnit.SECONDS);
                } while (process.isAlive() && retryCount > 0);
                if (process.isAlive()) {
                    LOG.error("Could not shutdown process - It may has died");
                }
            } catch (InterruptedException ex) {
                LOG.error("Could not shutdown process", ex);
            }
        }
        mergedOutputMonitor.cancel(true);
        return this;
    }
    
    public List<String> getOutput() {
        return output;
    }
    
    public int getExitCode() {
        return process.exitValue();
    }
    
    private Future createOutputLogger(final InputStream stream) {
        return POOL.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                while (process.isAlive()) {
                    String line = reader.readLine();
                    while (line != null) {
                        output.add(line);
                        LOG.info(String.format("\t%s", line));
                        line = reader.readLine();
                    }
                }
            } catch (NullPointerException | IOException ex) {
                LOG.debug("Closing output logger", ex);
            }
        });
    }
    
}
