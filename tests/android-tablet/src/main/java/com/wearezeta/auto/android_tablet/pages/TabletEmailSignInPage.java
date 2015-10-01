package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletEmailSignInPage extends AndroidTabletPage {

	public TabletEmailSignInPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private EmailSignInPage getEmailSignInPage() throws Exception {
		return (EmailSignInPage) this
				.getAndroidPageInstance(EmailSignInPage.class);
	}

	public void setLogin(String email) throws Exception {
		getEmailSignInPage().setLogin(email);
	}

	public void setPassword(String password) throws Exception {
		getEmailSignInPage().setPassword(password);
	}

	public void tapSignInButton() throws Exception {
		final By loginButtonLocator = By.id(EmailSignInPage.idLoginButton);
		getDriver().findElement(loginButtonLocator).click();
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 60;

	public boolean waitUntilNotVisible() throws Exception {
		try {
			return DriverUtils.waitUntilLocatorDissapears(getDriver(),
					By.id(EmailSignInPage.idLoginInput),
					VISIBILITY_TIMEOUT_SECONDS);
		} finally {
			if (ScreenOrientationHelper.getInstance().fixOrientation(
					getDriver()) == ScreenOrientation.PORTRAIT) {
				if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
						By.id(ContactListPage.idSelfUserAvatar), 3)) {
					// The app will open full-color view instead of doing swipe
					// w/o these two operators :-@
					this.tapOnCenterOfScreen();
					this.tapOnCenterOfScreen();
					DriverUtils.swipeByCoordinates(getDriver(), 1000, 30, 50,
							90, 50);
				}
			}
		}
	}

	public void verifyErrorMessageText(String expectedMsg) throws Exception {
		getEmailSignInPage().verifyErrorMessageText(expectedMsg);
	}

	public void acceptErrorMessage() throws Exception {
		getEmailSignInPage().acceptErrorMessage();
	}
}
