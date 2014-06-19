package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.*;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
	
	private String message;
	
	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Throwable {
	    PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
	    PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Throwable {
	    PagesCollection.dialogPage.tapOnCursorInput();
	}
	
	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.typeMessage(message + "\n");
	}
	
	@When("^I press send$")
	public void WhenIPressSend() throws Throwable {
		//PagesCollection.dialogPage.typeMessage("\\n");
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}
	
	@Then("^I see Hello message from me in the dialog$")
	public void ISeeHelloMessageFromMeInTheDialog() throws Throwable {
		String hellomessage = "HELLO FROM";
		String dialogHelloMessage = PagesCollection.dialogPage.getHelloCellFromDialog();
		Assert.assertTrue("Message \"" + dialogHelloMessage + "\" is not correct HELLO FROM message.", dialogHelloMessage.contains(hellomessage));
	}
	
	@Then("^I see Hey message from me in the dialog$")
	public void ISeeHeyMessageFromMeInTheDialog() throws Throwable {
		String heymessage = "HEY FROM";
		String dialogHeyMessage = PagesCollection.dialogPage.getHeyCellFromDialog();
		Assert.assertTrue("Message \"" + dialogHeyMessage + "\" is not correct HEY FROM message.", dialogHeyMessage.contains(heymessage));
	}

	@When("^I type the message and send it$")
	public void ITypeTheMessageAndSendIt() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}
	
	@When("^I swipe left on dialog page$")
	public void WhenISwipeLeftOnDialogPage() throws IOException{
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage)PagesCollection.dialogPage.swipeLeft(1000);
	}

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue(dialogLastMessage.equals((message).trim()));
	}
	
	@Then("^I see Pending Connect to (.*) message on Dialog page$")
	public void ISeePendingConnectMessage(String user) throws Throwable {
		
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageValid(user));
		Assert.assertTrue(PagesCollection.dialogPage.isPendingButtonVisible());
	}


}
