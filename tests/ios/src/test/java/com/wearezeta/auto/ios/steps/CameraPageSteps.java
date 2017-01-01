package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CameraPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class CameraPageSteps {
    private CameraPage getCameraPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(CameraPage.class);
    }

    /**
     * Tap the corresponding button on Camera page
     *
     * @param name one of possible button names
     * @throws Exception
     * @step. ^I tap (Take Photo|Camera Roll|Close) button on Camera page$
     */
    @When("^I tap (Take Photo|Camera Roll|Close) button on Camera page$")
    public void TTapButton(String name) throws Exception {
        getCameraPage().tapButton(name);
    }

    /**
     * Verify whether the particular button is visible
     *
     * @step. I see (Take Photo|Camera Roll|Close) button on Camera page$
     * @param name button name
     * @throws Exception
     */
    @Then("^I see (Take Photo|Camera Roll|Close) button on Camera page$")
    public void TSeeButton(String name) throws Exception {
        Assert.assertTrue(String.format("The '%s' button is not visible on Camera screen", name),
                getCameraPage().isButtonVisible(name));
    }
}
