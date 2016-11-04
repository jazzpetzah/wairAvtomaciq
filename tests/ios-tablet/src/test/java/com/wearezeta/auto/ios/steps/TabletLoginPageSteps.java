package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import com.wearezeta.auto.ios.pages.TabletLoginPage;

import com.wearezeta.auto.ios.tools.FastLoginContainer;
import cucumber.api.java.en.Given;

public class TabletLoginPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletLoginPage getTabletLoginPage() throws Exception {
        return pagesCollection.getPage(TabletLoginPage.class);
    }

    private FirstTimeOverlay getFirstTimeOverlayPage() throws Exception {
        return pagesCollection.getPage(FirstTimeOverlay.class);
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
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        getTabletLoginPage().setLogin(self.getEmail());
        getTabletLoginPage().setPassword(self.getPassword());
        getTabletLoginPage().tapLoginButton();
        getTabletLoginPage().waitForLoginToFinish();
        getTabletLoginPage().acceptAlert();
        getFirstTimeOverlayPage().accept();
        getTabletLoginPage().dismissSettingsWarning();
    }
}
