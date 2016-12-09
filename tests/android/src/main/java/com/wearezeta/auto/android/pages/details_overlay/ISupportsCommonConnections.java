package com.wearezeta.auto.android.pages.details_overlay;

public interface ISupportsCommonConnections {
    boolean waitUntilCommonUserVisible(String userName) throws Exception;
    boolean waitUntilCommonUserInvisible(String userName) throws Exception;
}
