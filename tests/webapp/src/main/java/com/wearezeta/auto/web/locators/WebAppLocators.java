package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class WebAppLocators {

	public static final class ActivationPage {
		public static final String xpathSuccessfullResult = "//div[@id='200']//p[contains(@class, 'title') and contains(.,'Account created')]";
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

		private static final String xpathConvoItemByNamePattern = "%s//div[@data-uie-name='item-conversation' and @data-uie-value='%s' and not(contains(@class, 'archived'))]";

		public static final String xpathParentContactListItem = "//div[@id='conversation-list']";
		public static final String cssParentContactListItem = "div#conversation-list";

		public static final String cssIncomingPendingConvoItem = cssParentContactListItem + " div[data-uie-name=item-pending-request]";

		public static final String xpathOpenArchivedConvosButton = "//*[@data-uie-name='go-archive']";

		public static final Function<String, String> xpathArchiveButtonByContactName = (
				name) -> String.format(xpathConvoItemByNamePattern
				+ "/parent::li//*[@data-uie-name='do-archive']",
				xpathParentContactListItem, name);

		public static final Function<String, String> xpathMuteButtonByContactName = (
				name) -> String.format(xpathConvoItemByNamePattern
				+ "/parent::li//*[@data-uie-name='do-silence']",
				xpathParentContactListItem, name);

		public static final Function<String, String> xpathUnmuteButtonByContactName = (
				name) -> String.format(xpathConvoItemByNamePattern
				+ "/parent::li//*[@data-uie-name='do-notify']",
				xpathParentContactListItem, name);

		public static final String classMuteIcon = "conversation-muted";

		public static final Function<String, String> xpathOptionsButtonByContactName = (
				name) -> String.format(xpathConvoItemByNamePattern
				+ "/parent::li//*[@data-uie-name='go-options']",
				xpathParentContactListItem, name);

		public static final String cssOptionsButton = ".text-theme.conversation-list-item [data-uie-name=go-options]";

		public static final String cssSelfProfileEntry = "[data-uie-name=go-self-profile]";

		public static final Function<String, String> cssContactListEntryByName = (
				name) -> String
				.format("%s div[data-uie-name=item-conversation][data-uie-value='%s']",
						cssParentContactListItem, name);

		public static final String xpathContactListEntries = xpathParentContactListItem
				+ "//li[//*[@data-uie-name='item-conversation'] and not(contains(@class, 'archived'))]";
		public static final String xpathArchivedContactListEntries = xpathParentContactListItem
				+ "//li[//*[@data-uie-name='item-conversation'] and contains(@class, 'archived')]";

		public static final Function<String, String> xpathArchivedContactListEntryByName = (
				name) -> String
				.format("%s//li[contains(@class, 'archived')]//div[@data-uie-name='item-conversation' and @data-uie-value='%s']",
						xpathParentContactListItem, name);

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

		public static final String xpathPingButton = "//*[@data-uie-name='do-ping' or @data-uie-name='do-hot-ping']";

		public static final String xpathCallButton = "//*[@data-uie-name='do-call']";

		public static final String xpathTalkingHalo = "//*[contains(@class,'cc-halo-talking')]";

		public static final String classPingMessage = "pinged";

		public static final String xpathCloseButton = "//*[contains(@class,'cc-button')]//*[contains(@class,'icon-close')]";
	}

	public static final class ConnectToPage {

		public static final String xpathFormatAcceptRequestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='accept']";

		public static final String xpathFormatIgnoreReqestButton = "//div[@class='connect-name' and span[text()='%s']]/following-sibling::div/div[@id='ignore']";
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

		public static final String cssSendPictureInput = "div#self-upload input[data-uie-name=do-select-picture]";

		public static final String xpathConfirmPictureSelectionButton = xpathRootDiv
				+ "//*[@data-uie-name='do-set-picture']";

		public static final String xpathNextCarouselImageBtn = xpathRootDiv
				+ "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-right')]";

		public static final String xpathPreviousCarouselImageBtn = xpathRootDiv
				+ "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-left')]";
	}

	public static final class Common {

		public static final String CONTACT_LIST_ONE_PERSON_WAITING = "1 person waiting";
	}
}
