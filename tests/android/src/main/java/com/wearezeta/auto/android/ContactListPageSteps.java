package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}

	@When("^I tap on name (.*)$")
	public void WhenITapOnName(String name) throws IOException  {
		PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
	}
	
	@Then ("Contact list appears with my name (.*)")
	 public void ThenContactListAppears(String value) {
		 
		 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
		 
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));
		 
	 }

}
