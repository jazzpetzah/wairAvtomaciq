package com.wearezeta.auto.common.test_context;

import java.util.Optional;

public abstract class TestContextHolder<T extends TestContext> {
    private volatile Optional<T> testContext = Optional.empty();

    public T getTestContext() {
        return testContext.orElseThrow(
                () -> new IllegalStateException("Test context is expected to be set")
        );
    }

    public void setTestContext(T testContext) {
        this.testContext.ifPresent(TestContext::reset);
        this.testContext = Optional.of(testContext);
    }

    public void resetTestContext() {
        if (testContext.isPresent()) {
            testContext.get().reset();
            testContext = Optional.empty();
        }
    }

    protected TestContextHolder() {
    }
}
