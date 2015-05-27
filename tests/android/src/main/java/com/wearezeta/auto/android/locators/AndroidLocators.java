package com.wearezeta.auto.android.locators;

import java.util.function.Function;

public final class AndroidLocators {
	public static final class Gmail {
		public static final String idSubject = "subject";

		public static final String idBoby = "body";
	}

	public static final class Browsers {
		public static final String xpathChrome = "//*[@value='Chrome']";

		public static final String xpathFirefox = "//*[@value='Firefox']";

		public static final String xpathConnect = "//android.webkit.WebView/android.view.View[6]";

		public static final String idUrlBar = "url_bar";

		public static final String idFirefoxUrlBar = "url_bar_entry";

		public static final String idFirefoxUrlBarEditText = "url_edit_text";

		public static final String xpathNativeBrowserURLBar = "//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]";

		public static final String xpathNativeBrowserMenu = "//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageButton[2]";

		public static final String xpathNativeBrowserShareButton = "//android.widget.ListView[1]/android.widget.LinearLayout[5]/android.widget.RelativeLayout[1]/android.widget.TextView[1]";

		public static final String xpathNativeBrowserShareWireButton = "//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[3]/android.widget.LinearLayout[1]/android.widget.TextView[contains(@text,'Wire')]";

		public static final String nameNativeBrowserMenuButton = "android.widget.ImageButton";

		public static final String nameNativeBrowserMoreOptionsButton = "android.widget.TextView";

		public static final String nameNativeBrowserShareWireButton = "android.widget.TextView";

		public static final class ForgotPasswordPage {

			public static final String xpathEmailEditField = "//android.widget.EditText[@content-desc='Email']";

			public static final String xpathEnterNewPasswordEditField = "//android.widget.EditText[following-sibling::android.widget.Button[@content-desc='CHANGE PASSWORD']]";

			public static final String xpathChangePasswordButton = "//android.widget.Button[@content-desc='CHANGE PASSWORD']";
		}
	}

	public static final class CallingOverlay {
		public static final String idCallingMute = "cib__calling_mute";

		public static final String idCallingAccept = "gtv__calling__accept";

		public static final String idIncominCallerAvatar = "civ__calling";

		public static final String idCallMessage = "ttv__calling__message";

		public static final String idCallingMicMute = "cib__calling__mic_mute";

		public static final String idCallingSpeaker = "cib__calling__speaker";

		public static final String idCallingDismiss = "cib__calling__dismiss";

		public static final String idOngoingCallMicrobar = "ocpv__ongoing";

		public static final String idOngoingCallMinibar = "ocpv__ongoing_small";

	}

	public static final class SettingsPage {

		public static final String xpathSettingPageTitle = "//*[@id='title' and @value='Settings']";

		public static final String xpathSettingPageChangePassword = "//*[@id='title' and @value='Change Password']";
	}

	public static final class LoginPage {

		public static final String idIHaveAccountButton = "zb__welcome__sign_in";

		public static final String idSignUpButton = "ttv__welcome__create_account";

		public static final String idLoginButton = "pcb__signin__email";

		public static final String idForgotPass = "ttv_signin_forgot_password";

		public static final String idLoginInput = "get__sign_in__email";

		public static final String idPasswordInput = "get__sign_in__password";

		public static final String idLoginProgressViewContainer = "fl__sign_in__progress_view__container";

		public static final String idWelcomeButtonsContainer = "cm__choose";

		public static final String idWelcomeSlogan = "tv__welcome__terms_of_service";

		public static final Function<String, String> xpathLoginMessageByText = text -> String
				.format("//*[@id='message' and @value='%s']", text);
	}

	public static final class AboutPage {

		public static final String xpathAboutClose = "//*[@id='fl__about__container']//GlyphTextView";

		public static final String idAboutLogo = "iv__about__logo";

	}

	public static final class CallingOverlayPage {
		public static final String idCallingOverlayContainer = "coc__calling__overlay_container";

		public static final String idIgnoreButton = "cib__calling_mute";

		public static final String idAcceptButton = "gtv__calling__accept";

		public static final String idCallingUsersName = "ttv__calling__message";

	}

	/**
	 * All elements in this class are found when you select a user in Wire with
	 * whom you are not yet connected. OR when another user sends a connection
	 * request to you.
	 */
	public static final class ConnectToPage {
		public static final String idConnectToHeader = "taet__participants__header";

		public static final String idConnectToCharCounter = "ttv__send_connect_request__connect_button__character_counter";

		public static final String idConnectRequestAccept = "zb__connect_request__accept_button";

		public static final String idConnectRequestIgnore = "zb__connect_request__ignore_button";

		public static final String idPaticipantsPendingLabel = "ttv__participants__left_label";
	}

	public static final class ContactListPage {
		public static final String idConversationListFrame = "pfac__conversation_list";

		public static final String idOpenStartUIButton = "gtv__conversation_list__sticky_menu__trigger_startui";

		public static final String idContactListNames = "tv_conv_list_topic";

		public static final String xpathLoadingContactListItem = "//*[@id='tv_conv_list_topic' and contains(@value, '…')]";

		public static final String idConfirmCancelButton = "cancel";

		public static final String idYourName = "ttv__conversation_list__sticky_menu__profile_link";

		public static final String idConvList = "pv__conv_list";

		public static final String idSelfUserAvatar = "civ__searchbox__self_user_avatar";

		public static final String idMissedCallIcon = "sci__list__missed_call";

		public static final String idConfirmCancelButtonPicker = "zb__confirm_dialog__cancel_button";

		public static final Function<String, String> xpathContactByName = name -> String
				.format("//*[@id='tv_conv_list_topic' and @value='%s']", name);

		public static final String idSimpleDialogPageText = "ttv__simple_dialog__text";

		public static final Function<String, String> xpathMutedIconByConvoName = convoName -> String
				.format("%s/parent::*//*[@id='tv_conv_list_voice_muted']",
						xpathContactByName.apply(convoName));

		public static final String idSearchButton = "gtv_pickuser__searchbutton";

		public static final Function<String, String> xpathPlayPauseButtonByConvoName = convoName -> String
				.format("%s/parent::*//*[@id='tv_conv_list_media_player']",
						xpathContactByName.apply(convoName));
	}

	public static final class CommonLocators {
		public static final String classNameFrameLayout = "FrameLayout";

		public static final String classListView = "SwipeListView";

		public static final String idPager = "conversation_pager";

		public static final String idConfirmBtn = "confirm";

		public static final String idEditText = "cet__cursor_view";

		public static final String idGalleryBtn = "gtv__camera_control__pick_from_gallery";

		public static final String idCloseImageBtn = "gtv__single_image_message__close";

		public static final String idSearchHintClose = "zb__search_hint__close_button";

		public static final String xpathGalleryCameraAlbum = "//android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.view.View[1]";

		public static final String xpathGalleryMoreShareOptions = "//android.widget.ListView[1]/android.widget.LinearLayout[%s]/android.widget.LinearLayout[1]/android.widget.TextView[1]";

		public static final String idConversationSendOption = "tv_conv_list_topic";

		public static final String xpathDismissUpdateButton = "//*[@value='Dismiss']";
	}

	public static final class DialogPage {
		public static final String idBackgroundOverlay = "v_background_dark_overlay";

		public static final String idMediaBarControl = "gtv__conversation_header__mediabar__control";

		public static final String idYoutubePlayButton = "gtv__youtube_message__play";

		public static final String idMissedCallMesage = "ttv__row_conversation__missed_call";

		public static final String idMediaBarClose = "gtv__conversation_header__mediabar__cancel";

		public static final String idPlayPauseMedia = "gtv__media_play";

		public static final String idAddParticipants = "gtv__cursor_participants";

		public static final String idPingMessage = "ttv__row_conversation__ping_message";

		public static final String idCursorFrame = "cv";

		public static final String idPingIcon = "gtv__knock_icon";

		public static final String idMessage = "ltv__row_conversation__message";

		public static final String idDialogTakePhotoButton = "gtv__camera_control__take_a_picture";

		public static final String idDialogChangeCameraButton = "gtv__camera__top_control__back_camera";

		public static final String xpathConfirmOKButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";

		public static final String idDialogImages = "iv__row_conversation__message_image";

		public static final String idConnectRequestDialog = "connect_request_root";

		public static final String idConnectRequestMessage = "contact_request_message";

		public static final String idConnectRequestConnectTo = "user_name";

		public static final String idNewConversationNameMessage = "ttv__row_conversation__new_conversation_name";

		public static final String idDialogPageBottomFrameLayout = "cv";

		public static final String idDialogPageBottom = "fl__cursor__user_avatar_container";

		public static final String idConnectRequestChatLabel = "ttv__row_conversation__connect_request__chathead_footer__label";

		public static final String idConnectRequestChatUserName = "ttv__row_conversation__connect_request__chathead_footer__username";

		public static final String idAddPicture = "gtv__cursor_picture";

		public static final String idPing = "gtv__cursor_knock";

		public static final String idCallingMessage = "ttv__calling__message";

		public static final String idCall = "gtv__cursor_call";

		public static final String idMute = "cib__calling__mic_mute";

		public static final String idSpeaker = "cib__calling__speaker";

		public static final String idCancelCall = "cib__calling__dismiss";

		public static final Function<String, String> xpathConversationMessageByText = text -> String
				.format("//*[@id='ltv__row_conversation__message' and @value='%s']",
						text);

		public static final String xpathLastConversationMessage = "(//*[@id='ltv__row_conversation__message'])[last()]";

		public static final String xpathLastPingMessage = "(//*[@id='ttv__row_conversation__ping_message'])[last()]";
	}

	public static final class LockscreenCallingPage {
		public static final String idCallingUserName = "ttv__notifications__incoming_call__lockscreen__header";

		public static final String idLockScreenLogo = "gtv__notifications__incoming_call__lockscreen__logo";

		public static final String idIncomingCallChathead = "civ__notifications__incoming_call__chathead";
	}

	public static final class OtherUserPersonalInfoPage {
		public static final String idParticipantsHeader = "ttv__participants__header";

		public static final String idParticipantsHeaderEditable = "taet__participants__header__editable";

		public static final String idParticipantsSubHeader = "ttv__participants__sub_header";

		public static final String idOtherUserPersonalInfoSingleName = "ttv__single_participants__header";

		public static final String idOtherUserPersonalInfoSingleMail = "ttv__single_participants__sub_header";

		public static final String idUserProfileConfirmationMenu = "user_profile_confirmation_menu";

		public static final String idLeftActionButton = "gtv__participants__left__action";

		public static final String idLeftActionLabel = "ttv__participants__left_label";

		public static final String idRightActionButton = "gtv__participants__right__action";

		public static final String idUnblockBtn = "zb__connect_request__unblock_button";

		public static final String idRenameButton = "ttv__conversation_settings__rename";

		public static final String idArchiveButton = "ttv__conversation_settings__archive";

		public static final String idSilenceButton = "ttv__conversation_settings__silence";

		public static final String idLeaveButton = "ttv__conversation_settings__leave";

		public static final String idBlockButton = "ttv__conversation_settings__block";

		public static final Function<String, String> xpathParticipantAvatarByName = name -> String
				.format("//*[@id='pfac__participants']//ChatheadWithTextFooter[.//*[@value='%s']]",
						name.toUpperCase());
	}

	public static final class PeoplePickerPage {

		public static final Function<String, String> xpathTopConversationContactByName = name -> String
				.format("//*[@value='%s']", name.toUpperCase());

		public static final String xpathGmailLink = "//*[@value='Gmail']";

		public static final String xpathDestinationFrame = "//*[@id='resolver_grid' or @id='resolver_list']";

		public static final String idParticipantsClose = "gtv__participants__close";

		@SuppressWarnings("unused")
		private static final String xpathTopPeopleRoot = "//*[@id='fl__conversation_list_main']";

		public static final String idPickerTopPeopleHeader = "ttv_pickuser__list_header_title";

		public static final String idPickerUserSlidingRow = "ll__pickuser__sliding_row";

		public static final String idPickerUserHideMenu = "hrum__pickuser__hide_menu";

		public static final String idPickerRecomendedName = "ttv_pickuser__recommended_name";

		public static final String idPickerRecomendedQuickAdd = "gtv__pickuser__recommended__quick_add";

		public static final String idPickerUsersUnselected = "pick_user_chathead_unselected";

		public static final String idPickerListContainer = "pfac__pickuser__header_list_view";

		public static final String idPickerBtnDone = "ttv_pickuser_confirmbutton__title";

		public static final String idPickerSearch = "puet_pickuser__searchbox";

		public static final String idPickerGrid = "gv_pickuser__topresult__gridview";

		public static final String idPickerSearchUsers = "ttv_pickuser__searchuser_name";

		public static final String idPickerRows = "ll_pickuser__rowview_searchuser";

		public static final String idPeoplePickerClearbtn = "gtv_pickuser__clearbutton";

		public static final String idCreateConversationIcon = "gtv_pickuser_confirmbutton__icon";

		public static final String idConnectionRequiesMessage = "cet__send_connect_request__first_message";

		public static final String idSendConnectionRequestButton = "zb__send_connect_request__connect_button";

		public static final String idConnectButton = "rl__participants__left__action";

		public static final String idNoResultsFound = "ttv_pickuser__error_header";

		public static final String idPeoplePickerSerchConversations = "ttv_pickuser_searchconversation_name";

		public static final String xpathOtherText = "//*[@value='OTHERS']";

		public static final String idSendInvitationBubble = "fl_pickuser__invite__bubble";

		public static final String xpathSendInvitationFrame = "//LinearLayout[.//FrameLayout[@id='fl_pickuser__invite__bubble']]";

		public static final Function<String, String> xpathPeoplePickerGroupByName = name -> String
				.format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']",
						name);

		public static final Function<String, String> xpathPeoplePickerContactByName = name -> String
				.format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']",
						name);
	}

	public static final class PersonalInfoPage {
		// TODO: There should be only one button. Post a bug to AN project to
		// remove extra view

		private static final String xpathParentSelfProfileOverlay = "//*[@id='fl__conversation_list__profile_overlay']";

		public static final String idBackgroundOverlay = "v_background_dark_overlay";

		public static final String xpathSettingsBox = xpathParentSelfProfileOverlay
				+ "//*[@id='ll__settings_box_container']";

		public static final String xpathEmailField = xpathParentSelfProfileOverlay
				+ "//*[@id='ttv__profile__email']";

		public static final String xpathNameField = xpathParentSelfProfileOverlay
				+ "//*[@id='ttv__profile__name']";

		public static final String xpathNameEdit = xpathParentSelfProfileOverlay
				+ "//*[@id='tet__profile__guided']";

		public static final String idChangePhotoBtn = "gtv__camera_control__change_image_source";

		public static final String idGalleryBtn = "gtv__camera_control__pick_from_gallery";

		public static final String xpathSettingsBtn = xpathParentSelfProfileOverlay
				+ "//*[@id='ttv__profile__settings_box__settings']";

		public static final String xpathProfileOptionsButton = xpathParentSelfProfileOverlay
				+ "//*[@id='gtv__profile__settings_button']";

		public static final String xpathAboutButton = xpathParentSelfProfileOverlay
				+ "//*[@id='ttv__profile__settings_box__about']";

		public static final String xpathSignOutBtn = xpathParentSelfProfileOverlay
				+ "//*[@id='ttv__profile__settings_box__signout']";

		public static final String xpathSelfProfileClose = xpathParentSelfProfileOverlay
				+ "//*[@id='gtv__profile__close_button']";

		public static final String idOpenFrom = "tiles";
	}

	public static final class RegistrationPage {
		private static final String xpathParentSignUpContainer = "//*[@id='fl__sign_up__main_container']";

		public static final String xpathNameField = "("
				+ xpathParentSignUpContainer
				+ "//*[@id='tet__profile__guided'])[1]";

		public static final String xpathEmailField = "("
				+ xpathParentSignUpContainer
				+ "//*[@id='tet__profile__guided'])[2]";

		public static final String idRegistrationBack = "gtv__sign_up__previous";

		public static final String idCreateUserBtn = "zb__sign_up__create_account";

		public static final String idVerifyEmailBtn = "ttv__sign_up__resend";

		public static final String idNewPasswordField = "tet__sign_up__password";

		public static final String idNextArrow = "gtv__sign_up__next";

		public static final String idSignUpGalleryIcon = "gtv__sign_up__gallery_icon";

	}

	public static final class UnknownUserDetailsPage {
		public static final String idOtherUsersName = "taet__participants__header";

		public static final String idConnectButton = "ttv__participants__left_label";

		public static final String idCommonUsersLabel = "ttv__connect_request__common_users__label";

	}
}
