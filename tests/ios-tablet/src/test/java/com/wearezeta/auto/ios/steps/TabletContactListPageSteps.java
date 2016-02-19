package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.TabletContactListPage;

import cucumber.api.java.en.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TabletContactListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletContactListPage getTabletContactListPage() throws Exception {
        return pagesCollection.getPage(TabletContactListPage.class);
    }

    private Map<String, BufferedImage> savedConvoItemScreenshots = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param nameAlias conversation name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) conversation item on iPad$
     */
    @When("^I remember the state of (.*) conversation item on iPad$")
    public void IRememberConvoItemState(String nameAlias) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        this.savedConvoItemScreenshots.put(name,
                getTabletContactListPage().getConversationEntryScreenshot(name));
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param nameAlias          conversation name/alias
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of (.*) conversation item is (not )?changed on iPad$"
     */
    @Then("^I see the state of (.*) conversation item is (not )?changed on iPad$")
    public void IVerifyConvoState(String nameAlias, String shouldNotBeChanged) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        if (!this.savedConvoItemScreenshots.containsKey(name)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of '%s' conversation entry first", name));
        }
        final BufferedImage actualConvoItemScreenshot =
                getTabletContactListPage().getConversationEntryScreenshot(name);
        final double score = ImageUtil.getOverlapScore(this.savedConvoItemScreenshots.get(name),
                actualConvoItemScreenshot, ImageUtil.RESIZE_NORESIZE);
        final double minScore = 0.999;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be the same (%.3f >= %.3f)",
                            name, score, minScore), score < minScore);
        } else {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be changed (%.3f < %.3f)",
                            name, score, minScore), score >= minScore);
        }
    }


    /**
     * Swipes down on Contact list on iPad
     *
     * @throws Exception
     * @step ^I swipe down contact list on iPad$
     */
    @When("^I swipe down contact list on iPad$")
    public void ISwipeDownContactListOniPad() throws Exception {
        getTabletContactListPage().swipeDown(500);
    }

    /**
     * Opens archived conversations on iPad
     *
     * @throws Exception
     * @step ^I open archived conversations on iPad$
     */
    @When("^I open archived conversations on iPad$")
    public void IOpenArchivedConvOnIpad() throws Exception {
        getTabletContactListPage().swipeUp(1000);
    }

    /**
     * Verifies that mute a call button in landscape in conv list is not shown
     *
     * @throws Throwable
     * @step. ^I dont see mute call button in conversation list on iPad$
     */
    @Then("^I dont see mute call button in conversation list on iPad$")
    public void IDontSeeMuteCallButtonInConversationLisOniPad()
            throws Throwable {
        Assert.assertFalse("Mute call button is still visible",
                getTabletContactListPage().isMuteCallButtonVisible());
    }

}
