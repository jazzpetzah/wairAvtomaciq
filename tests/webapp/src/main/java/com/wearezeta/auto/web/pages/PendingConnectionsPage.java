package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PendingConnectionsPage extends WebPage {

	public PendingConnectionsPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void acceptRequestFromUser(String userName) throws Exception {
		String xpath = WebAppLocators.ConnectToPage.xpathAcceptRequestButtonByName
				.apply(userName);
		final WebElement acceptButton = getDriver()
				.findElement(By.xpath(xpath));
		DriverUtils.waitUntilElementClickable(this.getDriver(), acceptButton);
		acceptButton.click();
		// it takes some time to refresh the conversations list
		Thread.sleep(1000);
	}

	public void ignoreRequestFromUser(String userName) throws Exception {
		String xpath = WebAppLocators.ConnectToPage.xpathIgnoreReqestButtonByName
				.apply(userName);
		final WebElement ignoreButton = getDriver()
				.findElement(By.xpath(xpath));
		DriverUtils.waitUntilElementClickable(this.getDriver(), ignoreButton);
		// Waiting till self-profile page elements will not be clickable
		Thread.sleep(1000);
		ignoreButton.click();
	}
}
