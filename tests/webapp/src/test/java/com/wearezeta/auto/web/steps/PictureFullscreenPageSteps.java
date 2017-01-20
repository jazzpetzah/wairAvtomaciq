package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.PictureFullscreenPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;

public class PictureFullscreenPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.69;
    private final WebAppTestContext context;

    public PictureFullscreenPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("^I( do not)? see picture (.*) in picture fullscreen$")
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
    
    @Then("^I see the picture in fullscreen is (not )?liked by me$")
    public void IsLikedByMe(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Liked by me", context.getPagesCollection().getPage(PictureFullscreenPage.class).isLikedByMeVisible(),
                    is(true));
        } else {
            assertThat("Liked by me", context.getPagesCollection().getPage(PictureFullscreenPage.class).isLikedByMeInvisible(),
                    is(true));
        }
    }

    @And("^I see a timestamp in picture fullscreen$")
    public void ISeeTimestampOnFullscreen() throws Exception {
        assertThat("Timestamp on picture fullscreen", context.getPagesCollection().getPage(PictureFullscreenPage.class).isTimestampVisible(),
                is(true));
    }

    @And("^I see a name (.*) in picture fullscreen$")
    public void ISeeSenderNameOnFullscreen(String name) throws Exception {
        if (!name.equals("YOU")){
            name = context.getUsersManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        }
        assertThat("Name on picture fullscreen", context.getPagesCollection().getPage(PictureFullscreenPage.class).getSenderName().equals(name));
    }

    @Then("^I (do not )?see like button in picture fullscreen$")
    public void IsLikedByButton(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Like button", context.getPagesCollection().getPage(PictureFullscreenPage.class).isLikeButtonVisible(),
                    is(true));
        } else {
            assertThat("Like button", context.getPagesCollection().getPage(PictureFullscreenPage.class).isLikeButtonInvisible(),
                    is(true));
        }
    }

    @Then("^I (do not )?see download button in picture fullscreen$")
    public void ISeeDownloadButton(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Download button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDownloadButtonVisible(), is(true));
        } else {
            assertThat("Download button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDownloadButtonInvisible(), is(true));
        }
    }

    @Then("^I (do not )?see delete for me button in picture fullscreen$")
    public void ISeeDeleteForMeButton(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Delete for me button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDeleteForMeButtonVisible(), is(true));
        } else {
            assertThat("Delete for me button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDeleteForMeButtonInvisible(), is(true));
        }
    }
    
    @Then("^I (do not )?see delete everywhere button in picture fullscreen$")
    public void ISeeDeleteEverywhereButton(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Delete everywhere button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDeleteEverywhereButtonVisible(), is(true));
        } else {
            assertThat("Delete everywhere button", context.getPagesCollection().getPage(PictureFullscreenPage.class).
                    isDeleteEverywhereButtonInvisible(), is(true));
        }
    }

    @When("^I click like button in picture fullscreen$")
    public void IClickLikeButton() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickLikeButton();
    }

    @When("^I click download button in picture fullscreen$")
    public void IClickDownloadButton() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickDownloadButton();
    }

    @When("^I click delete( for me| everywhere) button in picture fullscreen$")
    public void IClickDeleteForMeButton(String forMe) throws Exception {
        if (" for me".equalsIgnoreCase(forMe)) {
            context.getPagesCollection().getPage(PictureFullscreenPage.class).clickDeleteForMeButton();
        } else {
            context.getPagesCollection().getPage(PictureFullscreenPage.class).clickDeleteEverywhereButton();
        }
    }

    @When("^I click x button to close picture fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickXButton();
    }

    @When("^I click on black border to picture close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(PictureFullscreenPage.class).clickOnModalBackground();
    }

}
