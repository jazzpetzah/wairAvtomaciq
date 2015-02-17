package com.wearezeta.auto.osx.pages;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class MainMenuPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(MainMenuPage.class.getSimpleName());
	
	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.nameSignOutMenuItem)
	private WebElement signOutMenuItem;
	
	public MainMenuPage(String URL, String path) throws Exception {
		super(URL, path);
	}

	public void SignOut() throws Exception {
		// signOutMenuItem.click();
		quitZClient();
		try {
			OSXCommonUtils.deleteZClientLoginFromKeychain();
			OSXCommonUtils.removeAllZClientSettingsFromDefaults();
			OSXCommonUtils.deleteCacheFolder();
			OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(LoginPage.class));
		} catch (Exception ex) {
			log.error("Can't clear ZClient settings in OSX.\n" + ex.getMessage());
		}
		try { Thread.sleep(1000); } catch (InterruptedException e) { }

		driver.navigate().to(CommonUtils.getOsxApplicationPathFromConfig(MainMenuPage.class));
	}
	
	public void quitZClient() throws IOException {
		try {
			quitZClientMenuItem.click();
		} catch (NoSuchElementException e) {
			log.debug("Can't find Quit Wire button. Source: " + driver.getPageSource());
			throw e;
		}
	}
	
	@Override
	public void Close() throws Exception {
		try {
			SignOut();
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
