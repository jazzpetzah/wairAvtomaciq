package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
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
	public void IPressCameraButton() throws InterruptedException, IOException{
		DialogPage page = PagesCollection.personalInfoPage.pressCameraButton();
		PagesCollection.dialogPage = (DialogPage) page;
	  Thread.sleep(2000);
	}
	
	@When("^I return to personal page$")
	public void IReturnToPersonalPage() throws Throwable {
		PagesCollection.personalInfoPage.tapOnPersonalPage();
		Thread.sleep(2000);
	}

	@Then("^I see new profile picture in place$")
	public void ISeeNewProfilePictureInPlace() throws Throwable {
		
	}



}
