package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PendingConnectionsPage extends WebPage {

	public PendingConnectionsPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void acceptRequestFromUser(String userName) throws Exception {
		String xpath = WebAppLocators.ConnectToPage.xpathAcceptRequestButtonByName
				.apply(userName);
		final WebElement acceptButton = getDriver().findElement(By.xpath(xpath));
		DriverUtils.waitUntilElementClickable(driver, acceptButton);
		acceptButton.click();
		// it takes some time to refresh the conversations list
		Thread.sleep(1000);
	}

	public void ignoreRequestFromUser(String userName) throws Exception {
		String xpath = WebAppLocators.ConnectToPage.xpathIgnoreReqestButtonByName
				.apply(userName);
		final WebElement ignoreButton = getDriver().findElement(By.xpath(xpath));
		DriverUtils.waitUntilElementClickable(driver, ignoreButton);
		// Waiting till self-profile page elements will not be clickable
		Thread.sleep(1000);
		ignoreButton.click();
	}
}
