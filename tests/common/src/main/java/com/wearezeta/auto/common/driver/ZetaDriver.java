package com.wearezeta.auto.common.driver;

public interface ZetaDriver {
	public final long INIT_TIMEOUT = 1000 * 60 * 5; // milliseconds

	public boolean isSessionLost();
}
