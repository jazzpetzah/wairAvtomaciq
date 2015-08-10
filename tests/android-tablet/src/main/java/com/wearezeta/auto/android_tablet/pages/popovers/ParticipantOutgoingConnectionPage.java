package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class ParticipantOutgoingConnectionPage extends AbstractOutgoingConnectionPage {

	private static final String idConnectButton = "gtv__participants__left__action";

	private static final String xpathPendingButton = "//*[@id='ttv__participants__left_label' and @value='Pending']";

	public ParticipantOutgoingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver, GroupPopover container)
			throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected By getBlockButtonLocator() {
		return By.id(idConnectButton);
	}

	public boolean waitUntilPendingButtonIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathPendingButton));
	}

}
