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
		
		public static final String classNameSpinner = "loading-spinner";
	}

	public static final class ContactListPage {
		
		public static final String xpathArchive = "//div[contains(@class, 'conversation-list-item-archive')]//div[contains(@class, 'center-column') and text()='Archive']";

		public static final String xpathParentContactListItem = "//div[@id='conversation-list']";

		public static final String classArchiveButton = "icon-archive";

		public static final String classMuteButton = "icon-silence";

		public static final String classMuteIcon = "conversation-muted";

		public static final String classActionsButton = "icon-more";

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
				+ "/div[2]//li/div[contains(@class, 'center-column')]";

		public static final String classNameOpenPeoplePickerButton = "icon-plus";
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

		public static final String xpathSelfUserNameInput = "//input-element[@class='self-profile-name']/textarea";

		public static final String classNameSelfUserMail = "self-profile-mail";
	}

	public static final class ConversationPage {

		public static final String xpathTextMessageEntry = "//div[contains(@class,'message')]/div[@class='text']";

		public static final String xpathFormatSpecificTextMessageEntry = "//div[contains(@class,'message')]/div[@class='text' and text()='%s']";

		public static final String idConversationInput = "conversation-input-text";

		public static final String classNameShowParticipantsButton = "show-participants";

		public static final String xpathActionMessageEntry = "//div[contains(@class,'special')]//div[@class='action' and contains(text(), '%s')]";

		public static final String xpathSendImageLabel = "//label[contains(@class,'controls-right-button') and contains(@class,'icon-library')]";

		public static final String cssRightControlsPanel = "div.controls-right";

		public static final String cssSendImageLabel = "label.controls-right-button.icon-library.icon-button";

		public static final String xpathSendImageInput = xpathSendImageLabel
				+ "/input[@type='file']";

		public static final String cssSendImageInput = "input[type=\"file\"]";

		public static final String xpathImageMessageEntry = "//div[@class='message-asset-image']";
		
		public static final String xpathPingButton = "//span[contains(@class, 'icon-ping') and contains(@class,'controls-right-button')]";
		
		public static final String classPingMessage = "pinged";
	}

	public static final class ConnectToPage {

		public static final String xpathFormatAcceptRequestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='accept']";

		public static final String xpathFormatIgnoreReqestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='ignore']";
	}

	public static final class PeoplePickerPage {

		public static final String classNameSearchInput = "search-input";

		public static final String xpathFormatSearchListItemWithName = "//div[@class='search-list-item-name' and text()='%s']";

		public static final String classNameCreateConversationButton = "search-button-add";

		public static final String xpathSearhResultList = "//div[@class='search-list-item']";

		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"%s/div[@class='search-list-item-name' and text()='%s']",
				xpathSearhResultList, name);
	}

	public static final class UserProfilePopupPage {

		public static final String idUserProfilePage = "participants-bubble";

		public static final String xpathNameAddPeopleButton = "//div[contains(@class,'footer-button-left') and contains(@class,'icon-plus')]";

		public static final String xpathNameBlockButton = "//div[contains(@class,'footer-button-right') and contains(@class,'icon-block')]";

		public static final String xpathUserName = "//div[@class='name' and @data-uie-name='status-user']";

		public static final String xpathLeaveGroupChat = "//div[contains(@class,'participants-group-leave')]";

		public static final String xpathConfirmLeaveButton = "//div[@class='zeta-button zeta-button-medium' and text()='leave']";

		public static final String xpathConfirmRemoveButton = "//div[@class='zeta-button zeta-button-medium' and text()='remove']";

		public static final String xpathConfirmAddButton = "//div[@class='zeta-button zeta-button-medium' and text()='continue']";

		public static final String xpathParticipantName = "//div[@class='search-list-item-name' and text()='%s']/..";

		public static final String xpathRemoveFromGroupChat = "//div[contains(@class,'footer-button-right') and contains(@class,'icon-leave')]";

		public static final String xpathGroupAddPeopleButton = "//*[contains(@class,'participants-group-add')]";

		public static final String xpathProfilePageSearchField = "//div[@class='participants-search-header']//input[@class='search-input']";

		public static final String xpathAddPeopleMessage = "//div[@class='confirm-content' and span[text()='Add people and share history?']]";
		
		public static final String xpathConversationTitle = "//div[@class='participants-group-header']/div[@class='name']/div";
		
		public static final String xpathConversationTitleInput = "//div[@class='participants-group-header']/div[@class='name']/textarea";

	}

	public static final class ConnectToPopup {

		public static final String idConnectionPopupWindow = "people-picker-user-bubble";

		public static final String xpathUserName = "//div[@id='people-picker-user-bubble']//div[@class='name']";

		public static final String classNameConnectionMessage = "user-profile-connect-message";

		public static final String xpathConnectButton = "//div[@id='people-picker-user-bubble']//div[text()='connect']";

	}

	public static final class Common {

		public static final String CONTACT_LIST_ONE_PERSON_WAITING = "One person waiting";
	}
}
