package com.wearezeta.auto.common.calling.models;

import java.util.NoSuchElementException;

public enum CallingServiceStatus {
	// This state is set when calling/waiting instance just started, but is not
	// ready to make/accept calls yet

	STARTING,
	// This state is set only for waiting instance when it is already
	// initialized and is prepared to switch to "waiting for call" state
	WAITING,
	// This state is set for calling/waiting instance when it is successfully
	// initialized and can make an outgoing/accept an incoming call
	READY,
	// This state is set for calling/waiting instance when it has successfully
	// made/accepted a call and this call is still in progress
	ACTIVE,
	// This state is set for calling/waiting instance when it has successfully
	// made/accepted a call and this call is still in progress and is muted
	ACTIVE_MUTED,
	// This state is set for calling/waiting instance when it has successfully
	// started and got command to stop, but is not stopped yet
	STOPPING,
	// This state is set for calling/waiting instance when it was stopped (with
	// a command or because of some failure)
	INACTIVE;

	public static CallingServiceStatus fromString(String stringRepresentation) {
		for (CallingServiceStatus status : CallingServiceStatus.values()) {
			if (status.toString().equalsIgnoreCase(stringRepresentation)) {
				return status;
			}
		}
		throw new NoSuchElementException(String.format(
				"Unknown call status '%s'", stringRepresentation));
	}

	public static boolean isSubSetContains(CallingServiceStatus[] subSet,
			CallingServiceStatus item) {
		for (CallingServiceStatus status : subSet) {
			if (item == status) {
				return true;
			}
		}
		return false;
	}
}
