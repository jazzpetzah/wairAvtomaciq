package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AndroidTabletPage extends AndroidPage {

	public AndroidTabletPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected <T extends AndroidPage> T getAndroidPageInstance(
			Class<T> pageClass) throws Exception {
		return this.instantiatePage(pageClass);
	}

	@Override
	public void selectFirstGalleryPhoto() throws Exception {
		AndroidCommonUtils.executeAdb("shell input keyevent 20");
		int ntry = 1;
		final int maxTries = 5;
		do {
			Thread.sleep(500);
			AndroidCommonUtils.executeAdb("shell input keyevent 20");
			Thread.sleep(500);
			AndroidCommonUtils.executeAdb("shell input keyevent 23");
			try {
				if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
						By.xpath(DialogPage.xpathConfirmOKButton), 3)) {
					break;
				}
			} catch (WebDriverException e) {
				AndroidCommonUtils.executeAdb("shell input keyevent 19");
			}
			ntry++;
		} while (ntry <= maxTries);
		if (ntry > maxTries) {
			throw new RuntimeException("Failed to tap the first gallery image!");
		}
	}
}
