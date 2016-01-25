package com.wearezeta.auto.ios.tools;

@FunctionalInterface
public interface IRunnableWithException {
    void run() throws Throwable;
}
