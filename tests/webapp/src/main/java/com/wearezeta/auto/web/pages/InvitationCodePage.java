package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
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

	private String url;
	private String path;

	public InvitationCodePage(String url, String path) throws Exception {
		super(url, path, true);

		this.url = url;
		this.path = path;
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
		return new LoginPage(url, path);
	}
}
