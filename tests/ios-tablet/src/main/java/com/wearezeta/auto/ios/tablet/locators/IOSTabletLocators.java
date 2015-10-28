package com.wearezeta.auto.ios.tablet.locators;

public final class IOSTabletLocators {
	
	public static final String xpathPopover = "//UIAPopover[@visible='true']";

	public static final class CameraRollTabletPopoverPage {

		public static final String xpathIPADCameraLibraryFirstFolder = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATableView[1]/UIATableCell[@name='Moments']";
		public static final String xpathIPADCameraLibraryFirtImage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIACollectionView[1]/UIACollectionCell[1]";
		public static final String xpathIPADCameraLibraryConfirmButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='OK']";
	}

	public static final class TabletPendingUserPopoverPage {
		public static final String xpathUserName = "//UIAPopover/UIAStaticText[contains(@name, '%s')]";
		public static final String xpathConnectButton = "//UIAPopover/UIAStaticText[@name='CONNECT']";
	}

	public static final class TabletPeoplePickerPage {
		public static final String nameShareContactsButton = "SHARE CONTACTS";
		public static final String xpathIPADPeoplePickerResultUserName = "//UIAPopover//UIAStaticText[@name='%s']";
		public static final String nameSearchField = "SEARCH BY NAME OR EMAIL";
		public static final String xpathSearchField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATextView[@name='textViewSearch']";
	}

	public static final class TabletConversatonListPage {

		public static final String xpathConversationListPage = "//UIAApplication[1]/UIAWindow[2]/UIACollectionView[1]";

	}

	public static final class TabletOtherUserInfoPage {
		public static final String nameOtherUserMetaControllerRightButtoniPadPopover = "OtherUserMetaControllerRightButton";
		public static final String xpathOtherUserNameField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAStaticText[2]";
		public static final String xpathOtherUserEmailField = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATextView[2]";
		public static final String xpathOtherUserConnectLabel = "//UIAPopover/UIAStaticText[@name='CONNECT']";
		public static final String xpathOtherUserConnectButton = "//UIAStaticText[@name='CONNECT']/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']";
	}

	public static final class TabletGroupConversationDetailPopoverPage {
		public static final String nameRenameButtonEllipsisMenue = "RENAME";
		public static final String xpathGroupConvTotalNumber = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAStaticText[contains(@name,'PEOPLE')]";
		public static final String namePeopleCountWord = " PEOPLE";
		public static final String xpathPopoverAvatarCollectionView = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIACollectionView[1]";
		public static final String xpathSilenceButtonEllipsisMenu = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='SILENCE']";
		public static final String xpathNotifyButtonEllipsisMenu = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='NOTIFY']";
	}

}