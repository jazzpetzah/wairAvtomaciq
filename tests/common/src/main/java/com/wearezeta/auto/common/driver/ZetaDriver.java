package com.wearezeta.auto.common.driver;

public interface ZetaDriver {
	public static final long INIT_TIMEOUT_MILLISECONDS = 1000 * 60 * 3;

	public boolean isSessionLost();
}
