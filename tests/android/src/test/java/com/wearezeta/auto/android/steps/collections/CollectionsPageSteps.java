package com.wearezeta.auto.android.steps.collections;


import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.collections.CollectionsPage;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class CollectionsPageSteps {

    private CollectionsPage getCollectionsPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(CollectionsPage.class);
    }

    /**
     * Tap on all buttons on Collection page
     *
     * @param buttonName the name of button
     * @throws Exception
     * @step. I tap on (Close|Back) button on Collection page$
     */
    @When("^I tap on (Close|Back) button on Collection page$")
    public void ITapButton(String buttonName) throws Exception {
        getCollectionsPage().tapOnButton(buttonName);
    }

    /**
     * Tap on the different type of item
     *
     * @param number   the order of picture, such as 1st ,2nd, 3rd, 4th...
     * @param category which couldbe picture, link preview or file sharing
     * @throws Exception
     * @step. ^I (tap|long tap) on the (\d+)(?:st|nd|rd|th) (picture|link preview|file sharing) on Collections page$
     */
    @When("^I (tap|long tap) on the (\\d+)(?:st|nd|rd|th) (picture|link preview|file sharing) on Collections page$")
    public void ITapOnXthItem(String tapType, int number, String category) throws Exception {
        getCollectionsPage().tapOnItemByNumber(tapType, number, category);
    }

    /**
     * Tap show all label for different category
     *
     * @param category the name of category
     * @throws Exception
     * @step. ^I tap Show All label next to category (picture|link preview|file sharing) on Collection page$
     */
    @When("^I tap Show All label next to category (picture|link preview|file sharing) on Collection page$")
    public void ITapShowAll(String category) throws Exception {
        getCollectionsPage().tapShowAll(category);
    }

    /**
     * Verify the count of imtes
     *
     * @param expectCount    the expect count
     * @param category       the category name
     * @param timeoutSeconds the timeout in seconds
     * @throws Exception
     * @step. ^I see (\d+) (pictures?|link previews?|file sharings?) in (\d+) seconds on Collection page$
     */
    @Then("^I see (\\d+) (pictures?|link previews?|file sharings?) in (\\d+) seconds on Collection page$")
    public void ISeeTheCountOfItems(int expectCount, String category, int timeoutSeconds) throws Exception {
        category = category.endsWith("s") ? category.substring(0, category.length() - 1) : category;
        Assert.assertTrue(String.format("Cannot find %d %s", expectCount, category),
                getCollectionsPage().waitUntilCountOfItemsByCategory(category, expectCount, Timedelta.fromSeconds(timeoutSeconds)));
    }

    /**
     * Verify I see the collection category
     *
     * @param shouldNotSee equals null means the category should be visible
     * @param categoryName the name of category
     * @throws Exception
     * @step. ^I( do not)? see (PICTURES category|LINKS category|FILES category|NO ITEM placeholder) on Collections page$
     */
    @Then("^I( do not)? see (PICTURES category|LINKS category|FILES category|NO ITEM placeholder) on Collections page$")
    public void ISeeCollectionCategory(String shouldNotSee, String categoryName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The '%s' is invisible", categoryName),
                    getCollectionsPage().waitUntilItemIsVisible(categoryName));
        } else {
            Assert.assertTrue(String.format("The '%s' is still visible", categoryName),
                    getCollectionsPage().waitUntilItemIsInvisible(categoryName));
        }
    }

    /**
     * Check item is visible
     *
     * @param type    item type which could be timestamp, sender name
     * @param hasText equals null means no text
     * @param text    the exact text
     * @throws Exception
     * @step. I see (sender name)( "(.*)")? on Collection page$
     */
    @Then("^I see (sender name)( \"(.*)\")? on Collection page$")
    public void ISeeUserInfo(String type, String hasText, String text) throws Exception {
        if (hasText != null) {
            text = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
        }
        org.jcodec.common.Assert.assertTrue(
                String.format("The item '%s' with text '%s' is invisible on Collection image preview", type, text),
                getCollectionsPage().waitUntilTopToolbarItemVisible(type, text));
    }

}
