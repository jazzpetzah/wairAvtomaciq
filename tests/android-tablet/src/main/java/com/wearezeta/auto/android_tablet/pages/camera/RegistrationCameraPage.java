package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class RegistrationCameraPage extends AbstractCameraPage {
	public static final String idCameraButton = "gtv__sign_up__lens_icon";
	
	public RegistrationCameraPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	protected By getLensButtonLocator() {
		return By.id(idCameraButton);
	}

}
