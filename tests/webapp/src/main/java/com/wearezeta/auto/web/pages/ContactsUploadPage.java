package com.wearezeta.auto.web.pages;

import java.util.Set;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;

public class ContactsUploadPage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathCloseButton)
	private WebElement closeButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathShareContactsButton)
	private WebElement shareContactsButton;

	public ContactsUploadPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilVisible(int secondsTimeout) throws Exception {
		assert DriverUtils
				.isElementDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactsUploadPage.xpathShareContactsButton),
						secondsTimeout) : "Contacts upload dialog has not been show within "
				+ secondsTimeout + "second(s) timeout";
	}

	public void close() {
		closeButton.click();
	}

	public void clickShareContactsButton() {
		shareContactsButton.click();
	}

	public GoogleLoginPage switchToGooglePopup() throws Exception {
		WebDriver driver = this.getDriver();
		Set<String> handles = driver.getWindowHandles();
		handles.remove(driver.getWindowHandle());
		assert handles.size() > 0 : "No Popup for Google was found";
		driver.switchTo().window(handles.iterator().next());
		return new GoogleLoginPage(this.getLazyDriver());
	}
}
