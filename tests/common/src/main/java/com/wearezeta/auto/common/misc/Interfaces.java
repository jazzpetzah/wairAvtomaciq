package com.wearezeta.auto.common.misc;

public interface Interfaces {
    
    @FunctionalInterface
    interface FunctionFor2Parameters<A, B, C> {
        public A apply(B b, C c);
    }

}
