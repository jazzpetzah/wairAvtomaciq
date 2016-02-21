package com.wearezeta.auto.android.common;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface StateGetter {
    BufferedImage getState() throws Exception;
}
