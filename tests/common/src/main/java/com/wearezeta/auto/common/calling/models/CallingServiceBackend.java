package com.wearezeta.auto.common.calling.models;

import java.util.NoSuchElementException;

public enum CallingServiceBackend {
	AUTOCALL, WEBDRIVER, BLENDER;

	public static CallingServiceBackend fromString(String stringRepresentation) {
		for (CallingServiceBackend backend : CallingServiceBackend.values()) {
			if (backend.toString().equalsIgnoreCase(stringRepresentation)) {
				return backend;
			}
		}
		throw new NoSuchElementException(String.format(
				"Unknown call service backend '%s'", stringRepresentation));
	}

}
