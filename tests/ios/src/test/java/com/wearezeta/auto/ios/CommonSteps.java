package com.wearezeta.auto.ios;


import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.ios.tools.IOSSimulatorPhotoLibHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
	
	@Before ("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {
		commonBefore();
		
		String path = CommonUtils.getAppPathFromConfig(TestRun.class);
		
		if (PagesCollection.loginPage == null)
		{
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}
	
	@Before ("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {
		commonBefore();
		
		String path = CommonUtils.getAppPathFromConfig(TestRun.class);
		
		if (PagesCollection.loginPage == null)
		{
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path, false);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}
	
	private void commonBefore() throws Exception {
		try {
			String[] picturepath = new String[] {CommonUtils.getUserPicturePathFromConfig(CommonSteps.class)};
			IOSSimulatorPhotoLibHelper.CreateSimulatorPhotoLib("7.1-64", picturepath, true);
		}
		catch(Exception ex){
			System.out.println("Failed to deploy pictures into simulator");
		}
		
		boolean generateUsersFlag = Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class));
		
		if ((CommonUtils.yourUsers.size() == 0 
				|| !CommonUtils.yourUsers.get(0).getUserState().equals(UsersState.AllContactsConnected))) {
			
			if (generateUsersFlag) {
				CommonUtils.generateUsers(3);
				Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
				TestPreparation.createContactLinks();
			} else {
				CommonUtils.usePrecreatedUsers();
			}
		}
	}
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 IOSPage.clearPagesCollection();
		 IOSKeyboard.dispose();
	 }

}
