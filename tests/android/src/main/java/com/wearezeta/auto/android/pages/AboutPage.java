package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AboutPage extends AndroidPage {
	@FindBy(id = AndroidLocators.AboutPage.idAboutLogo)
	private WebElement aboutLogo;

	@FindBy(xpath = AndroidLocators.AboutPage.xpathAboutClose)
	private WebElement aboutClose;

	public AboutPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		throw new RuntimeException("Swipe is not supported on About page");
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(aboutLogo);
	}

	public PersonalInfoPage tapOnVersion() throws Exception {
		aboutClose.click();
		return new PersonalInfoPage(this.getLazyDriver());
	}

	public boolean isInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(AndroidLocators.AboutPage.xpathAboutClose));
	}

}
