package com.wearezeta.auto.ios.locators;

public final class IOSLocators {
	
	public static final String nameMainWindow = "ZClientMainWindow";
	
	public static final String nameSignInButton = "SignIn";
	
	public static final String nameRegisterButton = "SignUp";
	
	public static final String xpathTermsOfServiceButton = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]";
	
	public static final String nameLoginButton = "ConfirmSignIn";
	
	public static final String nameLoginField = "SignInEmail";
	
	public static final String namePasswordField = "SignInPassword";
	
	public static final String nameErrorMailNotification = "PLEASE PROVIDE A VALID EMAIL ADDRESS";
	
	public static final String nameWrongCredentialsNotification = "WRONG ADDRESS OR PASSWORD.  PLEASE TRY AGAIN.";
	
	public static final String nameIgnoreUpdateButton = "Ignore";

	public static final String nameTermsPrivacyLinks = "TermsPrivacyTextView"; 
	
	public static final String nameTermsPrivacyCloseButton = "WebViewCloseButton";
	
	public static final String classNameContactListNames = "UIACollectionCell";
	
	public static final String xpathContactListNames = "//UIAApplication/UIAWindow/UIACollectionView/UIACollectionCell/UIAStaticText";
	
	public static final String nameProfileSettingsButton = "SettingsButton";

	public static final String classNameUIAButton = "UIAButton";
	
	public static final String xpathEmailField = "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]";

	public static final String xpathUserProfileName = "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
	
	public static final String classNameDialogMessages = "UIATableCell";

	public static final String xpathDialogTextMessage = "//UIATableCell/UIATextView";
	
	public static final String xpathFormatDialogTextMessage = "//UIATableCell/UIATextView[@value='%s']";
	
	public static final String nameConversationCursorInput = "ConversationTextInputField";
	
	public static final String nameTextInput = "ComposeControllerTextView";
	
	public static final String xpathLastDialogMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";
	
	public static final String xpathLastMessageFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[%s]/UIATextView[1]";
	
	public static final String xpathLastVideoFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[%s]/UIAWebView[1]";

	public static final String namePickerSearch = "textViewSearch";
	
	public static final String namePickerClearButton = "PeoplePickerClearButton";
	
	public static final String nameSendConnectButton = "SEND";
	
	public static final String nameConnectOtherUserButton = "CONNECT";
	
	public static final String nameIgnoreOtherUserButton = "IGNORE";
	
	public static final String clasNameConnectDialogLabel = "UIATextField";
	
	public static final String classNameConnectDialogInput = "UIATextView";
	
	public static final String xpathConnectCloseButton = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]";
	
	public static final String nameAddPictureButton = "ComposeControllerPictureButton";
	
	public static final String namePingButton = "ComposeControllerPingButton";
	
	public static final String nameCameraLibraryButton = "CameraLibraryButton"; //"FullScreenCameraRightButton";
	
	public static final String nameCameraRollCancel = "Cancel";
	
	public static final String xpathCameraLibraryFirstFolder = "//UIAApplication/UIAWindow/UIATableView/UIATableCell[1]";
	
	public static final String xpathLibraryFirstPicture = "//UIAApplication/UIAWindow/UIACollectionView/UIACollectionCell[1]";
	
	public static final String nameConfirmPictureButton = "ImageConfirmerConfirmButton";
	
	public static final String xpathMyUserInContactList = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";

	public static final String xpathFirstInContactList = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[2]/UIAStaticText[1]";
	
	public static final String xpathConnectMessageLabel = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]";
	
	public static final String xpathUnicUserPickerSearchResult = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]/UIACollectionCell[1]";
	
	public static final String namePendingButton = "PENDING";
	
	public static final String nameSignOutButton = "SignOutButton";
	
	public static final String nameConnectInput = "Type your first message to [%s]...";
	
	public static final String nameCreateConversationButton = "CREATE CONVERSATION";
	
	public static final String nameKeyboardGoButton = "Go";
	
	public static final String classUIATextView = "UIATextView";

	
	
	public static final String xpathNameMediaContainer = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";
	
	public static final String namePhotoButton = "PhotoLibraryButton";
	
	public static final String nameAlbum = "Saved Photos";
	
	public static final String classNamePhotos = "UIACollectionCell";
	
	public static final String nameConfirmImageButton = "ImageConfirmerConfirmButton";
	
	public static final String nameCancelImageButton = "ImageConfirmerCancelButton";
	
	public static final String xpathYourName = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAElement[1]/UIATextView[1]";
	
	public static final String nameYourEmail = "RegistrationEmailField";
	
	public static final String nameYourPassword = "RegistrationPasswordField";

	public static final String xpathRevealPasswordButton = "//UIAApplication[1]/UIAWindow[1]/UIASecureTextField[1]/UIAButton[1]";
	
	public static final String nameContinueButton = "CONTINUE";
	
	public static final String xpathHidePasswordButton = "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]/UIAButton[1]";
	
	public static final String classNameConfirmationMessage = "UIATextView";
	
	public static final String nameCreateAccountButton = "RegistrationCreateAccountButton";
	
	public static final String xpathLastChatMessage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/*[last()]";
	
	public static final String xpathFormatSpecificMessageContains = "//UIATextView[contains(@name,'%s')]";
	
	public static final String nameLeaveConversation = "metaControllerRightButton";
	
	public static final String nameLeaveConversationAlert = "Leave the conversation?";

	public static final String nameLeaveConversationButton = "LEAVE";
	
	public static final String nameYouHaveLeft = "YOU HAVE LEFT";
    
    public static final String nameComfirmRemoveButton = "REMOVE";

	public static final String nameRemoveFromConversation = "metaControllerRightButton";
	
	public static final String xpathOtherConversationCellFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]";
	
	public static final String xpathYouAddedMessageCellFormat = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]";
	
	public static final String xpathPersonalInfoPage = "//UIAApplication[1]/UIAWindow[1]";
	
	public static final String nameCameraButton = "cameraButton";
	
	public static final String idProvideValidEmailMessage = "PLEASE PROVIDE A VALID EMAIL ADDRESS"; 
	
	public static final String nameAddContactToChatButton = "metaControllerLeftButton";
	
	public static final String nameOtherUserEmailField = "ProfileOtherEmailField";
	
	public static final String xpathOtherUserName = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[@value='%s']";
	
	public static final String nameBackToWelcomeButton = "BackToWelcomeButton";
	
	public static final String nameForwardWelcomeButton = "ForwardWelcomeButton";
	
	public static final String nameConversationNameTextField = "ParticipantsView_GroupName";

	public static final String xpathMutedIcon = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[@name='%s']/UIAButton[3]";
	
	public static final String xpathContactListPlayPauseButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[@name='%s']/UIAButton";
	
	public static final String nameMuteButton = "ConvCellMuteButton";

	public static final String xpathNewGroupConversationNameChangeTextField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[2]/UIATextView[1]";
	
	public static final String nameExitGroupInfoPageButton = "metaControllerCancelButton";
	
	//public static final String nameExitOtherUserPersonalInfoPageButton = "ProfileOtherCloseButton";
	
	public static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";
	
	public static final String xpathAlbum = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]";
	
	//public static final String nameOtherPersonalInfoPageNameField = "ProfileOtherNameField";
	
	//public static final String nameOtherPersonalInfoPageEmailField = "ProfileOtherEmailField";
	
	public static final String xpathOtherPersonalInfoPageNameField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATextView[3]";
	
	public static final String xpathOtherPersonalInfoPageEmailField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATextView[4]";
	
	///////////////////////
	//Self profile page
	///////////////////////

	public static final String nameProfileName = "ProfileSelfNameField";
	
	public static final String xpathProfileNameEditField = "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
	
	public static final String nameSelfNameTooShortError = "AT LEAST 2 CHARACTERS ";
	
	public static final String nameSettingsAboutButton = "About";
	
	public static final String nameTermsOfUseButton = "Terms of Use";
	
	public static final String nameOptionsSettingsButton = "Settings";
	
	public static final String xpathSettingsPage = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]";
	
	public static final String nameSoundAlertsButton = "Sound Alerts";
	
	public static final String xpathSoundAlertsPage = "//UIAApplication[1]/UIAWindow[1]/UIANavigationBar[1]/UIAImage[1]";
	
	public static final String xpathAllSoundAlertsButton = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]";
	
	public static final String nameSettingsChangePasswordButton = "Change Password";
	
	public static final String xpathChangePasswordPageChangePasswordButton = "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAScrollView[1]/UIAWebView[1]/UIAButton[1]";
	
	///////////////////////
	//Other User Profile
	//////////////////////
	
	public static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";
	
	public static final String nameOtherProfilePagePendingLabel = "PENDING";
	
	public static final String nameOtherProfilePageStartConversationButton = "metaControllerLeftButton";
	 
	///////////////////////
	//Camera page locators
	///////////////////////
	public static final String xpathTakePhotoSmile = "//UIAApplication[1]/UIAWindow[1]/UIAImage[1]";
	
	public static final String nameTakePhotoHintLabel = "CHOOSE A PICTURE  AND PICK A COLOR";
	
	public static final String xpathFirstChatInChatListTextField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[2]/UIACollectionCell[1]/UIAStaticText[1]";
	
	public static final String xpathParticipantAvatar = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell[%s]";
	
	public static final String xpathNumberOfParticipantsText =  "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[3]";

	//needs name
	public static final String xpathAvatarCollectionView = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]";
    
	public static final String xpathParticipantAvatarCell = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell";
	
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

	public static String xpathNumberPeopleText = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText[3]";
	
	public static final String peopleCountTextSubstring = " people";
	
	///////////////////////
	//Video Player Locators
	///////////////////////
	
	public static final String xpathVideoMainPage = "//UIAWindow[UIAButton[@name='Done']]";
	
	public static final String nameVideoDoneButton = "Done";
	
	public static final String nameVideoSlider = "Track position";
	
	public static final String nameVideoFullScreenButton = "Full screen";
	
	public static final String nameVideoPreviousButton = "Previous track";
	
	public static final String nameVideoPauseButton = "Pause";
	
	public static final String nameVideoNextButton = "Next track";

	public static final String nameConnectAlertYes = "Yes";
	
	public static final String xpathReSendButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATextView[1]";
	///////////////////////
	//Media Bar Locators
	///////////////////////
	
	public static final String nameMediaBarPlayPauseButton = "mediabarPlayPauseButton";
	
	public static final String nameMediaBarCloseButton = "mediabarCloseButton";
	
	public static final String nameMediaBarTitle = "playingMediaTitle";
	
	public static final String nameMediaCellPlayButton = "mediaCellButton";
	
	public static final String xpathMediaConversationCell = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIATableView/UIATableCell/UIAWebView[last()]";
	
	public static final String xpathYoutubeConversationCell = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[last()]/UIAButton[2]";
	
	public static final String xpathConversationPage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIATableView[1]";
	
	public static final String MEDIA_STATE_PLAYING = "playing";
	
	public static final String MEDIA_STATE_PAUSED = "paused";
	
	public static final String MEDIA_STATE_STOPPED = "ended";
	
	
	/////////////////////////////
	//Image Full screen Locators
	/////////////////////////////
	
	public static final String nameImageInDialog = "ImageCell";
	
	public static final String nameImageFullScreenPage = "fullScreenPage";
	
	public static final String nameFullScreenCloseButton = "fullScreenCloseButton";
	
	public static final String nameFullScreenDownloadButton = "fullScreenDownloadButton";
	
	public static final String nameFullScreenSenderName = "fullScreenSenderName";
	
	public static final String nameFullScreenTimeStamp = "fullScreenTimeStamp";
	
	public static final String nameContactListLoadBar = "LoadBar";
	
	
	////////////////////////////
	//Editing menu (right click)
	////////////////////////////

	public static final String nameEditingItemSelect = "Select";
	
	public static final String nameEditingItemSelectAll = "Select All";
	
	public static final String nameEditingItemCopy = "Copy";
	
	public static final String nameEditingItemCut = "Cut";
	
	public static final String nameEditingItemPaste = "Paste";
	
	
	///////////////////////////
	//Group chat page locators
	///////////////////////////
	public static final String nameAddPeopleDialogHeader = "Add people and share history?";
	public static final String nameAddPeopleCancelButton = "CANCEL";
	public static final String nameAddPeopleContinueButton = "CONTINUE";
	public static final String nameYouAddetToGroupChatMessage = "YOU ADDED %s";
	public static final String nameYouRenamedConversationMessage = "%s\nYOU RENAMED THE CONVERSATION";
	
	
	////////////////////////////
	//Contact list locator
	////////////////////////////
	
	public static final String xpathPendingRequest = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[1]/UIACollectionCell[contains(@name,' waiting')]/UIAStaticText[1]";
	
	////////////////////////////////
	//Pending requests page locators
	//////////////////////////////////
	
	public static final String namePendingRequestIgnoreButton = "IGNORE";
	public static final String namePendingRequestConnectButton = "CONNECT";
	public static final String xpathPendingRequesterName = "//UIAApplication[1]/UIAWindow[1]/UIAScrollView[1]/UIATextView[1]";
	public static final String xpathPendingRequestMessage = "//UIAApplication[1]/UIAWindow[1]/UIAScrollView[1]/UIATextView[3]";
	
	/////////////////////////////
	//People picker
	///////////////////////////
	
	public static final String namePeoplePickerContactsLabel = "CONTACTS";
	public static final String namePeoplePickerOtheraLabel = "OTHERS";
	public static final String NamePeoplePickerTopPeopleLabel = "TOP PEOPLE";
	public static final String xpathPeoplePickerUserAvatar = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIACollectionView[1]/UIACollectionCell/UIAStaticText[@name='%s']/..";
	public static final String namePeoplePickerAddToConversationButton = "ADD TO CONVERSATION";
	public static final String xpathPeoplePickerTopConnectionsAvatar = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[1]/UIACollectionCell[%d]";
	public static final String nameLaterButton = "LATER";
	public static final String nameContinueUploadButton = "CONTINUE";
	public static final String namePeopleYouMayKnowLabel = "PEOPLE YOU MAY KNOW";
	
	//////////////////
	//Connect to page
	//////////////////
	
	public static final String nameSendConnectionInputField = "SendConnectionRequestMessageView";
	
	
	//////////////////
	//Keyboard
	//////////////////
	
	public static final String classNameKeyboard = "UIAKeyboard";
	
	public static final String nameKeyboardDeleteButton = "Delete";
	public static final String nameKeyboardReturnButton = "Return";
	
	////////////////
	//Tutorial
	////////////////
	
	public static final String nameTutorialText = "Pull down to start";
	public static final String nameTutorialView = "ZClientNotificationWindow";
	
	//Script locators
	public static final String scriptCursorInputPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textViews()[\"ConversationTextInputField\"]";
	public static final String scriptKeyboardReturnKeyPath = "target.frontMostApp().keyboard().elements()[\"Return\"]";
	public static final String scriptSignInEmailPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textFields()[\"SignInEmail\"]";
	public static final String scriptSignInPasswordPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].secureTextFields()[\"SignInPassword\"]";
	public static final String scriptRegistrationEmailInputPath = "UIATarget.localTarget().frontMostApp().windows()[\"ZClientMainWindow\"].textFields()[\"RegistrationEmailField\"]";

	public static final String nameYouLeftMessage = "YOU LEFT";
	public static final String nameYouPingedMessage = "YOU PINGED";
	public static final String nameYouPingedAgainMessage = "YOU PINGED AGAIN";
}

