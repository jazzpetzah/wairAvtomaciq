package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractParticipantPage extends AbstractPopoverPage {
	public static final By xpathConfirmRemoveButton = By.xpath("//*[@id='confirm' and @value='REMOVE']");

	public static final By xpathRemoveButton =
			By.xpath("//*[@id='fl__participant__tab__container']//*[@id='gtv__participants__right__action']");

	public AbstractParticipantPage(Future<ZetaAndroidDriver> lazyDriver, GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapRemoveButton() throws Exception {
		getElement(xpathRemoveButton).click();
	}

	public void tapConfirmRemovalButton() throws Exception {
		getElement(xpathConfirmRemoveButton).click();
	}
}
