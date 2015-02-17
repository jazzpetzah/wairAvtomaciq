package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PendingConnectionsPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PendingConnectionsPage.class
			.getSimpleName());

	public PendingConnectionsPage(String URL, String path) throws Exception {
		super(URL, path);
	}

	public void acceptRequestFromUser(String user) {
		String xpath = String.format(
				WebAppLocators.ConnectToPage.xpathFormatAcceptRequestButton,
				user);
		WebElement acceptButton = driver.findElement(By.xpath(xpath));
		DriverUtils.waitUntilElementClickable(driver, acceptButton);
		// Waiting till self-profile page elements will not be clickable
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		acceptButton.click();
	}
}
