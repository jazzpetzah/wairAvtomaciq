package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class WebAppLocators {

	public static final class InvitationCodePage {

		public static final String idCodeInput = "code";

		public static final String xpathProceedButton = "//button[contains(text(),'Proceed')]";
	}

	public static final class LoginPage {

		public static final String idLoginPage = "login-page";

		public static final String idEmailInput = "wire-email";

		public static final String idPasswordInput = "wire-password";

		public static final String idLoginButton = "wire-login";
	}

	public static final class ContactListPage {
		public static final String xpathArchive = "//div[contains(@class, 'conversation-list-item-archive')]//div[contains(@class, 'center-column') and text()='Archive']";
		
		public static final String xpathParentContactListItem = "//div[@id='conversation-list']";
		
		public static final String classArchiveButton = "zi-archive";
		
		public static final String classActionsButton = "zi-actions";
		
		public static final String xpathSelfProfileEntry = xpathParentContactListItem
				+ "//div[contains(@class, 'center-column')]";

		// index starts from 1
		// self name is not included
		public static final Function<Integer, String> xpathContactListEntryByIndex = (
				index) -> String.format(
				"%s/div[2]//ul/li[%d]//div[contains(@class, 'center-column')]",
				xpathParentContactListItem, index);

		public static final Function<String, String> xpathContactListEntryByName = (
				name) -> String
				.format("%s/div[2]//li[div[contains(@class, 'center-column') and text()='%s']]",
						xpathParentContactListItem, name);

		public static final String xpathContactListEntries = xpathParentContactListItem
				+ "/div[2]//li";
	}

	public static final class SettingsPage {
		public static final String xpathSettingsDialogRoot = "//div[@id='self-settings' and contains(@class, 'modal-show')]";
	}

	public static final class SelfProfilePage {
		public static final String xpathGearButton = "//div[@id='show-settings']";

		public static final String xpathGearMenuRoot = "//div[@id='setting-bubble' and contains(@class, 'bubble-show')]";

		public static final Function<String, String> xpathGearMenuItemByName = (
				name) -> String.format("%s//a[text()='%s']", xpathGearMenuRoot,
				name);
				
		public static final String xpathSelfUserName = "//input-element[@class='self-profile-name']/span";
		
		public static final String classNameSelfUserMail = "self-profile-mail";
	}

	public static final class ConversationPage {

		public static final String xpathMessageEntry = "//div[@class='message']/div[@class='text']";

		public static final String xpathFormatSpecificMessageEntry = "//div[@class='message']/div[@class='text' and text()='%s']";

		public static final String idConversationInput = "conversation-input-text";
	}
}
