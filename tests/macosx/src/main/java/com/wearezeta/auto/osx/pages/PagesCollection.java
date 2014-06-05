package com.wearezeta.auto.osx.pages;

import java.io.IOException;

public class PagesCollection {
	private LoginPage loginPage = null;
	private ContactListPage contactListPage = null;
	private ConversationPage conversationPage = null;
	
	public void closeAllPages() throws IOException {
		if (loginPage != null) loginPage.Close();
		if (contactListPage != null) contactListPage.Close();
		if (conversationPage != null) conversationPage.Close();
	}
	
	public LoginPage getLoginPage() {
		return loginPage;
	}
	
	public void setLoginPage(LoginPage loginPage) {
		this.loginPage = loginPage;
	}
	
	public ContactListPage getContactListPage() {
		return contactListPage;
	}
	
	public void setContactListPage(ContactListPage contactListPage) {
		this.contactListPage = contactListPage;
	}
	
	public ConversationPage getConversationPage() {
		return conversationPage;
	}
	
	public void setConversationPage(ConversationPage conversationPage) {
		this.conversationPage = conversationPage;
	}
	
	
}
