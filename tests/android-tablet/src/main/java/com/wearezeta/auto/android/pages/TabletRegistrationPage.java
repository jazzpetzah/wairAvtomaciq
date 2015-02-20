package com.wearezeta.auto.android.pages;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletRegistrationPage extends RegistrationPage {

	public TabletRegistrationPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}
	
	public PersonalInfoPage initProfilePage() throws Exception {
		refreshUITree();
		return new PersonalInfoPage(getDriver(), getWait());
	}

}