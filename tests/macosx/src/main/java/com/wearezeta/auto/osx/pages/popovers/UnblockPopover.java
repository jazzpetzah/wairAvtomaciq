package com.wearezeta.auto.osx.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.osx.locators.PopoverLocators;

public class UnblockPopover extends PopoverPage {

	@FindBy(how = How.ID, using = PopoverLocators.UnblockPopover.idUnblockButton)
	private WebElement unblockButton;

	public UnblockPopover(Future<ZetaOSXDriver> lazyDriver,
			Future<ZetaWebAppDriver> secondaryDriver) throws Exception {
		super(lazyDriver, secondaryDriver);
	}

	public void unblock() {
		unblockButton.click();
	}
}
