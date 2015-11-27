package com.wearezeta.auto.android_tablet.steps;

import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.android_tablet.pages.TabletPeoplePickerPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletPeoplePickerPage getPeoplePickerPage() throws Exception {
        return pagesCollection.getPage(TabletPeoplePickerPage.class);
    }

    private TabletConversationsListPage getConversationsListPage()
            throws Exception {
        return pagesCollection.getPage(TabletConversationsListPage.class);
    }

    /**
     * Verify that People Picker is visible or not
     *
     * @param shouldNotBeVisible equals to null is "do not" part does not exist
     * @throws Exception
     * @step. ^I (do not )?see People Picker page$
     */
    @When("^I (do not )?see People Picker page$")
    public void WhenITapOnTabletCreateConversation(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("People Picker page is not visible",
                    getPeoplePickerPage().waitUntilVisible());
        } else {
            Assert.assertTrue(
                    "People Picker page is visible, but should be hidden",
                    getPeoplePickerPage().waitUntilInvisible());
        }
    }

    /**
     * Enter user name or email into the corresponding People Picker field
     *
     * @param searchCriteria user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I enter \"(.*)\" into Search input on People [Pp]icker page$
     */
    @When("^I enter \"(.*)\" into Search input on People [Pp]icker page")
    public void IEnterStringIntoSearchField(String searchCriteria)
            throws Exception {
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.EMAIL_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.NAME_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
    }

    /**
     * Tap one of found items in People Picker results
     *
     * @param item user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I tap the found item (.*) on [Pp]eople [Pp]icker page$
     */
    @When("^I tap the found item (.*) on [Pp]eople [Pp]icker page$")
    public void ITapUserName(String item) throws Exception {
        item = usrMgr.replaceAliasesOccurences(item, FindBy.EMAIL_ALIAS);
        item = usrMgr.replaceAliasesOccurences(item, FindBy.NAME_ALIAS);
        item = usrMgr.replaceAliasesOccurences(item, FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().tapFoundItem(item);
    }

    private BufferedImage rememberedAvatar = null;
    final static double MAX_SIMILARITY_VALUE = 0.90;

    /**
     * Check whether the particular user avatar is visible
     *
     * @param shouldNotSee equals to null if "do not " part does not exist in the step
     *                     description
     * @param name    user name/alias
     * @param isGroup is not null if group avatar should be verified instead of single user avatar
     * @throws Exception
     * @step. ^I see "(.*)" (group )?avatar on [Pp]eople [Pp]icker page$
     */
    @When("^I (do not )?see \"(.*)\" (group )?avatar on [Pp]eople [Pp]icker page$")
    public void ISeeContactAvatar(String shouldNotSee,String name, String isGroup) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            if (isGroup == null) {
                Assert.assertTrue(String.format(
                        "The avatar for contact '%s' is not visible", name),
                        getPeoplePickerPage().waitUntilAvatarIsVisible(name));
            } else {
                Assert.assertTrue(String.format(
                        "The group avatar '%s' is not visible", name),
                        getPeoplePickerPage().waitUntilGroupAvatarIsVisible(name));
            }
        } else {
            if (isGroup == null) {
                Assert.assertTrue(String.format(
                        "The avatar for contact '%s' is visible", name),
                        getPeoplePickerPage().waitUntilAvatarIsInvisible(name));
            } else {
                Assert.assertTrue(String.format(
                        "The group avatar '%s' is visible", name),
                        getPeoplePickerPage().waitUntilGroupAvatarIsInvisible(name));
            }

        }
    }

    /**
     * Save the screenshot of current user avatar on People Picker page
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I remember (.*) avatar on [Pp]eople [Pp]icker page$
     */
    @When("^I remember (.*) avatar on [Pp]eople [Pp]icker page$")
    public void ITakeScreenshotOfContactAvatar(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        this.rememberedAvatar = getPeoplePickerPage()
                .takeAvatarScreenshot(name).orElseThrow(
                        IllegalStateException::new);
    }

    /**
     * Compare the screenshot of current user avatar on People Picker page with
     * the previous one
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I verify (.*) avatar on [Pp]eople [Pp]icker page is not the same
     * as the previous one$
     */
    @Then("^I verify (.*) avatar on [Pp]eople [Pp]icker page is not the same as the previous one$")
    public void IVerifyAvatarIsNotTheSame(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        if (this.rememberedAvatar == null) {
            throw new IllegalStateException(
                    "Please take a previous screenshot of user avatar first");
        }
        final BufferedImage currentAvatar = getPeoplePickerPage()
                .takeAvatarScreenshot(name).orElseThrow(
                        IllegalStateException::new);
        final double score = ImageUtil.getOverlapScore(currentAvatar,
                this.rememberedAvatar,
                ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
        Assert.assertTrue(
                String.format(
                        "The current contact avatar of '%s' is very similar to the previous one (%.2f <-> %.2f)",
                        name, score, MAX_SIMILARITY_VALUE),
                score < MAX_SIMILARITY_VALUE);
    }

    /**
     * Click the X button to close People Picker
     *
     * @throws Exception
     * @step. ^I close (?:the |\\s*)People Picker$
     */
    @And("^I close (?:the |\\s*)People Picker$")
    public void IClosePeoplePicker() throws Exception {
        getPeoplePickerPage().tapCloseButton();
    }

    private static final int TOP_PEOPLE_MAX_RETRIES = 5;

    /**
     * Try to reopen People Picker several times until Top People list is
     * visible
     *
     * @throws Exception
     * @step. ^I keep on reopening People Picker until I see Top People$
     */
    @And("^I keep on reopening People Picker until I see Top People$")
    public void IReopenPeoplePickerUntilTopPeopleAppears() throws Exception {
        int ntry = 1;
        while (!getPeoplePickerPage().waitUntilTopPeopleIsVisible()
                && ntry <= TOP_PEOPLE_MAX_RETRIES) {
            getPeoplePickerPage().tapCloseButton();
            Thread.sleep(3000);
            getConversationsListPage().tapSearchInput();
            ntry++;
        }
        if (ntry >= TOP_PEOPLE_MAX_RETRIES) {
            throw new AssertionError(String.format(
                    "Top People list was not shown after %s retries", ntry));
        }
    }

    /**
     * Tap the particular Top People avatar
     *
     * @param name name/alias
     * @throws Exception
     * @step. ^I tap (.*) avatar in Top People$
     */
    @When("^I tap (.*) avatar in Top People$")
    public void ITapAvatarInTopPeople(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getPeoplePickerPage().tapTopPeopleAvatar(name);
    }

    private String firstPYMKItemName = null;

    private String getFirstPYMKItemNameOrThrowError() {
        if (this.firstPYMKItemName == null) {
            throw new IllegalStateException(
                    "Please call the corresponding step to remember the PYMK item name first");
        } else {
            return this.firstPYMKItemName;
        }
    }

    /**
     * Stores the name of the first PYMK item into the internal data structure
     *
     * @throws Exception
     * @step. ^I remember the name of the first PYMK item on [Pp]eople [Pp]icker
     * page$
     */
    @When("^I remember the name of the first PYMK item on [Pp]eople [Pp]icker page$")
    public void IRememberTheFirstPYMKItem() throws Exception {
        firstPYMKItemName = getPeoplePickerPage().getFirstPYMKItemName();
    }

    /**
     * Tap the + button next to the frist PYMK item
     *
     * @throws Exception
     * @step. ^I tap \\+ button on the first PYMK item on [Pp]eople [Pp]icker
     * page$
     */
    @When("^I tap \\+ button on the first PYMK item on [Pp]eople [Pp]icker page$")
    public void ITapPlusButtonOnFirstPYMKItem() throws Exception {
        getPeoplePickerPage().tapPlusButtonOnFirstPYMKItem();
    }

    /**
     * Verify whether the previously remembered PYMK item is not visible in the
     * PYMK list
     *
     * @throws Exception
     * @step. ^I do not see the previously remembered PYMK item on [Pp]eople
     * [Pp]icker page$
     */
    @Then("^I do not see the previously remembered PYMK item on [Pp]eople [Pp]icker page$")
    public void IDoNotSeeRememberedPYMKItem() throws Exception {
        Assert.assertTrue(
                String.format(
                        "The previously remembered PYMK item '%s' is still visible in PYMK list",
                        firstPYMKItemName),
                getPeoplePickerPage().waitUntilPYMKItemInvisible(
                        this.getFirstPYMKItemNameOrThrowError()));
    }

    /**
     * Verify whether the previously remembered PYMK item exists in convo list
     *
     * @param shouldNotSee equals to null if "do not " part does not exist in the step
     *                     description
     * @throws Exception
     * @step. ^I (do not )?see conversations list with the previously remembered
     * PYMK item$
     */
    @Then("^I (do not )?see conversations list with the previously remembered PYMK item$")
    public void ISeeThePreviouslyRememberedPYMKItemInConvoList(
            String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format(
                            "The previously remembered PYMK item '%s' does not exist in the conversations list",
                            this.getFirstPYMKItemNameOrThrowError()),
                    getConversationsListPage().waitUntilConversationIsVisible(
                            this.getFirstPYMKItemNameOrThrowError()));
        } else {
            Assert.assertTrue(
                    String.format(
                            "The previously remembered PYMK item '%s' exist in the conversations list, but it should not",
                            this.getFirstPYMKItemNameOrThrowError()),
                    getConversationsListPage()
                            .waitUntilConversationIsInvisible(
                                    this.getFirstPYMKItemNameOrThrowError()));
        }
    }

    /**
     * Switch to the conversation. whose name is the same as the previously
     * remembered one. Conversations list should be already visible
     *
     * @throws Exception
     * @step. ^I switch to the conversation with the previously remembered PYMK
     * item$
     */
    @Then("^I switch to the conversation with the previously remembered PYMK item$")
    public void ISwitchToPreviouslyRememberedConvoName() throws Exception {
        getConversationsListPage().tapConversation(
                this.getFirstPYMKItemNameOrThrowError());
    }

    /**
     * Tap the first item in PYMK list
     *
     * @throws Exception
     * @step. ^I tap the first PYMK item on [Pp]eople [Pp]icker page$
     */
    @When("^I tap the first PYMK item on [Pp]eople [Pp]icker page$")
    public void ITapFirstPYMKItem() throws Exception {
        getPeoplePickerPage().tapFirstPYMKItem();
    }

    private static enum SwipeType {
        LONG, SHORT;
    }

    /**
     * Does short/long swipe right on the first PYMK item
     *
     * @param swipeType see SwipeType enum for more details about possible values
     * @throws Exception
     * @step. ^I do (short|long) swipe right the first PYMK item on [Pp]eople
     * [Pp]icker page$
     */
    @When("^I do (short|long) swipe right the first PYMK item on [Pp]eople [Pp]icker page$")
    public void IDoSwipeOnFirstPYMKItem(String swipeType) throws Exception {
        final SwipeType swipeEnumType = SwipeType.valueOf(swipeType
                .toUpperCase());
        switch (swipeEnumType) {
            case LONG:
                getPeoplePickerPage().longSwipeRightFirstPYMKItem();
                break;
            case SHORT:
                getPeoplePickerPage().shortSwipeRightFirstPYMKItem();
                break;
            default:
                throw new NoSuchElementException(String.format(
                        "Swipe type '%s' is not supported", swipeEnumType.name()));
        }
    }

    /**
     * Tap Hide button in the first PYMK item. The button should be already
     * visible
     *
     * @throws Exception
     * @step. ^I tap Hide button in the first PYMK item on [Pp]eople [Pp]icker
     * page$
     */
    @When("^I tap Hide button in the first PYMK item on [Pp]eople [Pp]icker page$")
    public void ITapHideButtonInFirstPYMKItem() throws Exception {
        getPeoplePickerPage().tapHideButtonInFirstPYMKItem();
    }

    /**
     * Perform long/short swipe down on People Picker page
     *
     * @param swipeTypeStr see SwipeType enum for the list of available values
     * @throws Exception
     * @step. ^I do (long|short) swipe down on [Pp]eople [Pp]icker page$
     */
    @When("^I do (long|short) swipe down on [Pp]eople [Pp]icker page$")
    public void IDoSwipeDown(String swipeTypeStr) throws Exception {
        final SwipeType swipeType = SwipeType.valueOf(swipeTypeStr
                .toUpperCase());
        switch (swipeType) {
            case SHORT:
                getPeoplePickerPage().doShortSwipeDown();
                break;
            case LONG:
                getPeoplePickerPage().doLongSwipeDown();
                break;
            default:
                throw new IllegalStateException(String.format(
                        "Swipe type '%s' is not supported", swipeTypeStr));
        }
    }

    /**
     * Tap the Create/Open Conversation button
     *
     * @throws Exception
     * @step. ^I tap (?:Open|Create) Conversation button on [Pp]eople [Pp]icker
     * page$
     */
    @When("^I tap (?:Open|Create) Conversation button on [Pp]eople [Pp]icker page$")
    public void ITapConversationActionButton() throws Exception {
        getPeoplePickerPage().tapOpenOrCreateConversationButton();
    }

    /**
     * Verify whether Open Conversation button is visible
     *
     * @param shouldBeVisible equals to null if the button should be visible
     * @param expectedCaption either 'Open' or 'Create'
     * @throws Exception
     * @step. ^I (do not )?see (?:the |\\s*)(Open|Create) Conversation button on
     * [Pp]eople [Pp]icker page$"
     */
    @Then("^I (do not )?see (?:the |\\s*)(Open|Create) Conversation button on [Pp]eople [Pp]icker page$")
    public void ISeeOpenConversationButton(String shouldBeVisible,
                                           String expectedCaption) throws Exception {
        if (shouldBeVisible == null) {
            Assert.assertTrue(
                    String.format("%s Conversation button is not visible",
                            expectedCaption),
                    getPeoplePickerPage()
                            .waitUntilOpenOrCreateConversationButtonIsVisible(
                                    expectedCaption));
        } else {
            Assert.assertTrue(
                    String.format(
                            "%s Conversation button is still visible, but should be hidden",
                            expectedCaption), getPeoplePickerPage()
                            .waitUntilOpenConversationButtonIsInvisible());
        }
    }

    /**
     * Tap the Camera button on quick access menu
     *
     * @throws Exception
     * @step. ^I tap Camera button on [Pp]eople [Pp]icker page$
     */
    @Then("^I tap Camera button on [Pp]eople [Pp]icker page$")
    public void ITapSendPhotoButton() throws Exception {
        getPeoplePickerPage().tapCameraButton();
    }

    /**
     * Tap the Call button in quick menu pane
     *
     * @throws Exception
     * @step. ^I tap Call button on [Pp]eople [Pp]icker page$
     */
    @When("^I tap Call button on [Pp]eople [Pp]icker page$")
    public void ITapCallButton() throws Exception {
        getPeoplePickerPage().tapCallButton();
    }

}
