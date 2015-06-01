package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears after inputing a phone number. It will
 * ask for either the activation code, or the login code, depending
 * on whether the phone number is new or has been used before.
 * @author deancook
 *
 */

public class VerificationPage extends AndroidPage {
	
	public static final String idCodeInput = "et__reg__phone";
	public static final String idConfirmButton = "pcb__activate";

	public VerificationPage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}

	public void inputVerificationCode(String verificationCode) throws Exception {
		this.getDriver().findElementById(idCodeInput).sendKeys(verificationCode);
	}

	public AddNamePage clickConfirm() throws Exception {
		this.getDriver().findElementById(idConfirmButton).click();
		return new AddNamePage(this.getLazyDriver());
	}
 
}
