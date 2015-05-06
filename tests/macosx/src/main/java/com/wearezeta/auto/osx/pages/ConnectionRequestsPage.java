package com.wearezeta.auto.osx.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ConnectionRequestsPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(ConnectionRequestsPage.class.getSimpleName());

	public ConnectionRequestsPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isRequestFromUserWithEmailExists(String email)
			throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatEmailText, email);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpath), 3);
	}

	public boolean isRequestFromUserWithEmailNotExists(String email)
			throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatEmailText, email);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(xpath), 3);
	}

	public void acceptRequestFromUserWithEmail(String email) throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatConnectButton,
				email);
		WebElement connectButton = getDriver().findElement(By.xpath(xpath));
		connectButton.click();
	}

	public void ignoreRequestFromUserWithEmail(String email) throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatBlockButton,
				email);
		WebElement blockButton = getDriver().findElement(By.xpath(xpath));
		blockButton.click();
	}

	public void acceptAllRequests() throws Exception {
		List<WebElement> connectButtons = this
				.getDriver()
				.findElements(
						By.id(OSXLocators.ConnectionRequestsPage.idAcceptConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void ignoreAllRequests() throws Exception {
		List<WebElement> connectButtons = this
				.getDriver()
				.findElements(
						By.id(OSXLocators.ConnectionRequestsPage.idIgnoreConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

}
