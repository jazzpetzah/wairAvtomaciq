package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class PendingOutgoingConnectionPopoverPage extends AbstractPopoverPage {
	@FindBy(xpath = PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathCancelRequestButton)
	private WebElement cancelRequestButton;
    @FindBy(css = PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.cssUniqueUsernameOutgoing)
    private WebElement uniqueUsernameOutgoing;

	public PendingOutgoingConnectionPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			ConnectToPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathCancelRequestButton;
	}

	public void clickCancelRequestButton() {
		cancelRequestButton.click();
	}

	public String getUniqueUsernameOutgoing() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), uniqueUsernameOutgoing);
		return uniqueUsernameOutgoing.getText();
	}
}
