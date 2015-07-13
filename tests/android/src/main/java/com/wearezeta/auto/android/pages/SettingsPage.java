package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	private static final String xpathServicesButton = "//*[@value='Services']";
	@FindBy(xpath = xpathServicesButton)
	private WebElement servicesButton;

	private static final String xpathConnectToSpotifyButton = "//*[@value='Connect with Spotify']";
	@FindBy(xpath = xpathConnectToSpotifyButton)
	private WebElement connectToSpotifyButton;

	private static final String xpathDisconnectFromSpotifyButton = "//*[@value='Disconnect with Spotify']";

	private static final String idSpotifyWebView = "com_spotify_sdk_login_webview";
	@FindBy(id = idSpotifyWebView)
	private WebElement spotifyWebView;

	// Elements in the web view need to be clicked by percentage
	private static final int percentageToSpotifyLoginButton = 38;
	private static final int percentageToSpotifyUsernameField = 37;
	private static final int percentageToSpotifyPasswordField = 45;
	private static final int percentageToSpotifyConfirmLogin = 65;

	private static final String xpathSettingPageTitle = "//*[@id='title' and @value='Settings']";

	private static final String xpathSettingPageChangePassword = "//*[@id='title' and @value='Change Password']";

	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsPageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageTitle));
	}

	public boolean isChangePasswordVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageChangePassword));
	}

	public void clickServicesButton() throws Exception {
		servicesButton.click();
	}

	public void clickConnectWithSpotifyButton() throws Exception {
		connectToSpotifyButton.click();
	}

	public void enterSpotifyCredentials(String username, String password)
		throws Exception {
		DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.id(idSpotifyWebView));
		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyLoginButton);

		Thread.sleep(1000);
		AndroidCommonUtils.type(username);

		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyPasswordField);

		Thread.sleep(1000);
		AndroidCommonUtils.type(password);

		AndroidCommonUtils.tapBackButton();

		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyConfirmLogin);
	}

	public boolean doesSpotifyOptionSayDisconnect() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathDisconnectFromSpotifyButton));
	}
}
