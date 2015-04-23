package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class InvitationCodePage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(InvitationCodePage.class.getSimpleName());

	@FindBy(how = How.ID, using = WebAppLocators.InvitationCodePage.idCodeInput)
	private WebElement codeInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.InvitationCodePage.xpathProceedButton)
	private WebElement proceedButton;

	public InvitationCodePage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait, url);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(driver,
				By.id(WebAppLocators.InvitationCodePage.idCodeInput));
	}

	public void inputCode(String code) {
		codeInput.sendKeys(code);
	}

	public LoginPage proceed() throws Exception {
		proceedButton.click();

		return new LoginPage(getDriver(), getWait(), getUrl());
	}
}
