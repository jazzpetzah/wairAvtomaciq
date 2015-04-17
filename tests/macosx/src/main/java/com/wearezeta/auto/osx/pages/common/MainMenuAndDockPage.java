package com.wearezeta.auto.osx.pages.common;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.welcome.LoginPage;

public class MainMenuAndDockPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(MainMenuAndDockPage.class.getSimpleName());

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameSendImageMenuItem)
	private WebElement sendImageMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameQuitWireMenuItem)
	private WebElement quitWireMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameSignOutMenuItem)
	private WebElement signOutMenuItem;

	public MainMenuAndDockPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait, OSXExecutionContext.wirePath);
	}

	public void signOut() throws Exception {
		// signOutMenuItem.click();
		quitWire();
		try {
			OSXCommonUtils.deleteWireLoginFromKeychain();
			OSXCommonUtils.removeAllZClientSettingsFromDefaults();
			OSXCommonUtils.deleteCacheFolder();
			OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
					.getBackendType(LoginPage.class));
		} catch (Exception ex) {
			log.error("Can't clear Wire settings in OSX.\n" + ex.getMessage());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		driver.navigate().to(OSXExecutionContext.wirePath);
	}

	public void quitWire() throws IOException {
		try {
			quitWireMenuItem.click();
		} catch (NoSuchElementException e) {
			log.debug("Can't find Quit Wire button. Source: "
					+ driver.getPageSource());
			throw e;
		}
	}

	@Override
	public void close() throws Exception {
		try {
			signOut();
			quitWireMenuItem.click();
		} catch (Exception e) {
		}
		super.close();
	}

	public void restoreClient() {
		clickWireIconOnDock();
	}

	public void clickWireIconOnDock() {
		clickAppIconOnDock(OSXConstants.Apps.WIRE);
	}

	public void clickAppIconOnDock(String appName) {

		driver.navigate().to(OSXConstants.Apps.DOCK);
		try {
			String xpath = String.format(
					OSXLocators.MainMenuPage.xpathFormatDockApplicationIcon,
					appName);
			driver.findElement(By.xpath(xpath)).click();
		} finally {
			if (driver != null)
				driver.navigate().to(OSXExecutionContext.wirePath);
		}
	}

	public ChoosePicturePage sendImage() throws Exception {
		sendImageMenuItem.click();
		return new ChoosePicturePage(this.getDriver(), this.getWait());
	}
}
