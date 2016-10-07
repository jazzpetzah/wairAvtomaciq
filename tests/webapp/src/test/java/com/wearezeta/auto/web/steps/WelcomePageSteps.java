package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WelcomePage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;

public class WelcomePageSteps {
        private final TestContext context;
        
    public WelcomePageSteps() {
        this.context = new TestContext();
    }

    public WelcomePageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Upload a picture from local file system. The picture file itself should
	 * already exist on the local file system and should be located in the
	 * ~/Documents folder
	 * 
	 * @step. ^I choose (.*) as my self picture on Welcome page$
	 * 
	 * @param pictureName
	 *            existing picture name
	 * 
	 * @throws Exception
	 */
	@And("^I choose (.*) as my self picture on Welcome page$")
	public void IUploadMyPicture(String pictureName) throws Exception {
		context.getPagesCollection().getPage(WelcomePage.class)
				.uploadPicture(pictureName);
	}

	/**
	 * Confirm current picture
	 * 
	 * @step. ^I confirm keeping picture on Welcome page$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm keeping picture on Welcome page$")
	public void IConfirmPicture() throws Exception {
		context.getPagesCollection().getPage(WelcomePage.class)
				.keepPicture();
	}

	/**
	 * Wait for Self Picture Upload dialog to vanish
	 * 
	 * @step. ^I wait for Self Picture Upload dialog to vanish$
	 * 
	 * @throws Exception
	 */
	@When("^I wait for Self Picture Upload dialog to vanish$")
	public void IWaitForPictureDialogToVanish() throws Exception {
		context.getPagesCollection().getPage(WelcomePage.class)
				.waitUntilNotVisible();
	}
}
