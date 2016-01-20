package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class BlockedConnectionPage extends AbstractPopoverPage {

	private static final By idUnblockButton = By.id("zb__connect_request__unblock_button");

	public BlockedConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			BlockedConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}
	
	public void tapUnblockButton() throws Exception {
		getElement(idUnblockButton).click();
	}

}
