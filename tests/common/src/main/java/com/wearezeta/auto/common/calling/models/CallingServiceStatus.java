package com.wearezeta.auto.common.calling.models;

import java.util.NoSuchElementException;

public enum CallingServiceStatus {
	Starting("starting"), Waiting("waiting"), Active("active"), ActiveMuted(
			"active_muted"), Stopping("stopping"), Inactive("inactive");

	private final String stringRepresentation;

	public String getStringRepresentation() {
		return this.stringRepresentation;
	}

	private CallingServiceStatus(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	@Override
	public String toString() {
		return this.getStringRepresentation();
	}

	public static CallingServiceStatus fromString(String stringRepresentation) {
		for (CallingServiceStatus status : CallingServiceStatus.values()) {
			if (status.getStringRepresentation().equalsIgnoreCase(
					stringRepresentation)) {
				return status;
			}
		}
		throw new NoSuchElementException(String.format(
				"Unknown call status '%s'", stringRepresentation));
	}
}
