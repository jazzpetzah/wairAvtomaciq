package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;


public class CallingLockscreenPage extends AndroidPage {

	@FindBy(id = AndroidLocators.LockscreenCallingPage.idLockScreenLogo)
	private WebElement lockScreenLogo;

	@FindBy(id = AndroidLocators.LockscreenCallingPage.idCallingUserName)
	private WebElement callingUsersName;

	@FindBy(id = AndroidLocators.LockscreenCallingPage.idIncomingCallChathead)
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
		return DriverUtils.isElementPresentAndDisplayed(lockScreenLogo);
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
