package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	
	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Throwable {
		Assert.assertTrue(PagesCollection.peoplePickerPage.isPeoplePickerPageVisible());
	}
	
	@When("I re-enter the people picker if top people list is not there")
	public void IRetryPeoplePickerIfNotLoaded() throws IOException, Exception{
		if(!PagesCollection.peoplePickerPage.isTopPeopleLabelVisible()){
			IClickCloseButtonDismissPeopleView();
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage.swipeDown(500);
			} else {
				PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage.swipeDownSimulator();
			}
		}
	}
	
	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
	    PagesCollection.peoplePickerPage.tapOnPeoplePickerSearch();
	}
	
	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.fillTextInPeoplePickerSearch(contact);
	}
	
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
	}
	
	@When("^I tap on NOT connected user name on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.connectToPage = PagesCollection.peoplePickerPage.clickOnNotConnectedUser(contact);
	}
	
	@When("^I search for user name (.*) and tap on it on People picker page$")
	public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
	    PagesCollection.peoplePickerPage.pickUserAndTap(contact);
	}
	
	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton(){
		Assert.assertTrue("Add to conversation button is not visible", PagesCollection.peoplePickerPage.isAddToConversationBtnVisible());
	}
	
	@When("^I don't see Add to conversation button$")
	public void WhenIDontSeeAddToConversationButton(){
		Assert.assertTrue("Add to conversation button is visible", PagesCollection.peoplePickerPage.addToConversationNotVisible());
	}
	
	@When("^I click on Go button$")
	public void WhenIClickOnGoButton() throws IOException{
		PagesCollection.groupChatPage = (GroupChatPage)PagesCollection.peoplePickerPage.clickOnGoButton();
	}

	@When("^I click clear button$")
	public void WhenIClickClearButton() throws IOException{
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage.dismissPeoplePicker();
	}
	
	@When("I tap on (.*) top connections")
	public void WhenITapOnTopConnections(int numberOfTopConnections){
		PagesCollection.peoplePickerPage.tapNumberOfTopConnections(numberOfTopConnections);
	}
	
	@When("I click on connected user (.*) avatar on People picker page")
	public void IClickOnUserIconToAddItToExistingGroupChat(String contact) throws Throwable{
		String name = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.peoplePickerPage.clickConnectedUserAvatar(name);
	}
	
	@When("I see contact list on People picker page")
	public void ISeeContactListOnPeoplePickerPage(){
		Assert.assertTrue("Contacts label is not shown", PagesCollection.peoplePickerPage.isContactsLabelVisible());
	}
	
	@When("I see top people list on People picker page")
	public void ISeeTopPeopleListOnPeoplePickerPage(){
		Assert.assertTrue("Top People label is not shown", PagesCollection.peoplePickerPage.isTopPeopleLabelVisible());
	}
	
	@When("I tap on connected user (.*) on People picker page")
	public void ISelectUserOnPeoplePickerPage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.peoplePickerPage.selectUser(name);
	}
	
	@When("I see Create Conversation button on People picker page")
	public void ISeeCreateConversationButton(){
		Assert.assertTrue("Create Conversation button is not visible.", PagesCollection.peoplePickerPage.isCreateConversationButtonVisible());
	}
	
	@When("I click Create Conversation button  on People picker page")
	public void IClickCreateConversationButton() throws Throwable{
		PagesCollection.groupChatPage = PagesCollection.peoplePickerPage.clickCreateConversationButton();
	}
	
	@When("I see user (.*) on People picker page is selected")
	public void ISeeUserIsSelectedOnPeoplePickerPage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserSelected(name));
	}
	
	@When("I see user (.*) on People picker page is NOT selected")
	public void ISeeUserIsNotSelectedOnPeoplePickerPage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertFalse(PagesCollection.peoplePickerPage.isUserSelected(name));
	}
	
	@When("I press backspace button")
	public void IPressBackspaceBtn(){
		PagesCollection.peoplePickerPage.hitDeleteButton();
	}
	
	@When("I swipe up on People picker page")
	public void ISwipeUpPeoplePickerPage() throws Throwable{
		PagesCollection.peoplePickerPage.swipeUp(500);
	}
	
	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Throwable{
		PagesCollection.groupChatPage = (GroupChatPage)PagesCollection.peoplePickerPage.clickAddToCoversationButton();
	}
	
	@When("I click close button to dismiss people view")
	public void IClickCloseButtonDismissPeopleView(){
		PagesCollection.peoplePickerPage.tapOnPeoplePickerClearBtn();
	}

}
