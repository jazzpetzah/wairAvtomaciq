package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class UnknownUserDetailsPage extends AndroidPage {
	private static final Function<String, String> xpathStrHeaderByUserName = name -> String
			.format("//*[@id='taet__participants__header' and @value='%s']", name);

	private static final By xpathConnectButton =
			By.xpath("//*[@id='ttv__participants__left_label' and @value='Connect']");

	private static final By xpathPendingButton =
            By.xpath("//*[@id='ttv__participants__left_label' and @value='Pending']");

	public UnknownUserDetailsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isPendingButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathPendingButton);
	}

	public boolean isNameExistInHeader(String expectedName) throws Exception {
		final By locator = By.xpath(xpathStrHeaderByUserName.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapConnectButton() throws Exception {
		getElement(xpathConnectButton).click();
	}

	public void tapPendingButton() throws Exception {
		getElement(xpathPendingButton).click();
	}
}
