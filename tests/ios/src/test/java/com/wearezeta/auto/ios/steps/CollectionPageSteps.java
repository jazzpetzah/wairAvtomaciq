package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CollectionPage;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class CollectionPageSteps {
    private CollectionPage getCollectionPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(CollectionPage.class);
    }

    /**
     * Verifies that the specific collection category is shown on the main collection overview
     *
     * @param shouldNotBeVisible equals to null if the category should be visible
     * @param categoryName       name of the collectin category
     * @throws Exception
     * @step. ^I (do not )?see collection category (PICTURES|LINKS|FILES)$
     */
    @Then("^I (do not )?see collection category (PICTURES|VIDEOS|LINKS|FILES)$")
    public void ISeeCollectionCategory(String shouldNotBeVisible, String categoryName) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("Collection category %s is not visible", categoryName),
                    getCollectionPage().isCollectionCategoryVisible(categoryName));
        } else {
            Assert.assertTrue(String.format("Collection category %s is visible, but should not be there",categoryName),
                    getCollectionPage().isCollectioncategoryInvisible(categoryName));
        }

    }
}
