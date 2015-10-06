package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears after clicking phone sign in button
 * 
 * @author vova-lohika
 *
 */

public class PhoneNumberSignInPage extends AndroidPage {

	public static final String idRegisterButton = "ll__activation_button__back";
	@FindBy(id = idRegisterButton)
	private WebElement registerButton;

	public PhoneNumberSignInPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public WelcomePage clickRegisterButton() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.id(idRegisterButton)))
			registerButton.click();
		return new WelcomePage(this.getLazyDriver());
	}
}
