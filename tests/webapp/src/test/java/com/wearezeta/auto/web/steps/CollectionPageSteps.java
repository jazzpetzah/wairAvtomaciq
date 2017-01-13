package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.CollectionPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CollectionPageSteps {

    private final WebAppTestContext context;

    public CollectionPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("^I see info about no collection items$")
    public void ISeeNoItems() throws Exception {
        assertThat("Wrong 'no items' message", context.getPagesCollection().getPage(CollectionPage.class).getNoItemsPlaceholder().toLowerCase(),
                equalTo("no items"));
    }

    @Then("^I see (\\d+) pictures? in collection$")
    public void ISeeXPictures(int amount) throws Exception {
        assertThat("Number of shown pictures", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfPictures(),
                equalTo(amount));
        // Label is not shown for less than 13 pictures
        if (amount > 12) {
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfPictureCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) videos? in collection$")
    public void ISeeXVideos(int amount) throws Exception {
        assertThat("Number of shown videos", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfVideos(),
                equalTo(amount));
        // Label is not shown for less than 5 videos
        if (amount > 4) {
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfVideoCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) files? in collection$")
    public void ISeeXFiles(int amount) throws Exception {
        assertThat("Number of files", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfFiles(),
                equalTo(amount));
        // Label is not shown for less than 5 files
        if (amount > 4) {
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfFileCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @Then("^I see (\\d+) links? in collection$")
    public void ISeeXLinks(int amount) throws Exception {
        assertThat("Number of links", context.getPagesCollection().getPage(CollectionPage.class).getNumberOfLinks(),
                equalTo(amount));
        // Label is not shown for less than 5 links
        if (amount > 4) {
            assertThat("Label to show all", context.getPagesCollection().getPage(CollectionPage.class).getLabelOfLinkCollectionSize(),
                    equalTo("Show all " + String.valueOf(amount)));
        }
    }

    @When("I click on first picture in collection$")
    public void IClickOnFirstPictureInCollection() throws Exception {
        context.getPagesCollection().getPage(CollectionPage.class).clickFirstPictureInCollection();
    }
}
