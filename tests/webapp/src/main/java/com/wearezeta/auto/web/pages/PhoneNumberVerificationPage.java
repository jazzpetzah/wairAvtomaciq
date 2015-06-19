package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class PhoneNumberVerificationPage extends WebPage {

	@FindBy(css = "[data-uie-name='enter-phone-code']")
	private WebElement phoneCodeField;

	public PhoneNumberVerificationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public PhoneNumberVerificationPage(Future<ZetaWebAppDriver> lazyDriver,
			String url) throws Exception {
		super(lazyDriver, url);
	}

	public ContactListPage enterCode(String code) throws Exception {
		phoneCodeField.clear();
		phoneCodeField.sendKeys(code);
		return new ContactListPage(getLazyDriver());
	}
}
