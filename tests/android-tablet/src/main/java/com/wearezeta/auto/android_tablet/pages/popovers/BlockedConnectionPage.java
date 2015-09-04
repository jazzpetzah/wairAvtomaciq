package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class BlockedConnectionPage extends AbstractPopoverPage {

	private static final String idUnblockButton = "zb__connect_request__unblock_button";
	@FindBy(id = idUnblockButton)
	private WebElement unblockButton;

	public BlockedConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			BlockedConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}
	
	public void tapUnblockButton() {
		unblockButton.click();
	}

}
