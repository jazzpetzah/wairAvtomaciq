package com.wearezeta.auto.android_tablet.steps;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import com.wearezeta.auto.common.misc.ElementState;
import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationsListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletConversationsListPage getConversationsListPage() throws Exception {
        return pagesCollection.getPage(TabletConversationsListPage.class);
    }

    /**
     * Wait until conversations list is fully loaded and successful load
     *
     * @throws Exception
     * @step. ^I see (?:the |\\s*)[Cc]onversations list$
     */
    @Given("^I see (?:the |\\s*)[Cc]onversations list$")
    public void ISeeConversationsList() throws Exception {
        getConversationsListPage().verifyConversationsListIsLoaded();
    }

    /**
     * Wait until conversations list is fully loaded and there are (no)
     * conversations
     *
     * @param shouldBeNoConversations is set to null if "no" part does not exist in the step
     * @throws Exception
     * @step. ^I see (?:the |\\s*)[Cc]onversations list with (no
     * )?conversations?$
     */
    @Given("^I see (?:the |\\s*)[Cc]onversations list with (no )?conversations?$")
    public void ISeeConversationsListPlusItem(String shouldBeNoConversations)
            throws Exception {
        getConversationsListPage().verifyConversationsListIsLoaded();
        if (shouldBeNoConversations == null) {
            Assert.assertTrue(
                    "No conversations are visible in the conversations list, but some are expected",
                    getConversationsListPage().isAnyConversationVisible());
        } else {
            Assert.assertTrue(
                    "Some conversations are visible in the conversations list, but zero is expected",
                    getConversationsListPage().isNoConversationsVisible());
        }
    }

    /**
     * Taps on the button at the bottom right corner of convo list
     *
     * @throws Exception
     * @step. ^I tap conversations list settings button$
     */
    @When("^I tap conversations list settings button$")
    public void ITapSettingsButton() throws Exception {
        getConversationsListPage().tapListSettingsButton();
    }

    /**
     * Tap the corresponding button in convo list to open Search UI
     *
     * @throws Exception
     * @step. ^I open Search UI$
     */
    @When("^I open Search UI$")
    public void IOpenSearchUI() throws Exception {
        getConversationsListPage().tapListActionsAvatar();
    }

    /**
     * Tap the corresponding conversation is the list
     *
     * @param name conversation name
     * @throws Exception
     * @step. ^I tap conversation (.*)"
     */
    @When("^I tap (?:the )conversation (.*)")
    public void ITapConversation(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getConversationsListPage().tapConversation(name);
    }

    /**
     * Verify whether the particular conversation is visible in the
     * conversations list or not
     *
     * @param shouldNotSee equals to null if "do not" part is not present
     * @param name         conversation name
     * @throws Exception
     * @step. ^I (do not)?see (?:the |\\s*)conversation (.*) in my conversations
     * list$
     */
    @Then("^I (do not )?see (?:the |\\s*)conversation (.*) in my conversations list$")
    public void ISeeOrNotTheConversation(String shouldNotSee, String name)
            throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format(
                    "There is no '%s' conversation in the conversations list",
                    name), getConversationsListPage()
                    .waitUntilConversationIsVisible(name));
        } else {
            Assert.assertTrue(
                    String.format(
                            "The conversation '%s' should not exist in the conversations list",
                            name), getConversationsListPage()
                            .waitUntilConversationIsInvisible(name));
        }
    }

    /**
     * Verify whether the particular conversation name is silenced or not
     *
     * @param name                conversation name/alias
     * @param shouldNotBeSilenced equals to null if the "not" part does not exist in the step
     * @throws Exception
     * @step. ^I see (?:the) conversation (.*) in my conversations list is (not
     * )?silenced$
     */
    @Then("^I see (?:the) conversation (.*) in my conversations list is (not )?silenced$")
    public void ISeeConversationSilenced(String name, String shouldNotBeSilenced)
            throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotBeSilenced == null) {
            Assert.assertTrue(
                    String.format("The conversation '%s' is not silenced", name),
                    getConversationsListPage().waitUntilConversationIsSilenced(
                            name));
        } else {
            Assert.assertTrue(String.format(
                    "The conversation '%s' should not be silenced", name),
                    getConversationsListPage()
                            .waitUntilConversationIsNotSilenced(name));
        }
    }

    /**
     * Verify whether missed call indicator is shown near the corresponding
     * convo list item
     *
     * @param shouldNotSee equals to null if "do not " part does not exist in the step
     * @param convoName    conversation name/alias
     * @throws Exception
     * @step. ^I (do not )?see missed call notification near (.*) conversation
     * list item$
     */
    @Then("^I (do not )?see missed call notification near (.*) conversations list item$")
    public void ISeeMissedCallNotification(String shouldNotSee, String convoName)
            throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format(
                            "Missed call notification is not visible for conversations list item '%s'",
                            convoName), getConversationsListPage()
                            .waitUntilMissedCallNotificationVisible(convoName));
        } else {
            Assert.assertTrue(
                    String.format(
                            "Missed call notification is still visible for conversations list item '%s'",
                            convoName),
                    getConversationsListPage()
                            .waitUntilMissedCallNotificationInvisible(convoName));
        }
    }

    /**
     * Perform long/short swipe on conversations list
     *
     * @param swipeTypeShortLong either short or long
     * @throws Exception
     * @step. ^I do (long|short) swipe up on conversations list$
     */
    @When("^I do (long|short) swipe up on conversations list$")
    public void IDoSwipe(String swipeTypeShortLong) throws Exception {
        switch (swipeTypeShortLong) {
            case "long":
                getConversationsListPage().doLongSwipeUp();
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Swipe type '%s' is not supported", swipeTypeShortLong));
        }
    }

    /**
     * Swipe left to show the conversation view
     *
     * @throws Exception
     * @step. ^I swipe left to show (?:the |\\s*)conversation view$
     */
    @When("^I swipe left to show (?:the |\\s*)conversation view$")
    public void ISwipeLeft() throws Exception {
        getConversationsListPage().doSwipeLeft();
    }

    /**
     * It is mandatory to locate PlayPause button by coordinates because
     * Selendroid constantly throws InvalidElementStateException if we try to
     * get it with findElement(s). And this happens only in tablets
     */
    private Map<String, Rectangle> playPauseButtonCoords = new HashMap<>();

    /**
     * Verifies whether Play/Pause button is visible next to the corrresponding
     * conversation name item.
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I see (?:Play|Pause) button next to the conversation name (.*)
     */
    @When("^I see (?:Play|Pause) button next to the conversation name (.*)")
    public void ISeePlayPauseButton(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(
                String.format(
                        "Play/Pause button is not visible next to conversation item '%s'",
                        convoName), getConversationsListPage()
                        .waitUntilPlayPauseButtonVisibleNextTo(convoName));
    }

    /**
     * Do swipe on a conversations list item to show actions overlay
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I swipe right (?:the |\\s*)conversations list item (.*)
     */
    @When("^I swipe right (?:the |\\s*)conversations list item (.*)")
    public void ISwipeRightListItem(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getConversationsListPage().swipeRightListItem(name);
    }

    private Map<String, ElementState> playPauseBtnStates = new HashMap<>();

    /**
     * Save the current state of PlayPause button into the internal data
     * structure
     *
     * @param convoName conversation name for which the screenshot should be taken
     * @throws Exception
     * @step. ^I remember the state of (?:Play|Pause) button next to the
     * conversation name (.*)
     */
    @When("^I remember the state of (?:Play|Pause) button next to the conversation name (.*)")
    public void IRememberTheStateOfPlayPauseButton(String convoName) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        playPauseBtnStates.put(name, new ElementState(
                () -> getConversationsListPage().getScreenshotOfPlayPauseButton(
                        playPauseButtonCoords.get(name)).orElseThrow(IllegalStateException::new)).remember()
        );
    }

    private final static double MAX_SIMILARITY_THRESHOLD = 0.6;
    private final static int STATE_CHANGE_TIMEOUT_SECONDS = 5;

    /**
     * Verify whether the current screenshot of PlayPause button is different
     * from the previous one
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I see the state of (?:Play|Pause) button next to the conversation
     * name (.*) is changed$
     */
    @Then("^I see the state of (?:Play|Pause) button next to the conversation name (.*) is changed$")
    public void ISeeThePlayPauseButtonStateIsChanged(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        if (!playPauseBtnStates.containsKey(convoName)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of previous button state for '%s' conversation first", convoName));
        }
        Assert.assertTrue(String.format(
                "The current and previous states of PlayPause button for '%s' conversation seems " +
                        "to be very similar after %d seconds", convoName, STATE_CHANGE_TIMEOUT_SECONDS),
                playPauseBtnStates.get(convoName).isChanged(STATE_CHANGE_TIMEOUT_SECONDS,
                        MAX_SIMILARITY_THRESHOLD));
    }

    /**
     * Tap PlayPause button next to the particular conversation name
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I tap (?:Play|Pause) button next to the conversation name (.*)
     */
    @When("^I tap (?:Play|Pause) button next to the conversation name (.*)")
    public void ITapPlayPauseButton(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        getConversationsListPage().tapPlayPauseMediaButton(playPauseButtonCoords.get(convoName));
    }

    /**
     * Stores the coordinates of the corresponding conversation item into the
     * internal structure. This step is mandatory to call before any steps,
     * which use Play/Pause button
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I remember the coordinates of conversation item (.*)
     */
    @When("^I remember the coordinates of conversation item (.*)")
    public void IRememberConvoItemCoords(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        playPauseButtonCoords.put(convoName, getConversationsListPage().calcPlayPauseButtonCoordinates(convoName));
    }
}
