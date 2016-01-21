package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears directly after a user logs in using an email
 * and password, IF that user has not already attached a phone number
 * to their account.
 * @author deancook
 *
 */

public class AddPhoneNumberPage extends AndroidPage {

	public static final By idNotNowButton = By.id("ttv__not_now");

	public AddPhoneNumberPage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}
	
	public void notNowButtonClick() throws Exception {
		getElement(idNotNowButton).click();
	}

}
