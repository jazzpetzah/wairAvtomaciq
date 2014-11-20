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
		for(int i = 0; i<3; i++){
			for (ClientUser contact : CommonUtils.contacts) {
				BackEndREST.autoTestSendRequest(contact,
						CommonUtils.yourUsers.get(i));
				contact.setUserState(UsersState.RequestSend);
				Thread.sleep(500);
			}

			BackEndREST.autoTestAcceptAllRequest(CommonUtils.yourUsers.get(i));
			ClientUser user = CommonUtils.yourUsers.get(i);
			user.setUserState(UsersState.AllContactsConnected);
			CommonUtils.yourUsers.set(i, user);
		}
	}
}