package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PageCreator;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

public class ContactListPageSteps {
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}
	
	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException  {
		PagesCollection.iOSPage = PagesCollection.contactListPage.tapOnName(name);
	}
	
	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws IOException  {
		IOSPage page = PagesCollection.contactListPage.tapOnName(name);
		
		if(page instanceof DialogPage)
		{
			PagesCollection.dialogPage = (DialogPage) page;
		}
		
		PagesCollection.iOSPage = page;
	}
	
	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		PagesCollection.peoplePickerPage= (PeoplePickerPage)PagesCollection.contactListPage.swipeDown(500);
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String value) {
		 
		Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());		 
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));		 
	}
	
	@Then("^I see contact list loaded with User name (.*) first in list$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
	    Assert.assertEquals(value, PagesCollection.contactListPage.getFirstDialogName());
	}

}
