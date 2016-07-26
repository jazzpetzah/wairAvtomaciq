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
     * @step. ^I tap (Take Photo|Switch Camera|Gallery|External Camera|External Video|Back) button on Extended cursor camera overlay$
     */
    @When("^I tap (Take Photo|Switch Camera|Gallery|External Camera|External Video|Back) button on Extended cursor camera overlay$")
    public void ITapButton(String buttonName) throws Exception {
        getExtendedCursorCameraOverlayPage().tapOnButton(buttonName);
    }

    /**
     * Swipe left on extended cursor camera
     *
     * @throws Exception
     * @step. ^I swipe left on Extended cursor camera overlay$
     */
    @When("^I swipe left on Extended cursor camera overlay$")
    public void IScrollLeft() throws Exception {
        getExtendedCursorCameraOverlayPage().swipeLeftOnOverlay(3000);
    }

    /**
     * Tap on thumbnail by row and column IN CURRENT VIEW
     *
     * @param row start from 1
     * @param col start from 1
     * @throws Exception
     * @step. ^I select thumbnail in row (\d+) and col (\d+) on Extended cursor camera overlay$
     */
    @When("^I select thumbnail in row (\\d+) and col (\\d+) on Extended cursor camera overlay$")
    public void ISelectThumbnail(int row, int col) throws Exception {
        getExtendedCursorCameraOverlayPage().tapOnThumbnail(row, col);
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
     * Verify whether the photo thumbnails are visible
     *
     * @throws Exception
     * @step. ^I see thumbnails in extended cursor camera overlay$
     */
    @Then("^I see thumbnails in extended cursor camera overlay$")
    public void ISeeThumbnails() throws Exception {
        Assert.assertTrue("The thumbnails in extended cursor camera overlay should be visible",
                getExtendedCursorCameraOverlayPage().waitUntilThumbnailsVisible());
    }

    /**
     * Verify whether the button is visible
     *
     * @param shouldNotSee equal null means the button should be visible
     * @param buttonName   button name
     * @throws Exception
     * @step. ^I see (Take Photo|Switch Camera|Gallery|External Camera|External Video) button on Extended cursor camera overlay$
     */
    @Then("^I (do not )?see (Take Photo|Switch Camera|Gallery|External Camera|External Video|Back) button on Extended cursor camera overlay$")
    public void ISeeButton(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            getExtendedCursorCameraOverlayPage().waitUntilButtonVisible(buttonName);
        } else {
            getExtendedCursorCameraOverlayPage().waitUntilButtonInvisible(buttonName);
        }
    }


}
