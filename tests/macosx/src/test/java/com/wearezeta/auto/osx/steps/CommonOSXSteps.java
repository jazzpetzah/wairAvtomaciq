package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

import static com.wearezeta.auto.osx.common.OSXCommonUtils.getSizeOfAppInMB;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;

import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.common.WrapperTestContext;


import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.steps.CommonWebAppSteps;

import static com.wearezeta.auto.web.steps.CommonWebAppSteps.log;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.net.URI;

import static org.hamcrest.Matchers.lessThan;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class CommonOSXSteps {

    public static final Logger LOG = ZetaLogger.getLog(CommonOSXSteps.class.getName());

    private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";

    private final WrapperTestContext context;

    public CommonOSXSteps() {
        this.context = new WrapperTestContext();
    }

    public CommonOSXSteps(WrapperTestContext context) {
        this.context = context;
    }
    
    @Given("^My browser( does not)? support[s]? calling$")
    public void MyBrowserSupportsCalling(String doesNot) throws Exception {
        if (doesNot == null) {
            // should support calling
            if (!WebAppExecutionContext.getBrowser().isSupportingCalls()) {
                throw new PendingException("Browser "
                        + WebAppExecutionContext.getBrowser().toString()
                        + " does not support calling.");
            }
        } else // should not support calling
         if (WebAppExecutionContext.getBrowser().isSupportingCalls()) {
                throw new PendingException(
                        "Browser "
                        + WebAppExecutionContext.getBrowser()
                        .toString()
                        + " does support calling but this test is just for browsers without support.");
            }
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(
                OSXExecutionContext.CURRENT_PLATFORM, count, myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
    }

    @Given("^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
    public void IChangeAccentColor(String userNameAlias, String newColor)
            throws Exception {
        context.getCommonSteps().IChangeUserAccentColor(userNameAlias, newColor);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$")
    public void ThereAreNUsersWhereXIsMeWithoutAvatar(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(
                OSXExecutionContext.CURRENT_PLATFORM, count, myNameAlias);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
    }

    @When("^User (\\w+) changes? avatar picture to (.*)")
    public void IChangeUserAvatarPicture(String userNameAlias, String path)
            throws Exception {
        String avatar;
        final String rootPath = "/images/";
        if (path.equals("default")) {
            avatar = DEFAULT_USER_PICTURE;
        } else {
            avatar = rootPath + path;
        }
        URI uri = new URI(CommonWebAppSteps.class.getResource(avatar)
                .toString());
        log.debug("Change avatar of user " + userNameAlias + " to "
                + uri.getPath());
        context.getCommonSteps().IChangeUserAvatarPicture(userNameAlias, uri.getPath());
    }

    @Given("^(\\w+) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias,
            String usersToNameAliases) throws Exception {
        context.getCommonSteps().UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^(\\w+) blocked (\\w+)$")
    public void UserBlocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        context.getCommonSteps().BlockContact(userAsNameAlias, userToBlockNameAlias);
    }

    @Given("^(.*) (?:has|have) group chat (.*) with (.*)")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
            String chatName, String otherParticipantsNameAlises)
            throws Exception {
        context.getCommonSteps().UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAlises);
    }

    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
        IChangeUserAvatarPicture(nameAlias, "default");
    }

    @Given("^User (\\w+) is [Mm]e without avatar$")
    public void UserXIsMeWithoutAvatar(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
    }

    @Given("^(.*) sent connection request to (.*)")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
            String usersToNameAliases) throws Throwable {
        context.getCommonSteps().ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
    }

    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        context.getCommonSteps().WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        context.getCommonSteps().WaitForTime(seconds);
    }

    @When("^(.*) muted conversation with (.*)$")
    public void MuteConversationWithUser(String userToNameAlias,
            String muteUserNameAlias) throws Exception {
        context.getCommonSteps()
                .MuteConversationWithUser(userToNameAlias, muteUserNameAlias);
    }

    @When("^(.*) archived conversation with (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias,
            String archivedUserNameAlias) throws Exception {
        context.getCommonSteps().ArchiveConversationWithUser(userToNameAlias,
                archivedUserNameAlias);
    }

    @When("^User (.*) pinged in the conversation with (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        context.getCommonSteps().UserPingedConversation(pingFromUserNameAlias,
                dstConversationName);
    }

    @When("^User (.*) sent message (.*) to conversation (.*)")
    public void UserSentMessageToConversation(String userFromNameAlias,
            String message, String conversationName) throws Exception {
        context.getCommonSteps().UserSentMessageToConversation(userFromNameAlias,
                conversationName, message);
    }

    @Given("^User (.*) added contacts? (.*) to group chat (.*)")
    public void UserXAddedContactsToGroupChat(String asUser, String contacts,
            String conversationName) throws Exception {
        context.getCommonSteps().UserXAddedContactsToGroupChat(asUser, contacts,
                conversationName);
    }

    @Given("^User (.*) has contacts? (.*) in address book")
    public void UserXHasContactsInAddressBook(String asUser, String emails)
            throws Exception {
        context.getCommonSteps().UserXHasContactsInAddressBook(asUser, emails);
    }

    @Given("(.*) takes? snapshot of current profile picture$")
    public void UserXTakesSnapshotOfProfilePicture(String asUser)
            throws Exception {
        context.getCommonSteps().UserXTakesSnapshotOfProfilePicture(asUser);
    }

    @Then("^I verify that current profile picture snapshot of (.*) differs? from the previous one$")
    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias) throws Exception {
        context.getCommonSteps()
                .UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias);
    }

    @Given("^My browser supports synthetic drag and drop$")
    public void MyBrowserSupportsSyntheticDragDrop() {
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingSyntheticDragAndDrop()) {
            throw new PendingException();
        }
    }

    @When("^I click menu bar item \"(.*)\" and menu item \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName) throws Exception {
        MainWirePage mainPage = context.getOSXPagesCollection().getPage(MainWirePage.class);
        mainPage.clickMenuBarItem(menuBarItemName, menuItemName);
    }

    @When("^I click menu bar item \"(.*)\" and menu items \"(.*)\" and \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName, String menuItemName2) throws Exception {
        MainWirePage mainPage = context.getOSXPagesCollection().getPage(MainWirePage.class);
        mainPage.clickMenuBarItem(menuBarItemName, menuItemName, menuItemName2);
    }

    @When("^I click menu bar item with name \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName) throws Exception {
        context.getOSXPagesCollection().getPage(MainWirePage.class).clickMenuBarItem(menuBarItemName);
    }

    @When("^I kill the app$")
    public void KillApp() throws Exception {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
//        clearDrivers();
    }

    @When("^I restart the app$")
    public void restartApp() throws Exception {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
//        context.getOSXPagesCollection().getPage(MainWirePage.class).pressShortCutForQuit();
//        Thread.sleep(2000);
//        startApp();
    }

    @Then("^I verify app has quit$")
    public void IVerifyAppHasQuit() throws Exception {
        int exitCode = killAllApps();
        assertEquals(1, exitCode);
    }

    @Then("^I verify the app is not bigger than (\\d+) MB$")
    public void IVerifyAppIsNotTooBig(long expectedSize) throws Exception {
        assertThat(getSizeOfAppInMB(), lessThan(expectedSize));
    }
    
    @When("^Contact (.*) sends? message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias,
                                              String msg, String deviceName, String convoType, String dstConvoName) throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0)
                ? CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            context.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        } else {
            context.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        }
    }
    
    @When("user (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias, String deviceName, String label) throws Exception {
        context.getCommonSteps().UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
    }
}
