package com.wearezeta.auto.android_tablet.common;

import java.util.Optional;

import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * This class is a dirty fix for App/Selendroid(?) issue The problem is that
 * device orientation is sometimes overriden without any reason and we have to
 * fix it in such a tricky way
 * 
 * @author elf
 *
 */
public final class ScreenOrientationHelper {

	private static ScreenOrientationHelper instance;

	public static synchronized ScreenOrientationHelper getInstance() {
		if (instance == null) {
			instance = new ScreenOrientationHelper();
		}
		return instance;
	}

	private ScreenOrientationHelper() {
	}

	private Optional<ScreenOrientation> orientation = Optional.empty();

	public void setOrientation(ScreenOrientation orientation) {
		this.orientation = Optional.of(orientation);
	}

	public void resetOrientation() {
		this.orientation = Optional.empty();
	}

	public ScreenOrientation fixOrientation(final ZetaAndroidDriver driver)
			throws InterruptedException {
		if (this.orientation.isPresent()) {
			driver.rotate(orientation.get());
			Thread.sleep(500);
			return this.orientation.get();
		} else {
			return driver.getOrientation();
		}
	}
}
