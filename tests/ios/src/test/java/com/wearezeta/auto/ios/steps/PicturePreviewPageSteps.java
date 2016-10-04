package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.PicturePreviewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PicturePreviewPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private PicturePreviewPage getPicturePreviewPage() throws Exception {
        return pagesCollection.getPage(PicturePreviewPage.class);
    }

    /**
     * Tap the corresponding button on Picture preview page
     *
     * @step. I tap (Sketch|Confirm|Cancel|Use Photo|Retake) button on Picture [Pp]review page$
     * @param name one of possible button names
     * @throws Exception
     */
    @When("^I tap (Sketch|Confirm|Cancel|Use Photo|Retake) button on Picture [Pp]review page$")
    public void ITapButton(String name) throws Exception {
        getPicturePreviewPage().tapButton(name);
    }

    /**
     * Verify picture preview page opened
     *
     * @throws Exception
     * @step. ^I see Picture [Pp]review page$
     */
    @Then("^I see Picture [Pp]review page$")
    public void ISeePicturePreviewPage() throws Exception {
        getPicturePreviewPage().isUsePhotoButtonVisible();
    }
}
