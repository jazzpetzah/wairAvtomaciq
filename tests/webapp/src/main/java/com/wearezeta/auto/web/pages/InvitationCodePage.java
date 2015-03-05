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
		boolean result = DriverUtils.waitUntilElementAppears(driver,
				By.id(WebAppLocators.InvitationCodePage.idCodeInput), 5);
		return result;
	}

	public void inputCode(String code) {
		codeInput.sendKeys(code);
	}

	public LoginPage proceed() throws Exception {
		proceedButton.click();
		//waiting till page will be loaded before opening #login page
		Thread.sleep(3000);
		String url = this.getUrl() + "#login";
		return new LoginPage(this.getDriver(), this.getWait(), url);
	}
}
