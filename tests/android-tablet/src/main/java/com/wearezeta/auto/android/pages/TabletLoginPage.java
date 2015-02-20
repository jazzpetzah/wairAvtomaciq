package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class TabletLoginPage extends LoginPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletLoginPage.CLASS_NAME, locatorKey = "idTabletSignUpButton")
	protected WebElement signUpButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletLoginPage.CLASS_NAME, locatorKey = "idTabletSignInButton")
	protected WebElement signInButton;
	
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
	
	public PersonalInfoPage initProfilePage() throws Exception {
		return new PersonalInfoPage(getDriver(), getWait());
	}
	
	public TabletContactListPage initContactListPage() throws Exception {
		return new TabletContactListPage(getDriver(), getWait());
	}
	
	public TabletRegistrationPage tabletJoin() throws Exception {
		signUpButton.click();
		return new TabletRegistrationPage(getDriver(), getWait());
	}
	
	public LoginPage tabletSignIn() throws IOException {
		refreshUITree();
		getWait().until(ExpectedConditions.visibilityOf(signInButton));
		signInButton.click();
		return this;
	}
	
	public static void clearTabletPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(TabletPagesCollection.class, AndroidPage.class);
	}
}