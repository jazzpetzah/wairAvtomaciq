package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	public static PagesCollection senderPages;
	
	@Before
	public void setUp() throws Exception {
		boolean generateUsersFlag = Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class));
	
		if ((CommonUtils.yourUsers.size() == 0 
				|| !CommonUtils.yourUsers.get(0).getUserState().equals(UsersState.AllContactsConnected))) {
			
			if (generateUsersFlag) {
				CommonUtils.generateUsers(2);
				TestPreparation.createContactLinks();
			} else {
				CommonUtils.usePrecreatedUsers();
			}
		}
		
		String path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
		senderPages = new PagesCollection();
		
		senderPages.setLoginPage(new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path));
	}
	
	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();
	}
}
