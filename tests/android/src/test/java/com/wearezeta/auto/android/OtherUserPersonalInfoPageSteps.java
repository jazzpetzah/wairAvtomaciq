package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	
	private final String BG_IMAGE_NAME = "aqaPictureContactBG.png";

	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name){
		if(PagesCollection.otherUserPersonalInfoPage == null)
		{
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage)PagesCollection.androidPage;
		}
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
	}
	
	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws Exception{
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
	
	@When("^I confirm block$")
	public void WhenIConfirmBlock() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}
	
	@When("^I press add contact button$")
	public void WhenIPressAddContactButton() throws IOException{
		 PagesCollection.otherUserPersonalInfoPage.tapAddContactBtn();
	}
	
	@When("^I Press Block button$")
	public void IPressBlockButton() {
		PagesCollection.otherUserPersonalInfoPage.clickBlockBtn();
		
	}
	
	@Then("^I see (.*) user name and email$")
	public void ISeeUserNameAndEmail(String contact) {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String email = "";
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().equals(contact)) {
				email = user.getEmail();
				break;
			}
		}
		
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(contact));
		//Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isOtherUserMailVisible(email));
	}
	
	@Then("^User info should be shown with Block button$")
	public void UserShouldBeShownWithUnBlockButton() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isUnblockBtnVisible());
	}
	
	@Then("^I click Unblock button$")
	public void IClickUnblockButton() throws Throwable {
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage.clickUnblockBtn();
	}
	
	@Then("^I see correct background image$")
	public void ThenISeeCorrectBackgroundImage() throws Throwable {
	   Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isBackGroundImageCorrect(BG_IMAGE_NAME));
	}
}
