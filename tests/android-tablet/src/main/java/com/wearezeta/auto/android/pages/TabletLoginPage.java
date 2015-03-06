package com.wearezeta.auto.android.pages;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletLoginPage extends LoginPage {
	
	public TabletLoginPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}
	
	public TabletLoginPage(ZetaAndroidDriver driver, WebDriverWait wait, boolean isUnicode) throws Exception {
		super(driver, wait);
	}
	
	public void doLogIn() throws Exception {
		confirmSignInButton.click();
	}
	
	public TabletPersonalInfoPage initProfilePage() throws Exception {
		return new TabletPersonalInfoPage(getDriver(), getWait());
	}
	
	public TabletContactListPage initContactListPage() throws Exception {
		return new TabletContactListPage(getDriver(), getWait());
	}
	
	public TabletRegistrationPage tabletJoin() throws Exception {
		signUpButton.click();
		return new TabletRegistrationPage(getDriver(), getWait());
	}
	
	public static void clearTabletPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(TabletPagesCollection.class, AndroidPage.class);
	}
}