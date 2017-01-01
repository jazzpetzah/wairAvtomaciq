package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletPhoneLoginPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class PhoneLoginPageSteps {
	private TabletPhoneLoginPage getTabletPhoneLoginPage() throws Exception {
		return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
				.getPage(TabletPhoneLoginPage.class);
	}

	/**
	 * Select Wire country on phone login page
	 * 
	 * @step. ^I select Wire country on Phone Login page$
	 * @throws Exception
	 */
	@When("^I select Wire country on Phone Login page$")
	public void ISelectWireCountry() throws Exception {
		getTabletPhoneLoginPage().selectWireCountry();
	}

    /**
     * Enter the phone number of self user
     *
     * @step. ^I enter my phone number on Phone Login page$
     *
     * @throws Exception
     */
    @When("^I enter my phone number on Phone Login page$")
    public void IEnterMyPhoneNumber() throws Exception {
        getTabletPhoneLoginPage().inputPhoneNumber(
                AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .getSelfUserOrThrowError().getPhoneNumber()
        );
    }

    /**
     * Tap commit button to proceed with phone number input
     *
     * @step. ^I tap Login button on Phone Login page$
     *
     * @throws Exception
     */
    @And("^I tap Login button on Phone Login page$")
    public void ICommitData() throws Exception {
        getTabletPhoneLoginPage().tapCommitButton();
    }
}
