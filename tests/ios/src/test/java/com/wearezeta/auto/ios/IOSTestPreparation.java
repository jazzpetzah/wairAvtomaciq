package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.Map.Entry;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.ios.pages.*;

public class IOSTestPreparation {

	public static boolean iOSAutoRun = false;
	
	public static void createContactLinks() throws IOException, InterruptedException
	{
		int counter = 0;
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			System.out.println("First login by " + login);
			logIn(login,CreateZetaUser.defaultPassword);
			logOut(login);
			iOSAutoRun  = true;
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
	
	private static void logIn(String userName,String userPassword) throws IOException, InterruptedException
	{
		PagesCollection.loginPage.isVisible();
		PagesCollection.loginPage.SignIn();
		PagesCollection.loginPage.setLogin(userName);
		PagesCollection.loginPage.setPassword(userPassword);
		PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.SignIn());
		PagesCollection.loginPage.waitForLogin();
		if(iOSAutoRun){
			PagesCollection.loginPage.swipeRight(500);
			PagesCollection.loginPage.swipeRight(500);
		}
		else{
			PagesCollection.loginPage.catchLoginAllert();
		}
	}
	
	private static void sendRequiests(String userName) throws IOException, InterruptedException
	{
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			String contactName = CommonUtils.getContactName(login);
			System.out.println("Sending request to " + login);
			PagesCollection.peoplePickerPage = (PeoplePickerPage) (PagesCollection.contactListPage.swipeDown(500));
			Thread.sleep(10000);
			PagesCollection.peoplePickerPage.tapOnPeoplePickerSearch();
			PagesCollection.connectToPage = PagesCollection.peoplePickerPage.pickUserAndTap(contactName);
			PagesCollection.contactListPage = (ContactListPage) PagesCollection.connectToPage.sendInvitation("");
			contact.setValue(UsersState.RequestSend);
		}
	}
	
	private static void logOut(String userName) throws IOException, InterruptedException
	{
		System.out.println("Logout by" + userName);
		PagesCollection.welcomePage = (WelcomePage) PagesCollection.contactListPage.tapOnName(CommonUtils.getContactName(userName));
		PagesCollection.personalInfoPage = (PersonalInfoPage)(PagesCollection.welcomePage.swipeLeft(500));
		PagesCollection.personalInfoPage.swipeUp(1000);
		PagesCollection.personalInfoPage.tapOptionsButtonByText("Sign out");
		PagesCollection.loginPage.isLoginButtonVisible();
	}
	
	private static void acceptAllRequests() throws IOException, InterruptedException
	{
		for(Entry<String, UsersState> contact : CommonUtils.contacts.entrySet()){
			String login = contact.getKey();
			String contactName = CommonUtils.getContactName(login);
			System.out.println("Second login by" + login);
			logIn(login, CreateZetaUser.defaultPassword);
			PagesCollection.iOSPage = PagesCollection.contactListPage.tapOnName(contactName);
			PagesCollection.welcomePage = (WelcomePage) PagesCollection.iOSPage;
			System.out.println("Request accepted by " + login);
			PagesCollection.welcomePage.acceptAllConnections();
			contact.setValue(UsersState.Connected);
			PagesCollection.loginPage.swipeRight(500);
			logOut(login);
		}
	}
}

