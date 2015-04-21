package com.wearezeta.auto.osx.pages.popovers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.PopoverLocators;

public class ConnectToPopover extends PopoverPage {

	@FindBy(how = How.ID, using = PopoverLocators.ConnectToPopover.idSendRequestButton)
	private WebElement connectButton;

	public ConnectToPopover(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(PopoverLocators.ConnectToPopover.idSendRequestButton));
	}

	public void sendConnectionRequest() {
		connectButton.click();
	}
}
