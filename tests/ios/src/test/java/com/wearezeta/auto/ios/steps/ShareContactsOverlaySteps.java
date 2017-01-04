package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.ShareContactsOverlay;
import cucumber.api.java.en.When;

public class ShareContactsOverlaySteps {
    private ShareContactsOverlay getShareContactsOverlay() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ShareContactsOverlay.class);
    }

    /**
     * Tap the corresponding button on the overlay
     *
     * @param btnName one of possible button names
     * @throws Exception
     * @step. ^I tap (Share Contacts|Not Now) button on Share Contacts overlay$
     */
    @When("^I tap (Share Contacts|Not Now) button on Share Contacts overlay$")
    public void ITapShareContactsButton(String btnName) throws Exception {
        getShareContactsOverlay().tapButton(btnName);
    }
}