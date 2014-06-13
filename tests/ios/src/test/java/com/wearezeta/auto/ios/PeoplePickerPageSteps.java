package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	
	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Throwable {
		 Assert.assertTrue(PagesCollection.peoplePickerPage.isPeoplePickerPageVisible());
	}
	
	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
	    PagesCollection.peoplePickerPage.tapOnPeoplePickerSearch();
	}
	
	@When("^I input in People picker search field user name(.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact) throws Throwable {
	    PagesCollection.peoplePickerPage.fillTextInPeoplePickerSearch(contact);
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
	    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
	}
	
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		IOSPage page = PagesCollection.peoplePickerPage.clickOnFoundUser(contact);
		
		if(page instanceof ConnectToPage) {
			PagesCollection.connectToPage = (ConnectToPage)page;
		}
		
		else {
			PagesCollection.peoplePickerPage = (PeoplePickerPage)page;
		}
	}
	
	@When("^I search for user name (.*) and tap on it on People picker page$")
	public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(String contact) throws Throwable {
	    PagesCollection.peoplePickerPage.pickUserAndTap(contact);
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
