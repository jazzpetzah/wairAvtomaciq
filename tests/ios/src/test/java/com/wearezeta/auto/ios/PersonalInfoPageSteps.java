package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	
	@When("^I swipe up for options$")
	public void WhenISwipeUpForOptions() throws IOException, Throwable {
		PagesCollection.personalInfoPage.swipeUp(500);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButtonByText(buttonName);
	}
	
	@When("^I tap on personal screen$")
	public void ITapOnPersonalScreen() throws InterruptedException{
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
	}
	
	@When("^I press Camera button$")
	public void IPressCameraButton() throws InterruptedException{
	  PagesCollection.personalInfoPage.pressCameraButton();
	  Thread.sleep(2000);
	}

}
