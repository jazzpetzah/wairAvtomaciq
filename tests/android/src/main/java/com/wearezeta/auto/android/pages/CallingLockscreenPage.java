package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CallingLockscreenPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LockscreenCallingPage.CLASS_NAME, locatorKey = "idLockScreenLogo")
	private WebElement lockScreenLogo;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LockscreenCallingPage.CLASS_NAME, locatorKey = "idCallingUserName")
	private WebElement callingUsersName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LockscreenCallingPage.CLASS_NAME, locatorKey = "idIncomingCallChathead")
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
		return DriverUtils.isElementDisplayed(this.getDriver(), lockScreenLogo);
	}

	public String getCallersName() throws Exception {
		refreshUITree();
		return callingUsersName.getText();
	}

	public CallingOverlayPage acceptCall() throws Exception {
		refreshUITree();
		DriverUtils.swipeRight(this.getDriver(), incomingCallChathead, 1500,
				200, 50);
		return new CallingOverlayPage(getLazyDriver());
	}

}
