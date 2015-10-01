package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletWelcomePage extends AndroidTabletPage {
	public final static String idRegisterButton = "zb__welcome__create_account";
	@FindBy(id = idRegisterButton)
	private WebElement registerButton;

	private final static Function<String, String> xpathLinkByText = text -> String
			.format("//*[@value='%s']", text);

	public TabletWelcomePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private WelcomePage getWelcomePage() throws Exception {
		return this.getAndroidPageInstance(WelcomePage.class);
	}

	public boolean waitForInitialScreen() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(WelcomePage.idHaveAccountButton), 15);
	}

	public void tapIHaveAnAccount() throws Exception {
		getWelcomePage().tapIHaveAnAccount();
	}

	public void tapRegisterButton() throws Exception {
		registerButton.click();
	}

	public boolean waitUntilLinkVisible(String linkText) throws Exception {
		final By locator = By.xpath(xpathLinkByText.apply(linkText));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapLink(String linkText) throws Exception {
		final By locator = By.xpath(xpathLinkByText.apply(linkText));
		getDriver().findElement(locator).click();
		;
	}
}
