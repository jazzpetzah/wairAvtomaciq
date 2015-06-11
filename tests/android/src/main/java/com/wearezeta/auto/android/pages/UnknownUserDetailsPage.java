package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class UnknownUserDetailsPage extends AndroidPage {
	private static final Function<String, String> xpathHeaderByUserName = name -> String
			.format("//*[@id='taet__participants__header']", name);

	private static final String xpathConnectButton = "//*[@id='ttv__participants__left_label' and @value='Connect']";
	@FindBy(xpath = xpathConnectButton)
	private WebElement connectButton;

	private static final String xpathPendingButton = "//*[@id='ttv__participants__left_label' and @value='Pending']";
	@FindBy(xpath = xpathPendingButton)
	private WebElement pendingButton;

	private static final String idCommonUsersLabel = "ttv__connect_request__common_users__label";
	@FindBy(id = idCommonUsersLabel)
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
		final By locator = By.xpath(xpathConnectButton);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean isPendingButtonVisible() throws Exception {
		final By locator = By.xpath(xpathPendingButton);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean isNameExistInHeader(String expectedName) throws Exception {
		final By locator = By.xpath(xpathHeaderByUserName.apply(expectedName));
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
