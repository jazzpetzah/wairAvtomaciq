package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import edu.emory.mathcs.backport.java.util.Arrays;
import io.appium.java_client.MobileBy;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConversationViewPage extends IOSPage {
    private static final By nameConversationBackButton = MobileBy.AccessibilityId("ConversationBackButton");

    private static final String nameStrConversationInputField = "inputField";

    private static final By fbNameConversationInput = FBBy.AccessibilityId(nameStrConversationInputField);

    private static final Function<String, String> xpathStrConversationInputByValue = value ->
            String.format("//XCUIElementTypeTextView[@name='%s' and @value='%s']",
                    nameStrConversationInputField, value);

    private static final By nameConversationInputAvatar = MobileBy.AccessibilityId("authorImage");

    private static final String DEFAULT_INPUT_PLACEHOLDER_TEXT = "TYPE A MESSAGE";
    private static final By nameInputPlaceholderText = MobileBy.AccessibilityId(DEFAULT_INPUT_PLACEHOLDER_TEXT);

    protected static final By nameYouRenamedConversation = MobileBy.AccessibilityId("YOU RENAMED THE CONVERSATION");

    /**
     * !!! The actual message order in DOM is reversed relatively to the messages order in the conversation view
     */
    private static final String xpathStrAllEntries = "//XCUIElementTypeTable/XCUIElementTypeCell";
    private static final By xpathAllEntries = By.xpath(xpathStrAllEntries);
    private static final String xpathStrRecentEntry = xpathStrAllEntries + "[1]";
    private static final By xpathRecentEntry = By.xpath(xpathStrRecentEntry);

    private static final String xpathStrAllTextMessages = xpathStrAllEntries +
            "/XCUIElementTypeTextView[boolean(string(@value))]";
    private static final By xpathAllTextMessages = By.xpath(xpathStrAllTextMessages);

    private static final Function<String, String> xpathStrRecentMessageByTextPart = text ->
            String.format("%s[1]/XCUIElementTypeTextView[contains(@value, '%s')]", xpathStrAllEntries, text);

    private static final Function<String, String> xpathStrRecentMessageByExactText = text ->
            String.format("%s[1][@value='%s']", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrMessageByTextPart = text ->
            String.format("%s[contains(@value, '%s')]", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrMessageByExactText = text ->
            String.format("%s[@value='%s']", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrSystemMessageByText = text ->
            String.format("//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@value='%s'] ]",
                    text.toUpperCase());

    private static final String xpathStrImageCells = xpathStrAllEntries + "[@name='ImageCell']";
    private static final By xpathImageCell = By.xpath(xpathStrImageCells);
    private static final By fbXpathRecentImageCell = FBBy.xpath(String.format("(%s)[1]", xpathStrImageCells));

    private static final By fbXpathMediaContainerCell =
            FBBy.xpath("//XCUIElementTypeTextView[contains(@value, '://')]/following-sibling::XCUIElementTypeButton");

    private static final By xpathGiphyImage =
            By.xpath("//XCUIElementTypeCell[ .//*[contains(@value, 'via giphy.com')] ]" +
                    "/following-sibling::XCUIElementTypeCell[@name='ImageCell']");

    private static final By namePlayButton = MobileBy.AccessibilityId("mediaBarPlayButton");

    private static final By namePauseButton = MobileBy.AccessibilityId("mediaBarPauseButton");

    private static final By xpathConversationPage = By.xpath("//XCUIElementTypeTable");

    private static final By nameMediaBarCloseButton = MobileBy.AccessibilityId("mediabarCloseButton");

    private static final By nameTitle = MobileBy.AccessibilityId("playingMediaTitle");

    private static final By nameGifButton = MobileBy.AccessibilityId("gifButton");

    public static final Function<String, String> xpathStrMissedCallButtonByContact = name ->
            String.format("//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s CALLED'] ]" +
                    "/XCUIElementTypeButton[@name='ConversationMissedCallButton']", name.toUpperCase());

    public static final By xpathStrMissedCallButtonByYourself =
            By.xpath(xpathStrMissedCallButtonByContact.apply("you"));

    public static final Function<String, String> xpathStrConnectingToUserLabelByName = name -> String.format(
            "//XCUIElementTypeStaticText[contains(@name, 'CONNECTING TO %s.')]", name.toUpperCase());

    private static final By nameShieldIconNextToInput = MobileBy.AccessibilityId("verifiedConversationIndicator");

    public static final String MEDIA_STATE_PLAYING = "playing";

    public static final String MEDIA_STATE_PAUSED = "paused";

    public static final String MEDIA_STATE_STOPPED = "ended";

    private static final By fbNameCursorSketchButton = FBBy.AccessibilityId("sketchButton");
    protected static final By fbNameAddPictureButton = FBBy.AccessibilityId("photoButton");
    private static final By fbNamePingButton = FBBy.AccessibilityId("pingButton");
    private static final By fbNameFileTransferButton = FBBy.AccessibilityId("uploadFileButton");
    private static final By fbNameVideoMessageButton = FBBy.AccessibilityId("videoButton");
    private static final By fbNameAudioMessageButton = FBBy.AccessibilityId("audioButton");
    private static final By fbNameShareLocationButton = FBBy.AccessibilityId("locationButton");

    private static final String xpathStrConversationViewTopBar =
            "//XCUIElementTypeNavigationBar[ ./XCUIElementTypeButton[@name='Back'] ]";
    private static final By xpathConversationViewTopBar = By.xpath(xpathStrConversationViewTopBar);
    private static Function<String, String> xpathStrToolbarByConversationName = name ->
            String.format("%s//XCUIElementTypeButton[starts-with(@name, '%s')]",
                    xpathStrConversationViewTopBar, name.toUpperCase());
    private static Function<String, String> xpathStrToolbarByExpr = expr ->
            String.format("%s//XCUIElementTypeButton[%s]", xpathStrConversationViewTopBar, expr);

    private static final By fbNameEllipsisButton = FBBy.AccessibilityId("showOtherRowButton");
    private static final By xpathAudioCallButton = MobileBy.AccessibilityId("audioCallBarButton");
    private static final By xpathVideoCallButton = MobileBy.AccessibilityId("videoCallBarButton");
    private static final By xpathConversationDetailsButton = By.xpath(xpathStrConversationViewTopBar +
            "/XCUIElementTypeButton[@name='Back']/following::" +
            "XCUIElementTypeButton[not(@name='ConversationBackButton') and boolean(string(@label))]");

    private static final By nameToManyPeopleAlert = MobileBy.AccessibilityId("Too many people to call");

    private static final Function<String, String> xpathStrUserNameInUpperToolbar = text ->
            String.format("%s//XCUIElementTypeButton[starts-with(@name, '%s')]", xpathStrConversationViewTopBar,
                    text.toUpperCase());

    private static final String nameStrFileTransferTopLabel = "FileTransferTopLabel";
    private static final By nameFileTransferTopLabel = MobileBy.AccessibilityId(nameStrFileTransferTopLabel);
    private static final Function<String, String> xpathTransferTopLabelByFileName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s' and @value='%s']", nameStrFileTransferTopLabel,
                    name.toUpperCase());

    private static final String nameStrFileTransferBottomLabel = "FileTransferBottomLabel";
    private static final By fbNameFileTransferBottomLabel = FBBy.AccessibilityId(nameStrFileTransferBottomLabel);
    private static final Function<String, String> xpathTransferBottomLabelByExpr = expr ->
            String.format("//XCUIElementTypeStaticText[@name='%s' and %s]", nameStrFileTransferBottomLabel, expr);
    private static final By nameFileTransferActionButton = MobileBy.AccessibilityId("FileTransferActionButton");

    private static final Function<String, String> xpathStrFilePreviewByFileName = fileName ->
            String.format("//XCUIElementTypeNavigationBar[@name='%s']", fileName);

    private static final By nameGenericFileShareMenu = MobileBy.AccessibilityId("Cancel");

    private static final By xpathFileUploadingLabel =
            By.xpath("//XCUIElementTypeStaticText[contains(@value,'UPLOADING…')]");

    private static final By nameShareButton = MobileBy.AccessibilityId("Share");

    private static final By fbNameVideoMessageActionButton = FBBy.AccessibilityId("VideoActionButton");

//    private static final By nameVideoMessageSizeLabel = MobileBy.AccessibilityId("VideoSizeLabel");

    private static final By fbNameAudioRecorderCancelButton = FBBy.AccessibilityId("audioRecorderCancel");

    private static final By nameSendAudioMessageButton = MobileBy.AccessibilityId("audioRecorderSend");

    private static final String strNamePlayAudioRecorderButton = "audioRecorderPlay";

    private static final By namePlayAudioRecorderButton = MobileBy.AccessibilityId(strNamePlayAudioRecorderButton);

    private static final Function<String, String> recordControlButtonWithState = state ->
            String.format("//XCUIElementTypeButton[@name='%s' and @value='%s']", strNamePlayAudioRecorderButton, state);

    private static final By nameAudioRecordTimeLabel = MobileBy.AccessibilityId("audioRecorderTimeLabel");

    private static final By nameAudioPlaceholderTimeLabel = MobileBy.AccessibilityId("AudioTimeLabel");

    private static final String strNameAudioActionButton = "AudioActionButton";
    private static final By fbNameAudioActionButton = FBBy.AccessibilityId(strNameAudioActionButton);

    private static final Function<Integer, String> xpathStrAudioActionButtonByIndex = index ->
            String.format("(//*[@name='%s'])[%s]", strNameAudioActionButton, index);

    private static final FunctionFor2Parameters<String, String, Integer> placeholderAudioMessageButtonStateByIndex =
            (buttonState, index) ->
                    String.format("(//XCUIElementTypeButton[@name='%s'])[%s][@value='%s']", strNameAudioActionButton,
                            index, buttonState);

    private static final By fbXpathShareLocationContainer = FBBy.xpath("//XCUIElementTypeMap/parent::*");

    private static final By nameDefaultMapApplication = MobileBy.AccessibilityId("CalloutArrow.png");

    private static final By fbNameLinkPreviewSource = FBBy.AccessibilityId("linkPreviewSource");

    private static final By nameLinkPreviewImage = MobileBy.AccessibilityId("linkPreviewImage");

    private static final Function<String, String> xpathStrDeleteOnLabelForUser = name ->
            String.format(
                    "//XCUIElementTypeCell[@name='%s']//XCUIElementTypeStaticText[starts-with(@label, 'Deleted on')]",
                    name.toUpperCase());

    private static final By nameUndoEdit = MobileBy.AccessibilityId("undoButton");
    private static final By nameConfirmEdit = MobileBy.AccessibilityId("confirmButton");
    private static final By nameCancelEdit = MobileBy.AccessibilityId("cancelButton");

    private static final Function<String, String> xpathStrLinkPreviewSrcByText = text ->
            String.format("//XCUIElementTypeStaticText[@name='linkPreviewSource' and @value='%s']",
                    getDomainName(text).toLowerCase());

    private static final FunctionFor2Parameters<String, String, Integer> xpathMessageByTextAndIndex =
            (messageText, index) -> String.format("%s[%s]/XCUIElementTypeTextView[@value='%s']",
                    xpathStrAllEntries, index, messageText);

    private static final By nameLikeButton = MobileBy.AccessibilityId("likeButton");

    private static final By fbNameSketchOnImageButton = FBBy.AccessibilityId("sketchOnImageButton");
    private static final By fbNameFullScreenOnImageButton = FBBy.AccessibilityId("openFullScreenButton");

    private static final By nameRecentMessageToolbox = MobileBy.AccessibilityId("MessageToolbox");

    private static final By fbXpathUploadMenu =
            FBBy.xpath("//XCUIElementTypeButton[@label='Cancel']/preceding-sibling::*[1]");

    protected static final String[] UPLOAD_MENU_ITEMS = new String[]{
            "Record a video", "Videos", "20 MB file", "Big file",
            "group-icon@3x.png", "CountryCodes.plist", "iCloud"
    };

    private static final int MAX_APPEARANCE_TIME = 20;

    private static final String FTRANSFER_MENU_DEFAULT_PNG = "group-icon@3x.png";
    private static final String FTRANSFER_MENU_TOO_BIG = "Big file";

    private static final Logger log = ZetaLogger.getLog(ConversationViewPage.class.getSimpleName());

    public ConversationViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static String getDomainName(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            // e.printStackTrace();
            return url;
        }
        final String domain = uri.getHost();
        return domain.toLowerCase().startsWith("www.") ? domain.substring(4) : domain;
    }

    public boolean isPartOfTextMessageVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByTextPart.apply(msg));
        return isElementDisplayed(locator);
    }

    public boolean waitUntilTextMessageIsNotVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByTextPart.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilPartOfTextMessageIsNotVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrRecentMessageByExactText.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapVideoCallButton() throws Exception {
        getElement(xpathVideoCallButton).click();
    }

    public boolean isVideoCallButtonOnToolbarVisible() throws Exception {
        return isElementDisplayed(xpathVideoCallButton);
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
        return isElementDisplayed(xpathAudioCallButton);
    }

    public boolean isAudioCallButtonOnToolbarNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathAudioCallButton);
    }

    public int getNumberOfMessageEntries() throws Exception {
        return selectVisibleElements(xpathAllEntries).size();
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return isElementDisplayed(fbNameConversationInput, 10);
    }

    public boolean waitForCursorInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), fbNameConversationInput);
    }

    public void clickOnCallButtonForContact(String contact) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(contact));
        getElement(locator).click();
    }

    public void tapOnCursorInput() throws Exception {
        getElement(fbNameConversationInput).click();
    }

    public void clearTextInput() throws Exception {
        getElement(fbNameConversationInput).clear();
    }

    public boolean isCurrentInputTextEqualTo(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrConversationInputByValue.apply(expectedMsg));
        return isElementDisplayed(locator, 3);
    }

    public boolean isRecentMessageContain(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrRecentMessageByTextPart.apply(expectedText));
        return isElementDisplayed(locator);
    }

    public boolean isLastMessageEqual(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrRecentMessageByExactText.apply(expectedText));
        return isElementDisplayed(locator);
    }

    private boolean isMediaBarPauseButtonVisible() throws Exception {
        return isElementDisplayed(namePauseButton, 3);
    }

    private void clickMediaBarPauseButton() throws Exception {
        getElement(namePauseButton, "Pause button is not visible on media bar").click();
    }

    public void pauseMediaContent() throws Exception {
        clickMediaBarPauseButton();
    }

    private boolean isMediaBarPlayButtonVisible() throws Exception {
        return isElementDisplayed(namePlayButton, 3);
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
        return isElementDisplayed(locator);
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

    public void openConversationDetails() throws Exception {
        getElement(xpathConversationDetailsButton).click();
        // Wait for animation
        Thread.sleep(500);
    }

    public boolean isMediaContainerVisible() throws Exception {
        return isElementDisplayed(fbXpathMediaContainerCell);
    }

    public boolean isMediaBarDisplayed() throws Exception {
        return isElementDisplayed(nameTitle);
    }

    public boolean isMediaBarNotDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameTitle);
    }

    public void tapHoldTextInput() throws Exception {
        ((FBElement) getElement(fbNameConversationInput)).longTap();
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

    public void typeMessage(String message, boolean shouldSend) throws Exception {
        final FBElement convoInput = (FBElement) getElement(fbNameConversationInput,
                "Conversation input is not visible after the timeout");
        final boolean wasKeyboardInvisible = this.isKeyboardInvisible(2);
        if (wasKeyboardInvisible) {
            convoInput.click();
            // Wait for keyboard opening animation
            Thread.sleep(KEYBOARD_OPEN_ANIMATION_DURATION);
        }
        convoInput.sendKeys(message);
        if (shouldSend) {
            this.tapKeyboardCommitButton();
        }
    }

    public void typeMessage(String message) throws Exception {
        typeMessage(message, false);
    }

    public void openGifPreviewPage() throws Exception {
        getElement(nameGifButton).click();
    }

    public boolean isConnectingToUserConversationLabelVisible(String username) throws Exception {
        final By locator = By.xpath(xpathStrConnectingToUserLabelByName.apply(username));
        return isElementDisplayed(locator);
    }

    public boolean isGiphyImageVisible() throws Exception {
        return isElementDisplayed(xpathGiphyImage);
    }

    public boolean isUserAvatarNextToInputVisible() throws Exception {
        return isElementDisplayed(nameConversationInputAvatar);
    }

    public boolean isUserAvatarNextToInputInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConversationInputAvatar);
    }

    public boolean isShieldIconVisibleNextToInputField() throws Exception {
        return isElementDisplayed(nameShieldIconNextToInput);
    }

    public boolean isShieldIconInvisibleNextToInputField() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameShieldIconNextToInput);
    }

    public void resendLastMessageInDialogToUser() throws Exception {
        getElement(nameRecentMessageToolbox).click();
    }

    public BufferedImage getMediaContainerStateGlyphScreenshot() throws Exception {
        final BufferedImage containerScreen =
                this.getElementScreenshot(getElement(fbXpathMediaContainerCell)).orElseThrow(() ->
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
        final FBElement convoInput = (FBElement) getElement(fbNameConversationInput,
                "Conversation input is not visible after the timeout");
        convoInput.click();
        // Wait for animation
        Thread.sleep(KEYBOARD_OPEN_ANIMATION_DURATION);
        this.inputStringFromPasteboard(convoInput, true);
    }

    public boolean areInputToolsVisible() throws Exception {
        return isElementDisplayed(fbNameAddPictureButton) || isElementDisplayed(fbNameEllipsisButton);
    }

    public boolean areInputToolsInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), fbNameAddPictureButton) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(), fbNameEllipsisButton);
    }

    public boolean isMissedCallButtonVisibleFor(String username) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(username));
        return isElementDisplayed(locator);
    }

    public boolean isSystemMessageVisible(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByText.apply(expectedMsg));
        return isElementDisplayed(locator);
    }

    public boolean isUpperToolbarVisible() throws Exception {
        return isElementDisplayed(xpathConversationViewTopBar);
    }

    public boolean isTooManyPeopleAlertVisible() throws Exception {
        return isElementDisplayed(nameToManyPeopleAlert);
    }

    public boolean isUserNameInUpperToolbarVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserNameInUpperToolbar.apply(name));
        return isElementDisplayed(locator);
    }

    public boolean isYouCalledMessageAndButtonVisible() throws Exception {
        return isElementDisplayed(xpathStrMissedCallButtonByYourself);
    }

    public Optional<BufferedImage> getRecentPictureScreenshot() throws Exception {
        return getElementScreenshot(getElement(fbXpathRecentImageCell));
    }

    protected String expandFileTransferItemName(String itemName) {
        switch (itemName) {
            case "FTRANSFER_MENU_DEFAULT_PNG":
                return FTRANSFER_MENU_DEFAULT_PNG;
            case "TOO_BIG":
                return FTRANSFER_MENU_TOO_BIG;
            default:
                return itemName;
        }
    }

    public void tapFileTransferMenuItem(String itemName) throws Exception {
        itemName = expandFileTransferItemName(itemName);
        final FBElement uploadMenu = (FBElement) getElement(fbXpathUploadMenu);
        final Dimension menuSize = uploadMenu.getSize();
        // FIXME: Workaround for menu items positions
        final int itemIdx = Arrays.asList(UPLOAD_MENU_ITEMS).indexOf(itemName);
        if (itemIdx < 0) {
            throw new IllegalArgumentException(String.format("Unknown upload menu item '%s'", itemName));
        }
        uploadMenu.tap(menuSize.getWidth() / 8,
                menuSize.getHeight() / UPLOAD_MENU_ITEMS.length * itemIdx +
                        menuSize.getHeight() / UPLOAD_MENU_ITEMS.length / 2);
    }

    public boolean isFileTransferTopLabelVisible() throws Exception {
        return isElementDisplayed(nameFileTransferTopLabel);
    }

    public boolean isFileTransferTopLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameFileTransferTopLabel);
    }

    public boolean isFileTransferBottomLabelVisible() throws Exception {
        return isElementDisplayed(fbNameFileTransferBottomLabel);
    }

    public void tapFileTransferActionButton() throws Exception {
        getElement(nameFileTransferActionButton).click();
    }

    private By getInputToolButtonByName(String btnName) {
        switch (btnName.toLowerCase()) {
            case "add picture":
                return fbNameAddPictureButton;
            case "ping":
                return fbNamePingButton;
            case "sketch":
                return fbNameCursorSketchButton;
            case "file transfer":
                return fbNameFileTransferButton;
            case "video message":
                return fbNameVideoMessageButton;
            case "audio message":
                return fbNameAudioMessageButton;
            case "share location":
                return fbNameShareLocationButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown input tools button name %s", btnName));
        }
    }

    public boolean isInputToolButtonByNameVisible(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (isElementDisplayed(locator)) {
            return true;
        } else {
            this.tapAtTheCenterOfElement((FBElement) getElement(fbNameEllipsisButton));
            return isElementDisplayed(locator, 3);
        }
    }

    public boolean isInputToolButtonByNameNotVisible(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (DriverUtils.waitUntilLocatorDissapears(getDriver(), locator) &&
                DriverUtils.waitUntilLocatorDissapears(getDriver(), fbNameEllipsisButton)) {
            return true;
        } else {
            this.tapAtTheCenterOfElement((FBElement) getElement(fbNameEllipsisButton));
            return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, 3);
        }
    }

    private boolean isTestImageUploaded = false;

    public void tapInputToolButtonByName(String name) throws Exception {
        final By locator = getInputToolButtonByName(name);
        if (locator.equals(fbNameAddPictureButton) && !isTestImageUploaded &&
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
        return isElementDisplayed(nameInputPlaceholderText);
    }

    public boolean isInputPlaceholderTextInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameInputPlaceholderText);
    }

    public void scrollToTheBottom() throws Exception {
        getElement(fbNameConversationInput).click();
        if (!isElementDisplayed(xpathRecentEntry)) {
            throw new IllegalStateException("Failed to scroll to the bottom of the conversation");
        }
    }

    public void tapMessageByText(boolean isLongTap, boolean isDoubleTap, String msg) throws Exception {
        final FBElement el = (FBElement) getElement(FBBy.xpath(xpathStrMessageByTextPart.apply(msg)));
        // The tap should be performed precisely on the text
        final int tapPercentX = 10;
        final int tapPercentY = 50;
        if (isDoubleTap) {
            doubleClickAt(el, tapPercentX, tapPercentY);
        } else {
            if (isLongTap) {
                longClickAt(el, tapPercentX, tapPercentY);
            } else {
                this.tapByPercentOfElementSize(el, tapPercentX, tapPercentY);
            }
        }
    }

    private WebElement locateCursorToolButton(By locator) throws Exception {
        final Optional<WebElement> toolButton = getElementIfDisplayed(locator, 3);
        if (toolButton.isPresent()) {
            return toolButton.get();
        } else {
            this.tapAtTheCenterOfElement((FBElement) getElement(fbNameEllipsisButton));
            // Wait for animation
            Thread.sleep(500);
            return getElement(locator);
        }
    }

    public void longTapInputToolButtonByName(String btnName) throws Exception {
        final FBElement dstElement = (FBElement) locateCursorToolButton(getInputToolButtonByName(btnName));
        dstElement.longTap();
    }

    public void longTapWithDurationInputToolButtonByName(String btnName, int durationSeconds) throws Exception {
        final FBElement dstElement = (FBElement) locateCursorToolButton(getInputToolButtonByName(btnName));
        dstElement.touchAndHold(durationSeconds);
    }

    private By getRecordControlButtonByName(String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "send":
                return nameSendAudioMessageButton;
            case "cancel":
                return fbNameAudioRecorderCancelButton;
            case "play":
                return namePlayAudioRecorderButton;
            default:
                throw new IllegalArgumentException(String.format("Button '%s' is not known as a record control button",
                        buttonName));
        }
    }

    public void tapRecordControlButton(String buttonName) throws Exception {
        //sometimes for such dynamic elements like record bar appium do not get the actual page source
        //in some cases this method helps to refresh elements tree.
        this.printPageSource();
        By button = getRecordControlButtonByName(buttonName);
        if (button.equals(namePlayAudioRecorderButton)) {
            getElement(button).click();
        } else {
            tapElementWithRetryIfStillDisplayed(button);
        }
    }

    public void tapAudioRecordWaitAndSwipe(int swipeDelaySeconds) throws Exception {
        final FBElement recordAudioMessageBtn = (FBElement) getElement(fbNameAudioMessageButton);
        recordAudioMessageBtn.touchAndHold(swipeDelaySeconds);
        getElement(nameSendAudioMessageButton).click();
//        new TouchAction(getDriver()).press(recordAudioMessageBtn)
//                .waitAction(swipeDelaySeconds * 1000)
//                .moveTo(getElement(xpathAudioCallButton))
//                .release()
//                .perform();
    }

    public void tapPlayAudioMessageButton(int placeholderIndex) throws Exception {
        final By locator = By.xpath(xpathStrAudioActionButtonByIndex.apply(placeholderIndex));
        getElement(locator).click();
    }

    public void tapPlayAudioMessageButton() throws Exception {
        getElement(fbNameAudioActionButton).click();
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
        return isElementDisplayed(getRecordControlButtonByName(buttonName));
    }

    private String getAudioMessageRecordTimeLabelValue() throws Exception {
        return getElement(nameAudioRecordTimeLabel).getAttribute("value");
    }

    private String getAudioMessagePlaceholderTimeLabelValue() throws Exception {
        return getElement(nameAudioPlaceholderTimeLabel).getAttribute("value");
    }

    public boolean isPlaceholderAudioMessageButtonState(String buttonState, int index) throws Exception {
        final By locator = By.xpath(placeholderAudioMessageButtonStateByIndex.apply(buttonState, index));
        return isElementDisplayed(locator);
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
        return isElementDisplayed(locator);
    }

    public boolean isRecordControlButtonState(String buttonState) throws Exception {
        final By locator = By.xpath(recordControlButtonWithState.apply(buttonState));
        return isElementDisplayed(locator);
    }

    public boolean isDefaultMapApplicationVisible() throws Exception {
        return isElementDisplayed(nameDefaultMapApplication, 15);
    }

    public boolean isLinkPreviewImageVisible() throws Exception {
        return isElementDisplayed(nameLinkPreviewImage);
    }

    public boolean isLinkPreviewImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLinkPreviewImage);
    }

    public boolean isFileTransferMenuItemVisible(String itemName) throws Exception {
        return isElementDisplayed(MobileBy.AccessibilityId(expandFileTransferItemName(itemName)),
                MAX_APPEARANCE_TIME);
    }

    public int getMessageHeight(String msg) throws Exception {
        final By locator = By.xpath(xpathStrMessageByExactText.apply(msg));
        return getElement(locator).getSize().getHeight();
    }

    public void selectDeleteMenuItem(String name) throws Exception {
        getElement(MobileBy.AccessibilityId(name)).click();
    }

    public boolean deleteMenuItemNotVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), MobileBy.AccessibilityId(name));
    }

    public boolean isDeletedOnLabelPresent(String name) throws Exception {
        final By locator = By.xpath(xpathStrDeleteOnLabelForUser.apply(name));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    private By getEditControlByName(String name) {
        switch (name.toLowerCase()) {
            case "undo":
                return nameUndoEdit;
            case "confirm":
                return nameConfirmEdit;
            case "cancel":
                return nameCancelEdit;
            default:
                throw new IllegalArgumentException(String.format("Unknown Edit control button '%s'", name));
        }
    }

    public void tapEditControlButton(String name) throws Exception {
        final By locator = getEditControlByName(name);
        getElement(locator).click();
        // Wait for the animation
        Thread.sleep(1000);
    }

    public boolean isLinkPreviewSourceVisible(String expectedSrc) throws Exception {
        final By locator = By.xpath(xpathStrLinkPreviewSrcByText.apply(expectedSrc));
        log.debug(String.format("Locating source text field on link preview: '%s'", locator));
        return isElementDisplayed(locator);
    }

    public boolean editControlButtonIsVisible(String name) throws Exception {
        final By locator = getEditControlByName(name);
        return isElementDisplayed(locator);
    }

    public boolean editControlButtonIsNotVisible(String name) throws Exception {
        final By locator = getEditControlByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isMessageByPositionDisplayed(String message, int position) throws Exception {
        final By locator = By.xpath(xpathMessageByTextAndIndex.apply(message, position));
        return isElementDisplayed(locator);
    }

    /**
     * Make sure all these locators resulting into FBElement instance
     */
    private By getContainerLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "image":
                return fbXpathRecentImageCell;
            case "media container":
            case "media":
                return fbXpathMediaContainerCell;
            case "location map":
                return fbXpathShareLocationContainer;
            case "file transfer placeholder":
                return fbNameFileTransferBottomLabel;
            case "audio message placeholder":
            case "audio message":
                return fbNameAudioActionButton;
            case "audio message recorder":
                return fbNameAudioRecorderCancelButton;
            case "video message":
                return fbNameVideoMessageActionButton;
            case "link preview":
                return fbNameLinkPreviewSource;
            default:
                throw new IllegalArgumentException(String.format("Unknown container name '%s'", name));
        }
    }

    public void tapContainer(String name, boolean isLongTap, boolean isDoubleTap) throws Exception {
        final By locator = getContainerLocatorByName(name);
        final FBElement dstElement = (FBElement) getElement(locator);
        if (isDoubleTap) {
            doubleClickAt(dstElement);
        } else if (isLongTap) {
            dstElement.longTap();
        } else {
            dstElement.click();
        }
    }

    public boolean isContainerVisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return isElementDisplayed(locator, MAX_APPEARANCE_TIME);
    }

    public boolean isContainerInvisible(String name) throws Exception {
        final By locator = getContainerLocatorByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public BufferedImage getLikeIconState() throws Exception {
        return getElementScreenshot(getElement(nameLikeButton)).orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot of Like/Unlike button")
        );
    }

    public void tapLikeIcon() throws Exception {
        getElement(nameLikeButton).click();
    }

    public boolean isLikeIconVisible() throws Exception {
        return isElementDisplayed(nameLikeButton);
    }

    public boolean isLikeIconInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLikeButton);
    }

    public void tapAtRecentMessage(int pWidth, int pHeight) throws Exception {
        DriverUtils.tapOnPercentOfElement(getDriver(), getElement(xpathRecentEntry), pWidth, pHeight);
    }

    public void tapImageButton(String buttonName) throws Exception {
        By locator = getImageButtonByName(buttonName);
        final FBElement dstElement = (FBElement) getElementIfExists(locator).orElseThrow(
                () -> new IllegalStateException(buttonName + "button can't be found")
        );
        this.tapAtTheCenterOfElement(dstElement);
    }

    private By getImageButtonByName(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "sketch":
                return fbNameSketchOnImageButton;
            case "fullscreen":
                return fbNameFullScreenOnImageButton;
            default:
                throw new Exception("Not recognized button name. Available 'sketch', 'fullscreen'");
        }
    }

    public void tapRecentMessageToolbox() throws Exception {
        getElement(nameRecentMessageToolbox).click();
    }

    public boolean waitUntilAllTextMessageAreNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathAllTextMessages);
    }

    public boolean waitUntilAnyTextMessagesAreVisible(int expectedCount) throws Exception {
        return waitUntilLocatorIsVisibleXTimes(xpathAllTextMessages, expectedCount);
    }

    public boolean waitUntilTextMessagesAreVisible(String s, int expectedCount) throws Exception {
        final By locator = By.xpath(xpathStrMessageByExactText.apply(s));
        return waitUntilLocatorIsVisibleXTimes(locator, expectedCount);
    }

    public boolean areNoImagesVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathImageCell);
    }

    public boolean areXImagesVisible(int expectedCount) throws Exception {
        return waitUntilLocatorIsVisibleXTimes(xpathImageCell, expectedCount);
    }

    private boolean waitUntilLocatorIsVisibleXTimes(By locator, int times) throws Exception {
        assert times > 0 : "Expected count should be greater than 0";
        final boolean result = isElementDisplayed(locator);
        if (times == 1) {
            return result;
        } else {
            if (result) {
                final long msStarted = System.currentTimeMillis();
                while (System.currentTimeMillis() - msStarted <=
                        Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass()))) {
                    if (selectVisibleElements(locator).size() >= times) {
                        return true;
                    }
                    Thread.sleep(500);
                }
            }
        }
        return false;
    }
}