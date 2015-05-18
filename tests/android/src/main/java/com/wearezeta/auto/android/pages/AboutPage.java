package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class AboutPage extends AndroidPage {
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.AboutPage.CLASS_NAME, locatorKey = "idAboutLogo")
	private WebElement aboutLogo;
	@AndroidFindBy(xpath = AndroidLocators.AboutPage.xpathAboutClose)
	private WebElement aboutClose;

	public AboutPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean aboutLogoIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(aboutLogo);
	}

	public PersonalInfoPage tapOnVersion() throws Exception {
		aboutClose.click();
		return new PersonalInfoPage(this.getLazyDriver());
	}

}
