package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	public static PagesCollection senderPages;
	
	 @Before
	 public void setUp() throws Exception {
		 String path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
		 senderPages = new PagesCollection();
		 
		 senderPages.setLoginPage(new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path));
	 }
	 
	 @After
	 public void tearDown() throws Exception {
		 senderPages.closeAllPages();
	 }
}
