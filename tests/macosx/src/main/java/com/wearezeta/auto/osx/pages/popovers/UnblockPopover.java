package com.wearezeta.auto.osx.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.PopoverLocators;

public class UnblockPopover extends PopoverPage {

	@FindBy(how = How.ID, using = PopoverLocators.UnblockPopover.idUnblockButton)
	private WebElement unblockButton;

	public UnblockPopover(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void unblock() {
		unblockButton.click();
	}
}
