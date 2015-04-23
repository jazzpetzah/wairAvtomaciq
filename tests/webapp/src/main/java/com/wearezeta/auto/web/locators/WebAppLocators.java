package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class WebAppLocators {

	public static final class ActivationPage {
		public static final String xpathBtnOpenWebApp = "//div[contains(@class, 'success') and not(contains(@class, 'hide'))]"
				+ "//*[contains(@class, 'btn-open-web') and contains(@class,'btn-success')]";
	}

	public static final class InvitationCodePage {

		public static final String idCodeInput = "code";

		public static final String xpathProceedButton = "//button[contains(text(),'Proceed')]";
	}

	public static final class LoginPage {

		public static final String xpathLoginPage = "//*[@data-uie-name='go-wire-dot-com']";

		public static final String xpathEmailInput = "//*[@data-uie-name='enter-email']";

		public static final String xpathPasswordInput = "//*[@data-uie-name='enter-password']";

		public static final String classNameSpinner = "loading-spinner";

		public static final String xpathSwitchToRegisterButtons = "//*[@data-uie-name='go-register']";

		public static final String xpathCreateAccountButton = "//*[@data-uie-name='do-register']";

		public static final String xpathSignInButton = "//*[@data-uie-name='do-sign-in']";
	}

	public static final class ContactListPage {
		public static final String xpathParentContactListItem = "//div[@id='conversation-list']";
		public static final String cssParentContactListItem = "div#conversation-list";

		public static final String cssIncomingPendingConvoItem = cssParentContactListItem
				+ " [data-uie-name=item-pending-requests]";

		public static final String xpathOpenArchivedConvosButton = "//*[@data-uie-name='go-archive']";

		public static final Function<String, String> xpathListItemRootWithControlsByName = name -> String
				.format("//li[contains(@class, 'show-controls') and .//*[@data-uie-name='item-conversation' and @data-uie-value='%s']]",
						name);

		public static final Function<String, String> xpathArchiveButtonByContactName = (
				name) -> xpathListItemRootWithControlsByName.apply(name)
				+ "//*[@data-uie-name='do-archive']";

		public static final Function<String, String> xpathMuteButtonByContactName = (
				name) -> xpathListItemRootWithControlsByName.apply(name)
				+ "//*[@data-uie-name='do-silence']";

		public static final Function<String, String> xpathUnmuteButtonByContactName = (
				name) -> xpathListItemRootWithControlsByName.apply(name)
				+ "//*[@data-uie-name='do-notify']";

		public static final Function<String, String> xpathMuteIconByContactName = (
				name) -> String.format(
				"//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/following::"
						+ "*[@data-uie-name='status-silence']", name);

		public static final String cssSelfProfileEntry = "[data-uie-name=go-self-profile]";

		public static final Function<String, String> cssContactListEntryByName = (
				name) -> String.format(
				"%s div[data-uie-name=item-conversation][data-uie-value='%s']",
				cssParentContactListItem, name);

		public static final Function<String, String> cssOptionsButtonByContactName = (
				name) -> String.format("%s + div [data-uie-name=go-options]",
				cssContactListEntryByName.apply(name));

		public static final String xpathContactListEntries = xpathParentContactListItem
				+ "//*[@data-uie-name='item-conversation']";
		public static final Function<Integer, String> xpathContactListEntryByIndex = (
				idx) -> String.format("(%s)[%s]", xpathContactListEntries, idx);
		public static final String xpathArchivedContactListEntries = xpathParentContactListItem
				+ "//*[@data-uie-name='item-conversation-archived']";

		// FIXME: bug in webapp -> @data-uie-value should belong to div
		public static final Function<String, String> xpathArchivedContactListEntryByName = (
				name) -> String
				.format("%s//*[@data-uie-name='item-conversation-archived' and ./ancestor-or-self::*[@data-uie-value='%s']]",
						xpathParentContactListItem, name);

		public static final String cssOpenPeoplePickerButton = "[data-uie-name=go-search]";
	}

	public static final class SettingsPage {
		public static final String xpathSettingsDialogRoot = "//div[@id='self-settings' and contains(@class, 'modal-show')]";

		public static final String xpathSettingsCloseButton = "//div[@id='self-settings']//*[@data-uie-name='do-close']";

		public static final String cssSoundAlertsLevel = "[data-uie-name=enter-sound-alerts]";
	}

	public static final class SelfProfilePage {
		public static final String xpathGearButton = "//div[@id='show-settings']";

		public static final String xpathGearMenuRoot = "//div[@id='setting-bubble' and contains(@class, 'bubble-show')]";

		public static final Function<String, String> xpathGearMenuItemByName = (
				name) -> String.format("%s//a[text()='%s']", xpathGearMenuRoot,
				name);

		public static final String xpathSelfUserName = "//*[@data-uie-name='enter-name']/span";

		public static final String xpathSelfUserNameInput = "//*[@data-uie-name='enter-name']/textarea";

		public static final String classNameSelfUserMail = "self-profile-mail";

		private static final String xpathAccentColorPicker = "//*[@data-uie-name='enter-accent-color']";

		public static final String xpathAccentColorPickerChildren = xpathAccentColorPicker
				+ "/div";

		public static final Function<Integer, String> xpathAccentColorDivById = (
				id) -> String.format("%s[%s]", xpathAccentColorPickerChildren,
				id);

		public static final String xpathCurrentAccentColorCircleDiv = xpathAccentColorPicker
				+ "/div[contains(@class, 'selected')]/div[contains(@class,'circle')]";

		public static final String xpathNameSelfUserMail = "//*[@data-uie-name='enter-email']";
	}

	public static final class ConversationPage {

		public static final Function<String, String> xpathMessageEntryByText = text -> String
				.format("//*[@data-uie-name='item-message']//div[contains(@class, 'text') and text()='%s']",
						text);

		public static final String idConversationInput = "conversation-input-text";

		// This is needed for IE workaround
		public static final String classNameShowParticipantsButton = "show-participants";

		public static final String xpathShowParticipantsButton = "//*[@data-uie-name='do-participants']";

		public static final String xpathActionMessageEntries = "//*[@data-uie-name='item-message' and contains(@class, 'special')]//div[contains(@class, 'action')]";

		public static final String cssRightControlsPanel = "div.controls-right";

		public static final String cssSendImageLabel = "label.controls-right-button.icon-library.icon-button";

		public static final String cssSendImageInput = "input[data-uie-name=do-share-image]";

		public static final String xpathImageMessageEntry = "//div[@class='message-asset-image']";

		public static final String xpathPingButton = "//*[@data-uie-name='do-ping' or @data-uie-name='do-hot-ping']";

		public static final String xpathCallButton = "//*[@data-uie-name='do-call']";

		public static final String classPingMessage = "pinged";

		public static final Function<String, String> textMessageByText = text -> String
				.format("//*[@data-uie-name='item-message']//*[text()='%s']",
						text);

		public static final String xpathMissedCallAction = "//*[@data-uie-value='call']//div[contains(@class, 'action')]";

		public static String xpathCallingBarRoot = "//div[contains(@class, 'call-controls')]";

		public static String xpathAcceptCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-check')]";

		public static String xpathEndCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-close')]";

		public static String xpathSilenceIncomingCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-minus')]";
	}

	public static final class ConnectToPage {
		public static final Function<String, String> xpathAcceptRequestButtonByName = name -> String
				.format("//div[contains(@class, 'connect-name') and span[text()='%s']]/following-sibling::div/div[@id='accept']",
						name);

		public static final Function<String, String> xpathIgnoreReqestButtonByName = name -> String
				.format("//div[contains(@class, 'connect-name') and span[text()='%s']]/following-sibling::div/div[@id='ignore']",
						name);
	}

	public static final class PeoplePickerPage {
		public static final String xpathRoot = "//div[@id='people-picker']";

		public static final String cssNameSearchInput = "div#people-picker input";

		public static final String xpathNameCreateConversationButton = xpathRoot
				+ "//div[contains(@class, 'search-button-add')]";

		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
				xpathRoot, name);

		public static final String xpathCloseSearchButton = xpathRoot
				+ "//div[contains(@class,'search-close')]";

		public static final String classNamePeoplePickerVisible = "people-picker-is-visible";
	}

	public static final class RegistrationPage {
		public static final String xpathRootForm = "//form[@id='form-create']";
		public static final String cssRootForm = "#form-create";

		public static final String cssNameFiled = cssRootForm
				+ " [data-uie-name=enter-name]";

		public static final String cssEmailFiled = cssRootForm
				+ " [data-uie-name=enter-email]";

		public static final String cssPasswordFiled = cssRootForm
				+ " [data-uie-name=enter-password]";

		public static final String idCreateAccountButton = "wire-create";

		public static final String cssVerificationEmail = ".form-posted-success span.wire-sent-email";

		public static final String xpathSwitchToSignInButton = "//*[@data-uie-name='go-sign-in']";
	}

	public static final class SelfPictureUploadPage {
		public static final String xpathRootDiv = "//div[@id='self-upload']";

		public static final String xpathSelectPictureButton = xpathRootDiv
				+ "//*[@data-uie-name='do-select-picture']/following-sibling::span";

		public static final String cssSendPictureInput = "div#self-upload input[data-uie-name=do-select-picture]";

		public static final String xpathConfirmPictureSelectionButton = xpathRootDiv
				+ "//*[@data-uie-name='do-set-picture']";

		public static final String xpathNextCarouselImageBtn = xpathRootDiv
				+ "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-right')]";

		public static final String xpathPreviousCarouselImageBtn = xpathRootDiv
				+ "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-left')]";
	}

	public static final class ContactsUploadPage {
		public static final String xpathRootDiv = "//div[@id='self-upload']";

		public static final String xpathCloseButton = xpathRootDiv
				+ "//*[@data-uie-name='do-close']";

		public static final String xpathShareContactsButton = xpathRootDiv
				+ "//*[@data-uie-name='do-google-import']";

		public static final String xpathShowSearchButton = xpathRootDiv
				+ "//*[@data-uie-name='go-search']";
	}

	public static final class Common {

		public static final String CONTACT_LIST_ONE_PERSON_WAITING = "1 person waiting";
	}
}
