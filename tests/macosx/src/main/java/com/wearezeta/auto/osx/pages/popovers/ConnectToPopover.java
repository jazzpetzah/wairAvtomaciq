package com.wearezeta.auto.osx.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.PopoverLocators;

public class ConnectToPopover extends PopoverPage {

	@FindBy(how = How.ID, using = PopoverLocators.ConnectToPopover.idSendRequestButton)
	private WebElement connectButton;

	public ConnectToPopover(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(PopoverLocators.ConnectToPopover.idSendRequestButton));
	}

	public void sendConnectionRequest() {
		connectButton.click();
	}
}
