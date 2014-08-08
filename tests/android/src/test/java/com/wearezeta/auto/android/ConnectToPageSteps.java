package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ConnectToPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {

	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.connectToPage = (ConnectToPage) PagesCollection.androidPage;
		Assert.assertEquals(contact.toLowerCase(),PagesCollection.connectToPage.getConnectToHeader());
	}
	
	@When("^I Connect with contact by pressing button$")
	public void WhenIConnectWithContactByPressionButton() throws Exception{
		PagesCollection.dialogPage = PagesCollection.connectToPage.pressConnectButton();	
	}
	
	
	@When("^I press Ignore connect button$")
	public void WhenIPressIgnoreConnectButton() throws Exception{
		if(PagesCollection.connectToPage == null)
		{
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.androidPage;
		}
		PagesCollection.contactListPage = PagesCollection.connectToPage.pressIgnorButton();	
	}
}
