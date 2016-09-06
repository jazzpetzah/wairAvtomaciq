package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
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
    private static final By idClickedImageSendingIndicator = By.id("v__row_conversation__pending");

    //region Conversation Row Locators
    // Text
    private static final String idStrRowConversationMessage = "tmltv__row_conversation__message";
    private static final Function<String, String> xpathStrConversationMessageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrRowConversationMessage, text);
    private static final Function<String, String> xpathStrUnsentIndicatorByText = text -> String
            .format("%s/parent::*/parent::*//*[@id='v__row_conversation__error']",
                    xpathStrConversationMessageByText.apply(text));
    private static final By xpathLastConversationMessage =
            By.xpath(String.format("(//*[@id='%s'])[last()]", idStrRowConversationMessage));
    private static final By xpathFirstConversationMessage =
            By.xpath(String.format("(//*[@id='%s'])[1]", idStrRowConversationMessage));

    // Image
    private static final String idStrConversationImages = "fl__row_conversation__message_image_container";
    public static final By idConversationImages = By.id(idStrConversationImages);
    private static final String xpathStrLastImage = String.format("(//*[@id='%s'])[last()]", idStrConversationImages);
    private static final By xpathLastImage = By.xpath(xpathStrLastImage);
    private static final By xpathUnsentIndicatorForImage = By
            .xpath("//*[@id='" + idStrConversationImages + "']/parent::*/parent::*//*[@id='v__row_conversation__error']");

    // System message
    private static final String idStrMissedCallMesage = "ttv__row_conversation__missed_call";
    private static final Function<String, String> xpathStrMissedCallMesageByText = text -> String
            .format("//*[@id='%s' and @value='%s']", idStrMissedCallMesage, text.toUpperCase());

    // Ping
    public static final Function<String, String> xpathStrPingMessageByText = text -> String
            .format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']", text.toUpperCase());

    //endregion

    //region Conversation Cursor locators
    public static final String idStrCursorEditText = "cet__cursor";
    private static final By idCursorCamera = By.id("cursor_menu_item_camera");
    private static final By idCursorPing = By.id("cursor_menu_item_ping");
    private static final By idCursorMore = By.id("cursor_menu_item_more");
    private static final By idCursorLess = By.id("cursor_menu_item_less");
    private static final By idCursorVideoMessage = By.id("cursor_menu_item_video");
    private static final By idCursorShareLocation = By.id("cursor_menu_item_location");
    private static final By idCursorView = By.id("cal__cursor");
    private static final By idCursorSelfAvatar = By.id("civ__cursor__self_avatar");
    private static final String CURSOR_EDIT_TOOLTIP = "TYPE A MESSAGE";
    private static final By xpathCursorEditHint = By.xpath(
            String.format("//*[@id='ttv__cursor_hint' and contains(@value, '%s')]", CURSOR_EDIT_TOOLTIP));
    public static final By idCursorEditText = By.id(idStrCursorEditText);
    public static Function<String, String> xpathCurosrEditTextByValue = value ->
            String.format("//*[@id='%s' and @value='%s']", idStrCursorEditText, value);
    //endregion

    //region Message footer
    private static final FunctionalInterfaces.FunctionFor2Parameters<String, String, String> xpathStrTemplateIdValue
            = (id, value) -> String.format("//*[@id='%s' and contains(@value,'%s')]", id, value);

    private static final String strIdMessageMetaLikeButton = "gtv__footer__like__button";
    private static final String strIdMessageMetaLikeHint = "gtv__footer__like__hint_arrow";
    private static final String strIdMessageMetaLikeDescription = "tv__footer__like__description";
    private static final String strIdMessageMetaStatus = "tv__footer__message_status";
    private static final String strIdMessageMetaFirstLike = "cv__first_like_chathead";
    private static final String strIdMessageMetaSecondLike = "cv__first_like_chathead";
    //endregion

    //region Message Bottom Menu
    private static final By idMessageBottomMenuForwardButton = By.id("message_bottom_menu_item_forward");
    private static final By idMessageBottomMenuDeleteLocalButton = By.id("message_bottom_menu_item_delete_local");
    private static final By idMessageBottomMenuDeleteGlobalButton = By.id("message_bottom_menu_item_delete_global");
    private static final By idMessageBottomMenuCopyButton = By.id("message_bottom_menu_item_copy");
    private static final By idMessageBottomMenuEditButton = By.id("message_bottom_menu_item_edit");
    private static final By idMessageBottomMenuLikeButton = By.id("message_bottom_menu_item_like");
    private static final By idMessageBottomMenuUnlikeButton = By.id("message_bottom_menu_item_unlike");
    //endregion

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

    private static final By idAudioRecordingSendButton = By.id("fl__audio_message__recording__send_button_container");

    private static final By idAudioRecordingPlayButton = By.id("fl__audio_message__recording__bottom_button_container");

    private static final By idAudioRecordingCancelButton = By.id("fl__audio_message__recording__cancel_button_container");

    private static final By xpathAudioMessageDurationText =
            By.xpath("//*[@id='ttv__audio_message__recording__duration' and not(text())]");

    private static final By idFileActionBtn = By.id("aab__row_conversation__action_button");

    private static final By idFileDialogActionOpenBtn = By.id("ttv__file_action_dialog__open");

    private static final By idFileDialogActionSaveBtn = By.id("ttv__file_action_dialog__save");

    private static final String xpathStrConversationToolbar = "//*[@id='t_conversation_toolbar']";

    private static final By xpathToolbar = By.xpath(xpathStrConversationToolbar);

    private static final By idResendButton = By.id("fl__row_conversation__message_error_container");

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

    public static final String idStrConversationRoot = "clv__conversation_list_view";
    public static final By idConversationRoot = By.id(idStrConversationRoot);
    private static final By xpathConversationContent = By.xpath("//*[@id='" + idStrConversationRoot + "']/*/*/*");

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

    private static final Function<String, String> xpathLinkPreviewUrlByValue = value -> String
            .format("//*[@id='ttv__row_conversation__link_preview__url' and @value='%s']", value);

    private static final String idStrSeparatorName = "ttv__row_conversation__separator__name";

    private static final Function<String, String> xpathTrashcanByName = name -> String
            .format("//*[@id='%s' and @value='%s']/following-sibling::*[@id='gtv__message_recalled']",
                    idStrSeparatorName, name.toLowerCase());

    private static final Function<String, String> xpathPenByName = name -> String
            .format("//*[@id='%s' and @value='%s']/following-sibling::*[@id='gtv__message_edited']",
                    idStrSeparatorName, name.toLowerCase());

    private static final Function<String, String> xpathMessageSeparator = name -> String
            .format("//*[@id='%s' and @value='%s']", idStrSeparatorName, name.toLowerCase());

    private static final By idYoutubeContainer = By.id("iv__row_conversation__youtube_image");

    private static final By idSoundcloudContainer = By.id("mpv__row_conversation__message_media_player");

    private static final By idFileTransferContainer = By.id("ll__row_conversation__file__message_container");

    private static final By idVideoMessageContainer = By.id("fl__video_message_container");

    private static final By idVideoContainerButton = By.id("gpv__row_conversation__video_button");

    private static final By idAudioMessageContainer = By.id("tfll__audio_message_container");

    private static final By idAudioContainerButton = By.id("aab__row_conversation__audio_button");

    private static final By idShareLocationContainer = By.id("fl__row_conversation__map_image_container");

    private static final By idLinkPreviewContainer = By.id("cv__row_conversation__link_preview__container");

    private static final By idAudioContainerSeekbar = By.id("sb__audio_progress");

    private static final By idAudioMessagePreviewSeekbar = By.id("sb__voice_message__recording__seekbar");

    private static final By idImageContainerSketchButton = By.id("gtv__row_conversation__image_sketch");

    private static final By idImageContainerFullScreenButton = By.id("gtv__row_conversation__image_fullscreen");

    private static final int MAX_CLICK_RETRIES = 5;

    private static final double LOCATION_DIFFERENCE_BETWEEN_TOP_TOOLBAR_AND_MEDIA_BAR = 0.01;

    private static final String FILE_UPLOADING_MESSAGE = "UPLOADING…";

    private static final String FILE_DOWNLOADING_MESSAGE = "DOWNLOADING…";

    private static final String FILE_UPLOAD_FAILED = "UPLOAD FAILED";

    private static final String FILE_MESSAGE_SEPARATOR = " · ";

    private static final int SCROLL_TO_BOTTOM_INTERVAL_MILLISECONDS = 1000;

    private static final int DEFAULT_SWIPE_DURATION = 2000;

    public enum MessageIndexLocator {
        FIRST(xpathFirstConversationMessage),
        LAST(xpathLastConversationMessage);

        private final By locator;

        MessageIndexLocator(By locator) {
            this.locator = locator;
        }

        public By getLocator() {
            return this.locator;
        }
    }


    public ConversationViewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    // region Screeshot buffer
    public BufferedImage getConvoViewStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idConversationRoot)).orElseThrow(
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
        return this.getElementScreenshot(getElement(idAudioRecordingPlayButton)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Audio message recording slide microphone button")
        );
    }
    //endregion

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

    public void clearMessageInCursorInput() throws Exception {
        getElement(idCursorEditText).clear();
    }

    public boolean waitUntilCursorInputTextVisible(String text) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCurosrEditTextByValue.apply(text)));
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
            case "share location":
                return idCursorShareLocation;
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
            getElementIfDisplayed(idCursorMore, 3).orElseGet(() ->
                    {
                        try {
                            return getElement(idCursorLess);
                        } catch (Exception e) {
                            throw new IllegalStateException("Cannot find element with Cursor less button");
                        }
                    }
            ).click();
            getElement(locator).click();
        }
    }

    public void longTapAudioMessageCursorBtn(int durationMillis) throws Exception {
        getDriver().longTap(getElement(idCursorAudioMessage), durationMillis);
    }

    public void longTapAudioMessageCursorBtnAndSwipeUp(int durationMillis) throws Exception {
        longTapAndSwipe(getElement(idCursorAudioMessage), () -> getElement(idAudioRecordingSendButton),
                DEFAULT_SWIPE_DURATION, durationMillis);
    }

    public void longTapAudioMessageCursorBtnAndRememberIcon(int durationMillis, ElementState elementState)
            throws Exception {
        longTapAndSwipe(getElement(idCursorAudioMessage), () -> getElement(idCursorAudioMessage),
                DEFAULT_SWIPE_DURATION, durationMillis, Optional.of(elementState::remember));
    }

    public boolean isCursorHintVisible(String hintMessage) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathCursorHintByValue.apply(hintMessage)));
    }

    public boolean isAudioMessageRecordingSlideVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idAudioMessageRecordingSileControl);
    }

    public boolean isAudioRecordingButtonVisible(String name) throws Exception {
        final By locator = getAudioRecordingButtonLocator(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isAudioMessageRecordingDurationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathAudioMessageDurationText);
    }

    private By getAudioRecordingButtonLocator(String btnName) {
        switch (btnName.toLowerCase()) {
            case "send":
                return idAudioRecordingSendButton;
            case "cancel":
                return idAudioRecordingCancelButton;
            case "play":
                return idAudioRecordingPlayButton;
            default:
                throw new IllegalStateException(String.format("Cannot identify audio recording button '%s'", btnName));
        }
    }

    public void tapAudioRecordingButton(String name) throws Exception {
        final By locator = getAudioRecordingButtonLocator(name);
        if (locator.equals(idAudioRecordingSendButton)) {
            getDriver().longTap(getElement(idAudioRecordingSendButton), DriverUtils.SINGLE_TAP_DURATION);
        } else {
            getElement(locator).click();
        }
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

    private By getTopBarButtonLocator(String btnName) {
        switch (btnName.toLowerCase()) {
            case "audio call":
                return idAudioCall;
            case "video call":
                return idVideoCall;
            case "back":
                return xpathToolBarNavigation;
            default:
                throw new IllegalArgumentException(String.format("Unknown top bar button name '%s'", btnName));
        }
    }

    public void tapTopBarButton(String name) throws Exception {
        final By locator = getTopBarButtonLocator(name);
        getElement(locator).click();
    }

    public void tapTopToolbarTitle() throws Exception {
        getElement(xpathToolbar, "Top toolbar title is not visible").click();
    }

    public void closeInputOptions() throws Exception {
        getElement(idCursorCloseButton, "Close cursor button is not visible").click();
    }

    public void tapRecentImage() throws Exception {
        getElement(xpathLastImage).click();
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

    public boolean isImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), idConversationImages) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(), idClickedImageSendingIndicator, 20);
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

    public boolean isConversationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idConversationRoot);
    }

    private static final double MAX_BUTTON_STATE_OVERLAP = 0.5;

    public void tapPlayPauseBtn() throws Exception {
        // TODO: Check whether swipe on SoundCloud, it will trigger long tap action?
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
        return isIndexMessageEqualsTo(expectedMessage, timeoutSeconds, MessageIndexLocator.LAST);
    }

    public boolean isFirstMessageEqualTo(String expectedMessage, int timeoutSeconds) throws Exception {
        return isIndexMessageEqualsTo(expectedMessage, timeoutSeconds, MessageIndexLocator.FIRST);
    }

    /**
     * Compare the indexed message with expected message, such first or last, or N-th message, but you need to
     * pass the correspondant locator.
     *
     * @param expectedMessage     expected message
     * @param timeoutSeconds      time out in seconds
     * @param messageIndexLocator the locator used for locating the N-th message
     * @return true measn expected message in expected index
     * @throws Exception
     */
    private boolean isIndexMessageEqualsTo(String expectedMessage, int timeoutSeconds,
                                           MessageIndexLocator messageIndexLocator) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(expectedMessage));
        return CommonUtils.waitUntilTrue(
                timeoutSeconds,
                500,
                () -> {
                    final Optional<WebElement> msgElement = getElementIfDisplayed(locator, 1);
                    if (msgElement.isPresent()) {
                        final String indexMessage = getElement(messageIndexLocator.getLocator(),
                                "Cannot find the indexed message in the conversation", 1).getText();
                        return expectedMessage.equals(indexMessage);
                    } else {
                        return false;
                    }
                }
        );
    }


    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idConversationImages));
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idFullScreenImage));
    }

    public boolean isImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idConversationImages);
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

    public int getCurrentNumberOfItemsInConversation() throws Exception {
        return selectVisibleElements(xpathConversationContent).size();
    }

    private static final long IMAGES_VISIBILITY_TIMEOUT = 10000; // seconds;

    public boolean waitForXImages(int expectedCount) throws Exception {
        assert expectedCount >= 0;
        final Optional<WebElement> imgElement = getElementIfDisplayed(idConversationImages);
        if (expectedCount <= 1) {
            return (expectedCount == 0 && !imgElement.isPresent()) || (expectedCount == 1 && imgElement.isPresent());
        }
        final long msStarted = System.currentTimeMillis();
        do {
            int actualCnt = getElements(idConversationImages).size();
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
        Thread.sleep(2000);
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

    private By getContainerLocatorByName(String containerType) {
        switch (containerType.toLowerCase()) {
            case "youtube":
                return idYoutubeContainer;
            case "soundcloud":
                return idSoundcloudContainer;
            case "file upload":
                return idFileTransferContainer;
            case "video message":
                return idVideoMessageContainer;
            case "audio message":
                return idAudioMessageContainer;
            case "share location":
                return idShareLocationContainer;
            case "link preview":
                return idLinkPreviewContainer;
            default:
                throw new IllegalArgumentException(String.format("Unknown container type: '%s'", containerType));
        }
    }

    public boolean isContainerVisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isContainerInvisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilLinkPreviewUrlVisible(String url) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathLinkPreviewUrlByValue.apply(url)));
    }

    public void tapContainer(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        getElement(locator).click();
    }

    public void longTapContainer(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        if (locator.equals(idAudioMessageContainer) || locator.equals(idVideoMessageContainer)) {
            // To avoid to tap on play button in Video message and Audio message container.
            final WebElement el = getElement(locator);
            final Point location = el.getLocation();
            final Dimension size = el.getSize();
            getDriver().longTap(location.x + size.width / 5, location.y + size.height / 5, DriverUtils.LONG_TAP_DURATION);
        } else {
            getDriver().longTap(getElement(locator), DriverUtils.LONG_TAP_DURATION);
        }
    }

    private By getTextLocatorByTextMessageType(String messageType, String message) {
        switch (messageType.toLowerCase()) {
            case "ping":
                return By.xpath(xpathStrPingMessageByText.apply(message));
            case "text":
                return By.xpath(xpathStrConversationMessageByText.apply(message));
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the type '%s'", messageType));
        }
    }

    public void tapMessage(String messageType, String message, String tapType) throws Exception {
        By locator = getTextLocatorByTextMessageType(messageType, message);
        switch (tapType.toLowerCase()) {
            case "tap":
                getElement(locator).click();
                break;
            case "long tap":
                getDriver().longTap(getElement(locator), DriverUtils.LONG_TAP_DURATION);
                break;
            case "double tap":
                getDriver().doubleTap(getElement(locator));
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the tap type '%s'", tapType));
        }
    }

    public void longTapRecentImage() throws Exception {
        getDriver().longTap(getElement(xpathLastImage), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapVideoMessageButton() throws Exception {
        getElement(idVideoContainerButton).click();
    }

    public void tapAudioMessageButton() throws Exception {
        getElement(idAudioContainerButton).click();
    }

    private By getImageContainerButtonLocator(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "sketch":
                return idImageContainerSketchButton;
            case "fullscreen":
                return idImageContainerFullScreenButton;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the button type '%s'", buttonName));
        }
    }

    public void tapImageContainerButton(String buttonName) throws Exception {
        By locator = getImageContainerButtonLocator(buttonName);
        getElement(locator).click();
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

    public int getMessageHeight(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConversationMessageByText.apply(msg));
        return Integer.parseInt(getElement(locator).getAttribute("height"));
    }

    //region Message Bottom Menu
    private By getMessageBottomMenuButtonLocatorByName(String btnName) {
        switch (btnName.toLowerCase()) {
            case "delete only for me":
                return idMessageBottomMenuDeleteLocalButton;
            case "delete for everyone":
                return idMessageBottomMenuDeleteGlobalButton;
            case "copy":
                return idMessageBottomMenuCopyButton;
            case "forward":
                return idMessageBottomMenuForwardButton;
            case "edit":
                return idMessageBottomMenuEditButton;
            case "like":
                return idMessageBottomMenuLikeButton;
            case "unlike":
                return idMessageBottomMenuUnlikeButton;
            default:
                throw new IllegalArgumentException(String.format("There is no '%s' button on Message Bottom Menu",
                        btnName));
        }
    }

    public void tapMessageBottomMenuButton(String name) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(name);
        getElement(locator, String.format("Message bottom menu %s button is invisible", name)).click();
    }

    public boolean waitUntilMessageBottomMenuButtonVisible(String btnNAme) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(btnNAme);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilMessageBottomMenuButtonInvisible(String btnNAme) throws Exception {
        final By locator = getMessageBottomMenuButtonLocatorByName(btnNAme);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
    //endregion

    //region Contact name icon
    public boolean waitUntilTrashIconVisible(String name) throws Exception {
        final By locator = By.xpath(xpathTrashcanByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilTrashIconInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathTrashcanByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilPenIconVisible(String name) throws Exception {
        final By locator = By.xpath(xpathPenByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPenIconInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathPenByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilMessageSeparatorVisible(String name, int timeout) throws Exception {
        final By locator = By.xpath(xpathMessageSeparator.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, timeout);
    }

    public boolean waitUntilMessageSeparatorInvisible(String name, int timeout) throws Exception {
        final By locator = By.xpath(xpathMessageSeparator.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, timeout);
    }

    public void tapAllResendButton() throws Exception {
        List<WebElement> resendButtonList = selectVisibleElements(idResendButton);
        for (WebElement resendButton : resendButtonList) {
            resendButton.click();
        }
    }
    //endregion

    //region Message Meta
    public boolean waitUntilMessageMetaItemVisible(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(locatorId));
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(locatorId));
    }

    public boolean waitUntilMessageMetaItemVisible(String itemType, String expectedItemText)
            throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrTemplateIdValue.apply(locatorId, expectedItemText)));
    }

    public boolean waitUntilMessageMetaItemInvisible(String itemType, String expectedItemText)
            throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathStrTemplateIdValue.apply(locatorId, expectedItemText)));
    }

    public void tapMessageMetaItem(String itemType) throws Exception {
        String locatorId = getMessageMetaLocatorIdString(itemType);
        getElement(By.id(locatorId)).click();
    }

    public BufferedImage getMessageLikeButtonState() throws Exception {
        By locator = By.id(strIdMessageMetaLikeButton);
        return this.getElementScreenshot(getElement(locator)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot for like button")
        );
    }

    public int getMessageStatusCount() throws Exception {
        By locator = By.id(strIdMessageMetaStatus);
        return selectVisibleElements(locator).size();
    }

    private String getMessageMetaLocatorIdString(String itemType) throws Exception {
        switch (itemType.toLowerCase()) {
            case "like button":
                return strIdMessageMetaLikeButton;
            case "like hint":
                return strIdMessageMetaLikeHint;
            case "like description":
                return strIdMessageMetaLikeDescription;
            case "message status":
                return strIdMessageMetaStatus;
            case "first like avatar":
                return strIdMessageMetaFirstLike;
            case "second like avatar":
                return strIdMessageMetaSecondLike;
            default:
                throw new IllegalStateException(
                        String.format("Cannot identify the item type '%s' in Message Meta", itemType));
        }
    }
    //endregion
}
