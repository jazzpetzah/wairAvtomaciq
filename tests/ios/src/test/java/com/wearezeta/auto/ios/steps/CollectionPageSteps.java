package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CollectionPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;

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
     * Send multiple images or videos to the conversation
     *
     * @param senderUserNameAlias sender name/alias
     * @param count               count of items to send
     * @param fileName            the name of existing media file.
     *                            Only m4a/mp4 files can be set for audio and video types
     * @param fileType            one of possible file types
     * @param dstConversationName destination conversation name
     * @throws Exception
     * @step. ^User (.*) sends (\d+) (images?|videos?) (.*) to conversation (.*)
     */
    @Given("^User (.*) sends (\\d+) (image|video|audio|temporary) files? (.*) to conversation (.*)")
    public void UserSendsMultiplePictures(String senderUserNameAlias, int count,
                                          String fileType, String fileName,
                                          String dstConversationName) throws Exception {
        final ClientUser srcUser = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(senderUserNameAlias);
        dstConversationName = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(dstConversationName, ClientUsersManager.FindBy.NAME_ALIAS);
        final String dstConvoId = BackendAPIWrappers.getConversationIdByName(srcUser, dstConversationName);
        String filePath;
        for (int i = 0; i < count; ++i) {
            switch (fileType) {
                case "image":
                    filePath = CommonUtils.getImagesPathFromConfig(this.getClass()) + File.separator + fileName;
                    IOSTestContextHolder.getInstance().getTestContext().getDevicesManager().
                            sendImage(srcUser, dstConvoId, filePath);
                    break;
                case "video":
                case "audio":
                    filePath = CommonUtils.getAudioPathFromConfig(this.getClass()) + File.separator + fileName;
                    IOSTestContextHolder.getInstance().getTestContext().getDevicesManager().
                            sendFile(srcUser, dstConvoId, filePath,
                                    fileType.equals("video") ? "video/mp4" : "audio/mp4", null);
                    break;
                case "temporary":
                    filePath = CommonUtils.getBuildPathFromConfig(this.getClass()) + File.separator + fileName;
                    IOSTestContextHolder.getInstance().getTestContext().getDevicesManager().
                            sendFile(srcUser, dstConvoId, filePath, "application/octet-stream", null);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unsupported '%s' file type", fileType));
            }
        }
    }

    /**
     * Send multiple messages to the conversation
     *
     * @param senderUserNameAlias sender name/alias
     * @param count               count of text messages to send
     * @param msg                 either 'default' to send the default message or the actual messages
     *                            enclosed by double quotes
     * @param dstConversationName destination conversation name
     * @throws Exception
     * @step. ^User (.*) sends (\d+) (default|".*") messages? to conversation (.*)
     */
    @Given("^User (.*) sends (\\d+) (default|\".*\") messages? to conversation (.*)")
    public void UserSendsMultipleVideos(String senderUserNameAlias, int count,
                                        String msg, String dstConversationName) throws Exception {
        if (msg.equals("default")) {
            msg = CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE;
        } else {
            msg = msg.replaceAll("^\"|\"$", "");
        }
        final ClientUser srcUser = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(senderUserNameAlias);
        dstConversationName = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(dstConversationName, ClientUsersManager.FindBy.NAME_ALIAS);
        final String dstConvoId = BackendAPIWrappers.getConversationIdByName(srcUser, dstConversationName);
        for (int i = 0; i < count; ++i) {
            IOSTestContextHolder.getInstance().getTestContext().getDevicesManager().
                    sendConversationMessage(srcUser, dstConvoId, msg);
        }
    }

    /**
     * Verify tiles count in the particular category
     *
     * @param expectedCount the expected tiles count
     * @throws Exception
     * @step. ^I see the count of tiles in collection category equals to (\d+)$
     */
    @Then("^I see the count of tiles in collection category equals to (\\d+)$")
    public void IVerifyTilesCount(int expectedCount) throws Exception {
        Assert.assertTrue(String.format("The count of tiles is not equal to %s", expectedCount),
                getCollectionPage().isTilesCountEqualTo(expectedCount));
    }

    /**
     * Tap the Show All label next to the corresponding category name
     *
     * @param categoryName one of possible category names
     * @throws Exception
     * @step. ^I tap Show All label next to collection category (PICTURES|VIDEOS|LINKS|FILES)$
     */
    @When("^I tap Show All label next to collection category (PICTURES|VIDEOS|LINKS|FILES)$")
    public void ITapAllLabel(String categoryName) throws Exception {
        getCollectionPage().tapShowAllLabel(categoryName);
    }

    /**
     * Emulate scrolling in collection view
     *
     * @step. I scroll collection view (up|down)
     * @param direction
     * @throws Exception
     */
    @And("^I scroll collection view (up|down)$")
    public void ISwipe(String direction) throws Exception {
        getCollectionPage().scroll(direction);
    }
}
