package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	@FindBy(how = How.XPATH, using = AndroidLocators.SettingsPage.xpathSettingPageTitle)
	private WebElement settingsTitle;

	public SettingsPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
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
}
