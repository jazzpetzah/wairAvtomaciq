package com.wearezeta.auto.common.sync_engine_bridge;

/**
 *
 */
interface IRemoteProcess extends IRemoteEntity {
	public void reconnect();
	
	public String getLogPath();

	public boolean isOtrOnly();
}
