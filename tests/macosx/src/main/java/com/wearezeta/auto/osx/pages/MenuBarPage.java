package com.wearezeta.auto.osx.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuBarPage extends OSXPage {

	@FindBy(how = How.XPATH, using = OSXLocators.AppMenu.developerMenu)
	protected WebElement developerMenu;

	@FindBy(how = How.XPATH, using = OSXLocators.AppMenu.developerStagingMenuItem)
	protected WebElement developerStagingMenuItem;

	public MenuBarPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void switchEnvironment() throws Exception {
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.developerMenu)));
		developerMenu.click();
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.developerStagingMenuItem)));
		developerStagingMenuItem.click();
	}

}
