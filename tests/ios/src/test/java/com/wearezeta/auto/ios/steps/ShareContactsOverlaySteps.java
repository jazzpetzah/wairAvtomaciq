package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.ShareContactsOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ShareContactsOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ShareContactsOverlay getShareContactsOverlay() throws Exception {
        return pagesCollection.getPage(ShareContactsOverlay.class);
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