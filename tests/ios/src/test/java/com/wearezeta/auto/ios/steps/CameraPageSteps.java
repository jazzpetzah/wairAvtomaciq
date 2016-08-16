package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraPage;
import cucumber.api.java.en.When;

public class CameraPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CameraPage getCameraPage() throws Exception {
        return pagesCollection.getPage(CameraPage.class);
    }

    /**
     * Tap the corresponding button on Camera page
     *
     * @step. ^I tap (Take Photo|Camera Roll|Close) button on Camera page$
     * @param name one of possible button names
     * @throws Exception
     */
    @When("^I tap (Take Photo|Camera Roll|Close) button on Camera page$")
    public void TTapButton(String name) throws Exception {
        getCameraPage().tapButton(name);
    }
}
