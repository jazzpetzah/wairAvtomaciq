package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.PicturePreviewPage;
import cucumber.api.java.en.When;

public class PicturePreviewPageSteps {
    private PicturePreviewPage getPicturePreviewPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(PicturePreviewPage.class);
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
}
