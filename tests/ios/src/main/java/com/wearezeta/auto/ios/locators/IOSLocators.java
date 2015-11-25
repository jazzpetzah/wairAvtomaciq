package com.wearezeta.auto.ios.locators;

public final class IOSLocators {

	public static final String nameMainWindow = "ZClientMainWindow";

	public static final String nameSignInButton = "I HAVE AN ACCOUNT";

	public static final String nameRegisterButton = "SignUp";

	public static final String nameTermsOfServiceButton = "LegalCheckmarkButton";

	public static final String xpathLoginButton = "//UIASecureTextField/UIAButton[1]";

	public static final String nameLoginField = "EmailField";

	public static final String namePasswordField = "PasswordField";

	public static final String nameErrorMailNotification = "PLEASE PROVIDE A VALID EMAIL ADDRESS";

	public static final String nameWrongCredentialsNotification = "Please verify your details and try again.";

	public static final String nameIgnoreUpdateButton = "Ignore";

	public static final String nameTermsPrivacyLinks = "TermsPrivacyTextView";

	public static final String nameTermsPrivacyCloseButton = "WebViewCloseButton";

	public static final String classNameContactListNames = "UIACollectionCell";

	public static final String xpathNameContactList = "//UIACollectionCell/UIAStaticText";

	public static final String xpathContactListCells = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell";

	public static final String xpathFirstContactCell = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]";

	public static final String nameSelfButton = "SelfButton";

	public static final String nameProfileSettingsButton = "SettingsButton";

	public static final String classNameUIAButton = "UIAButton";

	public static final String xpathEmailField = "//UIAElement[@name='ProfileSelfNameField']/following-sibling::UIAStaticText[1]";

	public static final String xpathUserProfileName = "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";

	public static final String classNameDialogMessages = "UIATableCell";

	public static final String xpathConnectionMessage = "//UIAStaticText[contains(@name, 'Let’s connect on Wire.')]";

	public static final String nameYouRenamedConversation = "YOU RENAMED THE CONVERSATION";

	public static final String xpathDialogTextMessage = "//UIATableCell/UIATextView";

	public static final String xpathDialogTitleBar = "//UIAStaticText[@name='%s']";

	public static final String xpathFormatDialogTextMessage = "//UIATableCell/UIATextView[@value='%s']";

	public static final String xpathUserMessageEntry = "//UIATableCell[@name='%s']";

	public static final String xpathFormatMissedCallButtonForContact = "//UIATableCell[UIAStaticText[@name='%s CALLED']]/UIAButton[@name='ConversationMissedCallButton']";

	public static final String nameConversationCursorInput = "ConversationTextInputField";

	public static final String nameTextInput = "ComposeControllerTextView";

	public static final String namePlusButton = "plusButton";

	public static final String nameOpenConversationDetails = "ComposeControllerConversationDetailButton";

	public static final String xpathPinged = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIAStaticText[contains(@name, 'PINGED')]";

	public static final String xpathPingedAgain = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIAStaticText[contains(@name, 'PINGED AGAIN')]";

	public static final String xpathLastDialogMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";

	public static final String xpathLastMessageFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[%s]/UIATextView[1]";

	public static final String xpathLastVideoFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[%s]/UIAWebView[1]";

	public static final String xpathPickerSearch = "//UIATextView[@name='textViewSearch' and @visible='true']";

	public static final String xpathPickerClearButton = "//*[@name='PeoplePickerClearButton' and @visible='true']";
	
	public static final String xpathContactViewCloseButton = "//*[@name='ContactsViewCloseButton' and @visible='true']";

	public static final String nameSendConnectButton = "SEND";

	public static final String nameConnectOtherUserButton = "CONNECT";

	public static final String xpathConnectOtherUserButton = "//UIAButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']";

	public static final String nameIgnoreOtherUserButton = "IGNORE";

	public static final String clasNameConnectDialogLabel = "UIATextField";

	public static final String classNameConnectDialogInput = "UIATextView";

	public static final String xpathConnectCloseButton = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]";

	public static final String nameAddPictureButton = "ComposeControllerPictureButton";

	public static final String namePingButton = "ComposeControllerPingButton";

	public static final String nameCameraLibraryButton = "cameraLibraryButton";

	public static final String nameCameraRollCancel = "Cancel";

	public static final String xpathCameraLibraryFirstFolder = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIATableView/UIATableCell[@name='Moments']";

	public static final String xpathLibraryFirstPicture = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIACollectionView/UIACollectionCell[1]";

	public static final String nameConfirmPictureButton = "OK";

	public static final String xpathMyUserInContactList = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";

	public static final String xpathFirstInContactList = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";

	public static final String xpathContactListEntryWithIndex = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[%d]/UIAStaticText[1]";

	public static final String xpathConnectMessageLabel = "//UIAStaticText[starts-with(@name, 'CONNECTING TO')]";

	public static final String xpathUnicUserPickerSearchResult = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]/UIACollectionCell[1]";

	public static final String namePendingButton = "PENDING";

	public static final String nameSignOutButton = "SignOutButton";

	public static final String nameConnectInput = "Type your first message to [%s]...";

	public static final String nameCreateConversationButton = "CREATE";

	public static final String nameKeyboardGoButton = "Return";

	public static final String classUIATextView = "UIATextView";

	public static final String xpathEmailVerifPrompt = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[contains(@name, 'We sent an email to ')]";

	public static final String xpathNameMediaContainer = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";

	public static final String xpathPhotoButton = "//UIAWindow[@name='ZClientMainWindow']/UIAButton[5]";

	public static final String nameAlbum = "Saved Photos";

	public static final String classNamePhotos = "UIACollectionCell";

	public static final String nameConfirmImageButton = "OK";

	public static final String nameCancelImageButton = "CANCEL";

	public static final String xpathYourName = "//UIAWindow[@name='ZClientMainWindow']/UIATextField[@value='YOUR FULL NAME']";

	public static final String nameYourEmail = "EmailField";

	public static final String nameYourPassword = "PasswordField";

	public static final String xpathRevealPasswordButton = "//UIAApplication[1]/UIAWindow[1]/UIASecureTextField[1]/UIAButton[1]";

	public static final String nameContinueButton = "CONTINUE";

	public static final String xpathHidePasswordButton = "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]/UIAButton[1]";

	public static final String xpathConfirmationMessage = "//UIAStaticText[contains(@name, 'We sent an email to %s.')]";

	public static final String xpathCreateAccountButton = "//UIASecureTextField[contains(@name, 'PasswordField')]/UIAButton";

	public static final String xpathLastChatMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/*[last()]";

	public static final String xpathStartedConversationMessage = "//UIAStaticText[starts-with(@name, 'YOU STARTED A CONVERSATION WITH')]";

	public static final String xpathFormatSpecificMessageContains = "//UIATextView[contains(@name,'%s')]";

	public static final String nameConversationMenu = "metaControllerRightButton";

	public static final String nameOtherUserConversationMenu = "OtherUserMetaControllerRightButton";

	public static final String nameLeaveConversationAlert = "Leave conversation?";

	public static final String nameLeaveConversationButton = "LEAVE";

	public static final String nameYouHaveLeft = "YOU HAVE LEFT";

	public static final String nameCancelButton = "CANCEL";

	public static final String nameSilenceConversationButton = "SILENCE";

	public static final String xpathSilenceConversationButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAButton[@name='SILENCE']";

	public static final String nameUnsilenceConversationButton = "NOTIFY";

	public static final String nameComfirmRemoveButton = "REMOVE";

	public static final String nameRemoveFromConversation = "OtherUserMetaControllerRightButton";

	public static final String xpathOtherConversationCellFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";

	public static final String xpathYouAddedMessageCellFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]";

	public static final String xpathPersonalInfoPage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";

	public static final String nameCameraButton = "cameraButton";

	public static final String namePictureButton = "CameraLibraryButton";

	public static final String idProvideValidEmailMessage = "PLEASE ENTER A VALID EMAIL ADDRESS";

	public static final String nameAddContactToChatButton = "metaControllerLeftButton";

	public static final String nameOtherUserAddContactToChatButton = "OtherUserMetaControllerLeftButton";

	public static final String nameOtherUserEmailField = "ProfileOtherEmailField";

	public static final String xpathOtherUserName = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[@value='%s']";

	public static final String nameBackToWelcomeButton = "BackToWelcomeButton";

	public static final String nameForwardWelcomeButton = "ForwardWelcomeButton";

	public static final String nameConversationNameTextField = "ParticipantsView_GroupName";

	public static final String xpathMutedIcon = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[@name='%s']/UIAButton[3]";

	public static final String xpathContactListPlayPauseButton = "//UIACollectionCell[@name='%s']/UIAButton[@name='mediaCellButton']";

	public static final String nameMuteButton = "ConvCellMuteButton";

	public static final String xpathNewGroupConversationNameChangeTextField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[2]/UIATextView[1]";

	public static final String nameExitGroupInfoPageButton = "metaControllerCancelButton";

	// public static final String nameExitOtherUserPersonalInfoPageButton =
	// "ProfileOtherCloseButton";

	public static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";

	public static final String xpathAlbum = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]";

	// public static final String nameOtherPersonalInfoPageNameField =
	// "ProfileOtherNameField";

	// public static final String nameOtherPersonalInfoPageEmailField =
	// "ProfileOtherEmailField";

	public static final String xpathOtherPersonalInfoPageNameField = "//UIAWindow[@name='ZClientMainWindow']/UIAStaticText[3]";

	public static final String xpathOtherPersonalInfoPageEmailField = "//UIAWindow[@name='ZClientMainWindow']/UIATextView[contains(@name, 'WIRE.COM')]";

	// /////////////////////
	// Self profile page
	// /////////////////////

	public static final String nameProfileName = "ProfileSelfNameField";

	public static final String xpathProfileNameEditField = "//UIAElement[@name='ProfileSelfNameField']/UIATextView";

	public static final String nameSelfNameTooShortError = "AT LEAST 2 CHARACTERS ";

	public static final String xpathSettingsAboutButton = "//UIAButton[@name='ABOUT' or @name='About']";

	public static final String nameTermsOfUseButton = "Terms of Use";

	public static final String xpathOptionsSettingsButton = "//UIAButton[@name='SETTINGS' or @name='Settings']";

	public static final String xpathSettingsPage = "//UIANavigationBar[@name='Settings']";

	public static final String nameSoundAlertsButton = "Alerts";

	public static final String xpathSoundAlertsPage = "//UIANavigationBar[@name='Alerts']";;

	public static final String xpathAllSoundAlertsButton = "//UIATableCell[@name='All']";

	public static final String nameSettingsChangePasswordButton = "Change Password";

	public static final String xpathChangePasswordPageChangePasswordButton = "//UIAButton[@name='CHANGE PASSWORD']";

	public static final String nameOptionsHelpButton = "HELP";

	public static final String xpathSettingsHelpHeader = "//UIAWebView/UIAStaticText[@name='Support']";

	public static final String xpathSettingsChatheadSwitch = "//UIASwitch[@name='Message previews']";

	public static final String nameSettingsBackButton = "Back";

	public static final String nameSettingsDoneButton = "Done";

	public static final String xpathAboutPageWireLogo = "//UIAApplication/UIAWindow/UIAButton[@name='wire.com']/preceding-sibling::UIAImage[1]";

	public static final String nameAboutCloseButton = "aboutCloseButton";

	public static final String nameWireWebsiteButton = "wire.com";

	public static final String xpathWireWebsiteUrl = "//UIAElement[@name ='URL']";

	public static final String xpathWireWebsitePageUrlValue = "//UIAApplication[1]/UIAWindow[2]/UIAButton[2]/UIAStaticText[2]";

	public static final String namePrivacyPolicyButton = "Privacy Policy";

	public static final String xpathBuildNumberText = "//UIAApplication/UIAWindow/UIAStaticText[contains(@name, 'Wire Swiss GmbH • version')]";

	public static final String nameCloseLegalPageButton = "WebViewCloseButton";

	public static final String xpathTermsOfUsePageText = "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIAStaticText[2]";

	public static final String xpathPrivacyPolicyPageText = "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIALink[1]/UIAStaticText[1]";

	// /////////////////////
	// Other User Profile
	// ////////////////////

	public static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";

	public static final String xpathOtherProfilePageCancelRequestLabel = "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]";

	public static final String nameOtherProfilePageStartConversationButton = "OtherUserMetaControllerLeftButton";

	public static final String nameOtherProfilePageCloseButton = "OtherUserProfileCloseButton";

	public static final class OtherUserProfilePage {
		public static final String xpathOtherProfileCancelRequestButton = "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']";
		public static final String nameCancelRequestConfirmationLabel = "Cancel Request?";
		public static final String xpathCancelRequestNoButton = "//UIAStaticText[@name='Cancel Request?']/following-sibling::UIAButton[@name='NO']";
		public static final String xpathCancelRequestYesButton = "//UIAStaticText[@name='Cancel Request?']/following-sibling::UIAButton[@name='YES']";
	}

	// /////////////////////
	// Camera page locators
	// /////////////////////
	public static final String xpathTakePhotoSmile = "//UIAApplication[1]/UIAWindow[1]/UIAImage[1]";

	public static final String nameTakePhotoHintLabel = "CHOOSE A PICTURE  AND PICK A COLOR";

	public static final String xpathFirstChatInChatListTextField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";

	public static final String xpathParticipantAvatar = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[%s]";

	public static final String xpathNumberOfParticipantsText = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[3]";

	public static final String nameCameraRollSketchButton = "editNotConfirmedImageButton";

	// needs name
	public static final String xpathAvatarCollectionView = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]";

	public static final String xpathParticipantAvatarCell = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]/UIACollectionCell";

	public static final String nameErrorPageButton = "BACK";

	public static final String nameCameraCloseButton = "CameraCloseButton";

	public static final String nameCameraShootButton = "CameraShootButton";

	public static final String nameCameraFlashButton = "CameraFlashButton";

	public static final String nameSwitchCameraButton = "CameraSwitchButton";

	public static final String nameCameraPhotoLibraryButton = "CameraLibraryButton";

	public static final String nameRegistrationCameraButton = "CameraButton";

	public static final String xpathConnectionMessageInput = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATextView[1]";

	public static String CONNECT_TO_MESSAGE = "Type your first message to";

	public static final String nameRegistrationNextButton = "ForwardWelcomeButton";

	public static final String nameKeyboardNextButton = "Next";

	public static final String nameVignetteOverlay = "••••";

	public static final String xpathCloseColorModeButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAButton[4]";

	public static String xpathNumberPeopleText = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[contains(@name, 'PEOPLE')]";

	public static final String addPeopleCountTextSubstring = "Add";

	public static final String peopleCountTextSubstring = " PEOPLE";

	// /////////////////////
	// Video Player Locators
	// /////////////////////

	public static final String xpathVideoMainPage = "//UIAWebView/UIAButton[@name='Home']";

	public static final String nameVideoDoneButton = "Done";

	public static final String nameVideoSlider = "Track position";

	public static final String nameVideoFullScreenButton = "Full screen";

	public static final String nameVideoPreviousButton = "Previous track";

	public static final String nameVideoPauseButton = "PauseButton";

	public static final String nameVideoNextButton = "Next track";

	public static final String nameConnectAlertYes = "Yes";

	public static final String xpathReSendButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATextView[1]";
	// /////////////////////
	// Media Bar Locators
	// /////////////////////

	public static final class MediaBar {
		public static final String namePlayButton = "mediaBarPlayButton";
		public static final String namePauseButton = "mediaBarPauseButton";
		public static final String nameCloseButton = "mediabarCloseButton";
		public static final String nameTitle = "playingMediaTitle";
	}
	
	public static final String nameMediaCellPlayButton = "mediaCellButton";

	public static final String xpathMediaConversationCell = "//UIAWindow[@name='ZClientMainWindow']/UIATableView[last()]/UIATableCell[last()]/UIAButton[@name='soundcloud']/following-sibling::UIAButton";

	public static final String xpathYoutubeVimeoConversationCell = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIAButton[1]";

	public static final String xpathConversationPage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]";

	public static final String nameSoundCloudPause = "Pause";

	// ///////////////////////////
	// Image Full screen Locators
	// ///////////////////////////

	public static final String nameImageInDialog = "ImageCell";

	public static final String nameImageFullScreenPage = "fullScreenPage";

	public static final String nameFullScreenCloseButton = "fullScreenCloseButton";

	public static final String nameFullScreenDownloadButton = "fullScreenDownloadButton";

	public static final String nameFullScreenSenderName = "fullScreenSenderName";

	public static final String nameFullScreenTimeStamp = "fullScreenTimeStamp";

	public static final String nameContactListLoadBar = "LoadBar";

	public static final String nameFullScreenSketchButton = "sketchButton";

	// //////////////////////////
	// Editing menu (right click)
	// //////////////////////////

	public static final String nameEditingItemSelect = "Select";

	public static final String nameEditingItemSelectAll = "Select All";

	public static final String nameEditingItemCopy = "Copy";

	public static final String nameEditingItemCut = "Cut";

	public static final String nameEditingItemPaste = "Paste";

	// /////////////////////////
	// Group chat page locators
	// /////////////////////////
	public static final String nameAddPeopleDialogHeader = "Add people and share history?";
	public static final String nameAddPeopleCancelButton = "CANCEL";
	public static final String nameAddPeopleContinueButton = "CONTINUE";
	public static final String xpathYouAddetToGroupChatMessage = "//UIAStaticText[contains(@name, 'YOU ADDED %s')]";
	public static final String nameYouRenamedConversationMessage = "YOU RENAMED THE CONVERSATION";
	public static final String nameConversationBackButton = "ConversationBackButton";

	public static final String xpathPeopleViewCollectionCell = "//UIAButton[@name='metaControllerCancelButton']/following-sibling::UIACollectionView/UIACollectionCell/UIAStaticText[@name='%s']";

	// //////////////////////////
	// Contact list locator
	// //////////////////////////

	public static final String xpathPendingRequest = "//UIACollectionCell[contains(@name,' waiting')]/UIAStaticText[1]";

	// public static final String nameArchiveButton = "ARCHIVE";

	// //////////////////////////////
	// Pending requests page locators
	// ////////////////////////////////

	public static final String namePendingRequestIgnoreButton = "IGNORE";
	public static final String namePendingRequestConnectButton = "CONNECT";
	public static final String xpathPendingRequesterName = "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]//UIAStaticText[contains(@name, 'Connect to')]";
	public static final String xpathPendingRequestMessage = "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]//UIAStaticText[3]";
	public static final String xpathYouBothKnowPeopleIcon = "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]/UIAButton[2]";
	public static final String nameYouBothKnowHeader = "YOU BOTH KNOW";

	// ///////////////////////////
	// People picker
	// /////////////////////////

	public static final String namePeoplePickerContactsLabel = "CONTACTS";
	public static final String namePeoplePickerOtheraLabel = "OTHERS";
	public static final String NamePeoplePickerTopPeopleLabel = "TOP PEOPLE";
	public static final String xpathPeoplePickerUserAvatar = "//UIACollectionCell[descendant::UIAButton[@name='HIDE'] and UIAStaticText[@name='%s']]";
	public static final String namePeoplePickerAddToConversationButton = "ADD TO CONVERSATION";
	public static final String xpathPeoplePickerTopConnectionsAvatar = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%d]";
	public static final String xpathPeoplePickerTopConnectionsName = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%d]/UIAStaticText[last()]";
	public static final String xpathPeoplePickerAllTopPeople = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell";
	public static final String nameShareButton = "SHARE CONTACTS";
	public static final String nameContinueUploadButton = "SHARE CONTACTS";
	public static final String namePeopleYouMayKnowLabel = "CONNECT";
	public static final String nameHideSuggestedContactButton = "HIDE";
	public static final String nameSendAnInviteButton = "INVITE MORE PEOPLE";
	public static final String xpathInviteCopyButton = "//UIACollectionCell[@name='Copy']";
	public static final String nameSuggestedContactType = "UIACollectionCell";
	public static final String nameHideSuggestedContactButtonType = "UIAButton";
	public static final String nameInstantConnectButton = "instantPlusConnectButton";
	public static final String xpathSearchResultCell = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]";
	public static final String xpathSearchResultCellAvatar = "//UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText";
	public static final String xpathSearchResultContainer = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]";

	// ////////////////
	// Connect to page
	// ////////////////

	public static final String nameSendConnectionInputField = "SendConnectionRequestMessageView";
	public static final String scriptSendConnectionInputPhone = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textViews()[\"SendConnectionRequestMessageView\"]";
	public static final String scriptSendConnectionInput = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].popover().textViews()[\"SendConnectionRequestMessageView\"]";

	// ////////////////
	// Keyboard
	// ////////////////

	public static final String classNameKeyboard = "UIAKeyboard";

	public static final String nameKeyboardDeleteButton = "Delete";
	public static final String nameKeyboardReturnButton = "Send";

	public static final class KeyboardButtons {
		public static final String nameHideKeyboardButton = "Hide keyboard";
		public static final String nameSpaceButton = "space";
		public static final String nameDoneButton = "Done";
	}

	// //////////////
	// Tutorial
	// //////////////

	public static final String nameTutorialText = "Pull down to start";
	public static final String nameTutorialView = "ZClientNotificationWindow";

	// Script locators
	public static final String scriptCursorInputPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textViews()[\"ConversationTextInputField\"]";
	public static final String scriptKeyboardReturnKeyPath = "target.frontMostApp().keyboard().elements()[\"Return\"]";
	public static final String scriptSignInEmailPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textFields()[\"EmailField\"]";
	public static final String scriptSignInPasswordPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].secureTextFields()[\"PasswordField\"]";
	public static final String scriptRegistrationEmailInputPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textFields()[\"RegistrationEmailField\"]";
	public static final String scriptSearchField = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textViews()[\"textViewSearch\"]";

	public static final String nameYouLeftMessage = "YOU LEFT";
	public static final String nameYouPingedMessage = "YOU PINGED";
	public static final String nameYouPingedAgainMessage = "YOU PINGED AGAIN";

	// public static final String xpathOtherUserPingedMessage =
	// "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[%s]/UIAStaticText[@name=' PINGED']";

	// //////////////////////////
	// Unblock user locator
	// //////////////////////////
	public static final String nameUnblockButton = "UNBLOCK";

	public static final String xpathContactListContainer = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]";

	public static final String nameBlockMenuButton = "BLOCK";

	// //////////////////////////
	// Reset Password from Sign In
	// //////////////////////////
	public static final String nameForgotPasswordButton = "FORGOT PASSWORD?";

	public static final String xpathSafariGoButton = "//UIAButton[@name='Go']";

	public static final String xpathSafariChangePasswordEmailField = "//UIAApplication[@name='Safari']//UIATextField[@value='Email']";

	public static final String xpathSafariEnterNewPasswordField = "//UIASecureTextField[@value='Enter new password']";

	public static final String xpathSafariURLButton = "//UIAButton[@name='URL']";

	public final class StartedCallPage {

		public static final String xpathCallingMessageUser = "//UIAStaticText[contains(@name, 'CALLING') and contains(@name, '%s')]";

		public static final String xpathStartedCallMessageUser = "//UIAStaticText[@name='%s']";

		public static final String xpathCallingMessage = "//UIAStaticText[contains(@name, 'CallStatusLabel')]";

		public static final String nameCallingUsersAvatar = "CallingUsersImage";

		public static final String nameEndCallButton = "LeaveCallButton";

		public static final String nameSpeakersButton = "CallSpeakerButton";

		public static final String nameMuteCallButton = "CallMuteButton";

	}

	public final class IncomingCallPage {

		public static final String nameCallingMessageUser = "CallStatusLabel";

		public static final String nameAcceptCallButton = "AcceptButton";

		public static final String nameEndCallButton = "LeaveCallButton";

		public static final String xpathCallingMessage = "//UIAStaticText[contains(@value, '%s') and contains(@value, ' IS CALLING')]";

		public static final String nameIgnoreCallButton = "IgnoreButton";

		public static final String xpathGroupCallingMessage = "//UIAStaticText[contains(@value, 'IS CALLING IN')]";

		public static final String nameJoinCallButton = "JOIN CALL";

		public static final String nameSecondCallAlert = "Answer call?";

		public static final String nameEndCallAlertButton = "End Call";

		public static final String xpathGroupCallAvatars = "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell";

		public static final String classNameUIACollectionCell = "UIACollectionCell";

		public static final String xpathGroupCallFullMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAAlert[@name='The call is full']";
	}

	public final class DialogPage {

		public static final String nameCallButton = "ComposeControllerVoiceButton";

		public static final String nameGifButton = "rightMenuButton";

		public static final String nameCursorSketchButton = "ComposeControllerSketchButton";

		public static final String xpathLoremIpsumText = "//UIATextView[contains(@name, 'Lorem ipsum')]";

		public static final String nameSoundCloudContainer = "Play on SoundCloud";

		public static final String nameCloseButton = "closeButton";

		public static final String xpathMyNameInDialog = "//UIAStaticText[@name='%s'][last()]";

		public static final String xpathGiphyImage = "//UIATextView[@name='via giphy.com']/following::UIATableCell[@name='ImageCell']";

		public static final String nameSoundCloudButton = "soundcloud";

		public static final String xpathMessageEntries = "//UIAWindow[@name='ZClientMainWindow']/UIATableView/UIATableCell";

		public static final String xpathSimpleMessageLink = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIATextView[1]";
		
		public static final String xpathConnectedToUserLabel = "//UIAStaticText[contains(@name, 'CONNECTED TO %s')]";

		public static final String xpathConversationWindow = "//UIATableView";

		public static final String xpathImage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[2]";
		
		public static final String xpathUserAvatarNextToInput = "//UIAImage[following-sibling::UIATextView[@name='ConversationTextInputField'] and @visible='true']";
	}

	public final class DialogInfoPage {
		public static final String nameEllipsisMenuButton = "metaControllerRightButton";
		public static final String xpathArchiveButton = "//UIAButton[@name='ARCHIVE']";

	}

	public final class ContactListPage {
		public static final String nameOpenStartUI = "START A CONVERSATION";
		public static final String nameSelfButton = "SelfButton";
		public static final String xpathArchiveConversationButton = "//UIAButton[@name='ARCHIVE' and @visible='true']";
		public static final String nameMuteCallButton = "MuteVoiceButton";
		public static final String xpathFormatActionMenuConversationName = "//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAStaticText[@name='%s']";
		public static final String xpathFormatActionMenuXButton = "//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAButton[@name='%s']";
		public static final String xpathSpecificContactListCell = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[@name='%s']";
	}
	
	public final class ConversationActionMenu {
		public static final String xpathActionMenu = "//UIAStaticText[following-sibling::UIAButton[@name='CANCEL'] and @visible='true']";
		public static final String xpathDeleteConversationButton = "//UIAButton[@name='DELETE' and @visible='true']";
		public static final String xpathConfirmDeleteButton = "//UIAButton[@name='CANCEL']/following-sibling::UIAButton[@name='DELETE']";
		public static final String nameAlsoLeaveCheckerButton = "ALSO LEAVE THE CONVERSATION";
	}

	public final class RegistrationPage {
		public static final String xpathCountryList = "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]";
		public static final String xpathCountry = "//UIAWindow[@name='ZClientMainWindow']/UIAButton[1]";
		public static final String xpathPhoneNumber = "//UIAWindow[@name='ZClientMainWindow']/UIATextField[1]";
		public static final String xpathActivationCode = "//UIAWindow[@name='ZClientMainWindow']/UIATextField[1]";
		public static final String xpathConfirmPhoneNumber = "//UIAWindow[@name='ZClientMainWindow']/UIATextField[1]/UIAButton[1]";
		public static final String nameAgreeButton = "I AGREE";
		public static final String nameSelectPictureButton = "SET A PICTURE";
		public static final String xpathVerificationPage = "//UIAStaticText[contains(@name, 'Enter the verification code we sent to')]";
		public static final String nameTermOfUsePage = "By continuing you agree to the Wire Terms of Use.";
		public static final String nameResendCodeButton = "RESEND";
		public static final String namePhoneNumberField = "PhoneNumberField";
		public static final String xpathYourFilledName = "//UIATextField[preceding-sibling::UIAStaticText[@value='What should we call you?' or @value='Create an account']]";
	}

	public final class LoginPage {
		public static final String namePhoneLoginButton = "PHONE SIGN IN";
		public static final String nameBackButton = "BackToWelcomeButton";
		public static final String nameEmailLoginButton = "EMAIL SIGN IN";
		public static final String nameMaybeLater = "MAYBE LATER";
		public static final String nameCountryPickerButton = "CountryPickerButton";
		public static final String xpathSetEmailPasswordSuggetionLabel = "//UIAStaticText[contains(@name, 'Add email address and password')]";
	}

	public final class PeoplePickerPage {
		public static final String nameLaterButton = "MAYBE LATER";
		public static final String nameContinueUploadButton = "SHARE";
		public static final String nameNotNowButton = "NOT NOW";
		public static final String xpathSuggestedContact = "//UIACollectionCell/UIAStaticText[@name='%s']";
		public static final String xpathSuggestedContactToSwipe = "//UIACollectionCell[descendant::UIAStaticText[@name='%s']]";
		public static final String xpathHideButtonForContact = "//UIAButton[@name='HIDE'][ancestor::UIACollectionCell[descendant::UIAStaticText[@name='%s']]]";
		public static final String nameOpenConversationButton = "OPEN";
		public static final String nameCallButton = "actionBarCallButton";
		public static final String nameSendImageButton = "actionBarCameraButton";
		public static final String xpathFormatFoundContact = "//UIAStaticText[@name='%s' and @visible='true']";
	}

	public final class CommonIOSLocators {
		public static final String nameLockScreenMessage = "SlideToUnlock";
	}

	public final class PersonalInfoPage {
		public static final String nameCloseButton = "CloseButton";
		public static final String nameAddPhoneNumberButton = "ADD PHONE NUMBER";
		public static final String xpathPhoneEmailField =  "//UIAStaticText[contains(@name, '%s')]";
		public static final String nameThemeSwitcherButton = "ThemeButton";
	}

	public final class GiphyPreviewPage {

		public static final String nameGiphyRefreshButton = "leftButton";

		public static final String nameGiphyLinkButton = "rightButton";

		public static final String nameGiphyTitleButton = "centerButton";

		public static final String xpathGithyImage = "//UIAButton[@name='rejectButton']/preceding-sibling::UIAImage[1]";

		public static final String nameGiphyCancelRequestButton = "rejectButton";

		public static final String nameGiphySendButton = "acceptButton";

		public static final String nameNoGifsText = "OOOPS, NO MORE GIFS";

		public static final String nameGiphyGrid = "giphyCollectionView";

	}

	public final class SketchPageElements {

		public static final String nameSketchSendButton = "SketchConfirmButton";

		public static final String nameSketchUndoButton = "SketchUndoButton";

		public static final String nameSketchCancelButton = "SketchCancelButton";

	}

	public final class Alerts {

		public static final String nameResentIn10min = "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.";
		public static final String nameInvalidPhoneNumber = "Please enter a valid phone number";
		public static final String nameInvalidEmail = "Please enter a valid email address";
		public static final String nameInvalidCode = "Please enter a valid code";
		public static final String nameAlreadyRegisteredNumber = "The phone number you provided has already been registered. Please try again.";
		public static final String nameAlreadyRegisteredEmail = "The email address you provided has already been registered. Please try again.";
	}

	// ///////////
	// Chathead
	// ///////////
	public static final String xpathChatheadName = "//UIAElement/following-sibling::UIAStaticText[@name='%s']";
	public static final String xpathChatheadMessage = "//UIAElement/following-sibling::UIAStaticText[@name='%s']";
	public static final String nameChatheadAvatarImage = "ChatheadAvatarImage";

	// /////////////////
	// Accent Color Picker
	// ////////////////
	public static final String nameAccentColorPicker = "AccentColorPickerView";
	
	public  final class ContactsUIPage {
		public static final String xpathSearchInput = "//UIATextView[UIAStaticText[@name='SEARCH BY NAME']]";
		public static final String xpathContactOnContactsUIList = "//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]";
		public static final String nameInviteOthersButton = "INVITE OTHERS";
		public static final String xpathOpenButtonNextToUser = "//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]/UIAButton[@name='OPEN']";
	}

}
