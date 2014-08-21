package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.json.JSONException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonSteps {

	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";
	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	private String path;

	@Before("~@unicode")
	public void setUp() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getUrlFromConfig(CommonSteps.class), path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}
	}

	@Before("@unicode")
	public void setUpUnicode() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getUrlFromConfig(CommonSteps.class), path, true);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}
	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		AndroidPage.clearPagesCollection();
	}

	@Given("^(.*) connection request is sended to me (.*)$")
	public void GivenConnectionRequestIsSendedToMe(String contact, String me) throws Throwable {
		ClientUser yourUser = null;
		me = CommonUtils.retrieveRealUserContactPasswordValue(me);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(me.toLowerCase())) {
				yourUser = user;
			}
		}

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				BackEndREST.loginByUser(user);
				BackEndREST.sendConnectRequest(user,
						yourUser, CONNECTION_NAME
						+ user.getName(),
						CONNECTION_MESSAGE);
				break;
			}

		}

	}
	
	@Given("My Contact (.*) has group chat with me (.*) and his Contact (.*) with name (.*)")
	public void GivenMyContactCreateGroupChatWithMeAndHisContact(String contact1, String me, String contact2, String chatName) throws IllegalArgumentException, UriBuilderException, IOException, JSONException, BackendRequestException, InterruptedException
	{
		ClientUser yourUser = null;
		ClientUser yourContact = null;
		ClientUser contactContact = null;
		boolean flag1 = false;
		boolean flag2 = false;
		me = CommonUtils.retrieveRealUserContactPasswordValue(me);
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);		
		
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(me.toLowerCase())) {
				yourUser = user;
				flag1 = true;
			}
			
			if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
				contactContact = user;
				flag2 = true;
			}
			if (flag1 && flag2) {
				break;
			}
		}
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
				yourContact=user;
				break;
			}
		}
		List<ClientUser> users = new LinkedList<ClientUser>();
		users.add(yourUser);
		users.add(contactContact);
		BackEndREST.createGroupConveration(yourContact,users, chatName);
	}

	@Given("^User (.*) is connected with (.*)")
	public void GivenUserIsConnectedWith(String contact1, String contact2) throws IllegalArgumentException, UriBuilderException, IOException, JSONException, BackendRequestException, InterruptedException{
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		ClientUser contactInfo1 = null;
		ClientUser contactInfo2 = null;
		boolean flag1 = false;
		boolean flag2 = false;
		List<ClientUser> newList = new ArrayList<ClientUser>();
		newList.addAll(CommonUtils.contacts);
		newList.addAll( CommonUtils.yourUsers);
		
		for (ClientUser user : newList) {
			if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
				contactInfo1 = user;
				flag1 = true;
			}
			if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
				contactInfo2 = user;
				flag2 = true;
			}
			if (flag1 && flag2) {
				break;
			}
		}
		
		contactInfo1 = BackEndREST.loginByUser(contactInfo1);
		BackEndREST.autoTestSendRequest(contactInfo1,contactInfo2);
		BackEndREST.autoTestAcceptAllRequest(contactInfo2);	
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

	@When("^(.*) ignore all requests$")
	public void IgnoreConnectRequest(String contact) throws IllegalArgumentException, UriBuilderException, IOException, JSONException, BackendRequestException{
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		BackEndREST.ignoreAllConnections(yourСontact);
	}
	
	@When("^I minimize the application$")
	public void IMimizeApllication() throws InterruptedException {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.minimizeApplication();
		}
	}
	
	@When("^I restore the application$")
	public void IRestoreApllication() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.restoreApplication();
		}
	}
		
	@When("^(.*) accept all requests$")
	public void AcceptConnectRequest(String contact) throws IllegalArgumentException, UriBuilderException, IOException, JSONException, BackendRequestException{
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		BackEndREST.acceptAllConnections(yourСontact);
	}
	
	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;
	
	private void commonBefore() throws IOException, InterruptedException,
	MessagingException, IllegalArgumentException, UriBuilderException,
	JSONException, BackendRequestException {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		} catch (Exception ex) {
			System.out.println("Failed to deploy pictures into simulator");
		}

		boolean generateUsersFlag = Boolean.valueOf(CommonUtils
				.getGenerateUsersFlagFromConfig(CommonSteps.class));

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
	
		path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
	}
}
