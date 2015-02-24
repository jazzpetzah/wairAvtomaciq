package com.wearezeta.auto.android.locators;

import com.wearezeta.auto.common.CommonUtils;

public final class TabletAndroidLocators {

	public static final String LOCATORS_PACKAGE = CommonUtils.getAndroidPackageFromConfig(TabletAndroidLocators.class);
	
	public static final String CLASS_NAME = "com.wearezeta.auto.android.locators.TabletAndroidLocators";
	
	public static final class TabletContactListPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletContactListPage";
		public static final String idProfileLink = LOCATORS_PACKAGE + ":id/ttv__conversation_list__sticky_menu__profile_link";
		
	}
	
	public static final class TabletLoginPage {
		
		public static final String CLASS_NAME = TabletAndroidLocators.CLASS_NAME + "$TabletLoginPage";
		
	}
}
