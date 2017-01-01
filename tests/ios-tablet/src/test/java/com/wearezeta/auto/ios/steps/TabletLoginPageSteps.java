package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import com.wearezeta.auto.ios.pages.TabletLoginPage;

import com.wearezeta.auto.ios.tools.FastLoginContainer;
import cucumber.api.java.en.Given;

public class TabletLoginPageSteps {
    private TabletLoginPage getTabletLoginPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletLoginPage.class);
    }

    private FirstTimeOverlay getFirstTimeOverlayPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(FirstTimeOverlay.class);
    }

    /**
     * Signing in on tablet with login and password
     *
     * @throws Exception
     * @step. ^I Sign in on tablet using my email$
     */
    @Given("^I Sign in on tablet using my email$")
    public void GivenISignInUsingEmail() throws Exception {
        getTabletLoginPage().switchToLogin();
        if (FastLoginContainer.getInstance().isEnabled()) {
            getTabletLoginPage().waitForLoginToFinish();
            return;
        }
        final ClientUser self = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .getSelfUserOrThrowError();
        getTabletLoginPage().setLogin(self.getEmail());
        getTabletLoginPage().setPassword(self.getPassword());
        getTabletLoginPage().tapLoginButton();
        getTabletLoginPage().waitForLoginToFinish();
        getTabletLoginPage().acceptAlert();
        getFirstTimeOverlayPage().accept();
        getTabletLoginPage().dismissSettingsWarningIfVisible();
    }
}
