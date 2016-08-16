package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollPage;
import cucumber.api.java.en.When;

public class CameraRollPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CameraRollPage getCameraRollPage() throws Exception {
        return pagesCollection.getPage(CameraRollPage.class);
    }

    /**
     * Tap the first visible picture on Camera Roll page
     *
     * @step. ^I select the first picture from Camera Roll$
     * @throws Exception
     */
    @When("^I select the first picture from Camera Roll$")
    public void ISelectFirstPicture() throws Exception {
        getCameraRollPage().selectFirstPicture();
    }
}
