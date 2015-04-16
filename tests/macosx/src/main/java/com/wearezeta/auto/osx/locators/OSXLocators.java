package com.wearezeta.auto.osx.locators;

public final class OSXLocators {

	public static final String idContactEntry = "clListItemNameField";
	public static final String idAddImageButton = "AddImageButton";
	public static final String idPeopleButton = "PeopleButton";
	public static final String idPeoplePickerSearchField = "people_picker_searchfield";
	public static final String xpathPeoplePickerTopContactsSectionHeader = "//AXStaticText[@AXValue='TOP PEOPLE']";
	public static final String idPeoplePickerDismissButton = "people_picker_cancel_button";
	public static final String idPeoplePickerSearchResultEntry = "people_picker_result_namefield";
	public static final String xpathPeoplePickerSearchResultTable = "//AXTable[@AXIdentifier='people_picker_searchresult']";
	public static final String idUnblockUserButton = "unblock";
	public static final String idPeoplePickerTopContactsGrid = "people_picker_user_collectionview";
	public static final String xpathPeoplePickerTopContacts = "//AXGrid[@AXIdentifier='people_picker_user_collectionview']";
	public static final String xpathPeoplePickerTopContactAvatar = "//AXRow[descendant::AXStaticText[@AXValue='TOP PEOPLE']]/following-sibling::AXRow[1]//AXUnknown";

	public static final String idShowMenuButton = "clshowMenuButton";
	public static final String idMuteButton = "muteButton";
	public static final String idArchiveButton = "archiveButton";
	public static final String idShowArchivedButton = "openArchiveButton";

	public static final String namePingMenuItem = "Ping";
	public static final String namePingAgainMenuItem = "Ping Again";
	public static final String idSendInvitationButton = "send";

	public static final String xpathMessageEntry = "//AXGroup/AXStaticText";
	public static final String xpathConversationLastNewNameEntry = "//AXHeading/AXStaticText[last()]";
	public static final String xpathNewMessageTextArea = "//AXTextArea";
	public static final String xpathConversationImageEntry = "//AXGroup/AXImage";
	public static final String xpathSearchResultsScrollArea = "//AXScrollArea[AXTable[@AXIdentifier='people_picker_searchresult']]";
	public static final String xpathConversationMessageGroup = "//AXWebArea/AXGroup";

	public static final String xpathFormatContactEntryWithName = "//AXStaticText[@AXIdentifier='clListItemNameField'][starts-with(@AXValue, '%s')]";
	public static final String xpathFormatSpecificMessageEntry = "//AXGroup/AXStaticText[@AXValue='%s']";
	public static final String xpathFormatPeoplePickerSearchResultUser = "//AXStaticText[@AXIdentifier='people_picker_result_namefield'][@AXValue='%s']";
	public static final String xpathConversationListScrollArea = "//AXScrollArea[@AXIdentifier='conversationList']";

	public static final String xpathOtherPingedMessage = "//AXGroup[AXStaticText[@AXValue='%s'] and AXStaticText[@AXValue=' PINGED']]";
	public static final String xpathOtherPingedAgainMessage = "//AXGroup[AXStaticText[@AXValue='%s'] and AXStaticText[@AXValue=' PINGED AGAIN']]";

	public static final String YOU_PINGED_MESSAGE = "YOU PINGED";
	public static final String YOU_PINGED_AGAIN_MESSAGE = "YOU PINGED AGAIN";
	public static final String USER_PINGED_MESSAGE = "PINGED";
	// public static final String USER_PINGED_AGAIN_MESSAGE = "PINGED AGIAN";
	public static final String YOU_ADDED_MESSAGE = "YOU ADDED";
	// public static final String YOU_REMOVED_MESSAGE = "YOU REMOVED";
	// public static final String CONNECTED_TO_MESSAGE = "CONNECTED TO";
	public static final String USER_ADDED_MESSAGE_FORMAT = " ADDED ";
	public static final String YOU_STARTED_CONVERSATION_MESSAGE = "YOU STARTED A CONVERSATION WITH";
	public static final String USER_STARTED_CONVERSATION_MESSAGE_FORMAT = " STARTED A CONVERSATION WITH ";

	public static final String idAddPeopleButtonGroupChat = "addButton";
	public static final String idConfirmationViewConfirmButton = "ConfirmationViewConfirmButton";
	public static final String idAddPeopleButtonSingleChat = "userProfileViewButtonOnTheLeft";
	public static final String idBlockUserButtonSingleChat = "userProfileViewButtonOnTheRight";
	public static final String idLeaveConversationButton = "leaveButton";
	public static final String idRemoveUserFromConversation = "userProfileViewButtonOnTheRight";

	public static final String xpathPictureFromImageFile = "//AXCheckBox[1]";
	public static final String xpathPictureFromCamera = "//AXCheckBox[2]";
	public static final String xpathPictureConfirmationButton = "//AXCheckBox[1]";
	public static final String xpathDoCameraShotButton = "//AXCheckBox[@AXValue='1']";

	public static final String xpathFormatPeoplePickerUserCell = "//AXUnknown[AXStaticText[@AXValue='%s']]";

	public static final String xpathPictureSettingsCloseButton = "//AXWindow[@AXRoleDescription='floating window']/AXButton[@AXRoleDescription='close button']";

	public static final String xpathFormatMutedButton = "//AXCell[AXStaticText[@AXValue='%s']]/AXImage[@AXTitle='']";

	public static final String xpathConversationNameEdit = "//AXPopover/AXScrollArea/AXTextArea";
	public static final String xpathUserAvatar = "//AXPopover/AXScrollArea/AXGrid/AXUnknown";

	public static final String xpathNumberOfPeopleInChat = "//AXPopover/AXStaticText";

	public static final String peopleCountTextSubstring = " PEOPLE";

	public static final String xpathOpenUserPictureSettingsButton = "//AXWindow/AXButton[starts-with(@AXHelp,'Change your picture')]";

	public static final String IMAGES_SOURCE_DIRECTORY = "Documents";

	// /////////////////////
	// Media Locators
	// /////////////////////
	public static final String idMediaBarPlayPauseButton = "mediaBarPlayButton";
	public static final String idMediaBarTitelButton = "mediaBarTitleButton";
	public static final String idMediaBarCloseButton = "mediaBarCloseButton";

	public static final String xpathSoundCloudLinkButton = "//AXGroup/AXGroup/AXGroup/AXButton";
	public static final String xpathSoundCloudMediaContainer = "//AXGroup/AXLink[last()]/AXImage";
	public static final String xpathSoundCloudMediaContainerWithoutImage = "//AXGroup/AXLink[@AXDescription='Play on SoundCloud']";
	public static final String xpathSoundCloudCurrentPlaybackTime = "(//AXGroup[AXGroup[AXLink]]/AXGroup/AXGroup/AXGroup[1]/AXStaticText)[last()]";

	public static final String SOUNDCLOUD_BUTTON_STATE_PAUSE = "Pause";
	public static final String SOUNDCLOUD_BUTTON_STATE_PLAY = "Play";
	public static final String xpathConversationViewScrollArea = "//AXScrollArea";

	public static final String idSelfProfileSettingsButton = "selfProfileViewSettingsButton";
	public static final String xpathFormatSelfProfileNameTextField = "//AXTextArea[@AXValue='%s']";
	public static final String idSelfProfileEmailTextField = "phoneTextField";
	public static final String idUserProfileViewBackButton = "userProfileViewBackButton";

	public static final String xpathFormatUserProfileViewContactName = "//AXWindow/AXPopover/AXScrollArea/AXTextArea[@AXValue='%s']";

	public static final String RANDOM_KEYWORD = "RANDOM";

	public static final String idSingleChatUserNameField = "userProfileNameField";
	public static final String xpathSingleChatUserEmailButton = "//AXPopover/AXTextArea/AXLink[@AXTitle='%s']";

	public static final String xpathRemoveUserPictureCheckBox = "//AXCheckBox[starts-with(@AXHelp,'Remove picture')]";
	public static final String xpathRemoveUserPictureConfirmation = "//AXButton[@AXTitle='DELETE']";
	public static final String xpathRemoveUserPictureCancel = "//AXButton[@AXTitle='CANCEL']";

	public static final String idShareContactsLaterButton = "ConfirmationViewCancelButton";

	public static final String xpathPeoplePopover = "//AXPopover";

	public static final String xpathImagePopupCloseButton = "//AXWindow[@AXRoleDescription='floating window']/AXButton[@AXRoleDescription='close button']";

	public static final String xpathOpenSingleChatButton = "//AXPopover/AXButton[@AXTitle='OPEN CONVERSATION']";

	public static final String xpathPendingButton = "//AXPopover/AXButton[@AXTitle='PENDING']";

	public static final String xpathConnectToUserButton = "//AXPopover/AXButton[@AXTitle='CONNECT']";

	public static final String xpathFormatSentConnectionRequestMessage = "//AXPopover//AXTextArea[@AXValue='%s']";

	public static final String xpathAvatarFullScreenWindow = "//AXWindow[@AXRoleDescription='floating window']";

	public static final class WelcomePage {

		public static final String idWelcomePage = "_NS:6";

		public static final String nameSignInButton = "SIGN IN";

		public static final String nameRegisterButton = "REGISTER";

		public static final String nameTermsOfUseLink = "TERMS OF USE";

		public static final String xpathAcceptTermsOfUseCheckbox = "//AXCheckBox";
	}

	public static final class LoginPage {

		public static final String idLoginPage = "_NS:6";

		public static final String idPasswordField = "TempLoginViewControllerPasswordField";

		public static final String appleScriptPasswordFieldPath = "text field 1 of window 1";

		public static final String relativePathEmailField = "id,"
				+ idPasswordField + ",0,-40";

		public static final String nameSignInButton = "SIGN IN";

		public static final String xpathForgotPasswordButton = "//AXButton[contains(@AXTitle, 'FORGOT PASSWORD')]";

		public static final String xpathWrongCredentialsMessage = "//AXTextArea[starts-with(@AXValue, 'WRONG ADDRESS OR PASSWORD')]";
	}

	public static final class RegistrationPage {

		public static final String idPasswordField = "TempLoginViewControllerPasswordField";

		public static final String relativePathFullNameField = "id,"
				+ idPasswordField + ",0,-80";

		public static final String relativePathEmailField = "id,"
				+ idPasswordField + ",0,-40";

		public static final String idCreateAccountButton = "RegistrationCreateButton";

		public static final String idRegistrationBackButton = "RegistrationBackButton";

		public static final String idTakePictureButton = "RegistrationTakePictureButton";

		public static final String idPickImageButton = "RegistrationPickImageButton";

		public static final String xpathConfirmPictureButton = "//AXButton[@AXIdentifier='']";

		public static final String xpathAcceptTakenPictureButton = "//AXButton[1]";

		public static final String xpathRejectTakenPictureButton = "//AXButton[2]";

		public static final String xpathChoosePictureAndSelectColourMessage = "//*[contains(@AXValue,'CHOOSE A PICTURE AND ') and contains(@AXValue,'SELECT A COLOUR')]";

		public static final String xpathPleaseProvideEmailAddressMessage = "//AXStaticText[@AXValue='PLEASE ENTER A VALID EMAIL ADDRESS']";

		public static final String ACTIVATION_RESPONSE_VERIFIED = "Account created.";
	}

	public static final class ChangePasswordPage {

		public static final String xpathEmailTextField = "//AXTextField[@AXPlaceholderValue='Email']";

		public static final String xpathPasswordTextField = "//AXTextField[@AXPlaceholderValue='Enter new password']";

		public static final String nameChangePasswordButton = "CHANGE PASSWORD";

		public static final String xpathPasswordChangedMessage = "//AXStaticText[@AXValue='Password changed']";

		public static final String nameQuitSafariButton = "Quit Safari";
	}

	public static final class CallPage {

		public static final String idJoinCallButton = "JoinButton";

		public static final String idCancelCallButton = "JoinButton";

		public static final String idMuteMicrophoneButton = "MuteButton";

		public static final String idIgnoreCallButton = "IgnoreCallButton";

		public static final String xpathFormatSubscriberName = "//AXStaticText[@AXValue='%s']";

		public static final String xpathFormatCallingUserMessage = "//AXStaticText[@AXValue='CALLING %s']";

		public static final String xpathFormatUserCallsMessage = "//AXStaticText[@AXValue='%s IS CALLING']";

		public static final String nameTransferCallHereButton = "TRANSFER CALL HERE";
	}

	public static final class NoInternetConnectionPage {

		public static final String idOKButton = "_NS:14";

		public static final String xpathNoInternetMessage = "//AXStaticText[contains(@AXValue,'No Internet Connection')]";
	}

	public static final class ProblemReportPage {

		public static final String idWindow = "_NS:162";

		public static final String nameCancelButton = "Cancel";

		public static final String nameSendButton = "Send";
	}

	public static final class CallingFloatingPage {

		public static final String xpathWindow = "//AXWindow[@AXRoleDescription='system floating window']";

		public static final String xpathCloseButton = "//AXButton[@AXRoleDescription='close button']";

		public static final String xpathFormatCallerNameText = "//AXStaticText[@AXValue='%s']";

		public static final String nameLaterButton = "LATER";

		public static final String nameAnswerButton = "ANSWER";
	}

	public static final class MainWirePage {

		public static final String xpathWindow = "//AXWindow[@AXRoleDescription='standard window']";

		public static final String xpathCloseButton = "//AXButton[@AXRoleDescription='close button']";

		public static final String xpathMinimizeButton = "//AXButton[@AXRoleDescription='minimize button']";

		public static final String xpathZoomButton = "//AXButton[@AXRoleDescription='zoom button']";
	}

	public static final class ContactListPage {

		public static final String idOpenSearchUIButton = "addConversationButton";

		public static final String xpathSelfProfileCLEntry = "//AXWindow/AXStaticText[@AXIdentifier='clListItemNameField']";

		public static final String regexPatternConnectRequests = ".* (person|people) waiting";

		public static final String titleSingleConnectRequestCLEntry = "One person waiting";

		public static final String titleFormatMultipleConnectRequestsCLEntry = "%s people waiting";

		public static final String xpathConnectionRequestsCLEntry = "//AXStaticText[@AXIdentifier='clListItemNameField' and (contains(@AXValue,'person waiting') or contains(@AXValue,'people waiting'))]";
	}

	public static final class PeoplePickerPage {

		public static final String nameAddToConversationButton = "ADD TO CONVERSATION";

		public static final String nameOpenConversationButton = "OPEN CONVERSATION";

		public static final String nameCreateConversationButton = "CREATE CONVERSATION";
	}

	public static final class ConnectionRequestsPage {

		public static final String xpathFormatBlockButton = "//AXCell[AXStaticText[@AXValue='%s']]/AXButton[@AXIdentifier='block']";

		public static final String xpathFormatConnectButton = "//AXCell[AXStaticText[@AXValue='%s']]/AXButton[@AXIdentifier='connect']";

		public static final String xpathFormatEmailText = "//AXCell[AXStaticText[@AXValue='%s']]";

		public static final String xpathFormatMessageText = "//AXCell[AXStaticText[@AXValue='%s']]/AXTextArea[@AXIdentifier='message']";

		public static final String xpathFormatTitleText = "//AXCell[AXStaticText[@AXValue='%s']]/AXStaticText[@AXIdentifier='title']";

		public static final String xpathFormatProfilePopupButton = "//AXCell[AXStaticText[@AXValue='%s']]/AXButton[@AXIdentifier='']";

		public static final String idAcceptConnectionRequestButton = "connect";

		public static final String idIgnoreConnectionRequestButton = "block";
	}

	public static final class ConversationPage {

		public static final String idConversationScrollArea = "ConversationScrollArea";

		public static final String idOpenPeoplePopoverButton = "PeopleButton";

		public static final String idSendImageButton = "AddImageButton";

		public static final String idPingButton = "PingIcon";

		public static final String idCallButton = "JoinChannelIcon";
	}

	public static final class MainMenuPage {

		public static final String nameSendImageMenuItem = "Picture…";

		public static final String nameSignOutMenuItem = "Sign Out";

		public static final String nameQuitWireMenuItem = "Quit Wire";

		public static final String xpathFormatDockApplicationIcon = "//AXDockItem[@AXRoleDescription='application dock item' and contains(@AXTitle,'%s')]";
	}

	public static final class VerificationPage {

		public static final String idEnvelopeImage = "_NS:9";

		public static final String idEmailSentMessage = "_NS:30";

		public static final String xpathFormatEmailSentMessage = "//AXStaticText[contains(@AXValue,'%s')]";

		public static final String idDidntGetTheMessageMessage = "_NS:51";

		public static final String nameReSendLink = "Re-send";
	}

	public static final class ChoosePicturePage {

		public static final String idOpenButton = "_NS:55";

		public static final String idCancelButton = "_NS:53";

		public static final String xpathFileListScrollArea = "//AXScrollArea";

		public static final String xpathSelectColumnViewButton = "//AXRadioButton[@AXDescription='column view' or @AXLabel='column view']";

		public static final String xpathFormatFinderImageFile = "//AXTextField[@AXValue='%s']";

		public static final String xpathFormatFavoritesFolderPopUp = "//AXStaticText[@AXValue='%s']";
	}
}
