package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {

    public static final Function<String, String> xpathStrSystemMessageByContent = content -> String
            .format("//*[starts-with(@id, 'ttv__row_conversation') and contains(@value, '%s')]", content.toUpperCase());

    private static final Function<String, String> xpathStrOutgoingInvitationMessageByContent = content -> String
            .format("//*[@id='ttv__connect_request__first_message' and @value='%s']", content);

    private static final Function<String, String> xpathStrSystemConnectionMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
                    content);

    private static final Function<String, String> xpathStrSystemConvoNameMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__new_conversation_name' and @value='%s']", content);

    private static final By idMissedCallImage = By.id("sci__conversation__missed_call__image");

    private static final By idSketchButtonOnPicturePreviewOverlay = By.id("gtv__single_image_message__sketch");

    public static final Function<String, String> xpathConversationMessageByValue = value -> String
            .format("//*[@id='tmltv__row_conversation__message' and @value='%s']", value);

    public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ConversationViewPage getConversationViewPage() throws Exception {
        return this.getAndroidPageInstance(ConversationViewPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), ConversationViewPage.idConversationRoot);
    }

    public boolean waitForSystemMessageContains(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByContent.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapTextInput() throws Exception {
        getConversationViewPage().tapOnTextInput();
    }

    public void typeMessage(String message) throws Exception {
        getConversationViewPage().typeMessage(message);
    }

    public void sendMessage() throws Exception {
        getDriver().tapSendButton();
    }

    public boolean waitUntilMessageIsVisible(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathConversationMessageByValue.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapCursorToolButton(String name) throws Exception {
        getConversationViewPage().tapCursorToolButton(name);
    }

    public boolean waitUntilPingMessageIsVisible(String expectedMessage) throws Exception {
        return getConversationViewPage().waitForPingMessageWithText(expectedMessage);
    }

    public boolean waitUntilAPictureAppears() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), ConversationViewPage.idConversationImageContainer);
    }

    public boolean waitUntilGCNIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMissedCallImage);
    }

    public boolean waitUntilMessageIsNotVisible(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathConversationMessageByValue.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitForOutgoingInvitationMessage(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrOutgoingInvitationMessageByContent.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPingMessageIsInvisible(String expectedMessage) throws Exception {
        return getConversationViewPage().waitForPingMessageWithTextDisappears(expectedMessage);
    }

    public void doSwipeRight() throws Exception {
        DriverUtils.swipeByCoordinates(getDriver(), 1000, 10, 50, 90, 50);
    }

    public void scrollToTheBottom() throws Exception {
        getConversationViewPage().scrollToTheBottom();
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return getConversationViewPage().getRecentPictureScreenshot();
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        return getConversationViewPage().getPreviewPictureScreenshot();
    }

    public void tapContainer(String tapType, String containerName) throws Exception {
        getConversationViewPage().tapContainer(tapType, containerName);
    }

    public boolean waitForSystemConnectionMessageContains(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrSystemConnectionMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForConversationNameSystemMessage(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrSystemConvoNameMessageByContent.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), ConversationViewPage.idConversationRoot);
    }

    public boolean waitUntilPicturesNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), ConversationViewPage.idConversationImageContainer);
    }

    public void tapPlayPauseButton() throws Exception {
        getConversationViewPage().tapPlayPauseBtn();
    }

    public boolean waitUntilClosePicturePreviewButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCloseImageBtn);
    }

    public boolean waitUntilClosePicturePreviewButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCloseImageBtn);
    }

    public void tapClosePicturePreviewButton() throws Exception {
        getElement(idCloseImageBtn).click();
    }

    public boolean waitUntilGiphyButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGiphyPreviewButton);
    }

    public boolean waitUntilGiphyButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idGiphyPreviewButton);
    }

    public void tapGiphyButton() throws Exception {
        getElement(idGiphyPreviewButton).click();
    }

    public boolean scrollUpUntilMediaBarVisible(final int maxScrollRetries) throws Exception {
        return getConversationViewPage().scrollUpUntilMediaBarVisible(maxScrollRetries);
    }

    public void tapMediaBarControlButton() throws Exception {
        getConversationViewPage().tapPlayPauseMediaBarBtn();
    }

    public BufferedImage getMediaControlButtonState() throws Exception {
        return getConversationViewPage().getMediaButtonState();
    }

    public void tapTopToolbarTitle() throws Exception {
        getConversationViewPage().tapTopToolbarTitle();
    }

    public void tapMessage(String messageType, String message, String tapType) throws Exception {
        getConversationViewPage().tapMessage(messageType, message, tapType);
    }

    //region Message Bottom Menu
    public void tapMessageBottomMenuButton(String btnName) throws Exception {
        getConversationViewPage().tapMessageBottomMenuButton(btnName);
    }

    public boolean waitUntilMessageBottomMenuButtonVisible(String name) throws Exception {
        return getConversationViewPage().waitUntilMessageBottomMenuButtonVisible(name);
    }

    public boolean waitUntilMessageBottomMenuButtonInvisible(String name) throws Exception {
        return getConversationViewPage().waitUntilMessageBottomMenuButtonInvisible(name);
    }
    //endregion

    public boolean isContainerVisible(String containerType) throws Exception {
        return getConversationViewPage().isContainerVisible(containerType);
    }

    public boolean isContainerInvisible(String containerType) throws Exception {
        return getConversationViewPage().isContainerInvisible(containerType);
    }

    public boolean isCursorToolbarVisible() throws Exception {
        return getConversationViewPage().isCursorToolbarVisible();
    }

    public boolean isCursorToolbarInvisible() throws Exception {
        return getConversationViewPage().isCursorToolbarInvisible();
    }

    public void longTapAudioMessageCursorBtn() throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtn(DriverUtils.LONG_TAP_DURATION);
    }

    public void longTapAudioMessageCursorBtn(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtn(durationSeconds * 1000);
    }

    public void tapAudioRecordingButton(String name) throws Exception {
        getConversationViewPage().tapAudioRecordingButton(name);
    }

    public void longTapAudioMessageCursorBtnAndSwipeUp(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtnAndSwipeUp(durationSeconds * 1000);
    }

    public void tapTopBarButton(String btnName) throws Exception {
        getConversationViewPage().tapTopBarButton(btnName);
    }

    public BufferedImage getFilePlaceholderActionButtonState() throws Exception {
        return getConversationViewPage().getFilePlaceholderActionButtonState();
    }

    public void waitUntilFileUploadIsCompleted(int timeoutSeconds, String size, String extension) throws Exception {
        getConversationViewPage().waitUntilFileUploadIsCompleted(timeoutSeconds, size, extension);
    }

    public boolean isFilePlaceHolderVisible(String fileFullName, String size, String extension, boolean isUpload,
                                            boolean isSuccess, int lookUpTimeoutSeconds) throws Exception {
        return getConversationViewPage().isFilePlaceHolderVisible(fileFullName, size, extension, isUpload, isSuccess,
                lookUpTimeoutSeconds);
    }

    public boolean isFilePlaceHolderInvisible(String fileFullName, String size, String extension, boolean isUpload,
                                              boolean isSuccess, int lookUpTimeoutSeconds) throws Exception {
        return getConversationViewPage().isFilePlaceHolderInvisible(fileFullName, size, extension, isUpload, isSuccess,
                lookUpTimeoutSeconds);
    }

    public void tapSketchOnPicturePreviewOverlay() throws Exception {
        getElement(idSketchButtonOnPicturePreviewOverlay).click();
    }

    public boolean waitUntilMessageMetaItemVisible(String itemType) throws Exception {
        return getConversationViewPage().waitUntilMessageMetaItemVisible(itemType);
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType) throws Exception {
        return getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType);
    }

    public boolean waitUntilMessageMetaItemVisible(String itemType, String expectedItemText)
            throws Exception {
        return getConversationViewPage().waitUntilMessageMetaItemVisible(itemType, expectedItemText);
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType, String expectedItemText)
            throws Exception {
        return getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType, expectedItemText);
    }

    public void tapImageContainerButton(String buttonName) throws Exception {
        getConversationViewPage().tapImageContainerButton(buttonName);
    }

    public BufferedImage getMessageLikeButtonState() throws Exception {
        return getConversationViewPage().getMessageLikeButtonState();
    }

    public void tapMessageMetaItem(String itemType) throws Exception {
        getConversationViewPage().tapMessageMetaItem(itemType);
    }

    public int getMessageStatusCount() throws Exception {
        return getConversationViewPage().getMessageStatusCount();
    }

    public boolean waitUntilTrashIconVisible(String name) throws Exception {
        return getConversationViewPage().waitUntilTrashIconVisible(name);
    }

    public boolean waitUntilTrashIconInvisible(String name) throws Exception {
        return getConversationViewPage().waitUntilTrashIconInvisible(name);
    }

    public boolean waitUntilPenIconVisible(String name) throws Exception {
        return getConversationViewPage().waitUntilPenIconVisible(name);
    }

    public boolean waitUntilPenIconInvisible(String name) throws Exception {
        return getConversationViewPage().waitUntilPenIconInvisible(name);
    }

    public void clearMessageInCursorInput() throws Exception {
        getConversationViewPage().clearMessageInCursorInput();
    }

    public boolean waitUntilCursorInputTextVisible(String text) throws Exception {
        return getConversationViewPage().waitUntilCursorInputTextVisible(text);
    }

}
