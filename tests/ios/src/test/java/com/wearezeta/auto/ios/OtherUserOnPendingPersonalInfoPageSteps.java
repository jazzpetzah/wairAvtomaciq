package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;
import cucumber.api.java.en.When;

public class OtherUserOnPendingPersonalInfoPageSteps {
	
	@When("^I see (.*) user pending profile page$")
	public void WhenISeeOtherUserProfilePage(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue("Username is not displayed", PagesCollection.otherUserOnPendingProfilePage.isUserNameDisplayed(name));
		Assert.assertTrue("Close button not displayed",PagesCollection.otherUserOnPendingProfilePage.isClosePageButtonVisible());
		Assert.assertTrue("Pending label is not displayed",PagesCollection.otherUserOnPendingProfilePage.isPendingLabelVisible());
	}
	
	@When("^I click on start conversation button on pending profile page$")
	public void WhenIClickOnStartConversatonButtonOnPendingProfilePage(){
		PagesCollection.otherUserOnPendingProfilePage.clickStartConversationButton();
	}

}
