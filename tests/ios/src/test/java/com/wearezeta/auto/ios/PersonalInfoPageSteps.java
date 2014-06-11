package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	
	@When("^I swipe up to bring up options$")
	public void WhenIPullUpForOptions() throws IOException {
		PagesCollection.personalInfoPage.swipeUp(1000);
	}

	@When("^I press Sign out button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButtonByText(buttonName);
	}

}
