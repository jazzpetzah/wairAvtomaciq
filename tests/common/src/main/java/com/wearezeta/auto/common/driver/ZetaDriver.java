package com.wearezeta.auto.common.driver;

public interface ZetaDriver {
	int MAX_COMMAND_DURATION = 60 * 3; // seconds
	int RECREATE_DELAY_SECONDS = 10;

	boolean isSessionLost();
}
