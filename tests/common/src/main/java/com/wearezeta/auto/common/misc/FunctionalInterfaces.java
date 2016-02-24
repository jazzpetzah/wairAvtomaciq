package com.wearezeta.auto.common.misc;

import java.awt.image.BufferedImage;

public interface FunctionalInterfaces {
    
    @FunctionalInterface
    interface FunctionFor2Parameters<A, B, C> {
        A apply(B b, C c);
    }

    @FunctionalInterface
    interface StateGetter {
        BufferedImage getState() throws Exception;
    }

}