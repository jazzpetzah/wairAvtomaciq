package com.wearezeta.auto.sync.client;

public enum InstanceState {
	CREATED, SENDING, FINAL_LISTENING, FINISHED, UPDATING,  RESTART,
	ERROR_CRASHED,
	ERROR_WRONG_PLATFORM;
}
