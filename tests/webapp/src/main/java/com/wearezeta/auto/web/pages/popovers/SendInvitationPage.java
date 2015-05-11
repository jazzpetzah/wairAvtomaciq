package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SendInvitationPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SendInvitationPopover.SendInvitationPage.xpathInvitationText)
	private WebElement invitationText;

	public SendInvitationPage(Future<ZetaWebAppDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SendInvitationPopover.SendInvitationPage.xpathInvitationText;
	}

	public String getInvitationText() {
		return invitationText.getText();
	}

	public void copyToClipboard() {
		invitationText.click();
		invitationText
				.sendKeys(Keys.chord(WebAppExecutionContext
						.isCurrentPlatfromWindows() ? Keys.CONTROL
						: Keys.COMMAND, "c"));
	}
}
