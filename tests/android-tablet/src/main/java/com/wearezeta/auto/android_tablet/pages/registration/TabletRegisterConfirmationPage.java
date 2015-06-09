package com.wearezeta.auto.android_tablet.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegisterConfirmationPage extends AndroidTabletPage {
	public static final Function<String, String> xpathCheckEmailLabelByemail = email -> String
			.format("//*[@id='ttv__sign_up__check_email' and contains(@value, '%s')]",
					email);

	public final static String idResendLink = "ttv__pending_email__resend";

	public TabletRegisterConfirmationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idResendLink), 20);
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idResendLink));
	}

	public boolean waitUntilEmailIsVisible(String expectedEmail)
			throws Exception {
		final By locator = By.xpath(xpathCheckEmailLabelByemail
				.apply(expectedEmail));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

}
