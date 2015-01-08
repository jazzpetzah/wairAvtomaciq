package com.wearezeta.auto.osx.steps;

import cucumber.api.java.en.When;

public class ZClientMenuSteps {
	 @When("I am signing out")
	 public void WhenIAmSigningOut() throws Exception {
		 CommonOSXSteps.senderPages.getMainMenuPage().SignOut();
	 }
}
