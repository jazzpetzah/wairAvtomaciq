package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import com.wearezeta.auto.common.sync_engine_bridge.Constants;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSElement;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConversationViewPage extends IOSPage {
    private static final By nameConversationBackButton = MobileBy.AccessibilityId("ConversationBackButton");

    private static final String nameStrConversationInputField = "inputField";

    private static final By nameConversationInput = MobileBy.AccessibilityId(nameStrConversationInputField);

    private static final Function<String, String> xpathStrConversationInputByValue = value ->
            String.format("//UIATextView[@name='%s' and @value='%s']", nameStrConversationInputField, value);

    private static final By nameConversationInputAvatar = MobileBy.AccessibilityId("authorImage");

    private static final By nameInputPlaceholderText = MobileBy.AccessibilityId("TYPE A MESSAGE");

    protected static final By nameYouRenamedConversation = MobileBy.AccessibilityId("YOU RENAMED THE CONVERSATION");

    /**
     * !!! The actual message order in DOM is reversed relatively to the messages order in the conversation view
     */
    private static final String xpathStrAllEntries = xpathStrMainWindow + "/UIATableView/UIATableCell";
    private static final By xpathAllEntries = By.xpath(xpathStrAllEntries);
    private static final By xpathFirstEntry = By.xpath(xpathStrAllEntries + "[1]");

    private static final String xpathStrAllTextMessages = xpathStrAllEntries + "/UIATextView[boolean(string(@value))]";
    private static final By xpathAllTextMessages = By.xpath(xpathStrAllTextMessages);

    private static final Function<String, String> xpathStrLastMessageByTextPart = text ->
            String.format("%s[1][contains(@value, '%s')]", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrLastMessageByExactText = text ->
            String.format("%s[1][@value='%s']", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathTableCellFromUser = text ->
            String.format("%s[@name='%s']", xpathStrAllEntries, text.toUpperCase());

    private static final Function<String, String> xpathStrMessageByTextPart = text ->
            String.format("%s[contains(@value, '%s')]", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrMessageByExactText = text ->
            String.format("%s[@value='%s']", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrMessageCellByTextPart = text ->
            String.format("%s[contains(@value, '%s')]/parent::*", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrSystemMessageByText = text ->
            String.format("//UIATableCell[@name='%s']", text.toUpperCase());

    private static final String xpathStrImageCells = xpathStrAllEntries + "[@name='ImageCell']";
    private static final By xpathImageCell = By.xpath(xpathStrImageCells);
    private static final By xpathLastImageCell = By.xpath(String.format("(%s)[1]", xpathStrImageCells));

    private static final By xpathMediaContainerCell =
            By.xpath(xpathStrAllTextMessages + "[contains(@value, '://')]/following-sibling::UIAButton");

    private static final By xpathGiphyImage = By
            .xpath(xpathStrAllTextMessages + "[@name='via giphy.com']/following::UIATableCell[@name='ImageCell']");

    private static final By xpathLastMessageResendButton =
            By.xpath(xpathStrAllTextMessages + "[1]/parent::*/UIAButton");

    private static final By namePlayButton = MobileBy.AccessibilityId("mediaBarPlayButton");

    private static final By namePauseButton = MobileBy.AccessibilityId("mediaBarPauseButton");

    private static final By xpathConversationPage = By.xpath(xpathStrMainWindow + "/UIATableView[1]");

    private static final By nameMediaBarCloseButton = MobileBy.AccessibilityId("mediabarCloseButton");

    private static final By nameTitle = MobileBy.AccessibilityId("playingMediaTitle");

    private static final By nameGifButton = MobileBy.AccessibilityId("gifButton");

    private static final By nameSoundCloudButton = MobileBy.AccessibilityId("soundcloud");

    public static final Function<String, String> xpathStrMissedCallButtonByContact = name -> String.format(
            "//UIATableCell[.//*[@name='%s CALLED']]/UIAButton[@name='ConversationMissedCallButton']",
            name.toUpperCase());

    public static final By xpathStrMissedCallButtonByYourself =
            By.xpath("//UIATableCell[.//*[@name='YOU CALLED']]/UIAButton[@name='ConversationMissedCallButton']");

    public static final Function<String, String> xpathStrConnectingToUserLabelByName = name -> String.format(
            "//UIAStaticText[contains(@name, 'CONNECTING TO %s.')]", name.toUpperCase());

    private static final By nameShieldIconNextToInput = MobileBy.AccessibilityId("verifiedConversationIndicator");

    public static final String MEDIA_STATE_PLAYING = "playing";

    public static final String MEDIA_STATE_PAUSED = "paused";

    public static final String MEDIA_STATE_STOPPED = "ended";

    private static final By nameCursorSketchButton = MobileBy.AccessibilityId("sketchButton");
    protected static final By nameAddPictureButton = MobileBy.AccessibilityId("photoButton");
    private static final By namePingButton = MobileBy.AccessibilityId("pingButton");
    private static final By nameFileTransferButton = MobileBy.AccessibilityId("uploadFileButton");
    private static final By nameVideoMessageButton = MobileBy.AccessibilityId("videoButton");
    private static final By nameAudioMessageButton = MobileBy.AccessibilityId("audioButton");
    private static final By nameShareLocationButton = MobileBy.AccessibilityId("locationButton");

    private static final String xpathStrConversationViewTopBar = "//UIANavigationBar[./UIAButton[@name='Back']]";
    private static final By xpathConversationViewTopBar = By.xpath(xpathStrConversationViewTopBar);
    private static Function<String, String> xpathStrToolbarByConversationName = name ->
            String.format("%s/UIAButton[starts-with(@name, '%s')]", xpathStrConversationViewTopBar, name.toUpperCase());
    private static Function<String, String> xpathStrToolbarByExpr = expr ->
            String.format("%s/UIAButton[%s]", xpathStrConversationViewTopBar, expr);

    private static final By nameEllipsisButton = MobileBy.AccessibilityId("showOtherRowButton");
    private static final By xpathAudioCallButton = MobileBy.AccessibilityId("audioCallBarButton");
    private static final By xpathVideoCallButton = MobileBy.AccessibilityId("videoCallBarButton");
    private static final By xpathConversationDetailsButton = By.xpath(xpathStrConversationViewTopBar +
            "/UIAButton[@name='Back']/following-sibling::" +
            "UIAButton[not(@name='ConversationBackButton') and boolean(string(@label))]");

    private static final By nameToManyPeopleAlert = MobileBy.AccessibilityId("Too many people to call");

    private static final Function<String, String> xpathStrUserNameInUpperToolbar = text ->
            String.format("%s/UIAButton[contains(@name, '%s')]", xpathStrConversationViewTopBar, text.toUpperCase());

    private static final String nameStrFileTransferTopLabel = "FileTransferTopLabel";
    private static final By nameFileTransferTopLabel = MobileBy.AccessibilityId(nameStrFileTransferTopLabel);
    private static final Function<String, String> xpathTransferTopLabelByFileName = name ->
            String.format("//UIAStaticText[@name='%s' and @value='%s']", nameStrFileTransferTopLabel, name.toUpperCase());

    private static final String nameStrFileTransferBottomLabel = "FileTransferBottomLabel";
    private static final By nameFileTransferBottomLabel = MobileBy.AccessibilityId(nameStrFileTransferBottomLabel);
    private static final Function<String, String> xpathTransferBottomLabelByExpr = expr ->
            String.format("//UIAStaticText[@name='%s' and %s]", nameStrFileTransferBottomLabel, expr);

//    private static final By nameFileTransferActionButton = MobileBy.AccessibilityId("FileTransferActionButton");

    private static final Function<String, String> xpathStrFilePreviewByFileName = fileName ->
            String.format("//UIANavigationBar[@name='%s']", fileName);

    private static final By nameGenericFileShareMenu = MobileBy.AccessibilityId("ActivityListView");

    private static final By xpathFileUploadingLabel = By.xpath("//UIAStaticText[contains(@value,'UPLOADING…')]");

    private static final By nameShareButton = MobileBy.AccessibilityId("Share");

    private static final By nameVideoMessageActionButton = MobileBy.AccessibilityId("VideoActionButton");

//    private static final By nameVideoMessageSizeLabel = MobileBy.AccessibilityId("VideoSizeLabel");

    private static final Function<String, String> xpathUserNameByText = text ->
            String.format("//UIATableCell[@name='%s']", text.toUpperCase());

    private static final By nameAudioRecorderCancelButton = MobileBy.AccessibilityId("audioRecorderCancel");

    private static final By nameSendAudioMessageButton = MobileBy.AccessibilityId("audioRecorderSend");

    private static final String strNamePlayAudioRecorderButton = "audioRecorderPlay";

    private static final By namePlayAudioRecorderButton = MobileBy.AccessibilityId(strNamePlayAudioRecorderButton);

    private static final Function<String, String> recordControlButtonWithState = state ->
            String.format("//UIAButton[@name='%s' and @value='%s']", strNamePlayAudioRecorderButton, state);

    private static final By nameAudioRecordTimeLabel = MobileBy.AccessibilityId("audioRecorderTimeLabel");

    private static final By nameAudioPlaceholderTimeLabel = MobileBy.AccessibilityId("AudioTimeLabel");

    private static final String strNameAudioActionButton = "AudioActionButton";
    private static final By nameAudioActionButton = MobileBy.AccessibilityId(strNameAudioActionButton);

    private static final Function<Integer, String> xpathStrAudioActionButtonByIndex = index ->
            String.format("(//*[@name='%s'])[%s]", strNameAudioActionButton, index);

    private static final FunctionFor2Parameters<String, String, Integer> placeholderAudioMessageButtonStateByIndex =
            (buttonState, index) ->
                    String.format("(//UIAButton[@name='%s'])[%s][@value='%s']", strNameAudioActionButton, index, buttonState);

    private static final By classNameShareLocationContainer = MobileBy.className("UIAMapView");

    private static final By nameDefaultRecievedLocationAddress = MobileBy.AccessibilityId(Constants.DEFAULT_GMAP_ADDRESS);

    private static final By nameDefaultSentLocationAddress = MobileBy.AccessibilityId("1800 Ellis St, San Francisco, CA  94102");

    private static final By xpathDefaultMapApplication = By.xpath("//UIAApplication[@name='Maps']");

    private static final By nameLinkPreviewSource = MobileBy.AccessibilityId("linkPreviewSource");

    private static final By nameLinkPreviewImage = MobileBy.AccessibilityId("linkPreviewImage");

    private static final int MAX_CONTAINER_APPEARANCE_TIME = 20;

    private static final Logger log = ZetaLogger.getLog(ConversationViewPage.class.getSimpleName());

    public ConversationViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isPartOfTextMessageVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByTextPart.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean waitUntilPartOfTextMessageIsNotVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByTextPart.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void tapVideoCallButton() throws Exception {
        getElement(xpathVideoCallButton).click();
    }

    public boolean isVideoCallButtonOnToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathVideoCallButton);
    }

    public boolean isVideoCallButtonOnToolbarNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathVideoCallButton);
    }

    public void returnToConversationsList() throws Exception {
        final Optional<WebElement> backBtn = getElementIfDisplayed(nameConversationBackButton);
        if (backBtn.isPresent()) {
            backBtn.get().click();
        } else {
            log.warn("Back button is not visible. Probably, the conversations list is already visible");
        }
    }

    public void tapAudioCallButton() throws Exception {
        getElement(xpathAudioCallButton).click();
    }

    public boolean isAudioCallButtonOnToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathAudioCallButton);
    }

    public boolean isAudioCallButtonOnToolbarNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathAudioCallButton);
    }

    public int getNumberOfMessageEntries() throws Exception {
        return selectVisibleElements(xpathAllEntries).size();
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationInput, 10);
    }

    public boolean waitForCursorInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConversationInput);
    }

    public void clickOnCallButtonForContact(String contact) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(contact));
        getElement(locator).click();
    }

    public void tapOnCursorInput() throws Exception {
        getElement(nameConversationInput).click();
    }

    public void clearTextInput() throws Exception {
        getElement(nameConversationInput).clear();
    }

    public boolean isCurrentInputTextEqualTo(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrConversationInputByValue.apply(expectedMsg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 3);
    }

    public boolean isLastMessageContain(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrLastMessageByTextPart.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLastMessageEqual(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrLastMessageByExactText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public int getMessagesCount() throws Exception {
        return getMessagesCount(null);
    }

    public int getMessagesCount(String expectedMessage) throws Exception {
        By locator = xpathAllTextMessages;
        if (expectedMessage != null) {
            locator = By.xpath(xpathStrMessageByTextPart.apply(expectedMessage));
        }
        return selectVisibleElements(locator).size();
    }

    public int getCountOfImages() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(), xpathImageCell)) {
            return getElements(xpathImageCell).size();
        }
        return 0;
    }

    public void startMediaContent() throws Exception {
        final Optional<WebElement> mediaLinkCell = getElementIfDisplayed(xpathMediaContainerCell, 3);
        if (mediaLinkCell.isPresent()) {
            mediaLinkCell.get().click();
        } else {
            final WebElement soundCloudButton = getElement(nameSoundCloudButton);
            this.getDriver().tap(1, soundCloudButton.getLocation().x + 200, soundCloudButton.getLocation().y + 200, 1);
        }
    }

    public boolean scrollDownTillMediaBarAppears() throws Exception {
        final int maxScrolls = 2;
        int nTry = 0;
        while (nTry < maxScrolls) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameTitle, 2)) {
                return true;
            }
            swipeDialogPageDown();
            nTry++;
        }
        return false;
    }

    private boolean isMediaBarPauseButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), namePauseButton, 3);
    }

    private void clickMediaBarPauseButton() throws Exception {
        getElement(namePauseButton, "Pause button is not visible on media bar").click();
    }

    public void pauseMediaContent() throws Exception {
        clickMediaBarPauseButton();
    }

    private boolean isMediaBarPlayButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), namePlayButton, 3);
    }

    private void clickMediaBarPlayButton() throws Exception {
        getElement(namePlayButton, "Play button is not visible on media bar").click();
    }

    public void playMediaContent() throws Exception {
        clickMediaBarPlayButton();
    }

    private void clickMediaBarCloseButton() throws Exception {
        getElement(nameMediaBarCloseButton, "Close button is not visible on Media bar").click();
    }

    public void stopMediaContent() throws Exception {
        clickMediaBarCloseButton();
    }

    public boolean isUpperToolbarContainNames(List<String> expectedNames) throws Exception {
        final String xpathExpr = String.join(" and ", expectedNames.stream()
                .map(x -> String.format("contains(@name, '%s')", x.toUpperCase()))
                .collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrToolbarByExpr.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public String getMediaStateFromMediaBar() throws Exception {
        if (isMediaBarPlayButtonVisible()) {
            return MEDIA_STATE_PAUSED;
        } else if (isMediaBarPauseButtonVisible()) {
            return MEDIA_STATE_PLAYING;
        }
        return MEDIA_STATE_STOPPED;
    }

    public void tapOnMediaBar() throws Exception {
        getElement(nameTitle).click();
    }

    private static final int TEXT_INPUT_HEIGHT = 300;
    private static final int TOP_BORDER_WIDTH = 40;

    public void openConversationDetails() throws Exception {
        getElement(xpathConversationDetailsButton).click();
    }

    @Override
    public void swipeUp(int time) throws Exception {
        final Point coords = getElement(nameMainWindow).getLocation();
        final Dimension elementSize = getElement(nameMainWindow).getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y + elementSize.height - TEXT_INPUT_HEIGHT,
                coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH, time);
    }

    public void swipeDialogPageDown() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.swipeDown();
        } else {
            DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationPage),
                    1000, 50, 30, 50, 95);
        }
    }

    public boolean isYoutubeContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathMediaContainerCell, 10);
    }

    public boolean isMediaContainerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathMediaContainerCell);
    }

    public boolean isMediaBarDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameTitle);
    }

    public boolean isMediaBarNotVisibled() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameTitle);
    }

    public boolean waitMediabarClose() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameTitle);
    }

    public void tapImageToOpen() throws Exception {
        getElement(xpathLastImageCell).click();
    }

    public void tapHoldTextInput() throws Exception {
        final WebElement textInput = getElement(nameConversationInput);
        this.getDriver().tap(1, textInput, DriverUtils.LONG_TAP_DURATION);
    }

    public void scrollToBeginningOfConversation() throws Exception {
        for (int i = 0; i < 2; i++) {
            if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
                IOSSimulatorHelper.swipeDown();
            } else {
                DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationPage),
                        500, 50, 10, 50, 90);
            }
        }
    }

    private static final long KEYBOARD_OPEN_ANIMATION_DURATION = 5500; // milliseconds

    public void typeAndSendConversationMessage(String message) throws Exception {
        final WebElement convoInput = getElement(nameConversationInput,
                "Conversation input is not visible after the timeout");
        convoInput.click();
        // Wait for animation
        Thread.sleep(KEYBOARD_OPEN_ANIMATION_DURATION);
//        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
//            inputStringFromKeyboard(convoInput, message, true);
//        } else {
        ((IOSElement) convoInput).setValue(message);
        this.clickKeyboardCommitButton();
//        }
    }

    public void typeMessage(String message) throws Exception {
        final WebElement convoInput = getElement(nameConversationInput,
                "Conversation input is not visible after the timeout");
        convoInput.click();
        // Wait for animation
        Thread.sleep(KEYBOARD_OPEN_ANIMATION_DURATION);
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            inputStringFromKeyboard(convoInput, message, false);
        } else {
            convoInput.sendKeys(message);
        }
    }

    public void clickOnPlayVideoButton() throws Exception {
        getElement(xpathMediaContainerCell).click();
    }

    public void openGifPreviewPage() throws Exception {
        getElement(nameGifButton).click();
    }

    public boolean isUserNameDisplayedInConversationView(String name) throws Exception {
        final By locator = By.xpath(xpathUserNameByText.apply(name));
        this.printPageSource();
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isConnectingToUserConversationLabelVisible(String username) throws Exception {
        final By locator = By.xpath(xpathStrConnectingToUserLabelByName.apply(username));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isGiphyImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGiphyImage);
    }

    public void longTapImage() throws Exception {
        this.getDriver().tap(1, getElement(xpathLastImageCell), DriverUtils.LONG_TAP_DURATION);
    }

    public boolean isUserAvatarNextToInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationInputAvatar);
    }

    public boolean isUserAvatarNextToInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConversationInputAvatar);
    }

    public void tapMessage(String expectedLink) throws Exception {
        final By locator = By.xpath(xpathStrMessageCellByTextPart.apply(expectedLink));
        // TODO: Find a better way to calculate these click coordinates
        DriverUtils.tapByCoordinatesWithPercentOffcet(getDriver(), getElement(locator), 20, 70);
    }

    public boolean isShieldIconVisibleNextToInputField() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameShieldIconNextToInput);
    }

    public boolean isShieldIconInvisibleNextToInputField() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameShieldIconNextToInput);
    }

    public void resendLastMessageInDialogToUser() throws Exception {
        getElement(xpathLastMessageResendButton).click();
    }

    public BufferedImage getMediaContainerStateGlyphScreenshot() throws Exception {
        final BufferedImage containerScreen =
                this.getElementScreenshot(getElement(xpathMediaContainerCell)).orElseThrow(() ->
                        new IllegalStateException("Cannot take a screenshot of media container"));
        final int stateGlyphWidth = containerScreen.getWidth() / 7;
        final int stateGlyphHeight = containerScreen.getHeight() / 7;
        final int stateGlyphX = (containerScreen.getWidth() - stateGlyphWidth) / 2;
        final int stateGlyphY = (containerScreen.getHeight() - stateGlyphHeight) / 2;
//        BufferedImage tmp = containerScreen.getSubimage(stateGlyphX, stateGlyphY, stateGlyphWidth, stateGlyphHeight);
//        ImageIO.write(tmp, "png", new File("/Users/elf/Desktop/" + System.currentTimeMillis() + ".png"));
        return containerScreen.getSubimage(stateGlyphX, stateGlyphY, stateGlyphWidth, stateGlyphHeight);
    }

    public void pasteAndCommit() throws Exception {
        final WebElement convoInput = getElement(nameConversationInput,
                "Conversation input is not visible after the timeout");
        convoInput.click();
        // Wait for animation
        Thread.sleep(KEYBOARD_OPEN_ANIMATION_DURATION);
        this.inputStringFromPasteboard(convoInput, true);
    }

    public boolean areInputToolsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameVideoMessageButton);
    }

    public boolean areInputToolsInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameVideoMessageButton);
    }

    public boolean isMissedCallButtonVisibleFor(String username) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(username));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSystemMessageVisible(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByText.apply(expectedMsg));
        this.printPageSource();
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUpperToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathConversationViewTopBar);
    }

    public boolean isTooManyPeopleAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameToManyPeopleAlert);
    }

    public boolean isUserNameInUpperToolbarVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserNameInUpperToolbar.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isYouCalledMessageAndButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathStrMissedCallButtonByYourself);
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return getElementScreenshot(getElement(xpathLastImageCell));
    }

    public void tapFileTransferMenuItem(String itemName) throws Exception {
        getElement(MobileBy.AccessibilityId(itemName)).click();
    }

    public boolean isFileTransferTopLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameFileTransferTopLabel);
    }

    public boolean isFileTransferTopLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameFileTransferTopLabel);
    }

    public boolean isFileTransferBottomLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameFileTransferBottomLabel);
    }

    private By getInputToolButtonByName(String btnName) {
        switch (btnName.toLowerCase()) {
            case "add picture":
                return nameAddPictureButton;
            case "ping":
                return namePingButton;
            case "sketch":
                return nameCursorSketchButton;
            case "file transfer":
                return nameFileTransferButton;
            case "video message":
                return nameVideoMessageButton;
            case "audio message":
                return nameAudioMessageButton;
            case "share location":
                return nameShareLocationButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown input tools button name %s", btnName));
        }
    }

    public boolean isInputToolButtonByNameVisible(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
            return true;
        } else {
            DriverUtils.tapOnPercentOfElement(getDriver(), getElement(nameEllipsisButton), 50, 50);
            return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 3);
        }
    }

    public boolean isInputToolButtonByNameNotVisible(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (DriverUtils.waitUntilLocatorDissapears(getDriver(), locator) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(), nameEllipsisButton)) {
            return true;
        } else {
            DriverUtils.tapOnPercentOfElement(getDriver(), getElement(nameEllipsisButton), 50, 50);
            return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, 3);
        }
    }

    private boolean isTestImageUploaded = false;

    public void tapInputToolButtonByName(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (locator.equals(nameAddPictureButton) && !isTestImageUploaded &&
                CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
            isTestImageUploaded = true;
        }
        locateCursorToolButton(locator).click();
    }

    public boolean waitUntilDownloadReadyPlaceholderVisible(String expectedFileName, String expectedSize,
                                                            int timeoutSeconds) throws Exception {
        final String nameWOExtension = FilenameUtils.getBaseName(expectedFileName);
        final String extension = FilenameUtils.getExtension(expectedFileName);

        final By topLabelLocator = By.xpath(xpathTransferTopLabelByFileName.apply(nameWOExtension));
        final By bottomLabelLocator = By.xpath(xpathTransferBottomLabelByExpr.apply(
                String.join(" and ",
                        String.format("starts-with(@value, '%s')", expectedSize.toUpperCase()),
                        String.format("contains(@value, '%s')", extension.toUpperCase())
                )
        ));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), topLabelLocator, timeoutSeconds) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), bottomLabelLocator, timeoutSeconds);
    }

    public void tapFileTransferPlaceholder() throws Exception {
        getElement(nameFileTransferBottomLabel).click();
    }

    public boolean waitUntilFilePreviewIsVisible(int secondsTimeout, String expectedFileName) throws Exception {
        final By locator = By.xpath(xpathStrFilePreviewByFileName.apply(expectedFileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, secondsTimeout);
    }

    public boolean isGenericFileShareMenuVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGenericFileShareMenu, timeoutSeconds);
    }

    public boolean fileUploadingLabelNotVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathFileUploadingLabel, timeoutSeconds);
    }

    public void tapShareButton() throws Exception {
        getElement(nameShareButton).click();
    }

    public void tapShareMenuItem(String itemName) throws Exception {
        getElement(MobileBy.AccessibilityId(itemName)).click();
    }

    public boolean isInputPlaceholderTextVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInputPlaceholderText);
    }

    public boolean isInputPlaceholderTextInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameInputPlaceholderText);
    }

    public void scrollToTheBottom() throws Exception {
        final int maxActions = 5;
        int actionIdx = 0;
        do {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathFirstEntry, 1)) {
                return;
            }
            swipeUp(1000);
            actionIdx++;
        } while (actionIdx < maxActions);
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathFirstEntry, 1)) {
            throw new IllegalStateException(String.format("The very first conversation entry is not visible after %s " +
                    "scrolling retries", actionIdx));
        }
    }

    public void tapAndHoldTextMessageByText(String msg) throws Exception {
        final WebElement lastMessage = getElement(By.xpath(xpathStrMessageByTextPart.apply(msg)));
        //Using this method because tap should be performed precisely on the text otherwise popup won't appear
        DriverUtils.tapOnPercentOfElement(getDriver(), lastMessage, 10, 50, 1000);
    }

    public void longTapMediaContainer() throws Exception {
        this.getDriver().tap(1, getElement(xpathMediaContainerCell), DriverUtils.LONG_TAP_DURATION);
    }

    public void longTapFileTransferPlaceholder() throws Exception {
        this.getDriver().tap(1, getElement(nameFileTransferTopLabel), DriverUtils.LONG_TAP_DURATION);
    }

    private WebElement locateCursorToolButton(By locator) throws Exception {
        final Optional<WebElement> toolButton = getElementIfDisplayed(locator, 3);
        if (toolButton.isPresent()) {
            return toolButton.get();
        } else {
            DriverUtils.tapOnPercentOfElement(getDriver(), getElement(nameEllipsisButton), 50, 50);
            return getElement(locator);
        }
    }

    public void longTapInputToolButtonByName(String btnName, boolean shouldKeepTap) throws Exception {
        final WebElement dstElement = locateCursorToolButton(getInputToolButtonByName(btnName));
        if (shouldKeepTap) {
            new TouchAction(getDriver()).press(dstElement).perform();
        } else {
            getDriver().tap(1, dstElement, DriverUtils.LONG_TAP_DURATION);
        }
    }

    public void longTapWithDurationInputToolButtonByName(String btnName, int durationSeconds) throws Exception {
        getDriver().tap(1, locateCursorToolButton(getInputToolButtonByName(btnName)), durationSeconds * 1000);
    }

    private By getRecordControlButtonByName(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "send":
                return nameSendAudioMessageButton;
            case "cancel":
                return nameAudioRecorderCancelButton;
            case "play":
                return namePlayAudioRecorderButton;
            default:
                throw new IllegalArgumentException(String.format("Button '%s' is not known as a record control button", buttonName));
        }
    }

    public void tapRecordControlButton(String buttonName) throws Exception {
        By button = getRecordControlButtonByName(buttonName);
        if (button.equals(namePlayAudioRecorderButton)) {
            getElement(button).click();
        } else {
            clickElementWithRetryIfStillDisplayed(button);
        }
    }

    public void tapVideoMessageContainer(String username) throws Exception {
        final By locator = By.xpath(xpathTableCellFromUser.apply(username));
        getElement(locator).click();
    }

    public void tapAudioRecordWaitAndSwipe(int swipeDelaySeconds) throws Exception {
        WebElement recordAudioMessageBtn = getElement(nameAudioMessageButton);
        int y = getElement(nameConversationInput).getLocation().getY() - recordAudioMessageBtn.getLocation().getY();
        new TouchAction(getDriver()).press(recordAudioMessageBtn)
                .waitAction(swipeDelaySeconds * 1000)
                .moveTo(0, 3 * y)
                .waitAction(1000)
                .release()
                .perform();
    }

    public void longTapAudioMessage() throws Exception {
        this.getDriver().tap(1, getElement(nameAudioActionButton), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapPlayAudioMessageButton(int placeholderIndex) throws Exception {
        final By locator = By.xpath(xpathStrAudioActionButtonByIndex.apply(placeholderIndex));
        getElement(locator).click();
    }

    public void tapPlayAudioMessageButton() throws Exception {
        getElement(nameAudioActionButton).click();
    }

    public void longTapLocationContainer() throws Exception {
        this.getDriver().tap(1, getElement(classNameShareLocationContainer), DriverUtils.LONG_TAP_DURATION);
    }

    public void tapLocationContainer() throws Exception {
        getElement(classNameShareLocationContainer).click();
    }

    public void longTapLinkPreviewContainer() throws Exception {
        this.getDriver().tap(1, getElement(nameLinkPreviewSource), DriverUtils.LONG_TAP_DURATION);
    }

    public BufferedImage getPlayAudioMessageButtonScreenshot(int placeholderIndex) throws Exception {
        final By locator = By.xpath(xpathStrAudioActionButtonByIndex.apply(placeholderIndex));
        return this.getElementScreenshot(getElement(locator)).orElseThrow(
                () -> new IllegalStateException(
                        String.format("Cannot get a screenshot of Play/Pause button on audio container #%d",
                                placeholderIndex))
        );
    }

    public BufferedImage getFirstPlayAudioMessageButtonScreenshot() throws Exception {
        return getPlayAudioMessageButtonScreenshot(1);
    }

    public BufferedImage getSecondPlayAudioMessageButtonScreenshot() throws Exception {
        return getPlayAudioMessageButtonScreenshot(2);
    }

    public boolean isRecordControlButtonVisible(String buttonName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getRecordControlButtonByName(buttonName));
    }

    private String getAudioMessageRecordTimeLabelValue() throws Exception {
        return getElement(nameAudioRecordTimeLabel).getAttribute("value");
    }

    private String getAudioMessagePlaceholderTimeLabelValue() throws Exception {
        return getElement(nameAudioPlaceholderTimeLabel).getAttribute("value");
    }

    public boolean isPlaceholderAudioMessageButtonState(String buttonState, int index) throws Exception {
        final By locator = By.xpath(placeholderAudioMessageButtonStateByIndex.apply(buttonState, index));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isPlaceholderTimeLabelValueChanging() throws Exception {
        String startTime = getAudioMessagePlaceholderTimeLabelValue();
        Thread.sleep(1000);
        String currentTime = getAudioMessagePlaceholderTimeLabelValue();
        return !startTime.equals(currentTime);
    }

    public boolean isRecordTimeLabelValueChanging() throws Exception {
        String startTime = getAudioMessageRecordTimeLabelValue();
        Thread.sleep(1000);
        String currentTime = getAudioMessageRecordTimeLabelValue();
        return !startTime.equals(currentTime);
    }

    public boolean isUserNameVisibleOnUpperToolbar(String contact) throws Exception {
        final By locator = By.xpath(xpathStrToolbarByConversationName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isRecordControlButtonState(String buttonState) throws Exception {
        final By locator = By.xpath(recordControlButtonWithState.apply(buttonState));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isDefaultReceivedShareLocationAddressVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameDefaultRecievedLocationAddress);
    }

    public boolean isDefaultReceivedShareLocationAddressNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameDefaultRecievedLocationAddress);
    }

    public boolean isDefaultSentShareLocationAddressVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameDefaultSentLocationAddress);
    }

    public boolean isDefaultSentShareLocationAddressNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameDefaultSentLocationAddress);
    }

    public boolean isDefaultMapApplicationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathDefaultMapApplication, 15);
    }

    private By getContainerIdentifier(String name) {
        switch (name.toLowerCase()) {
            case "media":
                return xpathMediaContainerCell;
            case "video message":
                return nameVideoMessageActionButton;
            case "audio message recorder":
                return nameAudioRecorderCancelButton;
            case "audio message":
                return nameAudioActionButton;
            case "location map":
                return classNameShareLocationContainer;
            case "link preview":
                return nameLinkPreviewSource;
            default:
                throw new IllegalArgumentException(String.format("Unknown container %s", name));
        }
    }

    public boolean isContainerVisible(String name) throws Exception {
        final By locator = getContainerIdentifier(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, MAX_CONTAINER_APPEARANCE_TIME);
    }

    public boolean isContainerInvisible(String name) throws Exception {
        final By locator = getContainerIdentifier(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isLinkPreviewImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameLinkPreviewImage);
    }

    public boolean isLinkPreviewImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLinkPreviewImage);
    }

    public boolean isFileTransferMenuItemVisible(String itemName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), MobileBy.AccessibilityId(itemName));
    }

    public int getMessageHeight(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByExactText.apply(msg));
        return getElement(locator).getSize().getHeight();
    }
}