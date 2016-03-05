package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.TabletContactListPage;

import cucumber.api.java.en.*;

import java.util.HashMap;
import java.util.Map;

public class TabletContactListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletContactListPage getTabletContactListPage() throws Exception {
        return pagesCollection.getPage(TabletContactListPage.class);
    }

    private Map<String, ElementState> savedConvoItemStates = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param nameAlias conversation name/alias
     * @param side either 'left' or 'right'
     * @throws Exception
     * @step. ^I remember the (left|right) side state of (.*) conversation item on iPad$
     */
    @When("^I remember the (left|right) side state of (.*) conversation item on iPad$")
    public void IRememberConvoItemState(String side, String nameAlias) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        final TabletContactListPage.EntrySide entrySide = TabletContactListPage.EntrySide.valueOf(side.toUpperCase());
        this.savedConvoItemStates.put(name,
                new ElementState(
                        () -> getTabletContactListPage().getConversationEntryScreenshot(entrySide, name)
                ).remember()
        );
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param nameAlias          conversation name/alias
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the (left|right) state of (.*) conversation item is (not )?changed on iPad$"
     */
    @Then("^I see the state of (.*) conversation item is (not )?changed on iPad$")
    public void IVerifyConvoState(String nameAlias, String shouldNotBeChanged) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        if (!this.savedConvoItemStates.containsKey(name)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of '%s' conversation entry first", name));
        }
        final double minScore = 0.999;
        final int timeoutSeconds = 35;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(String.format("The state of '%s' conversation item seems to be the same", name),
                    this.savedConvoItemStates.get(name).isChanged(timeoutSeconds, minScore));
        } else {
            Assert.assertTrue(String.format("The state of '%s' conversation item seems to be changed", name),
                    this.savedConvoItemStates.get(name).isNotChanged(timeoutSeconds, minScore));
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
     * Verifies that mute a call button in landscape in conv list is not shown
     *
     * @throws Exception
     * @step. ^I dont see mute call button in conversation list on iPad$
     */
    @Then("^I dont see mute call button in conversation list on iPad$")
    public void IDontSeeMuteCallButtonInConversationLisOniPad() throws Exception {
        Assert.assertFalse("Mute call button is still visible",
                getTabletContactListPage().isMuteCallButtonVisible());
    }

}
