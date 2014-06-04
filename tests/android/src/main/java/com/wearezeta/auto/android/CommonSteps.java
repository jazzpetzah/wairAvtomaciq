package com.wearezeta.auto.android;

import java.io.File;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	
	private String path;
	
	@Before
	 public void setUp() throws Exception {
		 
	        File app = new File(CommonUtils.getAppPathFromConfig(TestRun.class));
	        path = app.getAbsolutePath();
	        if ( PagesCollection.loginPage == null)
	        	{
	        		PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path);
	        	}
	        	
	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 AndroidPage.clearPagesCollection();
	 }
}
