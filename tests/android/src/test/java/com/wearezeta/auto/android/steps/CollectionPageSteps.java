package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CollectionPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class CollectionPageSteps extends AbstractAndroidPageSteps {

    private CollectionPage getCollectionPage() throws Exception {
        return pagesCollection.getPage(CollectionPage.class);
    }

    /**
     * checks if main collection title is visible
     *
     * @param shouldNotSee non-null value if title should be visible
     * @throws Exception
     * @step. ^I (do not )?see Collection page
     */
    @Then("^I (do not )?see Collection page")
    public void iSeeCollectionPage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Collection page should be visible ", getCollectionPage().isTitleVisible());
            return;
        }
        Assert.assertTrue("Collection page should be invisible ", getCollectionPage().isTitleInvisible());
    }


    @And("^I (do not )?see \"(.*)\" title on Conversation page$")
    public void iSeeUserNameTitleOnConversationPage(String shouldNotSee, String expectedValue) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("No such library name with: %s", expectedValue), getCollectionPage().isCollectionNameVisible(expectedValue));
            return;
        }
        Assert.assertTrue(String.format("There should not be such library name with: %s", expectedValue), getCollectionPage().isCollectionNameInvisible(expectedValue));
    }

}
