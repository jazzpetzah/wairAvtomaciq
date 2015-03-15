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

		public static final String xpathArchiveButton = "//*[@data-uie-name='do-archive']";

		public static final String xpathMuteButton = "//*[@data-uie-name='do-notify']";

		public static final String classMuteIcon = "conversation-muted";

		public static final String xpathActionsButton = "//*[@data-uie-name='go-options']";

		public static final String xpathSelfProfileEntry = "//*[@data-uie-name='go-self-profile']";

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

		public static final String xpathSelfUserName = "//*[@data-uie-name='enter-name']/span";

		public static final String xpathSelfUserNameInput = "//*[@data-uie-name='enter-name']/textarea";

		public static final String xpathNameSelfUserMail = "//*[@data-uie-name='enter-email']";
	}

	public static final class ConversationPage {

		public static final String xpathTextMessageEntry = "//*[@data-uie-name='item-message']//div[@class='text']";

		public static final String xpathFormatSpecificTextMessageEntry = "//*[@data-uie-name='item-message']//div[@class='text' and text()='%s']";

		public static final String idConversationInput = "conversation-input-text";

		// This is needed for IE workaround
		public static final String classNameShowParticipantsButton = "show-participants";

		public static final String xpathShowParticipantsButton = "//*[@data-uie-name='do-participants']";

		public static final String xpathActionMessageEntries = "//*[@data-uie-name='item-message' and contains(@class, 'special')]//div[contains(@class, 'action')]";

		public static final String cssRightControlsPanel = "div.controls-right";

		public static final String cssSendImageLabel = "label.controls-right-button.icon-library.icon-button";

		public static final String xpathSendImageInput = "//input[@type='file' and @data-uie-name='do-share-image']";

		public static final String cssSendImageInput = "input[type=\"file\"]";

		public static final String xpathImageMessageEntry = "//div[@class='message-asset-image']";

		// TODO: Ping and hot ping are two different buttons
		public static final String xpathPingButton = "//*[@data-uie-name='do-ping' or @data-uie-name='do-hot-ping']";

		public static final String classPingMessage = "pinged";
	}

	public static final class ConnectToPage {

		public static final String xpathFormatAcceptRequestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='accept']";

		public static final String xpathFormatIgnoreReqestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='ignore']";
	}

	public static final class PeoplePickerPage {

		public static final String classNameSearchInput = "search-input";

		public static final String classNameCreateConversationButton = "search-button-add";

		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"//div[@data-uie-name='item-user' and text()='%s']", name);

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
		public static final String xpathProfileRoot = "//*[@id='participants-bubble']";

		public static final String xpathAddPeopleButton = xpathProfileRoot + "//*[@data-uie-name='do-add-people']";

		public static final String xpathLeaveGroupChat = xpathProfileRoot + "//*[@data-uie-name='do-leave']";

		public static final String xpathConfirmLeaveButton = xpathProfileRoot + "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";

		public static final String xpathConfirmRemoveButton = xpathProfileRoot + "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";

		public static final String xpathRemoveFromGroupChat = xpathProfileRoot+ "//*[@data-uie-name='do-remove']";

		private static final String xpathHeaderDiv = xpathProfileRoot + "//div[contains(@class, 'participants-group-header')]";

		public static final String xpathConversationTitle = xpathProfileRoot + xpathHeaderDiv
				+ "/div[contains(@class, 'name')]/div";

		public static final String xpathConversationTitleInput = xpathProfileRoot + xpathHeaderDiv
				+ "/div[contains(@class, 'name')]/textarea";
	}

	public static final class UserProfilePopupPage {
		public static final String xpathProfilePopupRoot = "//user-profile";

		public static final String xpathAddPeopleButton = xpathProfilePopupRoot + "//*[@data-uie-name='do-add-people']";

		public static final String xpathNameBlockButton = xpathProfilePopupRoot + "//*[@data-uie-name='do-block']";

		public static final String xpathUserName = xpathProfilePopupRoot + "//*[@data-uie-name='status-user']";

		public static final String xpathParticipantName = xpathProfilePopupRoot + "//div[@class='search-list-item-name' and text()='%s']/..";
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
