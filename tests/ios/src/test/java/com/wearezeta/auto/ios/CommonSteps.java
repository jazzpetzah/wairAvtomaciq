package com.wearezeta.auto.ios;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.ios.tools.IOSSimulatorPhotoLibHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class CommonSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
	}
	
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

	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;
	
	private void commonBefore() throws Exception {
		try {
			String[] picturepath = new String[] {CommonUtils.getUserPicturePathFromConfig(CommonSteps.class)};
			IOSSimulatorPhotoLibHelper.CreateSimulatorPhotoLib("7.1-64", picturepath, true);
		}
		catch(Exception ex){
			System.out.println("Failed to deploy pictures into simulator");
		}
		
		boolean generateUsersFlag = Boolean.valueOf(CommonUtils.getGenerateUsersFlagFromConfig(CommonSteps.class));
		
		if (isFirstRun) {
			isFirstRun = false;
			if (generateUsersFlag) {
				CommonUtils.generateUsers(3);
				Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
				TestPreparation.createContactLinks();
			} else {
				CommonUtils.usePrecreatedUsers();
			}

			isFirstRunPassed = true;
		}

		if (!isFirstRunPassed) {
			throw new IOException("Skipped due to error in users creation.");
		}
	}
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 IOSPage.clearPagesCollection();
		 IOSKeyboard.dispose();
	 }
	 
	 @Given("^I have group chat with name (.*) with (.*) and (.*)$")
		public void GivenIHaveGroupChatWith(String chatName, String contact1,
				String contact2) throws Throwable {
			boolean flag1 = false;
			boolean flag2 = false;
			contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
			contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
			List<ClientUser> chatContacts = new LinkedList<ClientUser>();
			for (ClientUser user : CommonUtils.contacts) {
				if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
					chatContacts.add(user);
					flag1 = true;
				}
				if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
					chatContacts.add(user);
					flag2 = true;
				}
				if (flag1 && flag2) {
					break;
				}
			}
			BackEndREST.createGroupConveration(CommonUtils.yourUsers.get(0),
					chatContacts, chatName);
		}
	 
		@Given("I send invitation to (.*) by (.*)")
		public void ISendInvitationToUserByContact(String user, String contact) throws Throwable {
			user = CommonUtils.retrieveRealUserContactPasswordValue(user);
			contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
			BackEndREST.autoTestSendRequest(CommonUtils.findUserNamed(contact), CommonUtils.findUserNamed(user));
		}

}
