package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CallingLockscreenPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LockscreenCallingPage.CLASS_NAME, locatorKey = "idLockScreenLogo")
	private List<WebElement> lockScreenLogos;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isVisible() throws Exception {
		refreshUITree();
		if (lockScreenLogos.size() > 0) {
			return true;
		}
		return false;
	}

	public String getCallersName() {
		refreshUITree();
		return callingUsersName.getText();
	}
	
	public CallingOverlayPage acceptCall() throws Exception {
		refreshUITree();
		elementSwipeRight(incomingCallChathead, 1500);
		return new CallingOverlayPage(getDriver(), getWait());
	}

}
