package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		if(name.contains("aqaUser")){
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.getContactName(CommonUtils.yourUserName)));
		}
		else{
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		}
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws IOException  {
		if(name.contains("aqaUser")){
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(CommonUtils.getContactName(CommonUtils.contacts.firstKey()));
		}
		else{
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
		}
	}
	
	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException  {
		if(name.contains("aqaUser")){
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(CommonUtils.getContactName(CommonUtils.yourUserName));
		}
		else{
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
		}
	}
	
	@Then ("Contact list appears with my name (.*)")
	 public void ThenContactListAppears(String name) {
		 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
		 if(name.contains("aqaUser")){
			 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.getContactName(CommonUtils.yourUserName)));
		 }
		 else{
			 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		 }
		 
	 }

}
