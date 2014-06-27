package com.wearezeta.auto.common;

public final class IOSLocators {
	
	public static final String nameLoginPage = "ZClientMainWindow";
	
	public static final String nameSignInButton = "SignIn";
	
	public static final String nameSignUpButton = "SignUp";
	
	public static final String nameLoginButton = "ConfirmSignIn";
	
	public static final String nameLoginField = "SignInEmail";
	
	public static final String namePasswordField = "SignInPassword";
	
	public static final String classNameAlert = "UIAAlert";
	
	public static final String nameAlertOK = "OK";
	
	public static final String classNameContactListNames = "UIAStaticText";
	
	public static final String nameWelcomeLabel = "Welcome Thank you for joining us, you are now ready to go.";

	public static final String classNameUIAButton = "UIAButton";
	
	public static final String xpathEmailField = "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]";

	public static final String xpathUserProfileName = "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
	
	public static final String classNameDialogMessages = "UIATableCell";

	public static final String xpathCursorInput = "//UIAApplication[1]/UIAWindow[1]/UIAStaticText[last()]";//"TAP OR SLIDE  ";
	
	public static final String nameTextInput = "ComposeControllerTextView";
	
	public static final String xpathLastMessageFormat = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[%s]/UIATextView[1]";
	
	public static final String namePickerSearch = "textViewSearch";
	
	public static final String namePickerClearButton = "PeoplePickerClearButton";
	
	public static final String nameSendConnectButton = "SEND";
	
	public static final String clasNameConnectDialogLabel = "UIATextField";
	
	public static final String classNameConnectDialogInput = "UIATextView";
	
	public static final String xpathConnectCloseButton = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]";

	public static final String xpathFirstInContactList = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[1]/UIACollectionCell[2]/UIAStaticText[1]";
	
	public static final String classNameConnectMessageLabel = "UIATextField";
	
	public static final String xpathUnicUserPickerSearchResult = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[2]/UIACollectionCell[1]";
	
	public static final String namePendingButton = "PENDING";
	
	public static final String nameSignOutButton = "Sign out";
	
	public static final String nameConnectInput = "Type your first message to [%s]...";
	
	public static final String nameProfileOtherUserNameField = "ProfileOtherNameField";
	
	public static final String classNameProfileOtherUserEmailField = "UIATextField";
	
	public static final String nameCreateConversationButton = "CREATE CONVERSATION";
	
	public static final String nameAddToConversationButton = "ADD TO CONVERSATION";
	
	public static final String nameAcceptConnectionButton = "ACCEPT";
	
	public static final String classUIATextView = "UIATextView";

	public static final String xpathHelloCellFormat = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[last()]";
	
	public static final String xpathHeyCellFormat = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[last()]";
	
	public static final String nameCameraButton = "CameraButton";
	
	public static final String namePhotoButton = "PhotoLibraryButton";
	
	public static final String nameAlbum = "Saved Photos";
	
	public static final String classNamePhotos = "UIACollectionCell";
	
	public static final String nameConfirmImageButton = "ImageConfirmerConfirmButton";
	
	public static final String xpathYourName = "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
	
	public static final String nameYourEmail = "RegistrationEmailField";
	
	public static final String nameYourPassword = "RegistrationPasswordField";
	
	public static final String nameContinueButton = "CONTINUE";
	
	public static final String nameCreateAccountButton = "RegistrationCreateAccountButton";
	
	public static final String xpathLastGroupChatMessage = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIATextView[last()]";
	
	public static final String nameLeaveConversation = "Leave conversation";
	
	public static final String nameLeaveConversationAlert = "Leave conversation?";

	public static final String nameLeaveConversationButton = "LEAVE";
	
	public static final String nameYouHaveLeft = "YOU HAVE LEFT";

	public static final String nameRemoveFromConversation = "Remove";
	public static final String xpathOtherConversationCellFormat = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[last()]";

	public static final String nameComfirmRemoveButton = "REMOVE";
	public static final String nameAddPictureButton = "ComposeControllerPictureButton";
	
	public static final String nameCameraRollButton = "FullScreenCameraRightButton";
	
	public static final String xpathCameraRollAlertOK = "//UIAApplication[1]/UIAWindow[4]/UIAAlert[1]/UIATableView[2]/UIATableCell[1]";
	
	public static final String nameCameraRollCancel = "Cancel";
	
	public static final String xpathCameraRollTableCell = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]";
	
	public static final String xpathCameraRollPicture = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[1]/UIACollectionCell[1]";
	
	public static final String nameConfirmPictureButton = "ImageConfirmerConfirmButton";
		
	public String character;
	public void Language(String characters){
		this.character=characters;
	}
	public enum Language {
		GERMAN(),
		MANDARIN(),
		ENGLISH();
		
		char[] characters = {'a', '\uE403'};
	}
	
}
