package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.CommonSteps;
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
	private static final int percentageToSpotifyPasswordField = 84;
	private static final int percentageToSpotifyConfirmLogin = 65;

	private static final String xpathSettingPageTitle = "//*[@id='title' and @value='Settings']";

	private static final String xpathSettingPageChangePassword = "//*[@id='title' and @value='Change Password']";

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	
	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsPageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageTitle), 5);
	}

	public boolean isChangePasswordVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageChangePassword));
	}

	public void clickServicesButton() throws Exception {
		servicesButton.click();
		DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathServicesButton));
	}

	public void clickConnectWithSpotifyButton() throws Exception {
		connectToSpotifyButton.click();
		DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathConnectToSpotifyButton), 3);
	}

	public void openSpotifyLoginFields() throws Exception {
		DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.id(idSpotifyWebView));
		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyLoginButton);
	}

	public void enterSpotifyUsername(String username) throws Exception {
		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyUsernameField);
		commonSteps.WaitForTime(2);
		AndroidCommonUtils.type(username);
	}

	public void enterSpotifyPassword(String password) throws Exception {
		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyPasswordField);
		commonSteps.WaitForTime(3);
		AndroidCommonUtils.type(password);
	}

	public void navigateBackToSettingsScreen() throws Exception {
		// hides keyboard
		AndroidCommonUtils.tapBackButton();
		DriverUtils.tapOnPercentOfElement(getDriver(), spotifyWebView, 50,
			percentageToSpotifyConfirmLogin);
	}

	public boolean doesSpotifyOptionSayDisconnect() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathDisconnectFromSpotifyButton));
	}
}
