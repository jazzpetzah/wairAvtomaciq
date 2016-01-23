package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.AndroidPage;
import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {
    public static final String idStrRootLocator = "pfac__conversation__list_view_container";

    public static final Function<String, String> xpathStrSystemMessageByContent = content -> String
            .format("//*[@id='ltv__row_conversation__message' and contains(@value, '%s')]", content);

    public static final Function<String, String> xpathStrChatHeaderMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
                    content);

    private static final Function<String, String> xpathStrOutgoingInvitationMessageByContent = content -> String
            .format("//*[@id='ttv__connect_request__first_message' and @value='%s']", content);

    private static final Function<String, String> xpathStrSystemConnectionMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
                    content);

    private static final Function<String, String> xpathStrSystemConvoNameMessageByContent = content -> String
            .format("//*[@id='ttv__row_conversation__new_conversation_name' and @value='%s']", content);

    private static final By idMissedCallImage = By.id("sci__conversation__missed_call__image");

    private static final By idShowToolsButton = By.id("cursor_button_open");

    private static final By idCloseToolsButton = By.id("cursor_button_close");

    public static final Function<String, String> xpathConversationMessageByValue = value -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']",
                    value);

    public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private DialogPage getDialogPage() throws Exception {
        return this.getAndroidPageInstance(DialogPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idStrRootLocator));
    }

    public void tapShowDetailsButton() throws Exception {
        getElement(DialogPage.idParticipantsBtn).click();
    }

    public boolean waitForSystemMessageContains(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForChatHeaderMessageContains(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrChatHeaderMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapTextInput() throws Exception {
        // FIXME: Scroll to the bottom if cursor input is not visible
        this.scrollToTheBottom();

        getElement(AndroidPage.idCursorArea).click();
    }

    public void typeMessage(String message) throws Exception {
        getElement(idEditText).sendKeys(message);
    }

    public void sendMessage() throws Exception {
        getDriver().tapSendButton();
    }

    public boolean waitUntilMessageIsVisible(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathConversationMessageByValue
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void swipeOnTextInput() throws Exception {
        getDialogPage().swipeRightOnCursorInput();
    }

    public void tapPingButton() throws Exception {
        getDialogPage().tapPingBtn();
    }

    public void tapPingButtonIfVisible() throws Exception {
        getDialogPage().tapPingButtonIfVisible();
    }

    public boolean waitUntilPingMessageIsVisible(String expectedMessage)
            throws Exception {
        return getDialogPage().waitForPingMessageWithText(expectedMessage);
    }

    public boolean waitUntilAPictureAppears() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), DialogPage.idDialogImages);
    }

    public void tapShowInstrumentsButton() throws Exception {
        // FIXME: Workaround for incorrectly positioned cursor
        scrollToTheBottom();

        getElement(idShowToolsButton).click();
    }

    public void tapCloseInstrumentsButton() throws Exception {
        getElement(idCloseToolsButton).click();
    }

    public boolean waitUntilGCNIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMissedCallImage);
    }

    public boolean waitUntilMessageIsNotVisible(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathConversationMessageByValue
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitForOutgoingInvitationMessage(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrOutgoingInvitationMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPingMessageIsInvisible(String expectedMessage)
            throws Exception {
        return getDialogPage().waitForPingMessageWithTextDisappears(
                expectedMessage);
    }

    public void doSwipeRight() throws Exception {
        DriverUtils.swipeByCoordinates(getDriver(), 1000, 10, 50, 90, 50);
    }

    public void scrollToTheBottom() throws Exception {
        getDialogPage().scrollToTheBottom();
    }

    public Optional<BufferedImage> getRecentPictureScreenshot()
            throws Exception {
        return getDialogPage().getRecentPictureScreenshot();
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot()
            throws Exception {
        return getDialogPage().getPreviewPictureScreenshot();
    }

    public void tapRecentPicture() throws Exception {
        getDialogPage().clickLastImageFromDialog();
    }

    public boolean waitForSystemConnectionMessageContains(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrSystemConnectionMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForConversationNameSystemMessage(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrSystemConvoNameMessageByContent
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(idStrRootLocator));
    }

    public boolean waitUntilPicturesNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), DialogPage.idDialogImages);
    }

    public boolean waitUntilUnsentIndicatorIsVisible(String msg)
            throws Exception {
        return getDialogPage().waitForUnsentIndicator(msg);
    }

    public boolean waitUntilUnsentIndicatorIsVisibleForAPicture()
            throws Exception {
        return getDialogPage().waitForAPictureWithUnsentIndicator();
    }

    public void tapPlayPauseButton() throws Exception {
        getDialogPage().tapPlayPauseBtn();
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
        getDialogPage().tapSketchBtn();
    }

    public void tapSketchButtonOnPicturePreview() throws Exception {
        getDialogPage().tapSketchOnImageButton();
    }

    public BufferedImage getMediaButtonScreenshot() throws Exception {
        return getDialogPage().getMediaControlButtonScreenshot();
    }

    public boolean scrollUpUntilMediaBarVisible(final int maxScrollRetries)
            throws Exception {
        return getDialogPage().scrollUpUntilMediaBarVisible(maxScrollRetries);
    }

    public void tapMediaBarControlButton() throws Exception {
        getDialogPage().tapPlayPauseMediaBarBtn();
    }
}
