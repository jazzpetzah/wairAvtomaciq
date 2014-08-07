package com.wearezeta.auto.android;

import java.io.IOException;
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
import com.wearezeta.auto.common.UsersState;
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

	@Before("@nonUnicode")
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

	@Given("^(.*) connection request is sended to me$")
	public void GivenConnectionRequestIsSendedToMe(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				BackEndREST.loginByUser(user);
				BackEndREST.sendConnectRequest(user,
						CommonUtils.yourUsers.get(0), CONNECTION_NAME
						+ user.getName(),
						CONNECTION_MESSAGE);
				break;
			}

		}

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

	@When("^I press back button$")
	public void PressBackButton() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

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

		if ((CommonUtils.yourUsers.size() == 0 || !CommonUtils.yourUsers.get(0)
				.getUserState().equals(UsersState.AllContactsConnected))) {

			if (generateUsersFlag) {
				CommonUtils.generateUsers(3);
				TestPreparation.createContactLinks();
			} else {
				CommonUtils.usePrecreatedUsers();
			}
		}
		path = CommonUtils.getAppPathFromConfig(CommonSteps.class);
	}
}
