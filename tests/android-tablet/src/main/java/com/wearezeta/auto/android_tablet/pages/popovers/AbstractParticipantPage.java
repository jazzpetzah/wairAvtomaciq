package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractParticipantPage extends AbstractPopoverPage {
	public static final By xpathConfirRemoveButton = By.xpath("//*[@id='confirm' and @value='REMOVE']");

	public static final By xpathRemoveButton = OtherUserPersonalInfoPage.xpathRightActionButton;

	public AbstractParticipantPage(Future<ZetaAndroidDriver> lazyDriver,
			GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapRemoveButton() throws Exception {
		getElement(xpathRemoveButton).click();
	}

	public void tapConfirmRemovalButton() throws Exception {
		getElement(xpathConfirRemoveButton).click();
	}
}
