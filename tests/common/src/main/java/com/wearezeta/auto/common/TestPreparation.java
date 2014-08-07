package com.wearezeta.auto.common;

import java.io.IOException;

import javax.ws.rs.core.UriBuilderException;

import org.json.JSONException;

public class TestPreparation {

	public static void createContactLinks() throws IOException,
			InterruptedException, IllegalArgumentException,
			UriBuilderException, JSONException, BackendRequestException {
		for (ClientUser yourUser : CommonUtils.yourUsers) {
			yourUser = BackEndREST.loginByUser(yourUser);
			yourUser = BackEndREST.getUserInfo(yourUser);
		}

		for (ClientUser contact : CommonUtils.contacts) {
			BackEndREST.autoTestSendRequest(contact,
					CommonUtils.yourUsers.get(0));
			contact.setUserState(UsersState.RequestSend);
		}

		BackEndREST.autoTestAcceptAllRequest(CommonUtils.yourUsers.get(0));
		ClientUser user = CommonUtils.yourUsers.get(0);
		user.setUserState(UsersState.AllContactsConnected);
		CommonUtils.yourUsers.set(0, user);
	}
}