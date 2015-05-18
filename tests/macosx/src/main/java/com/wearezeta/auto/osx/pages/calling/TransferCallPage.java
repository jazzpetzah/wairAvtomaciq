package com.wearezeta.auto.osx.pages.calling;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class TransferCallPage extends CallPage {

	@FindBy(how = How.NAME, using = OSXLocators.CallPage.nameTransferCallHereButton)
	private WebElement transferCallHereButton;

	public TransferCallPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(OSXLocators.CallPage.nameTransferCallHereButton));
	}

	public StartedCallPage transferCall() throws Exception {
		transferCallHereButton.click();
		return new StartedCallPage(this.getLazyDriver());
	}
}
