package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SendInvitationPopoverContainer extends AbstractPopoverContainer {
	private SendInvitationPage sendInvitationPage = null;

	public SendInvitationPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.sendInvitationPage = new SendInvitationPage(lazyDriver, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SendInvitationPopover.xpathRootLocator;
	}

	public String getInvitationText() {
		return this.sendInvitationPage.getInvitationText();
	}

	public void copyToClipboard() throws Exception {
		this.sendInvitationPage.copyToClipboard();
	}
}
