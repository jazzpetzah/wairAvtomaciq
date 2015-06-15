package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingLockscreenPage extends AndroidPage {

	private static final String idLockScreenLogo = "gtv__notifications__incoming_call__lockscreen__logo";
	@FindBy(id = idLockScreenLogo)
	private WebElement lockScreenLogo;

	private static final String idCallingUserName = "ttv__notifications__incoming_call__lockscreen__header";
	@FindBy(id = idCallingUserName)
	private WebElement callingUsersName;

	private static final String idIncomingCallChathead = "civ__notifications__incoming_call__chathead";
	@FindBy(id = idIncomingCallChathead)
	private WebElement incomingCallChathead;

	public CallingLockscreenPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public boolean isVisible() throws Exception {
		final By locator = By.id(idLockScreenLogo);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public String getCallersName() throws Exception {
		return callingUsersName.getText();
	}

	public CallingOverlayPage acceptCall() throws Exception {
		DriverUtils.swipeRight(this.getDriver(), incomingCallChathead, 1500,
				200, 50);
		return new CallingOverlayPage(getLazyDriver());
	}

}
