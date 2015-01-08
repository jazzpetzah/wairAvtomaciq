package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class MainMenuPage extends OSXPage {
	// private static final Logger log = ZetaLogger.getLog(MainMenuPage.class.getSimpleName());
	
	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.nameSignOutMenuItem)
	private WebElement signOutMenuItem;
	
	public MainMenuPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public void SignOut() throws Exception {
		// signOutMenuItem.click();
		quitZClient();
		OSXCommonUtils.deleteZClientLoginFromKeychain();
		OSXCommonUtils.deleteCacheFolder();
		Thread.sleep(1000);
		driver.navigate().to(CommonUtils.getOsxApplicationPathFromConfig(MainMenuPage.class));
	}
	
	public void quitZClient() {
		quitZClientMenuItem.click();
	}
	
	@Override
	public void Close() throws IOException {
		try {
			SignOut();
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
