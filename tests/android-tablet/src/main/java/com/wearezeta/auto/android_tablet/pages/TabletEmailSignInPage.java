package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletEmailSignInPage extends AndroidTabletPage {
	public static final By idPhoneLoginButton = By.id("ttv__new_reg__sign_in__go_to__phone");

	public TabletEmailSignInPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private EmailSignInPage getEmailSignInPage() throws Exception {
		return this.getAndroidPageInstance(EmailSignInPage.class);
	}

	public void setLogin(String email) throws Exception {
		getEmailSignInPage().setLogin(email);
	}

	public void setPassword(String password) throws Exception {
		getEmailSignInPage().setPassword(password);
	}

	public void tapSignInButton() throws Exception {
		getElement(EmailSignInPage.idLoginButton).click();
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 60;

	public void verifyNotVisible() throws Exception {
		getEmailSignInPage().waitForLogInToComplete(true,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public void acceptErrorMessage() throws Exception {
		getEmailSignInPage().acceptErrorMessage();
	}

    public void tapPhoneLogin() throws Exception {
        getElement(idPhoneLoginButton).click();
    }
}
