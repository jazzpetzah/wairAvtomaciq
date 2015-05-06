package com.wearezeta.auto.osx.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ChangePasswordPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(ChangePasswordPage.class.getSimpleName());

	@FindBy(how = How.XPATH, using = OSXLocators.ChangePasswordPage.xpathEmailTextField)
	private WebElement emailTextField;

	@FindBy(how = How.XPATH, using = OSXLocators.ChangePasswordPage.xpathPasswordTextField)
	private WebElement passwordTextField;

	@FindBy(how = How.NAME, using = OSXLocators.ChangePasswordPage.nameChangePasswordButton)
	private WebElement changePasswordButton;

	@FindBy(how = How.NAME, using = OSXLocators.ChangePasswordPage.nameQuitSafariButton)
	private WebElement quitSafariButton;

	public ChangePasswordPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void enterEmailForChangePasswordAndSubmit(String email)
			throws Exception {
		this.getDriver().navigate().to(OSXConstants.BrowserActions.SAFARI);
		emailTextField.sendKeys(email);
		changePasswordButton.click();
		this.getDriver().navigate().to(OSXExecutionContext.wirePath);
	}

	public boolean resetPasswordSetNew(String password) throws Exception {
		this.getDriver().navigate().to(OSXConstants.BrowserActions.SAFARI);

		passwordTextField.sendKeys(password);
		changePasswordButton.click();

		boolean isFound = DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(OSXLocators.ChangePasswordPage.xpathPasswordChangedMessage),
						30);

		if (!isFound)
			log.debug(this.getDriver().getPageSource());

		quitSafariButton.click();

		this.getDriver().navigate().to(OSXExecutionContext.wirePath);

		return isFound;
	}

}
