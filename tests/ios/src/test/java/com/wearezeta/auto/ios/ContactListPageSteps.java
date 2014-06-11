package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.wearezeta.auto.ios.pages.PagesCollection;

public class ContactListPageSteps {
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}
	
	@When("^I tap on name (.*)$")
	public void WhenITapOnName(String name) throws IOException  {
		PagesCollection.iOSPage = PagesCollection.contactListPage.tapOnName(name);
	}
	
	@When("^I swipe down contact list$")
	public void I_swipe_down_contact_list() throws Throwable {
	    //TODO Express the Regexp above with the code you wish you had
	    throw new Exception();
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String value) {
		 
		Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());		 
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));		 
	}
	
	@Then("^I see contact list loaded with User name (.*) first in list$")
	public void I_see_User_name_first_in_contact_list(String value) throws Throwable {
	    //TODO Express the Regexp above with the code you wish you had
	    throw new Exception();
	}

}
