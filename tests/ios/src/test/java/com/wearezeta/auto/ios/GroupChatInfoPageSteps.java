package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;


import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {
	
	@When("^I press leave converstation button$")
	public void IPressLeaveConverstationButton() throws Throwable {
		PagesCollection.groupChatInfoPage.leaveConversation();
	}
	
	@When("^I change the conversation name$")
	public void IChangeConversationName() throws IOException{
	PagesCollection.groupChatInfoPage.changeConversationNameToRandom();
	}
	
	@Then("^I see that the conversation name is correct with (.*) and (.*)$")
	public void IVerifyCorrectConversationName(String contact1, String contact2) throws IOException{
		Assert.assertTrue(PagesCollection.groupChatInfoPage.isCorrectConversationName(contact1, contact2));
	}
	
	@When("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants) throws IOException{
		Assert.assertTrue(PagesCollection.groupChatInfoPage.isNumberOfParticipants(Integer.parseInt(realNumberOfParticipants)));
		}
	
	@When("^I tap on all of the participants and check their emails and names$")
	public void ITapAllParticipantsAndCheckElements() throws IOException{
		PagesCollection.groupChatInfoPage.tapAndCheckAllParticipants();
	}
	
	@When("^I see the correct participant avatars$")
	public void IVerifyCorrectParticipantAvatars() throws IOException{
		Assert.assertTrue(PagesCollection.groupChatInfoPage.areParticipantAvatarsCorrect());
	}
	
	@When("^I exit the group info page$")
	//may require reworking when the UI changes
	public void IExitGroupInfoPage(){
		PagesCollection.groupChatInfoPage.exitGroupInfoPage();
	}

	@When("^I see leave conversation alert$")
	public void ISeeLeaveConversationAlert() throws Throwable {
		
		Assert.assertTrue(PagesCollection.groupChatInfoPage.isLeaveConversationAlertVisible());
	}
	
	@Then("^I press leave$")
	public void IPressLeave() throws Throwable {
		PagesCollection.groupChatInfoPage.confirmLeaveConversation();
		PagesCollection.contactListPage.waitForContactListToLoad();
	}
	
	@When("^I select contact (.*)$")
	public void ISelectContact(String name) throws IOException {
		
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.otherUserPersonalInfoPage = PagesCollection.groupChatInfoPage.selectContactByName(name);
	}
	
	@Then("^I see that conversation has (.*) people$")
	public void ISeeThatConversationHasNumberPeople(int number) throws Throwable {
		
		int actualNumberOfPeople = PagesCollection.groupChatInfoPage.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat (" + actualNumberOfPeople
				+ ") is not the same as expected (" + number +")",
				actualNumberOfPeople == number);
	}

	@When("^I see (.*) participants avatars$")
	public void ISeeNumberParticipantsAvatars(int number) throws Throwable {
		
		int actual = PagesCollection.groupChatInfoPage.numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number +")",
				actual == number);
	}    
}
