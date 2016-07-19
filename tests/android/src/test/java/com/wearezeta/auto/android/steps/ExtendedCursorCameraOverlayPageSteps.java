package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.ExtendedCursorCameraOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ExtendedCursorCameraOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ExtendedCursorCameraOverlayPage getExtendedCursorCameraOverlayPage() throws Exception {
        return pagesCollection.getPage(ExtendedCursorCameraOverlayPage.class);
    }

    /**
     * Tap on the button in extended cursor camera overlay
     *
     * @param buttonName the button name in overlay
     * @throws Exception
     * @step. ^I tap (Take Photo|Switch Camera|Gallery) button on Extended cursor camera overlay$
     */
    @When("^I tap (Take Photo|Switch Camera|Gallery) button on Extended cursor camera overlay$")
    public void ITapButton(String buttonName) throws Exception {
        getExtendedCursorCameraOverlayPage().tapOnButton(buttonName);
    }

    /**
     * Verify whether the extended cursor camera overlay is visible
     *
     * @param shouldNotSee equals null means extended cursor camera overlays should be visible
     * @throws Exception
     * @step. ^I (do not )?see extended cursor camera overlay$
     */
    @Then("^I (do not )?see extended cursor camera overlay$")
    public void ISeeExtendedCursorCameraOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The extended cursor camera overlay should be visible",
                    getExtendedCursorCameraOverlayPage().waitUntilOverlayVisible());
        } else {
            Assert.assertTrue("The extended cursor camera overlay should be invisible",
                    getExtendedCursorCameraOverlayPage().waitUntilOverlayInvisible());
        }
    }

    /**
     * Close extended cursor camera overlay
     *
     * @throws Exception
     * @step ^I close Extended cursor camera overlay$
     */
    @When("^I close Extended cursor camera overlay$")
    public void ICloseOverly() throws Exception {
        getExtendedCursorCameraOverlayPage().navigateBack();
    }


}
