package com.wearezeta.auto.osx.pages;

import java.lang.reflect.Field;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class PagesCollection extends AbstractPagesCollection {

	public static MainMenuPage mainMenuPage = null;

	public static LoginPage loginPage = null;

	public static ContactListPage contactListPage = null;

	public static ConversationPage conversationPage = null;

	public static ChoosePicturePage choosePicturePage = null;

	public static PeoplePickerPage peoplePickerPage = null;

	public static ConversationInfoPage conversationInfoPage = null;

	public static UserProfilePage userProfilePage = null;

	public static RegistrationPage registrationPage = null;

	public static ChangePasswordPage changePasswordPage = null;

	public void closeAllPages() throws Exception {
		for (Field f : this.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (OSXPage.class.isAssignableFrom(f.getType())) {
				OSXPage page = (OSXPage) f.get(this);
				if (page != null) {
					page.close();
				}
			}
		}
	}
}
