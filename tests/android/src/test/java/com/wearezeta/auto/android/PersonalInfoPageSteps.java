package com.wearezeta.auto.android;

import java.io.IOException;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {

	@When("^I pull up for options$")
	public void WhenIPullUpForOptions() throws IOException {
		PagesCollection.personalInfoPaga.swipeUp(1000);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPaga.tapOptionsButtonByText(buttonName);
	}
	
}
