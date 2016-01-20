package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConversationViewCameraPage extends AbstractCameraPage {

	public ConversationViewCameraPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	protected By getLensButtonLocator() {
		return DialogPage.idAddPicture;
	}

}
