package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;

public class SettingsPage extends AndroidPage {

	@FindBy(how = How.XPATH, using = AndroidLocators.xpathSettingPageTitle)
	private WebElement settingsTitle;
	
	public SettingsPage(String URL, String path) throws Exception {
		super(URL, path);
		// TODO Auto-generated constructor stub
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
