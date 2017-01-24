package com.wearezeta.auto.android_tablet.steps.collections;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.collections.TabletCollectionImagePreviewPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class CollectionImagePreviewPageSteps {

    private TabletCollectionImagePreviewPage getCollectionImagePreviewPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletCollectionImagePreviewPage.class);
    }

    /**
     * Tap on all buttons on Collection image preview
     *
     * @param buttonName the name of button
     * @throws Exception
     * @step. ^I tap on (Like|Download|Share|Delete|View|Close|Back) button on Collection image preview$
     */
    @When("^I tap on (Like|Download|Share|Delete|View|Close|Back) button on Collection image preview$")
    public void ITapButton(String buttonName) throws Exception {
        getCollectionImagePreviewPage().tapOnButton(buttonName);
    }

    /**
     * Verify the collection image preview is visible
     *
     * @param shouldNotSee equals null means the image preview should be visible
     * @throws Exception
     * @step. ^I( do not)? see collection image preview$
     */
    @Then("^I( do not)? see collection image preview$")
    public void ISeeCollectionImagePreview(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The collection image preview is invisible",
                    getCollectionImagePreviewPage().waitUntilVisible());
        } else {
            Assert.assertTrue("The collection image preview is still visible",
                    getCollectionImagePreviewPage().waitUntilInvisible());
        }
    }

    /**
     * Check item is visible
     *
     * @param type    item type which could be timestamp, sender name
     * @param hasText equals null means no text
     * @param text    the exact text
     * @throws Exception
     * @step. I see (timestamp|sender name)( "(.*)")? on Collection image preview top toolbar$
     */
    @Then("^I see (timestamp|sender name)( \"(.*)\")? on Collection image preview top toolbar$")
    public void ISeeUserInfo(String type, String hasText, String text) throws Exception {
        if (hasText != null) {
            text = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
        }
        Assert.assertTrue(
                String.format("The item '%s' with text '%s' is invisible on Collection image preview", type, text),
                getCollectionImagePreviewPage().waitUntilTopToolbarItemVisible(type, text));
    }
}
