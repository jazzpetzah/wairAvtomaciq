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
		PagesCollection.otherUserPersonalInfoPage.clickBlockBtn();
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
		String email = null;
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().equals(contact)) {
				email = user.getEmail();
				break;
			}
		}
		
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(contact));
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isOtherUserMailVisible(email));
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
	
	//------ Group 
	
	public static final String GROUP_CHAT_NAME = "TempTestChat";

	@When("^I tap on group chat contact (.*)$")
	public void WhenITapOnGroupChatContact(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage.tapOnContact(contact);
		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
	}
	
	@When("^I swipe up group chat info page$")
	public void WhenISwipeUpGroupChatInfoPage() throws Throwable {	
		PagesCollection.otherUserPersonalInfoPage.swipeUp(500);
	}

	@When("^I swipe right on group chat info page$")
	public void WhenISwipeRightOnGroupChatInfoPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage.swipeRight(500);
	}

	@When("^I press back on group chat info page$")
	public void WhenIPressBackOnGroupChatInfoPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage.tabBackButton();
	}
	
	@When("^I press Leave conversation button$")
	public void WhenIPressLeaveConversationButton() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressLeaveConversationButton();
	}

	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}

	@When("^I select contact (.*)$")
	public void WhenISelectContact(String name) throws Throwable {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage.selectContactByName(name);
	}

	@Then("^I see that the conversation name is (.*)$")
	public void IVerifyCorrectConversationName(String name) throws IOException{
		Assert.assertEquals(PagesCollection.otherUserPersonalInfoPage.getConversationName(), name);
	}
	
	@Then("^I see the correct participant (.*) and (.*) avatars$")
	public void ISeeCorrectParticipantAvatars(String contact1, String contact2) throws IOException{
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage.isParticipantAvatars(CommonUtils.retrieveRealUserContactPasswordValue(contact1),
				CommonUtils.retrieveRealUserContactPasswordValue(contact2)));
	}
	
	@Then("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants) throws IOException{
		Assert.assertEquals(PagesCollection.otherUserPersonalInfoPage.getSubHeader(), realNumberOfParticipants + " people");
		}
	
	@Then("^I do not see (.*) on group chat info page$")
	public void ThenIDoNotSeeOnGroupChatInfoPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    Assert.assertFalse(PagesCollection.otherUserPersonalInfoPage.isContactExists(contact));
	}
	
	@Then("^I return to group chat page$")
	public void ThenIReturnToGroupChatPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage.tabBackButton();
	}
}
