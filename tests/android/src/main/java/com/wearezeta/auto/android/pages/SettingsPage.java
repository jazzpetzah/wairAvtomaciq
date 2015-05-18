package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	@FindBy(how = How.XPATH, using = AndroidLocators.SettingsPage.xpathSettingPageTitle)
	private WebElement settingsTitle;

	@FindBy(how = How.XPATH, using = AndroidLocators.SettingsPage.xpathSettingPageChangePassword)
	private WebElement settingsChangePassword;

	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSettingsPageVisible() {
		return settingsTitle.isDisplayed();
	}

	public boolean isChangePasswordVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(settingsChangePassword);
	}

	public CommonAndroidPage clickChangePassword() throws Exception {
		settingsChangePassword.click();
		Thread.sleep(2000); // Fix to pass animation
		return new CommonAndroidPage(this.getLazyDriver());
	}
}
