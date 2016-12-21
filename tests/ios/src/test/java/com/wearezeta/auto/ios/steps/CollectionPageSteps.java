package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CollectionPage;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class CollectionPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CollectionPage getCollectionPage() throws Exception {
        return pagesCollection.getPage(CollectionPage.class);
    }

    @Then("^I (do not )?see collection category (PICTURES|LINKS|FILES)$")
    public void ISeeCollectionCategory(String shouldNotBeVisible, String categoryName) throws Throwable {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Collection category is not visible",
                    getCollectionPage().isCollectionCategoryVisible(categoryName));
        } else {
            Assert.assertTrue("Collection category is visible, but should not be there",
                    getCollectionPage().isCollectioncategoryInvisible(categoryName));
        }

    }
}
