package com.wearezeta.auto.common.sync_engine_bridge;

/**
 *
 */
interface IRemoteProcess extends IRemoteEntity {

    void restart() throws Exception;

    String getLogPath();

    boolean isOtrOnly();

    void shutdown();
}
