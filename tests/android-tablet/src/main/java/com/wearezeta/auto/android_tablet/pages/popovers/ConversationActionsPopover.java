package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConversationActionsPopover extends AbstractPopoverContainer {
	public static final String idRootLocator = "fl__participant_dialog__main__container";

	private ConversationActionsMenuPage conversationActionsMenuPage;

	public ConversationActionsPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.conversationActionsMenuPage = new ConversationActionsMenuPage(
				lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public void selectMenuItem(String itemName) throws Exception {
		this.conversationActionsMenuPage.selectMenuItem(itemName);
	}
}
