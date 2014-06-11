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
	public void I_see_dialog_page() throws Throwable {
	    PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
	    PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on text input$")
	public void I_tap_on_text_input() throws Throwable {
	    PagesCollection.dialogPage.tapOnCursorInput();
	}

	@When("^I type the message and send it$")
	public void I_type_the_message() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}

	@Then("^I see my message in the dialog$")
	public void I_see_my_message_in_the_dialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertEquals(message, dialogLastMessage);
	}
	
	@Then("^I see Pending Connect to (.*) dialog$")
	public void I_see_Pending_Connect_dialog(String user) throws Throwable {
	    //TODO Express the Regexp above with the code you wish you had
	    throw new Exception();
	}


}
