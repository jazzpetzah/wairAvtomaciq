package com.wearezeta.auto.osx;

import java.io.IOException;
import java.util.Map.Entry;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;
import com.wearezeta.auto.osx.steps.CommonSteps;

public class MacOSXTestPreparation {
	
	public static void createContactLinks() throws IOException, InterruptedException {
		int counter = 0;
		for (Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()) {
			String login = contact.getKey();
			System.out.println("First login by " + login);
			logIn(login, CreateZetaUser.defaultPassword);
			logOut(login);
		}
		logIn(CommonUtils.yourUserName, CreateZetaUser.defaultPassword);
		sendRequests(CommonUtils.yourUserName);
		logOut(CommonUtils.yourUserName);
		acceptAllRequests();
		for (Entry<String, UsersState> contact: CommonUtils.contacts.entrySet()) {
			if (contact.getValue().equals(UsersState.Connected)) {
				counter++;
			}
		}
		if (counter == CommonUtils.contacts.size()) {
			CommonUtils.yourUserState = UsersState.AllContactsConnected;
		}
	}
	
	private static void logIn(String userName, String userPassword) throws IOException {
		LoginPage loginPage = new LoginPage(CommonUtils.getUrlFromConfig(CommonSteps.class),
				CommonUtils.getAppPathFromConfig(CommonSteps.class));
		CommonSteps.senderPages.setLoginPage(loginPage);
		loginPage.setLogin(userName);
		loginPage.setPassword(userPassword);
		CommonSteps.senderPages.setContactListPage((ContactListPage)(CommonSteps.senderPages.getLoginPage().SignIn()));
		loginPage.waitForLogin();
		loginPage.isLoginFinished(CommonUtils.getContactName(userName));
	}
	
	private static void sendRequests(String userName) throws IOException, InterruptedException {
		for (Entry<String, UsersState> contact: CommonUtils.contacts.entrySet()) {
			String login = contact.getKey();
			String contactName = CommonUtils.getContactName(login);
			System.out.println("Sending request to " + login);
			CommonSteps.senderPages.getContactListPage().openPeoplePicker();
			PeoplePickerPage peoplePicker = new PeoplePickerPage(CommonUtils.getUrlFromConfig(MacOSXTestPreparation.class),
					CommonUtils.getAppPathFromConfig(MacOSXTestPreparation.class));
			CommonSteps.senderPages.setPeoplePickerPage(peoplePicker);
			peoplePicker.searchForText(contactName);
			peoplePicker.areSearchResultsContainUser(contactName);
			peoplePicker.scrollToUserInSearchResults(contactName);
			peoplePicker.chooseUserInSearchResults(contactName);
			peoplePicker.sendInvitationToUserIfRequested();
			contact.setValue(UsersState.RequestSend);
			CommonSteps.senderPages.getContactListPage().isContactWithNameExists(contactName);
		}
	}
	
	private static void logOut(String userName) throws IOException, InterruptedException {
		ContactListPage contactList = CommonSteps.senderPages.getContactListPage();
		contactList.SignOut();
		contactList.waitForSignOut();
		contactList.isSignOutFinished();
	}
	
	private static void acceptAllRequests() throws IOException, InterruptedException {
		for (Entry<String, UsersState> contact: CommonUtils.contacts.entrySet()) {
			String login = contact.getKey();
			System.out.println("Second login by" + login);
			logIn(login, CreateZetaUser.defaultPassword);
			CommonSteps.senderPages.getContactListPage().acceptAllInvitations();
			System.out.println("Request accepted by " + login);
			contact.setValue(UsersState.Connected);
			logOut(login);
		}
	}
}
