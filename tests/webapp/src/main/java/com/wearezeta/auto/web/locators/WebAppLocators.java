package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class WebAppLocators {

	public static final class InvitationCodePage {

		public static final String idCodeInput = "code";

		public static final String xpathProceedButton = "//button[contains(text(),'Proceed')]";
	}

	public static final class LoginPage {

		public static final String xpathLoginPage = "//*[@data-uie-name='go-wire-dot-com']";

		public static final String xpathEmailInput = "//*[@data-uie-name='enter-email']";

		public static final String xpathPasswordInput = "//*[@data-uie-name='enter-password']";

		public static final String classNameSpinner = "loading-spinner";

		public static final String xpathSwitchToRegisterButton = "//*[@data-uie-name='go-register]";

		public static final String xpathCreateAccountButton = "//*[@data-uie-name='do-register']";

		public static final String xpathSignInButton = "//*[@data-uie-name='do-sign-in']";
	}

	public static final class ContactListPage {

		public static final String xpathArchive = "//div[contains(@class, 'conversation-list-item-archive')]";

		public static final String xpathParentContactListItem = "//div[@id='conversation-list']";

		public static final String classArchiveButton = "icon-archive";

		public static final String classMuteButton = "icon-silence";

		public static final String classMuteIcon = "conversation-muted";

		public static final String classActionsButton = "icon-more";

		public static final String xpathFormatActionsButton = "//li[contains(@class,'conversation-list-item') and @data-uie-value='%s']"
				+ "//span[contains(@data-uie-name,'go-options')]";

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

		public static final String xpathOpenPeoplePickerButton = "//*[@data-uie-name='go-search']";

		public static final String cssOpenPeoplePickerButton = "*[data-uie-name='go-search']";

		public static final String classNamePeoplePickerVisible = "people-picker-is-visible";
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

		public static final String xpathShowParticipantsButton = "//*[@data-uie-name='do-participants']";

		public static final String xpathActionMessageEntries = "//div[contains(@class,'special')]//div[@class='action']";

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

		public static final String xpathCloseSearchButton = "//div[contains(@class,'search-close')]";
	}

	public static final class ConversationPopupPage {

		public static final String idConversationPopupPage = "participants-bubble";
		private static final String xpathConversationPopupPage = "//div[@id='participants-bubble']";

		public static final String xpathAddPeopleMessage = xpathConversationPopupPage
				+ "//div[contains(@class, 'confirm-content')]";

		public static final String xpathConfirmAddButton = xpathConversationPopupPage
				+ "//*[@data-uie-name='do-confirm' and @data-uie-value='continue']";

		public static final String xpathProfilePageSearchHeader = "//div[contains(@class, 'participants-search-header')]";

		public static final String xpathProfilePageSearchField = xpathProfilePageSearchHeader
				+ "//input";
	}

	public static final class ParticipantsProfilePopupPage {

		private static final String xpathFooterDiv = "//div[contains(@class, 'participants-group-footer')]";

		public static final String xpathAddPeopleButton = xpathFooterDiv
				+ "/div[contains(@class, 'icon-plus')]";

		public static final String xpathLeaveGroupChat = xpathFooterDiv
				+ "/div[contains(@class, 'icon-leave')]";

		public static final String xpathConfirmLeaveButton = "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";

		public static final String xpathConfirmRemoveButton = "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";

		public static final String xpathRemoveFromGroupChat = "//*[@data-uie-name='do-remove']";

		private static final String xpathHeaderDiv = "//div[contains(@class, 'participants-group-header')]";

		public static final String xpathConversationTitle = xpathHeaderDiv
				+ "/div[contains(@class, 'name')]/div";

		public static final String xpathConversationTitleInput = xpathHeaderDiv
				+ "/div[contains(@class, 'name')]/textarea";
	}

	public static final class UserProfilePopupPage {

		public static final String xpathAddPeopleButton = "//*[@data-uie-name='do-add-people']";

		public static final String xpathNameBlockButton = "//*[@data-uie-name='do-block']";

		public static final String xpathUserName = "//*[@data-uie-name='status-user']";

		public static final String xpathParticipantName = "//div[@class='search-list-item-name' and text()='%s']/..";
	}

	public static final class ConnectToPopup {

		public static final String idConnectionPopupWindow = "people-picker-user-bubble";
		private static final String xpathConnectionPopupWindow = "//div[@id='people-picker-user-bubble']";

		public static final String xpathUserName = xpathConnectionPopupWindow
				+ "//*[@data-uie-name='status-user']";

		public static final String xpathNameConnectionMessage = xpathConnectionPopupWindow
				+ "//*[@data-uie-name='enter-connect-message']";

		public static final String xpathConnectButton = xpathConnectionPopupWindow
				+ "//*[@data-uie-name='do-connect']";

	}

	public static final class RegistrationPage {

		public static final String xpathNameFiled = "//*[@data-uie-name='enter-name']";

		public static final String idEmailFiled = "wire-create-email";

		public static final String idPasswordFiled = "wire-create-password";

		public static final String idCreateAccountButton = "wire-create";

		public static final String idVerificationEmail = "wire-sent-email";

		public static final String xpathSwitchToSignInButton = "//*[@data-uie-name='go-sign-in']";

		public static final String xpathGoToCreateAccountButton = "(//*[@data-uie-name='go-register'])[2]";
	}

	public static final class Common {

		public static final String CONTACT_LIST_ONE_PERSON_WAITING = "1 person waiting";
	}
}
