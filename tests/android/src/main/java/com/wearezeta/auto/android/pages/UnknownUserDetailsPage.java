package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class UnknownUserDetailsPage extends AndroidPage {
	@FindBy(xpath = AndroidLocators.UnknownUserDetailsPage.xpathConnectButton)
	private WebElement connectButton;

	@FindBy(xpath = AndroidLocators.UnknownUserDetailsPage.xpathPendingButton)
	private WebElement pendingButton;

	@FindBy(id = AndroidLocators.UnknownUserDetailsPage.idCommonUsersLabel)
	private WebElement commonUsersLabel;

	public UnknownUserDetailsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		return null;
	}

	public boolean isConnectButtonVisible() throws Exception {
		final By locator = By
				.xpath(AndroidLocators.UnknownUserDetailsPage.xpathConnectButton);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean isPendingButtonVisible() throws Exception {
		final By locator = By
				.xpath(AndroidLocators.UnknownUserDetailsPage.xpathPendingButton);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean isNameExistInHeader(String expectedName) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.UnknownUserDetailsPage.xpathHeaderByUserName
						.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public ConnectToPage tapConnectButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), connectButton);
		connectButton.click();
		return new ConnectToPage(this.getLazyDriver());
	}

	public void tapPendingButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), pendingButton);
		pendingButton.click();
	}
}
