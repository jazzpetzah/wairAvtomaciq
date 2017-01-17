package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CollectionPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
            Assert.assertTrue(String.format("Collection category %s is visible, but should not be there", categoryName),
                    getCollectionPage().isCollectioncategoryInvisible(categoryName));
        }
    }

    /**
     * Verify whether "No Items" placeholder is visible in collection
     *
     * @throws Exception
     * @step. ^I see "No Items" placeholder in collection view$
     */
    @Then("^I see \"No Items\" placeholder in collection view$")
    public void ISeeNoItemsPlaceholder() throws Exception {
        Assert.assertTrue("'No Items' placeholder is expected to be visible",
                getCollectionPage().isNoItemsPlaceholderVisible());
    }

    /**
     * Tap the corresponding item in collection by index
     *
     * @param index        item index, starts at 1
     * @param categoryName one of available category names
     * @throws Exception
     * @step. ^I tap the item number (\d+) in collection category (PICTURES|VIDEOS|LINKS|FILES)$
     */
    @When("^I tap the item number (\\d+) in collection category (PICTURES|VIDEOS|LINKS|FILES)$")
    public void ITapItemByIndex(int index, String categoryName) throws Exception {
        getCollectionPage().tapCategoryItemByIndex(categoryName, index);
    }

    /**
     * Verify whether full-screen image preview is shown on the screen
     *
     * @throws Exception
     * @step. ^I see full-screen image preview in collection view$
     */
    @Then("^I see full-screen image preview in collection view$")
    public void ISeeFullScreenImagePreview() throws Exception {
        Assert.assertTrue("Full-screen image preview is expected to be visible",
                getCollectionPage().isFullScreenImagePreviewVisible());
    }

    /**
     * Tap the corresponding button in collection view
     *
     * @param name one of available button names
     * @throws Exception
     * @step. ^I tap (Back|X) button in collection view$
     */
    @And("^I tap (Back|X) button in collection view$")
    public void ITapButton(String name) throws Exception {
        getCollectionPage().tapButton(name);
    }

    /**
     * Swipe left/right on full-screen image preview in collection view
     *
     * @param swipeDirection left or right
     * @throws Exception
     * @step. ^I swipe (left|right) on full-screen image preview in collection view$
     */
    @When("^I swipe (left|right) on full-screen image preview in collection view$")
    public void ISwipeOnImageFullscreenInCollectionView(String swipeDirection) throws Exception {
        getCollectionPage().swipeOnImageFullscreen(swipeDirection);
    }
}
