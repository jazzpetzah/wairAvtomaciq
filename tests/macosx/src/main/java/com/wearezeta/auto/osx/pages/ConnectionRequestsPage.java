package com.wearezeta.auto.osx.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ConnectionRequestsPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(ConnectionRequestsPage.class.getSimpleName());

	public ConnectionRequestsPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isRequestFromUserWithEmailExists(String email)
			throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatEmailText, email);
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 3);
	}

	public boolean isRequestFromUserWithEmailNotExists(String email)
			throws Exception {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatEmailText, email);
		return DriverUtils
				.waitUntilElementDissapear(driver, By.xpath(xpath), 3);
	}

	public void acceptRequestFromUserWithEmail(String email) {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatConnectButton,
				email);
		WebElement connectButton = driver.findElement(By.xpath(xpath));
		connectButton.click();
	}

	public void ignoreRequestFromUserWithEmail(String email) {
		String xpath = String.format(
				OSXLocators.ConnectionRequestsPage.xpathFormatBlockButton,
				email);
		WebElement blockButton = driver.findElement(By.xpath(xpath));
		blockButton.click();
	}

	public void acceptAllRequests() {
		List<WebElement> connectButtons = driver
				.findElements(By
						.id(OSXLocators.ConnectionRequestsPage.idAcceptConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void ignoreAllRequests() {
		List<WebElement> connectButtons = driver
				.findElements(By
						.id(OSXLocators.ConnectionRequestsPage.idIgnoreConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

}
