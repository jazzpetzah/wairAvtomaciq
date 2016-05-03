package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.misc.ElementState;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConversationViewPage extends AndroidPage {

    public static final By xpathConfirmOKButton = By.xpath("//*[@id='ttv__confirmation__confirm' and @value='OK']");

    public static final String idStrDialogImages = "iv__row_conversation__message_image";
    public static final By idDialogImages = By.id(idStrDialogImages);

    private static final By xpathLastPicture = By.xpath(String.format("(//*[@id='%s'])[last()]", idStrDialogImages));

    public static final By idCursorCamera = By.id("cursor_menu_item_camera");

    public static final By idCursorPing = By.id("cursor_menu_item_ping");

    public static final By idCursorView = By.id("cal__cursor");

    public static final By idCursorSelfAvatar = By.id("civ__cursor__self_avatar");

    public static final String CURSOR_EDIT_TOOLTIP = "TYPE A MESSAGE";

    public static final By xpathCursorEditHint = By.xpath(
            String.format("//*[@id='ttv__cursor_hint' and contains(@value, '%s')]", CURSOR_EDIT_TOOLTIP));

    private static final Function<String, String> xpathStrConversationMessageByText = text -> String
            .format("//*[@id='ltv__row_conversation__message' and @value='%s']", text);

    private static final Function<String, String> xpathStrUnsentIndicatorByText = text -> String
            .format("%s/parent::*/parent::*//*[@id='v__row_conversation__error']",
                    xpathStrConversationMessageByText.apply(text));

    private static final By xpathUnsentIndicatorForImage = By
            .xpath("//*[@id='" + idStrDialogImages + "']/parent::*/parent::*//*[@id='v__row_conversation__error']");

    public static final String idStrCursorEditText = "cet__cursor";

    public static final By idCursorEditText = By.id(idStrCursorEditText);

    public static Function<String, String> xpathCurosrEditTextByValue = value ->
            String.format("//*[@id='%s' and @value='%s']", idStrCursorEditText, value);

    private static final String idStrMissedCallMesage = "ttv__row_conversation__missed_call";
    private static final Function<String, String> xpathStrMissedCallMesageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrMissedCallMesage, text.toUpperCase());

    private static final By idCursorFrame = By.id("cursor_layout");

    public static final Function<String, String> xpathStrPingMessageByText = text -> String
            .format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']", text);

    private static final By xpathDialogTakePhotoButton = By
            .xpath("//*[@id='gtv__camera_control__take_a_picture' and @shown='true']");

    private static final By idSketchImagePaintButton = By.id("gtv__sketch_image_paint_button");

    private static final By idFullScreenImage = By.id("tiv__single_image_message__image");

    public static final By idVerifiedConversationShield = By.id("cursor_button_giphy");

    private static final By idPlayPauseMedia = By.id("gtv__media_play");

    private static final By idYoutubePlayButton = By.id("gtv__youtube_message__play");

    private static final String strIdMediaBarControl = "gtv__conversation_header__mediabar__control";

    private static final By idMediaBarControl = By.id(strIdMediaBarControl);

    private static final By xpathMediaBar = By.xpath(String.format("//*[@id='%s']/parent::*", strIdMediaBarControl));

    private static final By idCursorSketch = By.id("cursor_menu_item_draw");

    private static final By idAudioCall = By.id("action_audio_call");

    private static final By idVideoCall = By.id("action_video_call");

    private static final By idCursorFile = By.id("cursor_menu_item_file");

    private static final By idFileActionBtn = By.id("gtv__row_conversation__file__action");

    private static final By idFileDialogActionOpenBtn = By.id("ttv__file_action_dialog__open");

    private static final By idFileDialogActionSaveBtn = By.id("ttv__file_action_dialog__save");

    private static final String xpathStrConversationToolbar = "//*[@id='t_conversation_toolbar']";

    private static final By xpathToolbar = By.xpath(xpathStrConversationToolbar);

    private static final By xpathToolBarNavigation =
            By.xpath(String.format("%s/*[@value='' and count(*)=1]", xpathStrConversationToolbar));

    public static final By idCursorCloseButton = By.id("cursor_button_close");

    private static final String idStrNewConversationNameMessage = "ttv__row_conversation__new_conversation_name";

    private static Function<String, String> xpathStrNewConversationNameByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", idStrNewConversationNameMessage, value);

    private static final By xpathStrOtrVerifiedMessage = By
            .xpath("//*[@id='ttv__otr_added_new_device__message' and @value='ALL FINGERPRINTS ARE VERIFIED']");

    private static final By xpathStrOtrNonVerifiedMessage = By
            .xpath("//*[@id='ttv__otr_added_new_device__message' and contains(@value,'STARTED USING A NEW DEVICE')]");

    private static final Function<String, String> xpathStrOtrNonVerifiedMessageByValue = value -> String.format(
            "//*[@id='ttv__otr_added_new_device__message' and @value='%s STARTED USING A NEW DEVICE']", value
                    .toUpperCase());

    private static final By xpathLastConversationMessage = By.xpath("(//*[@id='ltv__row_conversation__message'])[last" +
            "()]");

    public static final String idStrDialogRoot = "clv__conversation_list_view";
    public static final By idDialogRoot = By.id(idStrDialogRoot);
    private static final By xpathDialogContent = By.xpath("//*[@id='" + idStrDialogRoot + "']/*/*/*");

    private static final By idSwitchCameraButton = By.id("gtv__camera__top_control__back_camera");

    private static Function<String, String> xpathMessageNotificationByValue = value -> String
            .format("//*[starts-with(@id,'ttv_message_notification_chathead__label') and @value='%s']", value);

    private static Function<String, String> xpathConversationTitleByValue = value -> String
            .format("//*[@id='tv__conversation_toolbar__title' and @value='%s']", value);

    private static Function<String, String> xpathFileNamePlaceHolderByValue = value -> String
            .format("//*[@id='ttv__row_conversation__file__filename' and @value='%s']", value);

    private static Function<String, String> xpathFileInfoPlaceHolderByValue = value -> String
            .format("//*[@id='ttv__row_conversation__file__fileinfo' and @value='%s']", value);

    private static Function<String, String> xpathConversationPeopleChangedByExp = exp -> String
            .format("//*[@id='ttv__row_conversation__people_changed__text' and %s]", exp);

    private static final int MAX_CLICK_RETRIES = 5;

    private static final double LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR = 0.01;

    private static final String FILE_UPLOADING_MESSAGE = "UPLOADING…";

    private static final String FILE_DOWNLOADING_MESSAGE = "DOWNLOADING…";

    private static final String FILE_UPLOAD_FAILED = "UPLOAD FAILED";

    private static final String FILE_MESSAGE_SEPARATOR = " · ";

    private static final int SCROLL_TO_BOTTOM_TIMEOUT_SECONDS = 60;

    private static final int SCROLL_TO_BOTTOM_INTERVAL_MILLISECONDS =1000;

    private static final double SCROLL_TO_BOTTOM_MIN_SIMILARITY_SCORE = 0.97;

    public ConversationViewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public BufferedImage getConvoViewStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idDialogRoot)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of conversation view")
        );
    }


    public BufferedImage getShieldStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idVerifiedConversationShield)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of verification shield")
        );
    }

    public BufferedImage getMediaButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idPlayPauseMedia)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Play/Pause button")
        );
    }

    public BufferedImage getTopToolbarState() throws Exception {
        return this.getElementScreenshot(getElement(xpathToolbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of upper toolbar")
        );
    }

    public BufferedImage getFilePlaceholderActionButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idFileActionBtn)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of file place holder action button")
        );
    }

    //region Cursor
    public boolean isCursorViewVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorView);
    }

    public boolean isTextInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorEditText);
    }

    public boolean isTextInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorEditText);
    }

    public boolean isTooltipOfTextInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathCursorEditHint);
    }

    public boolean isTooltipOfTextInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCursorEditHint);
    }

    public boolean isSelfAvatarOnTextInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorSelfAvatar);
    }

    public void tapOnTextInput() throws Exception {
        getElement(idCursorEditText).click();
    }

    public void typeAndSendMessage(String message, boolean hideKeyboard) throws Exception {
        final WebElement cursorInput = getElement(idCursorEditText);
        final int maxTries = 5;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(message);
            ntry++;
        }
        while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCurosrEditTextByValue.apply
                (message)), 2)
                && ntry < maxTries);
        if (ntry >= maxTries) {
            throw new IllegalStateException(String.format(
                    "The string '%s' was autocorrected. Please disable autocorrection on the device and restart the " +
                            "test.",
                    message));
        }
        pressKeyboardSendButton();
        if (hideKeyboard) {
            this.hideKeyboard();
        }
    }

    public void typeMessage(String message) throws Exception {
        getElement(idCursorEditText).sendKeys(message);
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCurosrEditTextByValue.apply(message)),
                2)) {
            log.warn(String.format("The message '%s' was autocorrected. This might cause unpredicted test results",
                    message));
        }
    }

    public void tapAddPictureBtn() throws Exception {
        getElement(idCursorCamera, "Add picture button is not visible").click();
    }

    public void tapPingBtn() throws Exception {
        getElement(idCursorPing, "Ping button is not visible").click();
    }

    public void tapSketchBtn() throws Exception {
        getElement(idCursorSketch, "Sketch button is not visible").click();
    }

    public void tapFileBtn() throws Exception {
        getElement(idCursorFile, "File button is not visible").click();
        //wait for 2 seconds for animation
        Thread.sleep(2000);
    }

    public boolean isPingButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idCursorPing);
    }

    public boolean isSketchButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idCursorSketch);
    }

    public boolean isAddPictureButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idCursorCamera);
    }

    public boolean isFileButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idCursorFile);
    }

    public boolean isPingButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idCursorPing);
    }

    public boolean isSketchButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idCursorSketch);
    }

    public boolean isAddPictureButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idCursorCamera);
    }

    public boolean isFileButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idCursorFile);
    }

    //endregion

    public void scrollToTheBottom() throws Exception {
        this.hideKeyboard();
        final long millisecondsStarted = System.currentTimeMillis();
        ElementState initState = new ElementState(
                () -> getConvoViewStateScreenshot());
        do {
            initState.remember();
            swipeByCoordinates(SCROLL_TO_BOTTOM_INTERVAL_MILLISECONDS, 50, 75, 50, 40);
        } while (System.currentTimeMillis() - millisecondsStarted <= SCROLL_TO_BOTTOM_TIMEOUT_SECONDS * 1000
                && initState.isChanged(1, SCROLL_TO_BOTTOM_MIN_SIMILARITY_SCORE));

        if (System.currentTimeMillis() - millisecondsStarted > SCROLL_TO_BOTTOM_TIMEOUT_SECONDS * 1000) {
            throw new IllegalStateException(String.format("Cannot scroll to the conversation bottom in %d seconds",
                    SCROLL_TO_BOTTOM_TIMEOUT_SECONDS));
        }
    }

    public void tapAudioCallBtn() throws Exception {
        getElement(idAudioCall, "Audio Call button is not visible").click();
    }

    public void tapVideoCallBtn() throws Exception {
        getElement(idVideoCall, "Video Call button is not visible").click();
    }

    public void tapTopToolbarTitle() throws Exception {
        getElement(xpathToolbar, "Top toolbar title is not visible").click();
    }

    public void tapTopToolbarBackButton() throws Exception {
        getElement(xpathToolBarNavigation, "Top toolbar back button is not visible").click();
    }

    public void closeInputOptions() throws Exception {
        getElement(idCursorCloseButton, "Close cursor button is not visible").click();
    }

    public void clickLastImageFromDialog() throws Exception {
        final WebElement lastPicture = getElement(xpathLastPicture);
        lastPicture.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathLastPicture, 3)) {
            try {
                lastPicture.click();
            } catch (WebDriverException e) {
                // silently ignore
            }
        }
    }

    public boolean waitForConversationNameChangedMessage(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrNewConversationNameByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForOtrVerifiedMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathStrOtrVerifiedMessage);
    }

    public boolean waitForOtrNonVerifiedMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathStrOtrNonVerifiedMessage);
    }

    public boolean waitForOtrNonVerifiedMessageCausedByUser(String userName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrOtrNonVerifiedMessageByValue.apply(userName)));
    }

    public boolean waitForMessage(String text) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(text));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitForPeopleMessage(String text) throws Exception {
        final By locator = By.xpath(xpathConversationPeopleChangedByExp.apply(String.format("contains(@value, '%s')",
                text.toUpperCase())));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitForXMessages(String msg, int times) throws Exception {
        By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        if (times > 0) {
            DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
        }
        return getElements(locator).stream().collect(Collectors.toList()).size() == times;
    }

    public boolean waitForUnsentIndicatorVisible(String text) throws Exception {
        final By locator = By.xpath(xpathStrUnsentIndicatorByText.apply(text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isImageExists() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), idDialogImages);
    }

    public boolean isConversationTitleVisible(String conversationTitle) throws Exception {
        final By locator = By.xpath(xpathConversationTitleByValue.apply(conversationTitle));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
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

    /**
     * Navigates back by swipe and initialize ConversationsListPage
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

    public boolean waitForPingMessageWithText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForPingMessageWithTextDisappears(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrPingMessageByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isConversationPeopleChangedMessageContainsNames(List<String> names) throws Exception {
        final String xpathExpr = String.join(" and ", names.stream().map(
                name -> String.format("contains(@value, '%s')", name.toUpperCase())
        ).collect(Collectors.toList()));
        final By locator = By.xpath(xpathConversationPeopleChangedByExp.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isTopToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathToolbar);
    }

    public boolean isAudioCallIconInToptoolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioCall);
    }

    public boolean isAudioCallIconInToptoolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idAudioCall);
    }

    public boolean isVideoCallIconInToptoolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idVideoCall);
    }

    public boolean isVideoCallIconInToptoolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idVideoCall);
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
                .orElseThrow(() -> new IllegalStateException("Failed to get a screenshot of Play/Pause button"));
        playPauseBtn.click();
        Thread.sleep(2000);
        int clickTry = 1;
        do {
            final BufferedImage currentState = getElementScreenshot(playPauseBtn)
                    .orElseThrow(() -> new AssertionError("Failed to get a screenshot of Play/Pause button"));
            final double overlapScore = ImageUtil.getOverlapScore(currentState, initialState, ImageUtil
                    .RESIZE_TO_MAX_SCORE);
            if (overlapScore < MAX_BUTTON_STATE_OVERLAP) {
                return;
            } else {
                playPauseBtn.click();
                Thread.sleep(2000);
            }
            clickTry++;
        } while (clickTry <= MAX_CLICK_RETRIES);
        assert (clickTry > MAX_CLICK_RETRIES) : "Media playback state has not been changed after " + MAX_CLICK_RETRIES
                + " retries";
    }

    public boolean waitUntilYoutubePlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idYoutubePlayButton);
    }

    public void tapPlayPauseMediaBarBtn() throws Exception {
        getElement(idMediaBarControl, "Media barr PlayPause button is not visible").click();
    }

    private boolean waitUntilMediaBarVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMediaBarControl, timeoutSeconds);
    }

    public boolean waitUntilMissedCallMessageIsVisible(String expectedMessage) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallMesageByText.apply(expectedMessage));
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
                } else {
                    Thread.sleep(500);
                }
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        return false;
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idDialogImages));
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idFullScreenImage));
    }

    public boolean isImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idDialogImages);
    }

    public boolean waitForAPictureWithUnsentIndicator() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathUnsentIndicatorForImage);
    }

    public boolean scrollUpUntilMediaBarVisible(final int maxScrollRetries) throws Exception {
        int swipeNum = 1;
        while (swipeNum <= maxScrollRetries) {
            swipeByCoordinates(1000, 50, 30, 50, 90);
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

    /**
     * @return false if Take Photo button is not visible after Switch Camera button is clicked
     * @throws Exception
     */
    public boolean tapSwitchCameraButton() throws Exception {
        getElement(idSwitchCameraButton).click();
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathDialogTakePhotoButton);
    }

    private static final long IMAGES_VISIBILITY_TIMEOUT = 10000; // seconds;

    public boolean waitForXImages(int expectedCount) throws Exception {
        assert expectedCount >= 0;
        final Optional<WebElement> imgElement = getElementIfDisplayed(idDialogImages);
        if (expectedCount <= 1) {
            return (expectedCount == 0 && !imgElement.isPresent()) || (expectedCount == 1 && imgElement.isPresent());
        }
        final long msStarted = System.currentTimeMillis();
        do {
            int actualCnt = getElements(idDialogImages).size();
            if (actualCnt >= expectedCount) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - msStarted <= IMAGES_VISIBILITY_TIMEOUT);
        return false;
    }

    public boolean waitForUnsentIndicator(String text) throws Exception {
        final By locator = By.xpath(xpathStrUnsentIndicatorByText.apply(text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitForMessageNotification(String message) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathMessageNotificationByValue.apply(message)));
    }

    public void tapMessageNotification(String message) throws Exception {
        getElement(By.xpath(xpathMessageNotificationByValue.apply(message))).click();
    }

    public boolean isMediaBarBelowUptoolbar() throws Exception {
        return isElementABelowElementB(getElement(xpathMediaBar), getElement(xpathToolbar),
                LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR);
    }

    public void waitUntilFileUploadIsCompleted(int timeoutSeconds, String size, String extension) throws Exception {
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                size + FILE_MESSAGE_SEPARATOR + extension.toUpperCase();
        fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR, FILE_UPLOADING_MESSAGE);
        DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeoutSeconds);
    }

    public boolean isFilePlaceHolderVisible(String fileFullName, String size, String extension,
                                            boolean isUpload, boolean isSuccess, int timeout) throws Exception {
        size = size.toUpperCase();
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                String.format("%s%s%s", size, FILE_MESSAGE_SEPARATOR, extension.toUpperCase());

        if (!isSuccess) {
            fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR,
                    isUpload ? FILE_UPLOAD_FAILED : FILE_DOWNLOADING_MESSAGE);
        }

        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathFileNamePlaceHolderByValue.apply(fileFullName)), timeout) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                        By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeout);
    }

    public boolean isFilePlaceHolderInvisible(String fileFullName, String size, String extension,
                                              boolean isUpload, boolean isSuccess, int timeout) throws Exception {
        size = size.toUpperCase();
        String fileInfo = StringUtils.isEmpty(extension) ? size :
                String.format("%s%s%s", size, FILE_MESSAGE_SEPARATOR, extension.toUpperCase());

        if (!isSuccess) {
            fileInfo = String.format("%s%s%s", fileInfo, FILE_MESSAGE_SEPARATOR,
                    isUpload ? FILE_UPLOAD_FAILED : FILE_DOWNLOADING_MESSAGE);
        }

        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathFileNamePlaceHolderByValue.apply(fileFullName)), timeout) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(),
                        By.xpath(xpathFileInfoPlaceHolderByValue.apply(fileInfo)), timeout);
    }

    public void tapFileActionButton() throws Exception {
        getElement(idFileActionBtn).click();
    }

    public void tapFileDialogActionButton(String action) throws Exception {
        switch (action) {
            case "save":
                getElement(idFileDialogActionSaveBtn).click();
                break;
            case "open":
                getElement(idFileDialogActionOpenBtn).click();
                break;
            default:
                throw new Exception(String.format("Cannot identify the action '%s' in File dialog", action));

        }
    }

}
