package com.wearezeta.auto.osx.locators;

public final class OSXLocators {
	
	public static final String idLoginPage = "_NS:6";
	public static final String idMainWindow = "Main";
	
	public static final String nameSignInButton = "Sign In";

	public static final String xpathLoginField = "//AXTextField[@AXPlaceholderValue='username']";
	
	public static final String xpathPasswordField = "//AXTextField[@AXPlaceholderValue='password']";

	public static final String idContactEntry = "clListItemNameField";
	public static final String idAddImageButton = "AddImageButton";
	public static final String idPeopleButton = "PeopleButton";
	public static final String idAddConversationButton = "addConversationButton";
	public static final String idPeoplePickerSearchField = "people_picker_searchfield";
	public static final String idPeoplePickerDismissButton = "people_picker_cancel_button";
	public static final String idPeoplePickerSearchResultEntry = "people_picker_result_namefield";
	public static final String idAcceptInvitationButton = "_NS:14";
	
	public static final String nameSayHelloMenuItem = "“Hello”";
	public static final String nameSignOutMenuItem = "Sign Out";
	public static final String nameQuitZClientMenuItem = "Quit ZClient";
	public static final String nameSendInvitationButton = "Send";
	
	public static final String xpathMessageEntry = "//AXGroup/AXStaticText";
	public static final String xpathNewMessageTextArea = "//AXTextArea";
	public static final String xpathFileListScrollArea = "//AXScrollArea";
	public static final String xpathConversationImageEntry = "//AXGroup/AXImage";
	public static final String xpathSearchResultsScrollArea = "//AXScrollArea[AXTable[@AXIdentifier='people_picker_searchresult']]";

	public static final String xpathFormatContactEntryWithName = "//AXStaticText[@AXIdentifier='clListItemNameField'][@AXValue='%s']";
	public static final String xpathFormatSpecificMessageEntry = "//AXGroup/AXStaticText[@AXValue='%s']";
	public static final String xpathFormatPeoplePickerSearchResultUser = "//AXStaticText[@AXIdentifier='people_picker_result_namefield'][@AXValue='%s']";
	public static final String xpathConversationListScrollArea = "//AXScrollArea[@AXIdentifier='conversationList']";
	
	public static final String YOU_KNOCKED_MESSAGE = "YOU KNOCKED";
	public static final String YOU_ADDED_MESSAGE = "YOU ADDED";
	public static final String YOU_REMOVED_MESSAGE = "YOU REMOVED";
	
	public static final String idChooseImageCancelButton = "_NS:53";
	public static final String idChooseImageOpenButton = "_NS:55";
	public static final String xpathFormatFinderImageFile = "//AXTextField[@AXValue='%s']";
	
	public static final String idAddPeopleButtonGroupChat = "addButton";
	public static final String idConfirmationViewConfirmButton = "ConfirmationViewConfirmButton";
	public static final String idAddPeopleButtonSingleChat = "userProfileViewButtonOnTheLeft";
	//OLD: public static final String idPeoplePickerAddToConversationButton = "people_picker_confirm_button";
	public static final String idPeoplePickerAddToConversationButton = "ConfirmationBarViewConfirmButton";
	public static final String idLeaveConversationButton = "leaveButton";
	public static final String idRemoveUserFromConversation = "userProfileViewButtonOnTheRight";
	public static final String idConversationScrollArea = "ConversationScrollArea";
	
	public static final String xpathPictureFromImageFile = "//AXCheckBox[1]";
	public static final String xpathPictureFromCamera = "//AXCheckBox[2]";
	public static final String xpathPictureConfirmationButton = "//AXCheckBox[1]";
	public static final String xpathDoCameraShotButton = "//AXCheckBox[1]";
	
	public static final String xpathFormatPeoplePickerUserCell = "//AXUnknown[AXStaticText[@AXValue='%s']]";

	public static final String xpathPictureSettingsCloseButton = "//AXWindow[@AXRoleDescription='floating window']/AXButton[@AXRoleDescription='close button']";
}
