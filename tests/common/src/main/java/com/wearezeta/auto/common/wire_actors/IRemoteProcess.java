package com.wearezeta.auto.common.wire_actors;

/**
 *
 */
interface IRemoteProcess extends IRemoteEntity {

    void restart() throws Exception;

    String getLogPath();

    boolean isOtrOnly();

    void shutdown();
}
