package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {
	public static final String idRootLocator = "fl__message_stream_land_container";

	@FindBy(id = AndroidLocators.DialogPage.idParticipantsBtn)
	private WebElement showDetailsButton;

	public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idRootLocator));
	}

	public void tapShowDetailsButton() throws Exception {
		showDetailsButton.click();
	}
}
