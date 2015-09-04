package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

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

	public TabletConversationsListPage tapSignInButton() throws Exception {
		final By loginButtonLocator = By.id(EmailSignInPage.idLoginButton);
		getDriver().findElement(loginButtonLocator).click();
		return new TabletConversationsListPage(getLazyDriver());
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 60;

	public boolean waitUntilNotVisible() throws Exception {
		try {
			return DriverUtils.waitUntilLocatorDissapears(getDriver(),
					By.id(EmailSignInPage.idLoginInput),
					VISIBILITY_TIMEOUT_SECONDS);
		} finally {
			ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
		}
	}

	public void verifyErrorMessageText(String expectedMsg) throws Exception {
		getEmailSignInPage().verifyErrorMessageText(expectedMsg);
	}

	public void acceptErrorMessage()  throws Exception  {
		getEmailSignInPage().acceptErrorMessage();
	}

}
