package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GroupPopover extends AbstractPopoverContainer {
	public final static String idRootLocator = "fl__participant_dialog__root";
	
	private ParticipantsPage participantsPage;

	public GroupPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.participantsPage = new ParticipantsPage(lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public void selectMenuItem(String itemName) throws Exception {
		this.participantsPage.selectMenuItem(itemName);
	}

	public void tapConfirmButton() {
		this.participantsPage.tapConfirmButton();
	}

}
