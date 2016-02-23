package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;

import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection
            .getInstance();

    private ImageFullScreenPage getImageFullScreenPage() throws Exception {
        return pagesCollection.getPage(ImageFullScreenPage.class);
    }

    @When("^I see Full Screen Page opened$")
    public void ISeeFullScreenPage() throws Exception {
        Assert.assertTrue(getImageFullScreenPage().isImageFullScreenShown());
    }

    @When("I tap on fullscreen page")
    public void ITapFullScreenPage() throws Exception {
        getImageFullScreenPage().tapOnFullScreenPage();
    }

    @When("I see sender first name (.*) on fullscreen page")
    public void ISeeSenderName(String sender) throws Exception {
        String senderFirstName = usrMgr.findUserByNameOrNameAlias(sender)
                .getName().split(" ")[0].toUpperCase();
        Assert.assertTrue(String.format("Sender name '%s' does not exist on the page", senderFirstName),
                getImageFullScreenPage().isSenderNameVisible(senderFirstName));
    }

    @When("I see send date on fullscreen page")
    public void ISeeSendDate() throws Exception {
        final String actualTimestamp = getImageFullScreenPage().getTimeStamp().trim();
        // TODO: Probably, the verification regex could be improved?
        Assert.assertTrue(String.format("The actual timestamp '%s' seems to be incorrect", actualTimestamp),
                actualTimestamp.length() > 0 && actualTimestamp.matches(".*\\b\\d+:\\d+\\b.*"));
    }

    @When("I see download button shown on fullscreen page")
    public void ISeeDownloadButtonOnFullscreenPage() throws Exception {
        Assert.assertTrue(getImageFullScreenPage().isDownloadButtonVisible());
    }

    @When("I tap download button on fullscreen page")
    public void ITapDownloadButtonOnFullscreenPage() throws Exception {
        getImageFullScreenPage().clickDownloadButton();
    }

    @When("I verify image caption and download button are not shown")
    public void IDontSeeImageCaptionAndDownloadButton() throws Exception {
        Assert.assertTrue("Image download button is visible, but should be hidden",
                getImageFullScreenPage().isDownloadButtonInvisible());
        Assert.assertTrue("Image timestamp label is visible, but should be hidden",
                getImageFullScreenPage().isSentTimeInvisible());
    }

    @When("I tap close fullscreen page button")
    public void ITapCloseFullscreenButton() throws Exception {
        getImageFullScreenPage().clickCloseButton();
    }

    /**
     * Presses the sketch button on the image fullscreen page
     *
     * @throws Exception
     * @step. ^I press Sketch button on image fullscreen page$
     */
    @When("^I press Sketch button on image fullscreen page$")
    public void IPressSketchButtonOnImageFullscreenPage() throws Exception {
        getImageFullScreenPage().clickSketchButton();
    }

}
