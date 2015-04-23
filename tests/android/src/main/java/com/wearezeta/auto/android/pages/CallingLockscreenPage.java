package com.wearezeta.auto.android.pages;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	public CallingLockscreenPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(), lockScreenLogo);
	}

	public String getCallersName() {
		refreshUITree();
		return callingUsersName.getText();
	}

	public CallingOverlayPage acceptCall() throws Exception {
		refreshUITree();
		DriverUtils.swipeRight(this.getDriver(), incomingCallChathead, 1500, 200, 50);
		return new CallingOverlayPage(getDriver(), getWait());
	}

}
