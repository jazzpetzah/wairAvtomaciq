package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws IOException {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}
	
	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException  {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.iOSPage = PagesCollection.contactListPage.tapOnName(name);
	}
	
	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws IOException  {
		
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		IOSPage page = PagesCollection.contactListPage.tapOnName(name);
		
		if(page instanceof DialogPage)
		{
			PagesCollection.dialogPage = (DialogPage) page;
		}
		
		PagesCollection.iOSPage = page;
	}
	
	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		if(CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.contactListPage.swipeDown(500);
		}
		else {
			PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.contactListPage.swipeDownSimulator();
		}
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String value) throws IOException {
		 
		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());		 
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));		 
	}
	
	@Then("^I see contact list loaded with User name (.*) first in list$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
		
		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
	    Assert.assertEquals(value, PagesCollection.contactListPage.getFirstDialogName());
	}
	
	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2) throws Throwable {
		
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeLeftOnDialogPage();
		
		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOherUserProfilePage(contact1);
		infoPageSteps.WhenISwipeDownOtherUserProfilePage();
		
		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();
		
		GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
		groupChatSteps.ThenISeeGroupChatPage(contact1, contact2);
	}
	
	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2) throws InterruptedException{
		
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		Assert.assertTrue(PagesCollection.contactListPage.isGroupChatAvailableInContactList());
	}
	
	@Then("^I tap on a group chat with (.*) and (.*)$")
	public void ITapOnGroupChat(String contact1, String contact2) throws IOException{
		
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		PagesCollection.contactListPage.tapOnGroupChat(contact1, contact2);
	}
	
	@Then("^I open archived conversations$")
	public void IOpenArchivedConversations() throws IOException, InterruptedException {
		Thread.sleep(3000);
		PagesCollection.contactListPage.swipeUp(500);
	}

}
