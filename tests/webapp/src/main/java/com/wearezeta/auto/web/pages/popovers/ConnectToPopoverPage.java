package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class ConnectToPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.ConnectToPopover.ConnectToPage.xpathConnectButton)
	private WebElement connectButton;

	public ConnectToPopoverPage(ZetaWebAppDriver driver, WebDriverWait wait,
			ConnectToPopoverContainer container) throws Exception {
		super(driver, wait, container);
	}

	public void clickConnectButton() {
		connectButton.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.ConnectToPage.xpathConnectButton;
	}
}
