package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class TabletLoginPage extends LoginPage {
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletLoginPage.CLASS_NAME, locatorKey = "idTabletSignUpButton")
	protected WebElement signUpButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletLoginPage.CLASS_NAME, locatorKey = "idTabletSignInButton")
	protected WebElement signInButton;
	
	private String url;
	private String path;
	
	public TabletLoginPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public TabletLoginPage(String URL, String path, boolean isUnicode) throws Exception {
		super(URL, path, isUnicode);
		this.url = URL;
		this.path = path;
	}
	
	public void doLogIn() throws Exception {
		confirmSignInButton.click();
	}
	
	public PersonalInfoPage initProfilePage() throws Exception {
		return new PersonalInfoPage(url, path);
	}
	
	public TabletContactListPage initContactListPage() throws Exception {
		return new TabletContactListPage(url, path);
	}
	
	public TabletRegistrationPage tabletJoin() throws Exception {
		signUpButton.click();
		return new TabletRegistrationPage(url, path);
	}
	
	public LoginPage tabletSignIn() throws IOException {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(signInButton));
		signInButton.click();
		return this;
	}
	
	public static void clearTabletPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(TabletPagesCollection.class, AndroidPage.class);
	}
}