package com.wearezeta.auto.common.driver;

public interface ZetaDriver {
	int DEFAULT_MAX_COMMAND_DURATION = 60 * 3; // seconds
	String HIDE_KEYBOARD_COMMAND = "hideKeyboard";

	boolean isSessionLost();
}
