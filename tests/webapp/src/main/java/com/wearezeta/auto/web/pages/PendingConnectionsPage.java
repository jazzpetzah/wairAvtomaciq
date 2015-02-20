package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PendingConnectionsPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PendingConnectionsPage.class.getSimpleName());

	public PendingConnectionsPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void acceptRequestFromUser(String user) throws Exception {
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
