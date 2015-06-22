package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class WebAppLocators {

	public static final class ActivationPage {

		public static final String xpathSuccessfullResult = "//div[@id='200']//p[contains(@class, 'title') and contains(.,'Account created')]";
		public static final String xpathBtnOpenWebApp = "//div[contains(@class, 'success') and not(contains(@class, 'hide'))]"
				+ "//*[contains(@class, 'btn-open-web') and contains(@class,'btn-success')]";
	}

	public static final class LoginPage {

		public static final String xpathLoginPage = "//*[@data-uie-name='go-wire-dot-com']";

		public static final String xpathEmailInput = "//*[@data-uie-name='enter-email']";

		public static final String xpathPasswordInput = "//*[@data-uie-name='enter-password']";

		public static final String classNameSpinner = "loading-spinner";

		public static final String xpathCreateAccountButton = "//*[@data-uie-name='do-register']";

		public static final String xpathSignInButton = "//*[@data-uie-name='do-sign-in']";

		public static final String cssPhoneSignInButton = "[data-uie-name='go-phone-sign-in']";

		public static final String xpathSwitchToRegisterButtons = "//*[@data-uie-name='go-register']";

		public static final String xpathChangePasswordButton = "//*[@data-uie-name='go-forgot-password']";

		public static final String xpathLoginErrorText = "//*[@data-uie-name='status-error']";
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

		public static final Function<String, String> cssContactListEntryByName = (
				name) -> String.format(
				"%s div[data-uie-name=item-conversation][data-uie-value='%s']",
				cssParentContactListItem, name);

		public static final Function<String, String> cssArchiveListEntryByName = (
				name) -> String
				.format("%s div[data-uie-name='item-conversation-archived'][data-uie-value='%s']",
						cssParentContactListItem, name);

		public static final Function<String, String> cssOptionsButtonByContactName = (
				name) -> cssContactListEntryByName.apply(name)
				+ "+ div span[data-uie-name='go-options']";

		public static final String cssSelfProfileAvatar = "[data-uie-name=go-self-profile]";

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

		public static final String cssOpenPeoplePickerButton = "[data-uie-name=enter-search]";

		public static final Function<String, String> xpathMissedCallNotificationByContactName = (
				name) -> String
				.format("//*[contains(@class, 'conversation-list-item') and div[@data-uie-value='%s']]//*[local-name() = 'svg' and @data-uie-name='status-unread']",
						name);

		public static final Function<String, String> xpathPingIconByContactName = (
				name) -> String
				.format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/parent::"
						+ "*//*[@data-uie-name='status-unread' and contains(@class, 'icon-ping')]",
						name);

		public static final Function<String, String> xpathUnreadDotByContactName = (
				name) -> String
				.format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/parent::"
						+ "*//*[@data-uie-name='status-unread' and contains(@class, 'conversation-list')]",
						name);
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

		public static final String xpathNameSelfUserPhoneNumber = "//*[@data-uie-name='enter-phone']";

		public static final String xpathCameraButton = "//*[@data-uie-name='go-profile-picture-selection']";

		public static final String xpathBackgroundAvatarAccentColor = "//div[contains(@class, 'background-accent bg-theme')]";
	}

	public static final class ConversationPage {

		public static final Function<String, String> xpathMessageEntryByText = text -> String
				.format("//*[@data-uie-name='item-message']//div[contains(@class, 'text') and text()='%s']",
						text);

		public static final Function<String, String> xpathEmbeddedYoutubeVideoById = text -> String
				.format("//iframe[contains(@src, '%s')]", text);

		public static final String idConversation = "conversation";

		public static final String idConversationInput = "conversation-input-text";

		// This is needed for IE workaround
		public static final String classNameShowParticipantsButton = "show-participants";

		public static final String cssShowParticipantsButton = "[data-uie-name='do-participants']";

		public static final String xpathActionMessageEntries = "//*[@data-uie-name='item-message' and contains(@class, 'special')]//div[contains(@class, 'action')]";

		public static final String cssRightControlsPanel = "div.controls-right";

		public static final String cssSendImageLabel = "label.controls-right-button.conversation-input-button.icon-camera.icon-button";

		public static final String cssSendImageInput = "input[data-uie-name=do-share-image]";

		public static final String xpathImageMessageEntry = "//div[@class='message-asset-image']";

		public static final String cssPingButton = "[data-uie-name='do-ping'], [data-uie-name='do-hot-ping']";

		public static final String xpathCallButton = "//*[@data-uie-name='do-call']";

		public static final String classPingMessage = "pinged";

		public static final Function<String, String> textMessageByText = text -> String
				.format("//*[@data-uie-name='item-message']//*[text()='%s']",
						text);

		public static final String cssLastTextMessage = "[data-uie-name='item-message']:last-child .text-inner";

		public static final String xpathMissedCallAction = "//*[@data-uie-value='call']//div[contains(@class, 'action')]";

		public static String xpathCallingBarRoot = "//div[contains(@class, 'call-controls')]";

		public static final Function<String, String> xpathCallingBarRootByName = text -> String
				.format("//div[contains(@class, 'call-controls') and div/div/span[text()='%s']]",
						text);

		public static String xpathAcceptCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-check')]";

		public static String xpathEndCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-close')]";

		public static String xpathSilenceIncomingCallButton = xpathCallingBarRoot
				+ "//*[contains(@class, 'icon-minus')]";

		public static final String xpathPictureFullscreen = "(//*[@data-uie-name='go-detail'])[last()]";

		public static final String xpathPictureIsFullscreen = "//div[contains(@class, 'modal-show')]";

		public static final String xpathXButton = "//div[contains(@class, 'detail-view-close-button')]//*[@data-uie-name='do-close-detail-view']";

		public static final String idBlackBorder = "detail-view";
	}

	public static final class ConnectToPage {

		public static final String xpathRequestAvatarPartial = "/../../div[contains(@class, 'sender')]//div[contains(@class, 'user-avatar-image')]";
		public static final String xpathRequestEmailPartial = "/following-sibling::div[contains(@class, 'mail')]";
		public static final String xpathRequestMessagePartial = "/following-sibling::div[contains(@class, 'message')]";

		public static final Function<String, String> xpathRequestByName = name -> String
				.format("//div[contains(@class, 'connect-name') and span[text()='%s']]",
						name);

		public static final Function<String, String> xpathAcceptRequestButtonByName = name -> String
				.format("//div[contains(@class, 'connect-name') and span[text()='%s']]/following-sibling::div/div[@id='accept']",
						name);

		public static final Function<String, String> xpathIgnoreReqestButtonByName = name -> String
				.format("//div[contains(@class, 'connect-name') and span[text()='%s']]/following-sibling::div/div[@id='ignore']",
						name);

		public static final String xpathAllConnectionRequests = "//div[contains(@class, 'connect-request')";
	}

	public static final class PeoplePickerPage {

		public static final String xpathRoot = "//div[@id='people-picker']";

		public static final String cssNameSearchInput = "[data-uie-name='enter-users']";

		public static final String xpathNameCreateConversationButton = "//*[@data-uie-name='do-add-create']";

		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
				xpathRoot, name);

		public static final String cssCloseSearchButton = ".search-header span[data-uie-name='do-close']";

		public static final Function<String, String> cssDismissIconByName = (
				name) -> String.format(
				"div[data-uie-value='%s'] span.icon-dismiss", name);

		public static final Function<String, String> cssAddIconByName = (name) -> String
				.format("div[data-uie-value='%s'] span.icon-add", name);

		public static final String classNamePeoplePickerVisible = "people-picker-is-visible";

		public static final String xpathSendInvitationButton = xpathRoot
				+ "//*[@id='invite-button']";

		public static final Function<String, String> xpathSearchPendingResultByName = (
				name) -> String
				.format("%s//*[@data-uie-name='item-user' and .//*[text()='%s'] and .//div[contains(@class,'checkmark icon-check')]]",
						xpathRoot, name);

		public static final String xpathTopPeople = "//*[@data-uie-name='status-top-people']";

		public static final Function<String, String> xpathTopPeopleListByName = (
				name) -> String
				.format("(//user-list[contains(@params, 'top_users')]//*[@data-uie-name='item-user' and .//*[text()='%s']])",
						name);

		public static final String xpathSelectedTopPeopleList = "//user-list[contains('top_users')]"
				+ "//*[@data-uie-name='item-user' and .//*[contains(@class,'selected')]]";
	}

	public static final class RegistrationPage {

		public static final String cssSwitchToSignInButton = "[data-uie-name='go-sign-in']";

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

		public static final String TITLE_ATTRIBUTE_LOCATOR = "title";

		public static final String HREF_ATTRIBUTE_LOCATOR = "href";
	}

	public static final class ProfilePicturePage {
		private static final String xpathRootDiv = "//div[@id='self-upload']";

		public static final String xpathSelectPictureButton = xpathRootDiv
				+ "//*[@data-uie-name='do-select-picture']/following-sibling::span";

		public static final String xpathConfirmPictureSelectionButton = xpathRootDiv
				+ "//*[@data-uie-name='do-set-picture']";

		public static String cssDropZone = "#self-upload .self-upload-center";
	}

	public static final class WarningPage {

		private static final String xpathRootDiv = "//div[@id='warnings']";

		public static final String xpathWarning = xpathRootDiv
				+ "//div[@class='warning']";

		public static final String xpathWarningClose = xpathRootDiv
				+ "//span[contains(@class, 'warning-bar-close')]";

		public static final Function<String, String> xpathLinkByCaption = (name) -> String
				.format("%s//div[contains(@class, 'warning-bar-message')]//a[text()='%s']",
						xpathRootDiv, name);
	}
}
