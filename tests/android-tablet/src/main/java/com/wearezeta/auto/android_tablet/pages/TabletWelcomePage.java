package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

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
	
	public TabletWelcomePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private WelcomePage getWelcomePage() throws Exception {
		return (WelcomePage) this.getAndroidPageInstance(WelcomePage.class);
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
}
