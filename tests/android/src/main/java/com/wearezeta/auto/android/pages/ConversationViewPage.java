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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.interactions.touch.TouchActions;

public class ConversationViewPage extends AndroidPage {

    public static final By xpathConfirmOKButton = By.xpath("//*[@id='ttv__confirmation__confirm' and @value='OK']");

    public static final String idStrDialogImages = "iv__row_conversation__message_image";
    public static final By idDialogImages = By.id(idStrDialogImages);

    private static final By xpathLastPicture = By.xpath(String.format("(//*[@id='%s'])[last()]", idStrDialogImages));

    public static final By idCursorCamera = By.id("cursor_menu_item_camera");

    public static final By idCursorPing = By.id("cursor_menu_item_ping");

    private static final By idCursorMore = By.id("cursor_menu_item_more");

    public static final By idCursorVideoMessage = By.id("cursor_menu_item_video");

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

    public static final Function<String, String> xpathStrPingMessageByText = text -> String
            .format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']", text.toUpperCase());

    private static final By idFullScreenImage = By.id("tiv__single_image_message__image");

    public static final By idVerifiedConversationShield = By.id("cursor_button_giphy");

    private static final By idPlayPauseMedia = By.id("gtv__media_play");

    private static final By idYoutubePlayButton = By.id("gtv__youtube_message__play");

    private static final String strIdMediaToolbar = "tb__conversation_header__mediabar";

    private static final By idMediaBarPlayBtn = By.xpath(String.format("//*[@id='%s']/*[2]", strIdMediaToolbar));

    private static final By idMediaToolbar = By.id(strIdMediaToolbar);

    private static final By idCursorSketch = By.id("cursor_menu_item_draw");

    private static final By idAudioCall = By.id("action_audio_call");

    private static final By idVideoCall = By.id("action_video_call");

    private static final By idCursorFile = By.id("cursor_menu_item_file");

    private static final By idCursorAudioMessage = By.id("cursor_menu_item_audio_message");

    private static final By idAudioMessageRecordingSileControl = By.id("fl__audio_message__recording__slide_control");

    private static final By idAudioMessageSendButton = By.id("fl__audio_message__recording__send_button_container");

    private static final By idAudioMessagePlayButton = By.id("fl__audio_message__recording__bottom_button_container");

    private static final By idAudioMessageCancelButton = By.id("fl__audio_message__recording__cancel_button_container");

    private static final By xpathAudioMessageDurationText =
            By.xpath("//*[@id='ttv__audio_message__recording__duration' and not(text())]");

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

    private static final Function<String, String> xpathMessageNotificationByValue = value -> String
            .format("//*[starts-with(@id,'ttv_message_notification_chathead__label') and @value='%s']", value);

    private static final Function<String, String> xpathConversationTitleByValue = value -> String
            .format("//*[@id='tv__conversation_toolbar__title' and @value='%s']", value);

    private static final Function<String, String> xpathFileNamePlaceHolderByValue = value -> String
            .format("//*[@id='ttv__row_conversation__file__filename' and @value='%s']", value);

    private static final Function<String, String> xpathFileInfoPlaceHolderByValue = value -> String
            .format("//*[@id='ttv__row_conversation__file__fileinfo' and @value='%s']", value);

    private static final Function<String, String> xpathConversationPeopleChangedByExp = exp -> String
            .format("//*[@id='ttv__row_conversation__people_changed__text' and %s]", exp);

    private static final Function<String, String> xpathCursorHintByValue = value -> String
            .format("//*[@id='ctv__cursor' and @value='%s']", value);

    private static final By idActionModeBarDeleteButton = By.id("action_delete");
    private static final By idActionModeBarCopyButton = By.id("action_copy");
    private static final By idActionModeBarCloseButton = By.id("action_mode_close_button");

    private static final By idYoutubeContainer = By.id("fl__youtube_image_container");

    private static final By idSoundcloudContainer = By.id("mpv__row_conversation__message_media_player");

    private static final By idFileTransferContainer = By.id("ll__row_conversation__file__message_container");

    private static final By idVideoMessageContainer = By.id("fl__video_message_container");

    private static final By idVideoContainerButton = By.id("gpv__row_conversation__video_button");

    private static final By idAudioMessageContainer = By.id("tfll__audio_message_container");

    private static final By idAudioContainerButton = By.id("gpv__row_conversation__audio_button");

    private static final By idAudioContainerSeekbar = By.id("sb__audio_progress");

    private static final By idAudioMessagePreviewSeekbar = By.id("sb__voice_message__recording__seekbar");

    private static final int MAX_CLICK_RETRIES = 5;

    private static final double LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR = 0.01;

    private static final String FILE_UPLOADING_MESSAGE = "UPLOADING…";

    private static final String FILE_DOWNLOADING_MESSAGE = "DOWNLOADING…";

    private static final String FILE_UPLOAD_FAILED = "UPLOAD FAILED";

    private static final String FILE_MESSAGE_SEPARATOR = " · ";

    private static final int SCROLL_TO_BOTTOM_INTERVAL_MILLISECONDS = 1000;

    private static final int DEFAULT_SWIPE_DURATION = 2000;


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

    public BufferedImage getAudioMessageSeekbarState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioContainerSeekbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of seekbar within audio message container")
        );
    }

    public BufferedImage getAudioMessagePreviewSeekbarState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioMessagePreviewSeekbar)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of seekbar within audio message preview ")
        );
    }

    public BufferedImage getAudioMessagePreviewMicrophoneButtonState() throws Exception {
        return this.getElementScreenshot(getElement(idAudioMessagePlayButton)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Audio message recording slide microphone button")
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

    private By getCursorToolButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "ping":
                return idCursorPing;
            case "add picture":
                return idCursorCamera;
            case "sketch":
                return idCursorSketch;
            case "file":
                return idCursorFile;
            case "audio message":
                return idCursorAudioMessage;
            case "video message":
                return idCursorVideoMessage;
            default:
                throw new IllegalArgumentException(String.format("Unknown tool button name '%s'", name));
        }
    }

    public void tapCursorToolButton(String name) throws Exception {
        final By locator = getCursorToolButtonLocatorByName(name);
        final Optional<WebElement> btn = getElementIfDisplayed(locator, 3);
        if (btn.isPresent()) {
            btn.get().click();
        } else {
            getElement(idCursorMore).click();
            getElement(locator).click();
        }
    }

    public void longTapAudioMessageCursorBtn(int duration) throws Exception {
        getDriver().longTap(getElement(idCursorAudioMessage), duration);
    }

    public void longTapAudioMessageCursorBtnAndSwipeUp(int longTapDurationMilliseconds) throws Exception {
        longTapAndSwipe(getElement(idCursorAudioMessage), () -> getElement(idAudioMessageSendButton),
                DEFAULT_SWIPE_DURATION, longTapDurationMilliseconds);
    }

    public void longTapAudioMessageCursorBtnAndRememberIcon(int longTapDurationMilliseconds, ElementState elementState)
            throws Exception {
        longTapAndSwipe(getElement(idCursorAudioMessage), () -> getElement(idCursorAudioMessage),
                DEFAULT_SWIPE_DURATION, longTapDurationMilliseconds, Optional.of(elementState::remember));
    }

    public boolean isCursorHintVisible(String hintMessage) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathCursorHintByValue.apply(hintMessage)));
    }

    public boolean isAudioMessageRecordingSlideVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessageRecordingSileControl);
    }

    public boolean isAudioMessageSendButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessageSendButton);
    }

    public boolean isAudioMessagePlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessagePlayButton);
    }

    public boolean isAudioMessageCancelButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessageCancelButton);
    }

    public boolean isAudioMessageRecordingDurationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathAudioMessageDurationText);
    }

    public void tapAudioMessageSendButton() throws Exception {
        // Workaround cause click doesn't work, it seems need real touch
        getDriver().longTap(getElement(idAudioMessageSendButton), DriverUtils.SINGLE_TAP_DURATION);
    }

    public void tapAudioMessageCancelButton() throws Exception {
        getElement(idAudioMessageCancelButton).click();
    }

    public void tapAudioMessagePlayButton() throws Exception {
        getElement(idAudioMessagePlayButton).click();
    }

    //endregion

    /**
     * It based on the cursor action , scroll to the bottom of view when you tap on input text field and focus on it
     *
     * @throws Exception
     */
    public void scrollToTheBottom() throws Exception {
        tapOnTextInput();
        this.hideKeyboard();
        swipeByCoordinates(SCROLL_TO_BOTTOM_INTERVAL_MILLISECONDS, 50, 75, 50, 40);
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

    public void tapRecentImage() throws Exception {
        getElement(xpathLastPicture).click();
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

    /**
     * Navigates back by swipe and initialize ConversationsListPage
     *
     * @throws Exception
     */
    public void navigateBack(int timeMilliseconds) throws Exception {
        swipeRightCoordinates(timeMilliseconds);
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
        getElement(idMediaBarPlayBtn, "Media bar PlayPause button is not visible").click();
    }

    private boolean waitUntilMediaBarVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMediaToolbar, timeoutSeconds);
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
        return isElementABelowElementB(getElement(idMediaToolbar), getElement(xpathToolbar),
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

    public boolean isMessageInvisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapDeleteActionModeBarButton() throws Exception {
        getElement(idActionModeBarDeleteButton).click();
    }

    public void tapCopyTopActionModeBarButton() throws Exception {
        getElement(idActionModeBarCopyButton).click();
    }

    public void tapCloseTopActionModeBarButton() throws Exception {
        getElement(idActionModeBarCloseButton).click();
    }

    public boolean isDeleteActionModeBarButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idActionModeBarDeleteButton);
    }

    public boolean isDeleteActionModeBarButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idActionModeBarDeleteButton);
    }

    public boolean isCopyActionModeBarButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idActionModeBarCopyButton);
    }

    public boolean isCopyActionModeBarButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idActionModeBarCopyButton);
    }

    public boolean isCloseActionModeBarButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idActionModeBarCloseButton);
    }

    public boolean isCloseActionModeBarButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idActionModeBarCloseButton);
    }

    public void longTapMessage(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        getDriver().longTap(getElement(locator), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapMessage(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        getElement(locator).click();
    }

    public boolean isYoutubeContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idYoutubeContainer);
    }

    public boolean isYoutubeContainerInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idYoutubeContainer);
    }

    public void tapYoutubeContainer() throws Exception {
        getElement(idYoutubeContainer).click();
    }

    public void longTapYoutubeContainer() throws Exception {
        getDriver().longTap(getElement(idYoutubeContainer), DriverUtils.LONG_TAP_DURATION);
    }

    public boolean isSoundcloudContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idSoundcloudContainer);
    }

    public boolean isSoundcloudContainerInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idSoundcloudContainer);
    }

    public void tapSoundcloudContainer() throws Exception {
        getElement(idSoundcloudContainer).click();
    }

    public void longTapSoundcloudContainer() throws Exception {
        getDriver().longTap(getElement(idSoundcloudContainer), DriverUtils.LONG_TAP_DURATION);
    }

    public boolean isFileUploadContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idFileTransferContainer);
    }

    public boolean isFileUploadContainerInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idFileTransferContainer);
    }

    public void tapFileUploadContainer() throws Exception {
        getElement(idFileTransferContainer).click();
    }

    public void longTapFileUploadContainer() throws Exception {
        getDriver().longTap(getElement(idFileTransferContainer), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapPingMessage(String message) throws Exception {
        getElement(By.xpath(xpathStrPingMessageByText.apply(message))).click();
    }

    public void longTapPingMessage(String message) throws Exception {
        getDriver().longTap(getElement(By.xpath(xpathStrPingMessageByText.apply(message))),
                DriverUtils.LONG_TAP_DURATION);
    }

    public void longTapRecentImage() throws Exception {
        getDriver().longTap(getElement(xpathLastPicture), DriverUtils.LONG_TAP_DURATION);
    }

    public boolean isVideoMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoMessageContainer);
    }

    public boolean isVideoMessageNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idVideoMessageContainer);
    }

    public boolean isAudioMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idAudioMessageContainer);
    }

    public boolean isAudioMessageNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idAudioMessageContainer);
    }

    public void tapVideoMessageButton() throws Exception {
        getElement(idVideoContainerButton).click();
    }

    public void tapAudioMessageButton() throws Exception {
        getElement(idAudioContainerButton).click();
    }

    public void tapVideoMessageContainer() throws Exception {
        getElement(idVideoMessageContainer).click();
    }

    public void longVideoMessageContainer() throws Exception {
        getDriver().longTap(getElement(idVideoMessageContainer), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapAudioMessageContainer() throws Exception {
        getElement(idAudioMessageContainer).click();
    }

    public void longAudioMessageContainer() throws Exception {
        WebElement el = getElement(idAudioMessageContainer);
        final Point location = el.getLocation();
        final Dimension size = el.getSize();
        getDriver().longTap(location.x + size.width / 2, location.y + size.height / 5, DriverUtils.LONG_TAP_DURATION);
    }

    public boolean isVideoMessageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoContainerButton);
    }

    public Optional<BufferedImage> getVideoContainerButtonState() throws Exception {
        return getElementScreenshot(getElement(idVideoContainerButton));
    }

    public Optional<BufferedImage> getAudioContainerButtonState() throws Exception {
        return getElementScreenshot(getElement(idAudioContainerButton));
    }

    public void longTapAndKeepAudioMessageCursorBtn() throws Exception {
        final WebElement audioMsgButton = getElement(idCursorAudioMessage);
        final int x = audioMsgButton.getLocation().getX() + audioMsgButton.getSize().getWidth() / 2;
        final int y = audioMsgButton.getLocation().getY() + audioMsgButton.getSize().getHeight() / 2;
        new TouchActions(getDriver()).down(x, y).perform();
    }

    public boolean isCursorToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCursorMore);
    }

    public boolean isCursorToolbarInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCursorMore);
    }
}
