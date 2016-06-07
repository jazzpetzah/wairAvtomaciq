package com.wearezeta.auto.web.common;

import java.util.NoSuchElementException;

public enum Browser {
	Safari("safari"), InternetExplorer("internetexplorer"), Chrome("chrome"), Firefox(
			"firefox"), Opera("opera"), MicrosoftEdge("MicrosoftEdge");

	private final String stringRepresentation;

	private Browser(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	@Override
	public String toString() {
		return this.stringRepresentation;
	}

	public boolean isOneOf(Browser[] set) {
		for (Browser item : set) {
			if (this == item) {
				return true;
			}
		}
		return false;
	}

	public static Browser fromString(String stringRepresentation) {
		for (Browser name : Browser.values()) {
			if (name.toString().equalsIgnoreCase(
					stringRepresentation.replace(" ", ""))) {
				return name;
			}
		}
		throw new NoSuchElementException(String.format(
				"Unknown browser name '%s'", stringRepresentation));
	}

	public boolean isSupportingCalls() {
		switch (this) {
		case Chrome:
		case Firefox:
		case Opera:
			return true;
		default:
			return false;
		}
	}

	public boolean isSupportingInlineVideo() {
		switch (this) {
		case Chrome:
			return true;
		default:
			return false;
		}
	}

	public boolean isSupportingConsoleLogManagement() {
		switch (this) {
		case Chrome:
		case Firefox:
		case Opera:
		case Safari:
			return true;
		default:
			return false;
		}
	}

	public boolean isSupportingSyntheticDragAndDrop() {
		switch (this) {
		case Chrome:
		case Firefox:
		case Opera:
		case InternetExplorer:
			return true;
		default:
			return false;
		}
	}

	public boolean isSupportingMaximizingTheWindow() {
		switch (this) {
		case InternetExplorer:
		case MicrosoftEdge:
			return false;
		default:
			return true;
		}
	}

	public boolean isSupportingSettingWindowSize() {
		switch (this) {
		case MicrosoftEdge:
			return false;
		default:
			return true;
		}
	}

	public boolean isSupportingNativeMouseActions() {
		switch (this) {
		case Chrome:
		case Firefox:
		case Opera:
			return true;
		default:
			return false;
		}
	}

	public boolean isSupportingDiabledButtonDetection() {
		switch (this) {
		case Safari:
			return false;
		default:
			return true;
		}
	}
}