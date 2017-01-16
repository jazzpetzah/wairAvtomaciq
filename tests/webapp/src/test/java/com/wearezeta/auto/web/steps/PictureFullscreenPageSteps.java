package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.PictureFullscreenPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;

public class PictureFullscreenPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.69;
    private final WebAppTestContext context;

    public PictureFullscreenPageSteps(WebAppTestContext context) {
        this.context = context;
    }
    
    @Then("^I( do not)? see picture (.*) in fullscreen$")
    public void ISeePictureInFullscreen(String doNot, String pictureName) throws Exception {
        PictureFullscreenPage pictureFullscreenPage = context.getPagesCollection().getPage(PictureFullscreenPage.class);
        if (doNot == null) {
            assertTrue(pictureFullscreenPage.isPictureInModalDialog());
            assertTrue(pictureFullscreenPage.isPictureInFullscreen());
            assertThat("Overlap score of image comparsion", pictureFullscreenPage.getOverlapScoreOfFullscreenImage(pictureName),
                    greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            assertTrue(pictureFullscreenPage.isPictureNotInModalDialog());
        }
    }

    @When("^I click x button to close fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickXButton();
    }

    @When("^I click on black border to close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickOnBlackBorder();
    }

}
