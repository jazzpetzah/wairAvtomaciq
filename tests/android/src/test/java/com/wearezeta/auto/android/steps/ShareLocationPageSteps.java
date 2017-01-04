package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.ShareLocationPage;
import cucumber.api.java.en.When;

public class ShareLocationPageSteps {
    private static final int LOAD_MAP_TIMEOUT_SECONDS = 15;

    private ShareLocationPage getShareLocationPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ShareLocationPage.class);
    }

    /**
     * Tap the corresponding button on Share Location page
     *
     * @param buttonName on of possible button names
     * @throws Exception
     * @step. ^I tap (Send) button on Share Location page$
     */
    @When("^I tap (Send) button on Share Location page$")
    public void ThenISeeFirstTimeOverlay(String buttonName) throws Exception {
        if (buttonName.toLowerCase().equals("send")) {
            getShareLocationPage().retryTapButtonUntilInvisible(buttonName, LOAD_MAP_TIMEOUT_SECONDS);
        } else {
            getShareLocationPage().tapButton(buttonName);
        }
    }
}
