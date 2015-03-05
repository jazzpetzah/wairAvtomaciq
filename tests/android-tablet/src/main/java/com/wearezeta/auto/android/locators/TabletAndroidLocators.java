package com.wearezeta.auto.android.locators;

import com.wearezeta.auto.common.CommonUtils;

public final class TabletAndroidLocators {

	public static final String LOCATORS_PACKAGE = CommonUtils.getAndroidPackageFromConfig(TabletAndroidLocators.class);
	
	public static final String CLASS_NAME = "com.wearezeta.auto.android.locators.TabletAndroidLocators";
	
	public static final class TabletCommonLocators {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletPersonalInfoPage";
		
	}
	
	public static final class TabletContactListPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletContactListPage";
		
	}
	
	public static final class TabletLoginPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletLoginPage";
		
	}
	
	public static final class TabletDialogPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletDialogPage";
	
		public static final String idProfileIcon = LOCATORS_PACKAGE + ":id/gtv__cursor_participants";
	
	}
	
	public static final class TabletPersonalInfoPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletPersonalInfoPage";
		
	}
}
