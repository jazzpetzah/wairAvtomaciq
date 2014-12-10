package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Then;
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
	
	@When("^I tap on create conversation$")
	public void WhenITapOnCreateConversation() throws Throwable {
	   PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage.tapCreateConversation();;
	}
	
	@When("^I press Clear button$")
	public void WhenIPressClearButton() throws Throwable {
	   PagesCollection.contactListPage = PagesCollection.peoplePickerPage.tapClearButton();
	}
	
	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String email = CommonUtils.retrieveRealUserEmailValue(contact);
		if (email != "") {
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(email);
		}
		else {
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
		}
	}
	
	@When("^I input in search field part (.*) of user name to connect to (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldPartOfUserName(String part, String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String[] list = contact.split("(?<=\\G.{"+ part +"})");
		PagesCollection.peoplePickerPage.typeTextInPeopleSearch(list[0]);
	}
	
	@When("^I input in search field user name to connect to (.*)$")
	public void WhenIInputInSearchFieldUserNameToConnectTo(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String email = CommonUtils.retrieveRealUserEmailValue(contact);
		if (email != "") {
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(email);
		}
		else {
			PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
		}
	}
	
	@When("^I add in search field user name to connect to (.*)$")
	public void WhenIAddInSearchFieldUserNameToConnectTo(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.peoplePickerPage.addTextToPeopleSearch(contact);
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);	
	}
	
	@Then("^I see that no results found$")
	public void ISeeNoResultsFound() {
		Assert.assertTrue(PagesCollection.peoplePickerPage.isNoResultsFoundVisible());
	}
	
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
		PagesCollection.androidPage = PagesCollection.peoplePickerPage.selectContact(contact);
		
		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
	}
	
	@When("^I  long tap on user name found on People picker page (.*)$")
	public void WhenILongTapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.peoplePickerPage.selectContactByLongTap(contact);
	}
	
	@When("^I tap on group name found on People picker page (.*)$")
	public void WhenITapOnGroupNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.androidPage = PagesCollection.peoplePickerPage.selectGroup(contact);
	}
	
	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton(){
		Assert.assertTrue("Add to conversation button is not visible", PagesCollection.peoplePickerPage.isAddToConversationBtnVisible());
	}
	
	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Exception{
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage.clickOnAddToCoversationButton();
	}
	
	@Then("^I see user (.*)  in People picker$")
	public void ThenISeeUserInPeoplePicker(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    Assert.assertTrue(PagesCollection.peoplePickerPage.userIsVisible(contact));	
	}
	
	@Then("^I see group (.*)  in People picker$")
	public void ThenISeeGroupInPeoplePicker(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    Assert.assertTrue(PagesCollection.peoplePickerPage.groupIsVisible(contact));	
	}
	
	
}
