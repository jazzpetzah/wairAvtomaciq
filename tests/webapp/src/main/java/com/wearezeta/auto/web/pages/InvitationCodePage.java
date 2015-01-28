package com.wearezeta.auto.web.pages;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class InvitationCodePage extends WebPage {

	private static final Logger log = ZetaLogger
			.getLog(InvitationCodePage.class.getSimpleName());

	@FindBy(how = How.ID, using = WebAppLocators.InvitationCodePage.idCodeInput)
	private WebElement codeInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.InvitationCodePage.xpathProceedButton)
	private WebElement proceedButton;

	private String url;
	private String path;

	public InvitationCodePage(String url, String path) throws IOException {
		super(url, path, true);

		this.url = url;
		this.path = path;
	}

	public boolean isVisible() {
		boolean result;
		try {
			result = codeInput.isDisplayed();
		} catch (NoSuchElementException e) {
			log.debug("No invitation page");
			result = false;
		}
		return result;
	}

	public void inputCode(String code) {
		codeInput.sendKeys(code);
	}

	public LoginPage proceed() throws IOException {
		proceedButton.click();
		return new LoginPage(url, path);
	}
}
