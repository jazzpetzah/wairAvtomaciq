package com.wearezeta.auto.common.sync_engine_bridge;

/**
 *
 */
interface IRemoteProcess extends IRemoteEntity {
	void reconnect();
	String getLogPath();
	boolean isOtrOnly();
}
