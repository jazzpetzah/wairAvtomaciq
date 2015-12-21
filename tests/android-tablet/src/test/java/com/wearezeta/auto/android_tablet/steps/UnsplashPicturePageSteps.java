package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.registration.TabletUnsplashPicturePage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class UnsplashPicturePageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletUnsplashPicturePage getUnsplashPicturePage() throws Exception {
		return pagesCollection.getPage(TabletUnsplashPicturePage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify whether Unsplash Picture screen is visible
	 * 
	 * @step. ^I see the Unsplash Picture page$
	 * @throws Exception
	 */
	@Given("^I see the Unsplash Picture page$")
	public void GivenISeeScreen() throws Exception {
		Assert.assertTrue("Welcome page is not shown", getUnsplashPicturePage()
				.waitUntilVisible());
	}

    /**
     * Tap the corresponding button on Unspash picture page
     *
     * @step. ^I tap (Choose My Own|Keep This Picture) button on Unsplash Picture page$"
     *
     * @param name either "Choose My Own" or "Keep This Picture"
     * @throws Exception
     */
    @When("^I tap (Choose My Own|Keep This Picture) button on Unsplash Picture page$")
    public void ITapButton(String name) throws Exception {
        if (name.trim().equals("Choose My Own")) {
            getUnsplashPicturePage().tapChooseMyOwnButton();
        } else {
            throw new RuntimeException("Tapping 'Keep This Picture' button is not implemented yed");
        }
    }

    /**
     * Tap the corresponding dialog button to select picture spurce
     *
     * @step. ^I select (Camera|Gallery) picture source on Unsplash Picture page$
     *
     * @param src either "Camera" or "Gallery"
     * @throws Exception
     */
    @When("^I select (Camera|Gallery) picture source on Unsplash Picture page$")
    public void ISelectPictureSource(String src) throws Exception {
        getUnsplashPicturePage().selectPictureSource(src);
    }

}
