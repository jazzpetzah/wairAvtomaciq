package com.wearezeta.auto.osx.pages.common;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;

public class MainMenuAndDockPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(MainMenuAndDockPage.class.getSimpleName());

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameSendImageMenuItem)
	private WebElement sendImageMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameQuitWireMenuItem)
	private WebElement quitWireMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.MainMenuPage.nameSignOutMenuItem)
	private WebElement signOutMenuItem;

	public MainMenuAndDockPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver, OSXExecutionContext.wirePath);
	}

	public void signOut() throws Exception {
		// signOutMenuItem.click();
		quitWire();
		try {
			OSXCommonUtils.deleteWireLoginFromKeychain();
			OSXCommonUtils.deleteCacheFolder();
		} catch (Exception ex) {
			log.error("Can't clear Wire settings in OSX.\n" + ex.getMessage());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		this.startApp();
	}

	public void quitWire() throws Exception {
		try {
			quitWireMenuItem.click();
		} catch (NoSuchElementException e) {
			log.debug("Can't find Quit Wire button. Source: "
					+ this.getDriver().getPageSource());
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

	public void restoreClient() throws Exception {
		clickWireIconOnDock();
	}

	public void clickWireIconOnDock() throws Exception {
		clickAppIconOnDock(OSXConstants.Apps.WIRE);
	}

	public void clickAppIconOnDock(String appName) throws Exception {
		this.getDriver().navigate().to(OSXConstants.Apps.DOCK);
		try {
			String xpath = String.format(
					OSXLocators.MainMenuPage.xpathFormatDockApplicationIcon,
					appName);
			getDriver().findElement(By.xpath(xpath)).click();
		} finally {
			if (this.getDriver() != null)
				this.getDriver().navigate().to(OSXExecutionContext.wirePath);
		}
	}

	public ChoosePicturePage sendImage() throws Exception {
		sendImageMenuItem.click();
		return new ChoosePicturePage(this.getLazyDriver());
	}
}
