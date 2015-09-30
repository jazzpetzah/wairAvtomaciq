package com.wearezeta.auto.osx.pages.osx;

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

	@FindBy(how = How.XPATH, using = OSXLocators.AppMenu.helpMenu)
	protected WebElement helpMenu;

	@FindBy(how = How.XPATH, using = OSXLocators.AppMenu.stagingMenuItem)
	protected WebElement stagingMenuItem;

	@FindBy(how = How.XPATH, using = OSXLocators.AppMenu.versionMenuItem)
	protected WebElement versionMenuItem;

	public MenuBarPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void switchEnvironment() throws Exception {
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.helpMenu)));
		helpMenu.click();
		new WebDriverWait(getDriver(), 1).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.versionMenuItem)));
		versionMenuItem.click();
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.helpMenu)));
		helpMenu.click();
		new WebDriverWait(getDriver(), 1).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.versionMenuItem)));
		versionMenuItem.click();
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.helpMenu)));
		helpMenu.click();
		new WebDriverWait(getDriver(), 10).until(ExpectedConditions
				.visibilityOfElementLocated(By
						.xpath(OSXLocators.AppMenu.stagingMenuItem)));
		stagingMenuItem.click();
	}

}
