package com.wearezeta.auto.ios;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	
	@Before
	 public void setUp() throws Exception {
		

		try {
			CommonUtils.iOSSimulatorCameraRoll();
		}
		catch(Exception ex){
			System.out.println("Failed to deploy pictures into simulator");
		}
		
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

		String path = CommonUtils.getAppPathFromConfig(TestRun.class);
		
		if ( PagesCollection.loginPage == null)
		{
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path);
		}

	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 IOSPage.clearPagesCollection();
	 }

}
