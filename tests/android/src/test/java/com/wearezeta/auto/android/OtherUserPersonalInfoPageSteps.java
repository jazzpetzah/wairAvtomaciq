package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {

	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
	}
	
	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws IOException{
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.otherUserPersonalInfoPage.swipeDown(1000);
	}
	
	@When("^I swipe up on other user profile page$")
	public void WhenISwipeUpOnOtherUserProfilePage() throws Throwable {
	    PagesCollection.otherUserPersonalInfoPage.swipeUp(500);
	}
	
	@When("^I click Remove$")
	public void WhenIClickRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.clickRemoveBtn();
	}
	
	@When("^I see warning message$")
	public void WhenISeeWarningMessage() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isRemoveFromConversationAlertVisible());
	}
	
	@When("^I confirm remove$")
	public void WhenIConfirmRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}
	
	@When("^I press add contact button$")
	public void WhenIPressAddContactButton() throws IOException{
		 PagesCollection.otherUserPersonalInfoPage.tapAddContactBtn();
	}
}
