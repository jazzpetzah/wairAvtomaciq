package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	
	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name){
		PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
	}
	
	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws IOException{
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.otherUserPersonalInfoPage.swipeDown(1000);
	}

}
