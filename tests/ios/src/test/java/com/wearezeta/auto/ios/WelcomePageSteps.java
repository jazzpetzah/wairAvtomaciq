package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PersonalInfoPage;
import com.wearezeta.auto.ios.pages.WelcomePage;

import cucumber.api.java.en.When;

public class WelcomePageSteps {
	
	@When("^I swipe left to personal screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		PagesCollection.welcomePage = (WelcomePage)(PagesCollection.iOSPage);
		PagesCollection.personalInfoPage = (PersonalInfoPage)(PagesCollection.welcomePage.swipeLeft(500));
		PagesCollection.personalInfoPage.waitForEmailFieldVisible();
	}


}
