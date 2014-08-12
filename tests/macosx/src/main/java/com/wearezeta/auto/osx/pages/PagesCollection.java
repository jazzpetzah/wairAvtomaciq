package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.lang.reflect.Field;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class PagesCollection extends AbstractPagesCollection {
	private MainMenuPage mainMenuPage = null;
	private LoginPage loginPage = null;
	private ContactListPage contactListPage = null;
	private ConversationPage conversationPage = null;
	private ChoosePicturePage choosePicturePage = null;
	private PeoplePickerPage peoplePickerPage = null;
	private ConversationInfoPage conversationInfoPage = null;
	private UserProfilePage userProfilePage = null;
	private RegistrationPage registrationPage = null;
	
	public void closeAllPages() throws IOException, IllegalArgumentException, IllegalAccessException {
		for (Field f : this.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (OSXPage.class.isAssignableFrom(f.getType())) {
				OSXPage page = (OSXPage) f.get(this);
				if (page != null) {
					page.Close();
				}
			}
		}
	}
	
	public LoginPage getLoginPage() {
		return loginPage;
	}
	
	public void setLoginPage(LoginPage loginPage) {
		this.loginPage = loginPage;
	}
	
	public ContactListPage getContactListPage() {
		return contactListPage;
	}
	
	public void setContactListPage(ContactListPage contactListPage) {
		this.contactListPage = contactListPage;
	}
	
	public ConversationPage getConversationPage() {
		return conversationPage;
	}
	
	public void setConversationPage(ConversationPage conversationPage) {
		this.conversationPage = conversationPage;
	}

	public ChoosePicturePage getChoosePicturePage() {
		return choosePicturePage;
	}

	public void setChoosePicturePage(ChoosePicturePage choosePicturePage) {
		this.choosePicturePage = choosePicturePage;
	}

	public PeoplePickerPage getPeoplePickerPage() {
		return peoplePickerPage;
	}

	public void setPeoplePickerPage(PeoplePickerPage peoplePickerPage) {
		this.peoplePickerPage = peoplePickerPage;
	}

	public ConversationInfoPage getConversationInfoPage() {
		return conversationInfoPage;
	}

	public void setConversationInfoPage(ConversationInfoPage conversationInfoPage) {
		this.conversationInfoPage = conversationInfoPage;
	}

	public UserProfilePage getUserProfilePage() {
		return userProfilePage;
	}

	public void setUserProfilePage(UserProfilePage userProfilePage) {
		this.userProfilePage = userProfilePage;
	}

	public RegistrationPage getRegistrationPage() {
		return registrationPage;
	}

	public void setRegistrationPage(RegistrationPage registrationPage) {
		this.registrationPage = registrationPage;
	}

	public MainMenuPage getMainMenuPage() {
		return mainMenuPage;
	}

	public void setMainMenuPage(MainMenuPage mainMenuPage) {
		this.mainMenuPage = mainMenuPage;
	}
	
}
