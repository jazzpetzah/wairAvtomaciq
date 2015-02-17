package com.wearezeta.auto.android.pages;

public class TabletRegistrationPage extends RegistrationPage {
	private String url;
	private String path;

	public TabletRegistrationPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public PersonalInfoPage initProfilePage() throws Exception {
		refreshUITree();
		return new PersonalInfoPage(url, path);
	}
}