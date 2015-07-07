package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears after inputing a phone number. It will ask for either
 * the activation code, or the login code, depending on whether the phone number
 * is new or has been used before.
 * 
 * @author deancook
 *
 */

public class PhoneNumberVerificationPage extends AndroidPage {

	public static final String idCodeInput = "et__reg__phone";
	@FindBy(id = idCodeInput)
	private WebElement codeInput;

	public static final String idConfirmButton = "pcb__activate";
	@FindBy(id = idConfirmButton)
	private WebElement confirmButton;

	public static final String idManualCodeButton = "tv__manual_button";
	@FindBy(id = idManualCodeButton)
	private WebElement manualCodeButton;
	
	public PhoneNumberVerificationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void inputVerificationCode(String verificationCode) throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCodeInput), 15) : "Verification code input has not been shown in time";
		codeInput.sendKeys(verificationCode);
	}

	public AddNamePage clickConfirm() throws Exception {
		confirmButton.click();
		return new AddNamePage(this.getLazyDriver());
	}

	public boolean waitUntilConfirmButtonDissapears() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idConfirmButton), 40);
	}
	
	public boolean waitUntilManualCodeButtonAppears() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.id(idManualCodeButton), 20);
	}
	
	public PhoneNumberVerificationPage clickManualCodeButton() throws Exception {
		manualCodeButton.click();
		 DriverUtils.waitUntilLocatorDissapears(getDriver(),
					By.id(idManualCodeButton), 15);
		return new PhoneNumberVerificationPage(this.getLazyDriver());
	}

}
