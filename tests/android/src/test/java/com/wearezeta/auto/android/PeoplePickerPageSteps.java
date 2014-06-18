package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {

	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Throwable {
		 Assert.assertTrue(PagesCollection.peoplePickerPage.isPeoplePickerPageVisible());
	}
	
	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
	    PagesCollection.peoplePickerPage.tapPeopleSearch();
	}
	
	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact) throws Throwable {
		if(contact.contains(CommonUtils.CONTACT_1)){
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(CommonUtils.contacts.get(0).getName());
		}
		else if(contact.contains(CommonUtils.CONTACT_2)){
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(CommonUtils.contacts.get(1).getName());
		}
		else{
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
		}
	}
	
	@When("^I input in search field user name to connect to (.*)$")
	public void WhenIInputInSearchFieldUserNameToConnectTo(String contact) throws Throwable {
		if(contact.contains(CommonUtils.CONTACT_1)){
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(CommonUtils.contacts.get(0).getName());
		}
		else if(contact.contains(CommonUtils.YOUR_USER_2)){
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(CommonUtils.yourUsers.get(1).getName());
		}
		else{
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
		}
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
		if(contact.contains(CommonUtils.CONTACT_1)){
		    PagesCollection.peoplePickerPage.waitUserPickerFindUser(CommonUtils.contacts.get(0).getName());
		}
		else if(contact.contains(CommonUtils.CONTACT_2)){
		    PagesCollection.peoplePickerPage.waitUserPickerFindUser(CommonUtils.contacts.get(1).getName());
		}
		else if(contact.contains(CommonUtils.YOUR_USER_2)){
			 PagesCollection.peoplePickerPage.waitUserPickerFindUser(CommonUtils.yourUsers.get(1).getName());
		}
		else{
		    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);	
		}
	}
	
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		AndroidPage page = null;
		if(contact.contains(CommonUtils.CONTACT_1)){
			page = PagesCollection.peoplePickerPage.selectContact(CommonUtils.contacts.get(0).getName());
		}
		else if(contact.contains(CommonUtils.CONTACT_2)){
			page = PagesCollection.peoplePickerPage.selectContact(CommonUtils.contacts.get(1).getName());
		}
		else if(contact.contains(CommonUtils.YOUR_USER_2)){
			page = PagesCollection.peoplePickerPage.selectContact(CommonUtils.yourUsers.get(1).getName());
		}
		else{
			page = PagesCollection.peoplePickerPage.selectContact(contact);
		}
		if(page instanceof ConnectToPage) {
			PagesCollection.connectToPage = (ConnectToPage) page;
		}
	}
	
	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton(){
		Assert.assertTrue("Add to conversation button is not visible", PagesCollection.peoplePickerPage.isAddToConversationBtnVisible());
	}
	
	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws IOException{
		PagesCollection.groupChatPage = (GroupChatPage)PagesCollection.peoplePickerPage.clickOnAddToCoversationButton();
	}
	
}
