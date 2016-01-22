package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DialogPage extends AndroidPage {

    private static final Logger log = ZetaLogger.getLog(DialogPage.class.getSimpleName());

    public static final String MEDIA_PLAY = "PLAY";
    public static final String MEDIA_PAUSE = "PAUSE";

    public static final By xpathConfirmOKButton = By.xpath("//*[@id='ttv__confirmation__confirm' and @value='OK']");

    public static final String idStrDialogImages = "iv__row_conversation__message_image";
    public static final By idDialogImages = By.id(idStrDialogImages);

    private static final By xpathLastPicture = By.xpath(String.format("(//*[@id='%s'])[last()]", idStrDialogImages));

    private static final By xpathE2EEDialogImagesBadges = By.xpath("//*[@id='" + idStrDialogImages +
            "']/parent::*/parent::*//*[@id='v__row_conversation__e2ee']");

    public static final By idAddPicture = By.id("cursor_menu_item_camera");

    private static final Function<String, String> xpathStrConversationMessageByText = text -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']", text);

    private static final Function<String, String> xpathStrEncryptedConversationMessageByText = text -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']/parent::*/parent::*" +
                    "//*[@id='v__row_conversation__e2ee']", text);

    private static final Function<String, String> xpathStrUnsentIndicatorByText = text -> String
            .format("%s/parent::*/parent::*//*[@id='v__row_conversation__error']",
                    xpathStrConversationMessageByText.apply(text));

    private static final By xpathUnsentIndicatorForImage = By.xpath("//*[@id='"
            + idStrDialogImages + "']/parent::*/parent::*//*[@id='v__row_conversation__error']");

    private static final By idCursorBtn = By.id("typing_indicator_button");

    private static final By idCursorBtnImg = By.id("typing_indicator_imageview");

    private static final String idStrMissedCallMesage = "ttv__row_conversation__missed_call";
    private static final Function<String, String> xpathStrMissedCallMesageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrMissedCallMesage, text);

    private static final By idCursorFrame = By.id("cursor_layout");

    public static final Function<String, String> xpathStrPingMessageByText = text -> String
            .format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']", text);

    private static final By xpathDialogTakePhotoButton =
            By.xpath("//*[@id='gtv__camera_control__take_a_picture' and @shown='true']");

    private static final By idSketchImagePaintButton = By.id("gtv__sketch_image_paint_button");

    private static final By idFullScreenImage = By.id("tiv__single_image_message__image");

    public static final By idParticipantsBtn = By.id("cursor_menu_item_participant");

    private static final String idStrStartChatLabel = "ttv__row_conversation__connect_request__chathead_footer__label";
    private static final Function<String, String> xpathStrStartChatLabelByPartOfText =
            text -> String.format("//*[@id='%s' and contains(@value, '%s')]", idStrStartChatLabel, text);

    private static final By idPlayPauseMedia = By.id("gtv__media_play");

    private static final By idYoutubePlayButton = By.id("gtv__youtube_message__play");

    private static final By idMediaBarControl = By.id("gtv__conversation_header__mediabar__control");

    private static final By idPing = By.id("cursor_menu_item_ping");

    private static final By idSketch = By.id("cursor_menu_item_draw");

    private static final By idCall = By.id("cursor_menu_item_calling");

    public static final By idCursorCloseButton = By.id("cursor_button_close");

    private static final By idMute = By.id("cib__calling__mic_mute");

    private static final By idSpeaker = By.id("cib__calling__speaker");

    private static final By idCancelCall = By.id("cib__calling__dismiss");

    private static final String idStrNewConversationNameMessage = "ttv__row_conversation__new_conversation_name";
    private static Function<String, String> xpathStrNewConversationNameByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", idStrNewConversationNameMessage, value);

    private static final By xpathLastConversationMessage =
            By.xpath("(//*[@id='ltv__row_conversation__message'])[last()]");

    private static final String idStrDialogRoot = "pfac__conversation__list_view_container";
    private static final By idDialogRoot = By.id(idStrDialogRoot);
    private static final By xpathDialogContent = By.xpath("//*[@id='" + idStrDialogRoot + "']/*/*/*");

    public static Function<String, String> xpathStrInputFieldByValue = value -> String.format("//*[@value='%s']", value);

    private static final By idSwitchCameraButton = By.id("gtv__camera__top_control__back_camera");

    private static final int DEFAULT_SWIPE_TIME = 500;
    private static final int MAX_SWIPE_RETRIES = 5;
    private static final int MAX_CLICK_RETRIES = 5;

    public DialogPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorArea);
    }

    public boolean waitForCursorInputNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorArea);
    }

    public void tapOnCursorInput() throws Exception {
        // FIXME: Scroll to the bottom if cursor input is not visible
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorBtnImg, 1)) {
            getElement(idCursorFrame).click();
        }
        getElement(idCursorArea).click();
    }

    public void pressPlusButtonOnDialogPage() throws Exception {
        getElement(idCursorBtn, "Plus cursor button is not visible").click();
    }

    public void swipeRightOnCursorInput() throws Exception {
        // FIXME: Workaround for a bug in the app
        scrollToTheBottom();

        final WebElement cursorArea = getElement(idCursorArea);
        int ntry = 1;
        do {
            DriverUtils.swipeElementPointToPoint(this.getDriver(), cursorArea,
                    DEFAULT_SWIPE_TIME, 10, 50, 90, 50);
            final int currentCursorOffset = cursorArea.getLocation().getX();
            if (currentCursorOffset > getDriver().manage().window().getSize()
                    .getWidth() / 2) {
                return;
            }
            log.debug(String.format(
                    "Failed to swipe the text cursor. Retrying (%s of %s)...",
                    ntry, MAX_SWIPE_RETRIES));
            ntry++;
            Thread.sleep(1000);
        } while (ntry <= MAX_SWIPE_RETRIES);
        throw new RuntimeException(
                String.format(
                        "Failed to swipe the text cursor on input field after %s retries!",
                        MAX_SWIPE_RETRIES));
    }

    // NOTE: This method is required to scroll conversation to the end.
    // NOTE: Click happens on the text input area if participants button is not
    // NOTE: visible
    public void scrollToTheBottom() throws Exception {
        // Close cursor if it is currently opened
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), DialogPage.idCursorCloseButton, 1)) {
            getElement(DialogPage.idCursorCloseButton).click();
            // Wait for animation
            Thread.sleep(500);
        } else {
            this.hideKeyboard();
            if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorFrame, 2)) {
                throw new IllegalStateException("Cursor frame is not visible");
            }
        }
        getElement(idCursorFrame).click();
        this.hideKeyboard();
    }

    public void tapAddPictureBtn() throws Exception {
        getElement(idAddPicture, "Add Picture button is not visible").click();
    }

    public void tapPingBtn() throws Exception {
        getElement(idPing, "Ping button is not visible").click();
    }

    public void tapSketchBtn() throws Exception {
        getElement(idSketch, "Sketch button is not visible").click();
    }

    public void tapCallBtn() throws Exception {
        getElement(idCall, "Call button is not visible").click();
    }

    public void closeInputOptions() throws Exception {
        getElement(idCursorCloseButton, "Close cursor button is not visible").click();
    }

    public void tapMuteBtn() throws Exception {
        getElement(idMute, "Mute button is not visible").click();
    }

    public void tapSpeakerBtn() throws Exception {
        getElement(idSpeaker, "Speaker button is not visible").click();
    }

    public void tapCancelCallBtn() throws Exception {
        getElement(idCancelCall, "Cancel call button is not visible").click();
    }

    private WebElement getButtonElementByName(String name) throws Exception {
        final String uppercaseName = name.toUpperCase();
        switch (uppercaseName) {
            case "MUTE":
                return getElement(idMute);
            case "SPEAKER":
                return getElement(idSpeaker);
            default:
                throw new NoSuchElementException(String.format(
                        "Button '%s' is unknown", name));
        }
    }

    public BufferedImage getCurrentButtonStateScreenshot(String name)
            throws Exception {
        final WebElement dstButton = getButtonElementByName(name);
        if (!DriverUtils.waitUntilElementClickable(getDriver(), dstButton)) {
            throw new IllegalStateException("The button is not clickable");
        }
        return getElementScreenshot(dstButton).orElseThrow(IllegalStateException::new);
    }

    public void typeAndSendMessage(String message) throws Exception {
        // FIXME: Find a better solution for text autocorrection issues
        final WebElement cursorInput = getElement(idEditText);
        final int maxTries = 5;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(message);
            ntry++;
        } while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrInputFieldByValue.apply(message)), 2)
                && ntry < maxTries);
        if (ntry >= maxTries) {
            throw new IllegalStateException(
                    String.format(
                            "The string '%s' was autocorrected. Please disable autocorrection on the device and restart the test.",
                            message));
        }
        pressKeyboardSendButton();
        this.hideKeyboard();
    }

    public void typeMessage(String message) throws Exception {
        getElement(idEditText).sendKeys(message);
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrInputFieldByValue.apply(message)), 2)) {
            log.warn(String
                    .format("The message '%s' was autocorrected. This might cause unpredicted test results",
                            message));
        }
    }

    public void clickLastImageFromDialog() throws Exception {
        getElement(xpathLastPicture, "No pictures are visible in the conversation view").click();
    }

    public boolean waitForConversationNameChangedMessage(String expectedName)
            throws Exception {
        final By locator = By.xpath(xpathStrNewConversationNameByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForMessage(String text) throws Exception {
        scrollToTheBottom();
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(text));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitForUnsentIndicator(String text) throws Exception {
        final By locator = By.xpath(xpathStrUnsentIndicatorByText.apply(text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isImageExists() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), idDialogImages);
    }

    public void confirm() throws Exception {
        final By locator = xpathConfirmOKButton;
        final WebElement okBtn = getElement(locator, "OK button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), okBtn)) {
            throw new IllegalStateException("OK button is not clickable");
        }
        okBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator)) {
            throw new IllegalStateException("OK button is still present on the screen after being clicked");
        }
    }

    public void tapSketchOnImageButton() throws Exception {
        getElement(idSketchImagePaintButton, "Draw sketch on image button is not visible").click();
    }

    public void takePhoto() throws Exception {
        final WebElement btn = getElement(xpathDialogTakePhotoButton, "Take Photo button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), btn)) {
            throw new IllegalStateException("Take Photo button is not clickable");
        }
        btn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathDialogTakePhotoButton)) {
            throw new IllegalStateException("Take Photo button is still visible after being clicked");
        }
    }

    public boolean waitUntilStartChatTitleContains(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrStartChatLabelByPartOfText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    /**
     * Navigates back by swipe and initialize ContactListPage
     *
     * @throws Exception
     */
    public void navigateBack(int timeMilliseconds) throws Exception {
        swipeRightCoordinates(timeMilliseconds);
    }

    public void openGallery() throws Exception {
        getElement(idGalleryBtn, "Gallery button is still not visible").click();
    }

    public void closeFullScreenImage() throws Exception {
        // Sometimes X button is opened automatically after some timeout
        final int MAX_TRIES = 4;
        int ntry = 1;
        while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCloseImageBtn, 4) && ntry <= MAX_TRIES) {
            this.tapOnCenterOfScreen();
            ntry++;
        }
        getElement(idCloseImageBtn).click();
    }

    public void tapConversationDetailsButton() throws Exception {
        final WebElement participantsBtn = getElement(idParticipantsBtn);
        participantsBtn.click();
        final long millisecondsStarted = System.currentTimeMillis();
        final long maxAnimationDurationMillis = 2000;
        do {
            Thread.sleep(200);
        } while (participantsBtn.getLocation().getY() > 0
                && System.currentTimeMillis() - millisecondsStarted < maxAnimationDurationMillis);
        assert participantsBtn.getLocation().getY() < 0;
    }

    public boolean waitForPingMessageWithText(String expectedText)
            throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForPingMessageWithTextDisappears(String expectedText)
            throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isGroupChatDialogContainsNames(List<String> names)
            throws Exception {
        final String convoText = getElement(xpathLastConversationMessage,
                "No messages are visible in the conversation view").getText();
        for (String name : names) {
            if (!convoText.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean isDialogVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idDialogRoot);
    }

    private static final double MAX_BUTTON_STATE_OVERLAP = 0.5;

    public void tapPlayPauseBtn() throws Exception {
        final WebElement playPauseBtn = getElement(idPlayPauseMedia, "Play/Pause button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), playPauseBtn)) {
            throw new IllegalStateException("Play/Pause button is not clickable");
        }
        final BufferedImage initialState = getElementScreenshot(playPauseBtn)
                .orElseThrow(
                        () -> new IllegalStateException("Failed to get a screenshot of Play/Pause button"));
        playPauseBtn.click();
        Thread.sleep(2000);
        int clickTry = 1;
        do {
            final BufferedImage currentState = getElementScreenshot(
                    playPauseBtn).orElseThrow(
                    () -> new AssertionError(
                            "Failed to get a screenshot of Play/Pause button"));
            final double overlapScore = ImageUtil.getOverlapScore(currentState,
                    initialState, ImageUtil.RESIZE_TO_MAX_SCORE);
            if (overlapScore < MAX_BUTTON_STATE_OVERLAP) {
                return;
            } else {
                playPauseBtn.click();
                Thread.sleep(2000);
            }
            clickTry++;
        } while (clickTry <= MAX_CLICK_RETRIES);
        assert (clickTry > MAX_CLICK_RETRIES) : "Media playback state has not been changed after "
                + MAX_CLICK_RETRIES + " retries";
    }

    public boolean waitUntilYoutubePlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idYoutubePlayButton);
    }

    public double getMediaBarControlIconOverlapScore(String label)
            throws Exception {
        String path = null;
        BufferedImage mediaImage = getElementScreenshot(getElement(idMediaBarControl))
                .orElseThrow(IllegalStateException::new);
        if (label.equals(MEDIA_PLAY)) {
            path = CommonUtils.getMediaBarPlayIconPath(DialogPage.class);
        } else if (label.equals(MEDIA_PAUSE)) {
            path = CommonUtils.getMediaBarPauseIconPath(DialogPage.class);
        }
        BufferedImage templateImage = ImageUtil.readImageFromFile(path);
        return ImageUtil.getOverlapScore(mediaImage, templateImage,
                ImageUtil.RESIZE_TO_MAX_SCORE);
    }

    public BufferedImage getMediaControlButtonScreenshot() throws Exception {
        return getElementScreenshot(getElement(idPlayPauseMedia)).orElseThrow(IllegalStateException::new);
    }

    public void tapPlayPauseMediaBarBtn() throws Exception {
        getElement(idMediaBarControl, "Media barr PlayPause button is not visible").click();
    }

    private boolean waitUntilMediaBarVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMediaBarControl, timeoutSeconds);
    }

    public boolean waitUntilMissedCallMessageIsVisible(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathStrMissedCallMesageByText
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLastMessageEqualTo(String expectedMessage, int timeoutSeconds) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(expectedMessage));
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            final Optional<WebElement> msgElement = getElementIfDisplayed(locator);
            if (msgElement.isPresent()) {
                final String lastMessage = getElement(xpathLastConversationMessage,
                        "Cannot find the last message in the dialog", 1).getText();
                if (expectedMessage.equals(lastMessage)) {
                    return true;
                }
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        return false;
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idDialogImages));
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot()
            throws Exception {
        return this.getElementScreenshot(getElement(idFullScreenImage));
    }

    public boolean isImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idDialogImages);
    }

    public boolean waitForAPictureWithUnsentIndicator() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathUnsentIndicatorForImage);
    }

    public boolean scrollUpUntilMediaBarVisible(final int maxScrollRetries)
            throws Exception {
        int swipeNum = 1;
        while (swipeNum <= maxScrollRetries) {
            swipeByCoordinates(1000, 50, 10, 50, 90);
            if (waitUntilMediaBarVisible(2)) {
                return true;
            }
            swipeNum++;
        }
        return false;
    }

    public int getCurrentNumberOfItemsInDialog() throws Exception {
        return selectVisibleElements(xpathDialogContent).size();
    }

    public BufferedImage getConvoViewScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idDialogRoot)).orElseThrow(IllegalStateException::new);
    }

    /**
     * @return false if Take Photo button is not visible after Switch Camera button is clicked
     * @throws Exception
     */
    public boolean tapSwitchCameraButton() throws Exception {
        getElement(idSwitchCameraButton).click();
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathDialogTakePhotoButton);
    }

    public boolean waitForXEncryptedMessages(String msg, int times) throws Exception {
        final By locator = By.xpath(xpathStrEncryptedConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) &&
                getDriver().findElements(locator).size() == times;
    }

    public boolean waitForXNonEncryptedMessages(String msg, int times) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) &&
                getDriver().findElements(locator).size() == times;
    }

    public boolean waitForXEncryptedImages(int times) throws Exception {
        final List<WebElement> allImageBadges = selectVisibleElements(xpathE2EEDialogImagesBadges);
        return times == allImageBadges.stream().filter(WebElement::isDisplayed).count();
    }

    public boolean waitForXNonEncryptedImages(int times) throws Exception {
        final List<WebElement> allImages = selectVisibleElements(idDialogImages);
        final List<WebElement> allImageBadges = selectVisibleElements(xpathE2EEDialogImagesBadges);
        return times == (allImages.size() - allImageBadges.stream().filter(WebElement::isDisplayed).count());
    }
}
