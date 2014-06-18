package com.wearezeta.auto.android;

import java.io.IOException;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {

	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name){
		if(name.contains(CommonUtils.CONTACT_1)){
			PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(CommonUtils.contacts.get(0).getName());
		}
		else{
			PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
		}
	}
	
	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws IOException{
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.otherUserPersonalInfoPage.swipeDown(1000);
	}
	
}
