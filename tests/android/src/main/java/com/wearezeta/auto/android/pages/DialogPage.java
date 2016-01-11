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
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DialogPage extends AndroidPage {

    private static final Logger log = ZetaLogger.getLog(DialogPage.class
            .getSimpleName());

    public static final String MEDIA_PLAY = "PLAY";
    public static final String MEDIA_PAUSE = "PAUSE";

    public static final String xpathConfirmOKButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";

    public static final String idDialogImages = "iv__row_conversation__message_image";
    private static final String xpathLastPicture = String.format(
            "(//*[@id='%s'])[last()]", idDialogImages);

    private static final String xpathEncryptedDialogImages = "//*[@id='" + idDialogImages +
            "']/parent::*/parent::/*//*[@id='v__row_conversation__e2ee']";
    private static final Function<Integer, String> xpathEncryptedDialogImageByIdx = idx ->
            String.format("(" + xpathEncryptedDialogImages + ")[%s]", idx);


    public static final String idAddPicture = "cursor_menu_item_camera";

    public static final String idSelfAvatar = "civ__cursor__self_avatar";
    @FindBy(id = idSelfAvatar)
    private WebElement selfAvatar;

    private static final Function<String, String> xpathConversationMessageByText = text -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']",
                    text);

    private static final Function<String, String> xpathEncryptedConversationMessageByText = text -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']/parent::*/parent::*" +
                    "//*[@id='v__row_conversation__e2ee']", text);

    private static final Function<String, String> xpathUnsentIndicatorByText = text -> String
            .format("%s/parent::*/parent::*//*[@id='v__row_conversation__error']",
                    xpathConversationMessageByText.apply(text));

    private static final String xpathUnsentIndicatorForImage = "//*[@id='"
            + idDialogImages
            + "']/parent::*/parent::*//*[@id='v__row_conversation__error']";

    @FindBy(id = giphyPreviewButtonId)
    private WebElement giphyPreviewButton;

    @FindBy(id = idEditText)
    private WebElement cursorInput;

    @FindBy(id = idCursorArea)
    private WebElement cursorArea;

    private static final String idCursorBtn = "typing_indicator_button";
    @FindBy(id = idCursorBtn)
    private WebElement cursorBtn;

    private static final String idCursorBtnImg = "typing_indicator_imageview";
    @FindBy(id = idCursorBtnImg)
    private WebElement cursorBtnImg;

    private static final String idMessage = "ltv__row_conversation__message";
    @FindBy(id = idMessage)
    private List<WebElement> messagesList;

    private static final String idMissedCallMesage = "ttv__row_conversation__missed_call";

    private static final Function<String, String> xpathMissedCallMesageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idMissedCallMesage, text);

    private static final String idCursorFrame = "cursor_layout";
    @FindBy(id = idCursorFrame)
    public WebElement cursorFrame;

    public static final Function<String, String> xpathPingMessageByText = text -> String
            .format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']",
                    text);

    private static final String idPingIcon = "gtv__knock_icon";
    @FindBy(id = idPingIcon)
    private WebElement pingIcon;

    private static final String xpathDialogTakePhotoButton = "//*[@id='gtv__camera_control__take_a_picture' and @shown='true']";
    @FindBy(xpath = xpathDialogTakePhotoButton)
    private WebElement takePhotoButton;

    private static final String idDialogChangeCameraButton = "gtv__camera__top_control__back_camera";
    @FindBy(id = idDialogChangeCameraButton)
    private WebElement changeCameraButton;

    @FindBy(xpath = xpathConfirmOKButton)
    private WebElement okButton;

    private static final String idSketchImagePaintButton = "gtv__sketch_image_paint_button";
    @FindBy(id = idSketchImagePaintButton)
    private WebElement sketchImagePaintButton;

    @FindBy(id = idDialogImages)
    private WebElement image;

    private static final String idFullScreenImage = "tiv__single_image_message__image";
    @FindBy(id = idFullScreenImage)
    private WebElement fullScreenImage;

    @FindBy(id = idDialogImages)
    private List<WebElement> imageList;

    private static final String idConnectRequestDialog = "connect_request_root";
    @FindBy(id = idConnectRequestDialog)
    private WebElement connectRequestDialog;

    public static final String idParticipantsBtn = "cursor_menu_item_participant";
    @FindBy(id = idParticipantsBtn)
    private WebElement participantsButton;

    private static final String idConnectRequestMessage = "contact_request_message";
    @FindBy(id = idConnectRequestMessage)
    private WebElement connectRequestMessage;

    private static final String idConnectRequestConnectTo = "user_name";
    @FindBy(id = idConnectRequestConnectTo)
    private WebElement connectRequestConnectTo;

    private static final String idBackgroundOverlay = "v_background_dark_overlay";
    @FindBy(id = idBackgroundOverlay)
    private WebElement backgroundOverlay;

    private static final String idStartChatLabel = "ttv__row_conversation__connect_request__chathead_footer__label";
    private static final Function<String, String> xpathStartChatLabelByPartOfText =
            text -> String.format("//*[@id='%s' and contains(@value, '%s')]", idStartChatLabel, text);

    private static final String idConnectRequestChatUserName = "ttv__row_conversation__connect_request__chathead_footer__username";
    @FindBy(id = idConnectRequestChatUserName)
    private WebElement connectRequestChatUserName;

    @FindBy(id = idGalleryBtn)
    private WebElement galleryBtn;

    @FindBy(id = idSearchHintClose)
    private WebElement closeHintBtn;

    @FindBy(id = idCloseImageBtn)
    private WebElement closeImageBtn;

    private static final String idPlayPauseMedia = "gtv__media_play";
    @FindBy(id = idPlayPauseMedia)
    private WebElement playPauseBtn;

    private static final String idYoutubePlayButton = "gtv__youtube_message__play";
    @FindBy(id = idYoutubePlayButton)
    private WebElement playYoutubeBtn;

    private static final String idMediaBarControl = "gtv__conversation_header__mediabar__control";
    @FindBy(id = idMediaBarControl)
    private WebElement mediaBarControl;

    @FindBy(id = idAddPicture)
    private WebElement addPictureBtn;

    private static final String idPing = "cursor_menu_item_ping";
    @FindBy(id = idPing)
    private WebElement pingBtn;

    private static final String idSketch = "cursor_menu_item_draw";
    @FindBy(id = idSketch)
    private WebElement sketchBtn;

    private static final String idCall = "cursor_menu_item_calling";
    @FindBy(id = idCall)
    private WebElement callBtn;

    public static final String idCursorCloseButton = "cursor_button_close";
    @FindBy(id = idCursorCloseButton)
    private WebElement closeBtn;

    private static final String idMute = "cib__calling__mic_mute";
    @FindBy(id = idMute)
    private WebElement muteBtn;

    private static final String idSpeaker = "cib__calling__speaker";
    @FindBy(id = idSpeaker)
    private WebElement speakerBtn;

    private static final String idCancelCall = "cib__calling__dismiss";
    @FindBy(id = idCancelCall)
    private WebElement cancelCallBtn;

    private static final String idNewConversationNameMessage = "ttv__row_conversation__new_conversation_name";

    private static Function<String, String> xpathNewConversationNameByValue = value -> String
            .format("//*[@id='%s' and @value='%s']",
                    idNewConversationNameMessage, value);

    private static final String xpathLastConversationMessage = "(//*[@id='ltv__row_conversation__message'])[last()]";
    @FindBy(xpath = xpathLastConversationMessage)
    private WebElement lastConversationMessage;

    private static final String idDialogRoot = "pfac__conversation__list_view_container";
    @FindBy(id = idDialogRoot)
    private WebElement dialogRoot;

    private static final String xpathDialogContent = "//*[@id='" + idDialogRoot
            + "']/*/*/*";
    @FindBy(xpath = xpathDialogContent)
    private List<WebElement> dialogContentList;

    private static final String idFullScreenImageImage = "tiv__single_image_message__animating_image";
    @FindBy(id = idFullScreenImageImage)
    private WebElement fullScreenImageImage;

    public static Function<String, String> xpathInputFieldByValue = value -> String
            .format("//*[@value='%s']", value);

    private static final String idSwitchCameraButton = "gtv__camera__top_control__back_camera";
    @FindBy(id = idSwitchCameraButton)
    private WebElement switchCameraButton;

    private static final int DEFAULT_SWIPE_TIME = 500;
    private static final int MAX_SWIPE_RETRIES = 5;
    private static final int MAX_CLICK_RETRIES = 5;

    public DialogPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCursorArea));
    }

    public boolean waitForCursorInputNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idCursorArea));
    }

    public void tapOnCursorInput() throws Exception {
        // FIXME: Scroll to the bottom if cursor input is not visible
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCursorBtnImg), 1)) {
            cursorFrame.click();
        }
        cursorArea.click();
    }

    public void pressPlusButtonOnDialogPage() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), cursorBtn);
        cursorBtn.click();
    }

    public void swipeRightOnCursorInput() throws Exception {
        // FIXME: Workaround for a bug in the app
        scrollToTheBottom();

        final By cursorLocator = By.id(idCursorArea);
        int ntry = 1;
        do {
            DriverUtils.swipeElementPointToPoint(this.getDriver(), cursorArea,
                    DEFAULT_SWIPE_TIME, 10, 50, 90, 50);
            final int currentCursorOffset = getDriver()
                    .findElement(cursorLocator).getLocation().getX();
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
        final By closeCursorBtn = By.id(DialogPage.idCursorCloseButton);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
                closeCursorBtn, 1)) {
            getDriver().findElement(closeCursorBtn).click();
            // Wait for animation
            Thread.sleep(500);
        } else {
            this.hideKeyboard();
            if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idCursorFrame), 2)) {
                throw new IllegalStateException("Cursor frame is not visible");
            }
        }
        cursorFrame.click();
        this.hideKeyboard();
    }

    public void tapAddPictureBtn() throws Exception {
        assert DriverUtils
                .waitUntilElementClickable(getDriver(), addPictureBtn);
        addPictureBtn.click();
    }

    public void tapPingBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), pingBtn);
        pingBtn.click();
    }

    public void tapSketchBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), sketchBtn);
        sketchBtn.click();
    }

    public void tapCallBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), callBtn);
        callBtn.click();
    }

    public void closeInputOptions() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), closeBtn);
        closeBtn.click();
    }

    public void tapMuteBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), muteBtn);
        muteBtn.click();
    }

    public void tapSpeakerBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), speakerBtn);
        speakerBtn.click();
    }

    public void tapCancelCallBtn() throws Exception {
        assert DriverUtils
                .waitUntilElementClickable(getDriver(), cancelCallBtn);
        cancelCallBtn.click();
    }

    private WebElement getButtonElementByName(String name) {
        final String uppercaseName = name.toUpperCase();
        switch (uppercaseName) {
            case "MUTE":
                return muteBtn;
            case "SPEAKER":
                return speakerBtn;
            default:
                throw new NoSuchElementException(String.format(
                        "Button '%s' is unknown", name));
        }
    }

    public BufferedImage getCurrentButtonStateScreenshot(String name)
            throws Exception {
        final WebElement dstButton = getButtonElementByName(name);
        assert DriverUtils.waitUntilElementClickable(getDriver(), dstButton);
        return getElementScreenshot(dstButton).orElseThrow(
                IllegalStateException::new);
    }

    public void typeAndSendMessage(String message) throws Exception {
        // FIXME: Find a better solution for text autocorrection issues
        final int maxTries = 5;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(message);
            ntry++;
        } while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathInputFieldByValue.apply(message)), 2)
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
        cursorInput.sendKeys(message);
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathInputFieldByValue.apply(message)), 2)) {
            log.warn(String
                    .format("The message '%s' was autocorrected. This might cause unpredicted test results",
                            message));
        }
    }

    public void clickLastImageFromDialog() throws Exception {
        final By locator = By.xpath(xpathLastPicture);
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "No pictures are visible in the conversation view";
        getDriver().findElement(locator).click();
    }

    public boolean waitForConversationNameChangedMessage(String expectedName)
            throws Exception {
        final By locator = By.xpath(xpathNewConversationNameByValue
                .apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForMessage(String text) throws Exception {
        scrollToTheBottom();
        final By locator = By.xpath(xpathConversationMessageByText.apply(text));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitForUnsentIndicator(String text) throws Exception {
        final By locator = By.xpath(xpathUnsentIndicatorByText.apply(text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isImageExists() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.id(idDialogImages));
    }

    public Optional<BufferedImage> getLastImageInFullScreen() throws Exception {
        return getElementScreenshot(fullScreenImageImage);
    }

    public void confirm() throws Exception {
        final By locator = By.xpath(xpathConfirmOKButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
        assert DriverUtils.waitUntilElementClickable(getDriver(), okButton);
        okButton.click();
        assert DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapSketchOnImageButton() throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idSketchImagePaintButton)) : "Draw sketch on image button is not visible";
        sketchImagePaintButton.click();
    }

    public void takePhoto() throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathDialogTakePhotoButton)) : "Take Photo button is not visible";
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                takePhotoButton) : "Take Photo button is not clickable";
        takePhotoButton.click();
        assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathDialogTakePhotoButton)) : "Take Photo button is still visible after being clicked";
    }

    public boolean waitUntilStartChatTitleContains(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStartChatLabelByPartOfText.apply(expectedText));
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
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idGalleryBtn)) : "Gallery button is still not visible";
        galleryBtn.click();
    }

    public void closeFullScreenImage() throws Exception {
        // Sometimes X button is opened automatically after some timeout
        final int MAX_TRIES = 4;
        int ntry = 1;
        while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCloseImageBtn), 4)
                && ntry <= MAX_TRIES) {
            this.tapOnCenterOfScreen();
            ntry++;
        }
        assert DriverUtils
                .waitUntilElementClickable(getDriver(), closeImageBtn);
        closeImageBtn.click();
    }

    public OtherUserPersonalInfoPage tapConversationDetailsButton()
            throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                participantsButton);
        participantsButton.click();
        final long millisecondsStarted = System.currentTimeMillis();
        final long maxAnimationDurationMillis = 2000;
        do {
            Thread.sleep(200);
        } while (participantsButton.getLocation().getY() > 0
                && System.currentTimeMillis() - millisecondsStarted < maxAnimationDurationMillis);
        assert participantsButton.getLocation().getY() < 0;
        return new OtherUserPersonalInfoPage(this.getLazyDriver());
    }

    public boolean waitForPingMessageWithText(String expectedText)
            throws Exception {
        final By locator = By.xpath(xpathPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForPingMessageWithTextDisappears(String expectedText)
            throws Exception {
        final By locator = By.xpath(xpathPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isGroupChatDialogContainsNames(List<String> names)
            throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathLastConversationMessage));
        final String convoText = lastConversationMessage.getText();
        for (String name : names) {
            if (!convoText.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean isDialogVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.id(idMessage));
    }

    private static final double MAX_BUTTON_STATE_OVERLAP = 0.5;

    public void tapPlayPauseBtn() throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idPlayPauseMedia));
        assert DriverUtils.waitUntilElementClickable(getDriver(), playPauseBtn);
        final BufferedImage initialState = getElementScreenshot(playPauseBtn)
                .orElseThrow(
                        () -> new AssertionError(
                                "Failed to get a screenshot of Play/Pause button"));
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idYoutubePlayButton));
    }

    public double getMediaBarControlIconOverlapScore(String label)
            throws Exception {
        String path = null;
        BufferedImage mediaImage = getElementScreenshot(mediaBarControl)
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
        return getElementScreenshot(playPauseBtn).orElseThrow(
                IllegalStateException::new);
    }

    public void tapPlayPauseMediaBarBtn() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                mediaBarControl);
        mediaBarControl.click();
    }

    private boolean waitUntilMediaBarVisible(int timeoutSeconds)
            throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idMediaBarControl), timeoutSeconds);
    }

    public boolean waitUntilMissedCallMessageIsVisible(String expectedMessage)
            throws Exception {
        final By locator = By.xpath(xpathMissedCallMesageByText
                .apply(expectedMessage));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public String getLastMessageFromDialog() {
        return lastConversationMessage.getText();
    }

    public Optional<BufferedImage> getRecentPictureScreenshot()
            throws Exception {
        return this.getElementScreenshot(image);
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot()
            throws Exception {
        return this.getElementScreenshot(fullScreenImage);
    }

    public boolean isImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.id(idDialogImages));
    }

    public boolean waitForAPictureWithUnsentIndicator() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathUnsentIndicatorForImage));
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

    public int getCurrentNumberOfItemsInDialog() {
        return dialogContentList.size();
    }

    public BufferedImage getConvoViewScreenshot() throws Exception {
        return this.getElementScreenshot(dialogRoot).orElseThrow(
                IllegalStateException::new);
    }

    /**
     * @return false if Take Photo button will not be visible after Switch Camera button is clicked
     * @throws Exception
     */
    public boolean tapSwitchCameraButton() throws Exception {
        switchCameraButton.click();
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathDialogTakePhotoButton));
    }

    public boolean waitForXEncryptedMessages(String msg, int times) throws Exception {
        final By locator = By.xpath(xpathEncryptedConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) &&
                getDriver().findElements(locator).size() == times;
    }

    public boolean waitForXNonEncryptedMessages(String msg, int times) throws Exception {
        final By locator = By.xpath(xpathConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) &&
                getDriver().findElements(locator).size() == times;
    }

    public boolean waitForXEncryptedImages(int times) throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idDialogImages)) :
                "No images are visible in the conversation view";
        return times == getDriver().findElementsByXPath(xpathEncryptedDialogImages).size();
    }

    public boolean waitForXNonEncryptedImages(int times) throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idDialogImages)) :
                "No images are visible in the conversation view";
        final List<WebElement> allImageBadges = getDriver().findElementsByXPath(xpathEncryptedDialogImages);
        final List<WebElement> allImages =  getDriver().findElementsById(idDialogImages);
        return times == (allImages.size() - allImageBadges.size());
    }
}
