package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class BlockedConnectionPopover extends AbstractPopoverContainer {
	public static final String idRootLocator = "fl__participant_dialog__main__container";

	private BlockedConnectionPage blockedConnectionPage;

	public BlockedConnectionPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.blockedConnectionPage = new BlockedConnectionPage(lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public void tapUnblockButton() throws Exception {
		this.blockedConnectionPage.tapUnblockButton();
	}
}
