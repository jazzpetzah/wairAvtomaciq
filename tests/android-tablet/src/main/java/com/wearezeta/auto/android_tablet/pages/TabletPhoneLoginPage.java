package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;

import java.util.concurrent.Future;

public class TabletPhoneLoginPage extends AndroidTabletPage {
	public TabletPhoneLoginPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	private WelcomePage getWelcomePage() throws Exception {
		return this.getAndroidPageInstance(WelcomePage.class);
	}

    private AreaCodePage getAreaCodePage() throws Exception {
        return this.getAndroidPageInstance(AreaCodePage.class);
    }

	public void tapCommitButton() throws Exception {
		getWelcomePage().clickConfirm();
	}

    public void inputPhoneNumber(PhoneNumber number) throws Exception {
        getWelcomePage().inputPhoneNumber(number.toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
    }

    public void selectWireCountry() throws Exception {
        getWelcomePage().clickAreaCodeSelector();
        getAreaCodePage().selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
    }
}
