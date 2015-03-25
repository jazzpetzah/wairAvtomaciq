package com.wearezeta.auto.osx.pages.welcome;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;

public class WelcomePage extends OSXPage {

	@FindBy(how = How.ID, using = OSXLocators.WelcomePage.idWelcomePage)
	private WebElement window;

	@FindBy(how = How.NAME, using = OSXLocators.WelcomePage.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.NAME, using = OSXLocators.WelcomePage.nameRegisterButton)
	private WebElement registerButton;

	@FindBy(how = How.NAME, using = OSXLocators.WelcomePage.nameTermsOfUseLink)
	private WebElement acceptTermsOfUseLink;

	@FindBy(how = How.XPATH, using = OSXLocators.WelcomePage.xpathAcceptTermsOfUseCheckbox)
	private WebElement acceptTermsOfUseCheckbox;

	public WelcomePage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.WelcomePage.idWelcomePage));
	}

	public RegistrationPage startRegistration() throws Exception {
		acceptTermsOfUseCheckbox.click();

		for (int i = 0; i < 3; i++) {
			if (OSXCommonUtils.osxAXValueToBoolean(registerButton
					.getAttribute("AXEnabled")))
				break;
		}
		registerButton.click();
		return new RegistrationPage(this.getDriver(), this.getWait());
	}

	public LoginPage startSignIn() throws Exception {
		signInButton.click();
		return new LoginPage(this.getDriver(), this.getWait());
	}

}
