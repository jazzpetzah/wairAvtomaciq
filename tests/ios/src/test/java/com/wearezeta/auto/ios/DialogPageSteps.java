package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

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
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.typeMessage(message);
	}
	
	@When("^I press send$")
	public void WhenIPressSend() throws Throwable {
		PagesCollection.dialogPage.typeMessage("\\n");
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}
	
	@Then("^I see Hello message in the dialog$")
	public void ISeeHelloMessageInTheDialog() throws Throwable {
		//TODO: implement check that HELLO message appear
	}
	
	@Then("^I see Hey message in the dialog$")
	public void ISeeHeyMessageInTheDialog() throws Throwable {
		//TODO: implement check that HEY message appear
	}

	@When("^I type the message and send it$")
	public void ITypeTheMessageAndSendIt() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue(dialogLastMessage.equals((message).trim()));
	}
	
	@Then("^I see Pending Connect to (.*) message on Dialog page$")
	public void ISeePendingConnectMessage(String user) throws Throwable {
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageValid(user));
		Assert.assertTrue(PagesCollection.dialogPage.isPendingButtonVisible());
	}


}
