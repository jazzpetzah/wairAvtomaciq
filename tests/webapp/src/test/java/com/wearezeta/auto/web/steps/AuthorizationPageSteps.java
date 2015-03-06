package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;

public class AuthorizationPageSteps {
	
	@Given("^I see authorization page$")
	public void ISeeSignInPage() throws Exception {
		
		Assert.assertTrue(PagesCollection.authorizationPage.isVisible());
	}
	
	@Given("^I click create account button$")
	public void IClickCreateAcount() throws Exception {
		
		PagesCollection.registrationPage = PagesCollection.authorizationPage.clickCreateAccountButton();
	}

}
