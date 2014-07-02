package com.wearezeta.auto.osx.steps;

import java.io.IOException;

import cucumber.api.java.en.When;

public class ZClientMenuSteps {
	 @When("I am signing out")
	 public void WhenIAmSigningOut() throws IOException {
		 CommonSteps.senderPages.getMainMenuPage().SignOut();
	 }
}
