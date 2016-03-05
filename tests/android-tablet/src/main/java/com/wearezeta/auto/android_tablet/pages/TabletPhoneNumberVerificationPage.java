package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.registration.PhoneNumberVerificationPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletPhoneNumberVerificationPage extends AndroidTabletPage {
	public TabletPhoneNumberVerificationPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	private PhoneNumberVerificationPage getPhoneNumberVerificationPage() throws Exception {
		return this.getAndroidPageInstance(PhoneNumberVerificationPage.class);
	}

	public void tapCommitButton() throws Exception {
        getPhoneNumberVerificationPage().clickConfirm();
	}

    public void inputConfirmationCode(String code) throws Exception {
        getPhoneNumberVerificationPage().inputVerificationCode(code);
    }
}
