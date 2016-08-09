package com.wearezeta.auto.android_tablet.steps;


import com.wearezeta.auto.android_tablet.pages.TabletExtendedCursorCameraOverlayPage;
import cucumber.api.java.en.When;

public class ExtendedCursorCameraOverlayPageSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletExtendedCursorCameraOverlayPage getExtendedCursorCameraOverlayPage() throws Exception {
        return pagesCollection.getPage(TabletExtendedCursorCameraOverlayPage.class);
    }

    /**
     * Tap on the button in extended cursor camera overlay
     *
     * @param buttonName
     * @throws Exception
     * @step. ^I tap (Take Photo|Switch Camera|Gallery) button on Extended cursor camera overlay$
     */
    @When("^I tap (Take Photo|Switch Camera|Gallery) button on Extended cursor camera overlay$")
    public void ITapButton(String buttonName) throws Exception {
        getExtendedCursorCameraOverlayPage().tapOnButton(buttonName);
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
