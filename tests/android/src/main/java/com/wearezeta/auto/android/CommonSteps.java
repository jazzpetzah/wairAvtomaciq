package com.wearezeta.auto.android;

import java.io.File;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	
	private String path;
	
	@Before
	 public void setUp() throws Exception {
		if(Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class)) && !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)){
			CommonUtils.generateUsers(2);
		}
		
	    File app = new File(CommonUtils.getAppPathFromConfig(CommonSteps.class));
	    path = app.getAbsolutePath();
	    if ( PagesCollection.loginPage == null){
	        	PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path);
	    }
	    
	    if(Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class)) && !CommonUtils.yourUserState.equals(UsersState.AllContactsConnected)){
	    	AndroidTestPreparation.createContactLinks();
	    }
	 }
	 
	 @After
	 public void tearDown() throws Exception {
		 PagesCollection.loginPage.Close();
		 AndroidPage.clearPagesCollection();
	 }
}
