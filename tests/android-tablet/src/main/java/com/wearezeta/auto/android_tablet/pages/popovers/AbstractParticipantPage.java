package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractParticipantPage extends AbstractPopoverPage {
	public static final String xpathConfirRemoveButton = "//*[@id='confirm' and @value='REMOVE']";
	@FindBy(xpath = xpathConfirRemoveButton)
	private WebElement confirmRemoveButton;

	public static final String xpathRemoveButton = OtherUserPersonalInfoPage.xpathRightActionButton;
	@FindBy(xpath = xpathRemoveButton)
	private WebElement removeButton;

	public AbstractParticipantPage(Future<ZetaAndroidDriver> lazyDriver,
			GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapRemoveButton() {
		removeButton.click();
	}

	public void tapConfirmRemovalButton() throws Exception {
		confirmRemoveButton.click();
	}
}
