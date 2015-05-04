package com.wearezeta.auto.osx.pages.welcome;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;

public class WelcomePage extends OSXPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(WelcomePage.class
			.getSimpleName());

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

	public WelcomePage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.id(OSXLocators.WelcomePage.idWelcomePage));
	}

	public RegistrationPage startRegistration() throws Exception {
		acceptTermsOfUseCheckbox.click();

		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.getDriver())
				.withTimeout(10, TimeUnit.SECONDS).pollingEvery(1,
						TimeUnit.SECONDS);

		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return registerButton.isEnabled();
			}
		});

		registerButton.click();

		return new RegistrationPage(this.getLazyDriver());
	}

	public LoginPage startSignIn() throws Exception {
		signInButton.click();
		return new LoginPage(this.getLazyDriver());
	}

	public void sendProblemReportIfAppears(ProblemReportPage reportPage)
			throws Exception {
		if (!isVisible()) {
			reportPage.sendReportIfAppears();
		}
	}
}
