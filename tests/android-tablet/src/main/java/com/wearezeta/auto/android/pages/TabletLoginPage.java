package com.wearezeta.auto.android.pages;

public class TabletLoginPage extends LoginPage {
	
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
}