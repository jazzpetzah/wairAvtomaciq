package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
	@FindBy(id = idCodeInput)
	private WebElement codeInput;
	
	public static final String idConfirmButton = "pcb__activate";
	@FindBy(id = idConfirmButton)
	private WebElement confirmButton;

	public VerificationPage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}

	public void inputVerificationCode(String verificationCode) throws Exception {
		codeInput.sendKeys(verificationCode);
	}

	public AddNamePage clickConfirm() throws Exception {
		confirmButton.click();
		return new AddNamePage(this.getLazyDriver());
	}
 
}
