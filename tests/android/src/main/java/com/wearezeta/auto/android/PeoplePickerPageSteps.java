package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ConnectToPage;
import com.wearezeta.auto.android.pages.PagesCollection;
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
	
	@When("^I input in search field user name to connect to (.*)$")
	public void WhenIInputInSearchFieldUserNameToConnectTo(String contact) throws Throwable {
		if(contact.contains("aqaUser")){
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(CommonUtils.getContactName(CommonUtils.contacts.firstKey()));
		}
		else{
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
		}
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
		if(contact.contains("aqaUser")){
		    PagesCollection.peoplePickerPage.waitUserPickerFindUser(CommonUtils.getContactName(CommonUtils.contacts.firstKey()));
		}
		else{
		    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);	
		}
	}
	
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		if(contact.contains("aqaUser")){
			PagesCollection.connectToPage = (ConnectToPage)(PagesCollection.peoplePickerPage.selectContact(CommonUtils.getContactName(CommonUtils.contacts.firstKey())));
		}
		else{
			PagesCollection.connectToPage = (ConnectToPage)(PagesCollection.peoplePickerPage.selectContact(contact));
		}
	}
	
}
