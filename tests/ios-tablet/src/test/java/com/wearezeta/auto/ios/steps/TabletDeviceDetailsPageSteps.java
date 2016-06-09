package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletDeviceDetailsPage;
import cucumber.api.java.en.And;

public class TabletDeviceDetailsPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletDeviceDetailsPage getTabletDeviceDetailsPage() throws Exception {
        return pagesCollection.getPage(TabletDeviceDetailsPage.class);
    }

    /**
     * Navigate back to the previous page
     *
     * @throws Exception
     * @step. ^I navigate back from Device Details page on iPad$
     */
    @And("^I navigate back from Device Details page on iPad$")
    public void INavigateBackFromDeviceDetails() throws Exception {
        getTabletDeviceDetailsPage().tapBackButton();
    }
}
