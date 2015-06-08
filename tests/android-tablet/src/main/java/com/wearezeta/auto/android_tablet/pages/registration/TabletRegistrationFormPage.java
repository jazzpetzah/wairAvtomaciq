package com.wearezeta.auto.android_tablet.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegistrationFormPage extends AndroidTabletPage {
	public static final String idNameInput = "ttv__signup__name";
	@FindBy(id = idNameInput)
	private WebElement nameInput;

	public static final String idEmailInput = "ttv__signup__email";
	@FindBy(id = idEmailInput)
	private WebElement emailInput;

	public static final String idPasswordInput = "ttv__signup__password";
	@FindBy(id = idPasswordInput)
	private WebElement passwordInput;

	public static final String idSubmitButton = "pcb__signin__email";
	@FindBy(id = idSubmitButton)
	private WebElement submitButton;

	public TabletRegistrationFormPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void enterName(String name) {
		nameInput.clear();
		nameInput.sendKeys(name);
	}

	public void enterEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void enterPassword(String password) {
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	public void tapSubmitButton() {
		submitButton.click();
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idEmailInput));
	}

}
