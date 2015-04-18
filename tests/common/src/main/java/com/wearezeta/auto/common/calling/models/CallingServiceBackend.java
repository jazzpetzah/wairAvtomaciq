package com.wearezeta.auto.common.calling.models;

import java.util.NoSuchElementException;

public enum CallingServiceBackend {
	Autocall("autocall"), Webdriver("webdriver"), Blender("blender");

	private final String stringRepresentation;

	private CallingServiceBackend(String name) {
		this.stringRepresentation = name;
	}

	@Override
	public String toString() {
		return this.stringRepresentation;
	}

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
