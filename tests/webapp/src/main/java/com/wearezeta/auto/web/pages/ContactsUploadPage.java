package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactsUploadPage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathCloseButton)
	private WebElement closeButton;

	public ContactsUploadPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void waitUntilVisible(int secondsTimeout) throws Exception {
		assert DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.ContactsUploadPage.xpathCloseButton),
				secondsTimeout) : "Contacts upload dialog has not been show within "
				+ secondsTimeout + "second(s) timeout";
	}

	public void close() {
		closeButton.click();
	}
}
