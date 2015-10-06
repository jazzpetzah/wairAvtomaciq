package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AboutPage extends AndroidPage {

	private static final String idAboutLogo = "iv__about__logo";

	private static final String xpathAboutClose = "//*[@id='fl__about__container']//GlyphTextView";
	@FindBy(xpath = xpathAboutClose)
	private WebElement aboutClose;

	public AboutPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idAboutLogo));
	}

	public PersonalInfoPage tapOnVersion() throws Exception {
		aboutClose.click();
		return new PersonalInfoPage(this.getLazyDriver());
	}

	public boolean isInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(xpathAboutClose));
	}

}
