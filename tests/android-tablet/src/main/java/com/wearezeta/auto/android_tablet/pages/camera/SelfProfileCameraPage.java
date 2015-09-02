package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SelfProfileCameraPage extends AbstractCameraPage {
	public static final String idChangePictureButton = "gtv__camera_control__change_image_source";

	public SelfProfileCameraPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	protected By getLensButtonLocator() {
		return By.id(idChangePictureButton);
	}
}
