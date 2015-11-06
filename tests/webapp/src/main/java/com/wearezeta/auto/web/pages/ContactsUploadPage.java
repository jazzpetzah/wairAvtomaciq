package com.wearezeta.auto.web.pages;

import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactsUploadPage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathCloseButton)
	private WebElement closeButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathShowSearchButton)
	private WebElement xpathShowSearchButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactsUploadPage.xpathShareContactsButton)
	private WebElement shareContactsButton;

	public ContactsUploadPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilVisible(int secondsTimeout) throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactsUploadPage.xpathShareContactsButton),
						secondsTimeout) : "Contacts upload dialog has not been show within "
				+ secondsTimeout + "second(s) timeout";
	}

	public void close() {
		closeButton.click();
	}

	public void clickShowSearchButton() throws Exception {
		xpathShowSearchButton.click();
	}

	public void clickShareContactsButton() {
		shareContactsButton.click();
	}

	public void switchToGooglePopup() throws Exception {
		WebDriver driver = this.getDriver();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(
				DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS);
		try {
			wait.until(drv -> {
				return (drv.getWindowHandles().size() > 1);
			});
		} catch (TimeoutException e) {
			throw new TimeoutException("No Popup for Google was found", e);
		}
		Set<String> handles = driver.getWindowHandles();
		handles.remove(driver.getWindowHandle());
		driver.switchTo().window(handles.iterator().next());
	}
}
