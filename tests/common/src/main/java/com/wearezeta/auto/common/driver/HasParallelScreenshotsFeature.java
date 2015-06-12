package com.wearezeta.auto.common.driver;

public interface HasParallelScreenshotsFeature {
	// This is marker interface for classed, which have parallel screenshoting
	// feature enabled
	public int getMaxScreenshotMakersCount();
	public void forceStopOfScreenshoting();
}
