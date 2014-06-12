package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.osx.MacOSXTestPreparation;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	public static PagesCollection senderPages;
	
	@Before
	public void setUp() throws Exception {
		if (Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class))
				&& !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)) {
			CommonUtils.generateUsers(2);
		}
		
		String path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
		senderPages = new PagesCollection();
		
		senderPages.setLoginPage(new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path));
		
		if (Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class))
				&& !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)) {
	    	MacOSXTestPreparation.createContactLinks();
	    }
	}
	
	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();
	}
}
