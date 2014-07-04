package com.wearezeta.auto.android;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.common.ZetaFormatter;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class CommonSteps {

	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";
	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	private String path;

	@Before
	public void setUp() throws Exception {

		try {
			CommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		}
		catch(Exception ex){
			System.out.println("Failed to deploy pictures into simulator");
		}
		
		if(Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class)) &&  (CommonUtils.yourUsers.size()==0 || !CommonUtils.yourUsers.get(0).getUserState().equals(UsersState.AllContactsConnected))){
			CommonUtils.generateUsers(2);
			TestPreparation.createContactLinks();
		}

		path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
		if ( PagesCollection.loginPage == null){
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class), path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		AndroidPage.clearPagesCollection();
	}
	
	@Given("^connection request is sended to me$")
	public void GivenConnectionRequestIsSendedToMe() throws Throwable {
		BackEndREST.sendConnectRequest(CommonUtils.yourUsers.get(2), CommonUtils.yourUsers.get(0), CONNECTION_NAME + CommonUtils.yourUsers.get(2).getName(), CONNECTION_MESSAGE);
	}
	
	@Given("^I have group chat with name (.*) with (.*) and (.*)$")
	public void GivenConnectionRequestIsSendedToMe(String chatName, String contact1, String contac2) throws Throwable {
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		for (ClientUser user : CommonUtils.contacts) {
			if(user.getName().equals(contact1)) {
				BackEndREST.createGroupConveration(CommonUtils.yourUsers.get(0), CommonUtils.contacts, chatName);
			}
		}
	}	
}
