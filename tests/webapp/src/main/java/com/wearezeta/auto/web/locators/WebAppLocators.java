package com.wearezeta.auto.web.locators;

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

		public static final String xpathContactListEntry = "//div[@class='center-column']";

		public static final String xpathFormatContactEntryWithName = "//div[@class='center-column' and text()='%s']";
	}

	public static final class ConversationPage {

		public static final String xpathMessageEntry = "//div[@class='message']/div[@class='text']";

		public static final String xpathFormatSpecificMessageEntry = "//div[@class='message']/div[@class='text' and text()='%s']";

		public static final String idConversationInput = "conversation-input-text";
	}
}
