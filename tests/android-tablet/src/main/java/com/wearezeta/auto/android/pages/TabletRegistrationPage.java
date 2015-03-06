package com.wearezeta.auto.android.pages;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegistrationPage extends RegistrationPage {

	public TabletRegistrationPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}
	
	public TabletPersonalInfoPage initProfilePage() throws Exception {
		refreshUITree();
		return new TabletPersonalInfoPage(getDriver(), getWait());
	}

}