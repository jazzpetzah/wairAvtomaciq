package com.wearezeta.auto.web.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

public class ParallelParameterized extends Parameterized {
    
    private static final String PICKLE_MAX_PARALLEL_KEY = "picklejar.parallel.max";
    private static final String PICKLE_TEST_CLASS_TIMEOUT_KEY = "picklejar.timeout.testclass";

    private static final int GLOBAL_TEST_TIMEOUT_MINUTES = Integer.parseInt(System.getProperty(
            PICKLE_TEST_CLASS_TIMEOUT_KEY, 
            "180"));
    private static final int PICKLE_MAX_PARALLEL = Integer.parseInt(System.getProperty(
            PICKLE_MAX_PARALLEL_KEY, 
            Integer.toString(Runtime.getRuntime().availableProcessors())));

    private static class ThreadPoolScheduler implements RunnerScheduler {

        private ExecutorService executor;

        public ThreadPoolScheduler() {
            executor = Executors.newFixedThreadPool(PICKLE_MAX_PARALLEL);
        }

        @Override
        public void finished() {
            // every task of testclass is submitted
            executor.shutdown();
            try {
                // global timeout to execute everyting
                executor.awaitTermination(GLOBAL_TEST_TIMEOUT_MINUTES, TimeUnit.MINUTES);
                System.out.println("Hit global test timeout");
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
