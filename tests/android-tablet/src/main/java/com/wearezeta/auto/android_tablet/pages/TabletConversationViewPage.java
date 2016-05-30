package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.TakePicturePage;
import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {

    public static final Function<String, String> xpathStrSystemMessageByContent = content -> String
            .format("//*[@id='ltv__row_conversation__message' and contains(@value, '%s')]", content);

    private static final Function<String, String> xpathStrOutgoingInvitationMessageByContent = content -> String
            .format("//*[@id='ttv__connect_request__first_message' and @value='%s']", content);

    private static final Function<String, String> xpathStrSystemConnectionMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
                    content);

    private static final Function<String, String> xpathStrSystemConvoNameMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__new_conversation_name' and @value='%s']", content);

    private static final By idMissedCallImage = By.id("sci__conversation__missed_call__image");

    public static final Function<String, String> xpathConversationMessageByValue = value -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']", value);

    public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ConversationViewPage getConversationViewPage() throws Exception {
        return this.getAndroidPageInstance(ConversationViewPage.class);
    }

    private TakePicturePage getTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), ConversationViewPage.idDialogRoot);
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

    public void tapPingButton() throws Exception {
        getConversationViewPage().tapPingBtn();
    }

    public void tapTopToolbarTitle() throws Exception {
        getConversationViewPage().tapTopToolbarTitle();
    }

    public boolean waitUntilPingMessageIsVisible(String expectedMessage) throws Exception {
        return getConversationViewPage().waitForPingMessageWithText(expectedMessage);
    }

    public boolean waitUntilAPictureAppears() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), ConversationViewPage.idDialogImages);
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

    public void tapRecentPicture() throws Exception {
        getConversationViewPage().tapRecentImage();
    }

    public boolean waitForSystemConnectionMessageContains(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrSystemConnectionMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForConversationNameSystemMessage(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrSystemConvoNameMessageByContent.apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), ConversationViewPage.idDialogRoot);
    }

    public boolean waitUntilPicturesNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), ConversationViewPage.idDialogImages);
    }

    public boolean waitUntilUnsentIndicatorIsVisible(String msg) throws Exception {
        return getConversationViewPage().waitForUnsentIndicatorVisible(msg);
    }

    public boolean waitUntilUnsentIndicatorIsVisibleForAPicture() throws Exception {
        return getConversationViewPage().waitForAPictureWithUnsentIndicator();
    }

    public void tapPlayPauseButton() throws Exception {
        getConversationViewPage().tapPlayPauseBtn();
    }

    public boolean waitUntilClosePicturePreviewButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCloseImageBtn);
    }

    public boolean waitUntilClosePicturePreviewButtonInvisible()
            throws Exception {
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

    public void tapSketchButton() throws Exception {
        getConversationViewPage().tapSketchBtn();
    }

    public void tapFileButton() throws Exception {
        getConversationViewPage().tapFileBtn();
    }

    public void tapSketchButtonOnPicturePreview() throws Exception {
        getTakePicturePage().tapSketchOnImageButton();
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
}
