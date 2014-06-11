package com.wearezeta.auto.android;


import java.io.IOException;
import java.util.Map.Entry;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.*;

public class AndroidTestPreparation {
	
	public static void createContactLinks() throws IOException, InterruptedException
	{
		int counter = 0;
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			System.out.println("First login by " + login);
			logIn(login,CreateZetaUser.defaultPassword);
			logOut(login);
		}
		logIn(CommonUtils.yourUserName,CreateZetaUser.defaultPassword);
		sendRequiests(CommonUtils.yourUserName);
		logOut(CommonUtils.yourUserName);
		acceptAllRequests();
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			if(contact.getValue().equals(UsersState.Connected)){
				counter++;
			}
		}
		if(counter == CommonUtils.contacts.size())
		{
			CommonUtils.yourUserState = UsersState.AllContactsConnected;
		}
	}
	
	private static void logIn(String userName,String userPassword) throws IOException
	{
		PagesCollection.loginPage =(LoginPage)(PagesCollection.loginPage.SignIn());
		PagesCollection.loginPage.setLogin(userName);
		PagesCollection.loginPage.setPassword(userPassword);
		PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.SignIn());
		PagesCollection.loginPage.waitForLogin();
		PagesCollection.loginPage.isLoginFinished(CommonUtils.getContactName(userName));
	}
	
	private static void sendRequiests(String userName) throws IOException, InterruptedException
	{
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			String contactName = CommonUtils.getContactName(login);
			System.out.println("Sending request to " + login);
			PagesCollection.peoplePickerPage = (PeoplePickerPage)(PagesCollection.contactListPage.swipeDown(500));
			PagesCollection.peoplePickerPage.waitForPickerSearch();
			PagesCollection.peoplePickerPage.tapPeopleSearch();
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contactName);
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.peoplePickerPage.selectContact(contactName);
			PagesCollection.contactListPage = PagesCollection.connectToPage.tapSend();
			contact.setValue(UsersState.RequestSend);
			PagesCollection.loginPage.isLoginFinished(userName);
		}
	}
	
	private static void logOut(String userName) throws IOException, InterruptedException
	{
		PagesCollection.instructionsPage = (InstructionsPage) PagesCollection.contactListPage.tapOnName(CommonUtils.getContactName(userName));
		PagesCollection.personalInfoPaga = (PersonalInfoPage)(PagesCollection.instructionsPage.swipeLeft(500));
		PagesCollection.personalInfoPaga.waitForEmailFieldVisible();
		PagesCollection.personalInfoPaga.swipeUp(1000);
		PagesCollection.personalInfoPaga.tapOptionsButtonByText("Sign out");
		PagesCollection.loginPage.isWelcomeButtonsExist();
	}
	
	private static void acceptAllRequests() throws IOException, InterruptedException
	{
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			String contactName = CommonUtils.getContactName(login);
			System.out.println("Second login by" + login);
			logIn(login, CreateZetaUser.defaultPassword);
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(contactName);
			PagesCollection.instructionsPage = (InstructionsPage) PagesCollection.androidPage;
			PagesCollection.instructionsPage.waitConnectRequestDialog();
			System.out.println("Request accepted by " + login);
			PagesCollection.instructionsPage.acceptAllConnections();
			PagesCollection.instructionsPage.swipeRight(500);
			contact.setValue(UsersState.Connected);
			logOut(login);
		}
	}
}
