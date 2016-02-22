package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletPhoneNumberVerificationPage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class PhoneNumberVerificationPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletPhoneNumberVerificationPage getTabletPhoneNumberVerificationPage() throws Exception {
        return pagesCollection.getPage(TabletPhoneNumberVerificationPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Type phone number verification code into the corresponding field
     *
     * @throws Exception
     * @step. ^I enter my code on Phone Number Verification page$
     */
    @When("^I enter my code on Phone Number Verification page$")
    public void IEnterCode() throws Exception {
        final String verificationCode = BackendAPIWrappers.getLoginCodeByPhoneNumber(
                usrMgr.getSelfUserOrThrowError().getPhoneNumber());
        getTabletPhoneNumberVerificationPage().inputConfirmationCode(verificationCode);
    }

    /**
     * Tap commit button to proceed with verification code input
     *
     * @throws Exception
     * @step. ^I tap Commit button on Phone Number Verification page$
     */
    @And("^I tap Commit button on Phone Number Verification page$")
    public void ICommitData() throws Exception {
        getTabletPhoneNumberVerificationPage().tapCommitButton();
    }
}
