package com.wearezeta.auto.ios.tablet.locators;

public final class IOSTabletLocators {

	public static final class CameraRollTabletPopoverPage {

		public static final String xpathIPADCameraLibraryFirstFolder = "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATableView[1]/UIATableCell[@name='Moments']";
		public static final String xpathIPADCameraLibraryFirtImage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIACollectionView[1]/UIACollectionCell[1]";
		public static final String xpathIPADCameraLibraryConfirmButton = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='OK']";
	}
	
	public static final class TabletPendingUserPopoverPage {
		public static final String xpathUserName = "//UIAPopover/UIAStaticText[contains(@name, '%s')]";
	}
	
	

}