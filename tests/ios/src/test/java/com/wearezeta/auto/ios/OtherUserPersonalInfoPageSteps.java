package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	
	@When("^I see (.*) user profile page$")
	public void WhenISeeOtherUserProfilePage(String name){
		
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage.isOtherUserProfileEmailVisible(name);
	}
	
	@When("^I press Add button$")
	public void WhenIPressAddButton() throws IOException, InterruptedException {
		
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.otherUserPersonalInfoPage.addContactToChat();
	}
	
	@When("^I swipe up on other user profile page$")
	public void ISwipeUpOnUserProfilePage() throws IOException {
		PagesCollection.otherUserPersonalInfoPage.swipeUp(1000);
	}
	
	@When("^I click Remove$")
	public void IClickRemove() {
		PagesCollection.otherUserPersonalInfoPage.removeFromConversation();
	}
	
	@When("^I see warning message$")
	public void ISeeAreYouSure() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isRemoveFromConversationAlertVisible());
	}
	
	@When("^I confirm remove$")
	public void IConfirmRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.confirmRemove();
	}
	
	@Then("^I see the user profile from (.*)$")
	public void ISeeTheUserProfileFrom(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		
		boolean isShown = PagesCollection.otherUserPersonalInfoPage.isOtherUserProfileEmailVisible(contact);
		
		Assert.assertTrue("I can see the contacts email on the user profile page", isShown);
	    
	}

}
