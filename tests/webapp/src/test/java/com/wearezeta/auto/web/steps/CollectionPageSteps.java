package com.wearezeta.auto.web.steps;

import java.awt.image.BufferedImage;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppTestContext;

import com.wearezeta.auto.web.pages.CollectionDetailsPage;

import com.wearezeta.auto.web.common.WebCommonUtils;

import com.wearezeta.auto.web.pages.CollectionPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;

public class CollectionPageSteps {

    private final WebAppTestContext context;

    public CollectionPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    private static final int MAXPICTURES = 12;
    private static final int MAXVIDEOS = 4;
    private static final int MAXFILES = 4;
    private static final int MAXLINKS = 4;

    @When("I close collection overview$")
    public void ICloseCollectionOverview() throws Exception {
        context.getPagesCollection().getPage(CollectionPage.class).clickClose();
    }

    @Then("^I see info about no collection items$")
    public void ISeeNoItems() throws Exception {
        assertThat("Wrong 'no items' message", context.getPagesCollection().getPage(CollectionPage.class).getNoItemsPlaceholder().toLowerCase(),
                equalTo("no items"));
    }

    @Then("^I see (\\d+) pictures? in collection$")
    public void ISeeXPictures(int amount) throws Exception {
        if (amount <= MAXPICTURES && amount >= 0) {
            assertThat("Number of shown pictures", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfPictures(),
                    equalTo(amount));
        }
        // Label is not shown for less than 13 pictures
        else if (amount > MAXPICTURES) {
            assertThat("Number of shown pictures", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfPictures(),
                    equalTo(MAXPICTURES));
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfPictureCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) videos? in collection$")
    public void ISeeXVideos(int amount) throws Exception {
        if (amount <= MAXVIDEOS && amount >= 0) {
            assertThat("Number of shown videos", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfVideos(),
                    equalTo(amount));
        }
        // Label is not shown for less than 5 videos
        else if (amount > MAXVIDEOS) {
            assertThat("Number of shown videos", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfVideos(),
                    equalTo(MAXVIDEOS));
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfVideoCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) files? in collection$")
    public void ISeeXFiles(int amount) throws Exception {
        if (amount <= MAXFILES && amount >= 0) {
            assertThat("Number of files", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfFiles(),
                    equalTo(amount));
        }
        // Label is not shown for less than 5 files
        else if (amount > MAXFILES) {
            assertThat("Number of files", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfFiles(),
                    equalTo(MAXFILES));
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfFileCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) audios? in collection$")
    public void ISeeXAudios(int amount) throws Exception {
        if (amount <= MAXFILES && amount >= 0) {
            assertThat("Number of audios", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfAudioFiles(),
                    equalTo(amount));
        }
        // Label is not shown for less than 5 audios
        else if (amount > MAXFILES) {
            assertThat("Number of audios", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfAudioFiles(),
                    equalTo(MAXFILES));
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfAudioCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) links? in collection$")
    public void ISeeXLinks(int amount) throws Exception {
        if (amount <= MAXLINKS && amount >= 0) {
            assertThat("Number of links", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfLinks(),
                    equalTo(amount));
        }
        // Label is not shown for less than 5 links
        else if (amount > MAXLINKS) {
            assertThat("Number of links", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfLinks(),
                    equalTo(MAXLINKS));
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfLinkCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see link with link (.*) in collection overview$")
    public void ISeeLinkWithUrl(String url) throws Exception {
        assertThat("Missing Url in preview overview",
                context.getPagesCollection().getPage(CollectionPage.class).getLinkPreviewUrls(),
                hasItem(url));
    }

    @Then("^I see link with title (.*) in collection overview$")
    public void ISeeLinkWithTitle(String title) throws Exception {
        assertThat("Missing title in preview overview",
                context.getPagesCollection().getPage(CollectionPage.class).getLinkPreviewTitles(),
                hasItem(title));
    }

    @Then("^I see link with image (.*) in collection overview$")
    public void ISeeLinkWithImage(String filename) throws Exception {
        final String picturePath = WebCommonUtils.getFullPicturePath(filename);
        BufferedImage originalImage = ImageUtil.readImageFromFile(picturePath);
        BufferedImage linkPreviewImage = context.getPagesCollection().getPage(CollectionPage.class).getLinkPreviewImage();

        // Image matching with SIFT does not work very well on really small images
        // because it defines the maximum number of matching keys
        // so we scale them to double size to get enough matching keys
        final int scaleMultiplicator = 2;
        originalImage = ImageUtil.resizeImage(originalImage, scaleMultiplicator);
        linkPreviewImage = ImageUtil.resizeImage(linkPreviewImage, scaleMultiplicator);

        assertThat("Not enough good matches", ImageUtil.getMatches(originalImage, linkPreviewImage), greaterThan(80));
    }

    @Then("^I see link from contact (.*) in collection overview$")
    public void ISeeLinkFromContact(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
        assertThat("contact in preview overview", context.getPagesCollection().getPage(CollectionPage.class).getLinkPreviewFroms(),
                hasItem(contact));
    }

    @When("I click on picture (\\d+) in collection$")
    public void IClickOnFirstPictureInCollection(int index) throws Exception {
        context.getPagesCollection().getPage(CollectionPage.class).clickFirstPictureInCollection(index);
    }

    @When("I click on Show all pictures button in collections$")
    public void IClickOnAllPicturesButtonInCollections() throws Exception{
        context.getPagesCollection().getPage(CollectionPage.class).clickShowAllPicturesInCollection();
    }

    @Then("^I see (\\d+) pictures? in pictures details in collections$")
    public void ISeeXPicturesInDetails(int amount) throws Exception {
        assertThat("Number of shown pictures", context.getPagesCollection().getPage(CollectionDetailsPage.class).getNumberOfPictures(),
                    equalTo(amount));
    }

    @When("^I click on back button on collection details page$")
    public void IClickOnBackButtonOnCollectionDetails() throws Exception{
        context.getPagesCollection().getPage(CollectionDetailsPage.class).clickBackButtonCollectionDetails();
    }

    @When("I click show all files label$")
    public void IClickShowAllLabel() throws Exception {
        context.getPagesCollection().getPage(CollectionPage.class).clickShowAllFilesLabel();
    }

    @Then("I see (\\d+) files in files detail page$")
    public void ISeeAllFilesDetailPage(int amount) throws Exception {
        assertThat("Number of files", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfFilesInFileDetailPage(),
                equalTo(amount));
    }
}
