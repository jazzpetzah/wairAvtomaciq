package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

/**
 * Parallel JUnit runner for parameterized test classes
 * 
 * @author mkoppen
 * @deprecated
 */
@Deprecated
public class ParallelParameterized extends Parameterized {

    private static final Logger LOG = LogManager.getLogger();

    private static class ThreadPoolScheduler implements RunnerScheduler {

        private ExecutorService executor;

        public ThreadPoolScheduler() {
            executor = Executors.newFixedThreadPool(Config.PICKLE_MAX_PARALLEL);
        }

        @Override
        public void finished() {
            // every task of testclass is submitted
            executor.shutdown();
            try {
                // global timeout to execute everyting
                executor.awaitTermination(Config.GLOBAL_TEST_TIMEOUT_MINUTES, TimeUnit.MINUTES);
                LOG.log(Level.INFO, "Shutting down test suite");
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }

    }

    public ParallelParameterized(Class klass) throws Throwable {
        super(klass);
        setScheduler(new ThreadPoolScheduler());
    }
}
