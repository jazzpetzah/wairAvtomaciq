package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AboutPage extends AndroidPage {

	private static final By idAboutLogo = By.id("iv__about__logo");

	private static final By xpathAboutClose = By.xpath("//*[@id='fl__about__container']//GlyphTextView");

	public AboutPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idAboutLogo);
	}

	public void tapOnVersion() throws Exception {
		getElement(xpathAboutClose).click();
	}

	public boolean isInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathAboutClose);
	}

}
