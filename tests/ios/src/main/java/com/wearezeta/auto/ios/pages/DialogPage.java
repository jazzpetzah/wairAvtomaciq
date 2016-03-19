package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;


public class DialogPage extends IOSPage {
    private static final By nameConversationBackButton = MobileBy.AccessibilityId("ConversationBackButton");

    private static final By nameConversationCursorInput = MobileBy.AccessibilityId("ConversationTextInputField");

    private static final By namePlusButton = MobileBy.AccessibilityId("plusButton");

    private static final By nameOpenConversationDetails = MobileBy.AccessibilityId("ComposeControllerConversationDetailButton");

    protected static final By nameYouRenamedConversation = MobileBy.AccessibilityId("YOU RENAMED THE CONVERSATION");

    /**
     * !!! The actual message order in DOM is reversed relatively to the messages order in the conversation view
     */
    private static final String xpathStrAllEntries = xpathStrMainWindow + "/UIATableView/UIATableCell";
    private static final By xpathAllEntries = By.xpath(xpathStrAllEntries);

    private static final String xpathStrAllTextMessages = xpathStrAllEntries + "/UIATextView[boolean(string(@value))]";
    private static final By xpathAllTextMessages = By.xpath(xpathStrAllTextMessages);

    private static final Function<String, String> xpathStrLastMessageByTextPart = text ->
            String.format("%s[1][contains(@value, '%s')]", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrLastMessageByExactText = text ->
            String.format("%s[1][@value='%s']", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrMessageByTextPart = text ->
            String.format("%s[contains(@value, '%s')]", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathConversationEntryTemplate =
            xpathExpr -> String.format("//UIAStaticText[%s]", xpathExpr);

    private static final Function<String, String> xpathStrMessageCellByTextPart = text ->
            String.format("%s[contains(@value, '%s')]/parent::*", xpathStrAllTextMessages, text);

    private static final Function<String, String> xpathStrSystemMessageByText = text ->
            String.format("%s[@name='%s']", xpathStrAllEntries, text.toUpperCase());

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

    private static final By nameInputOptionsCloseButton = MobileBy.AccessibilityId("closeButton");

    private static final By nameTitle = MobileBy.AccessibilityId("playingMediaTitle");

    private static final By nameGifButton = MobileBy.AccessibilityId("rightMenuButton");

    private static final By nameSoundCloudButton = MobileBy.AccessibilityId("soundcloud");

    public static final Function<String, String> xpathStrMissedCallButtonByContact = name -> String.format(
            "//UIATableCell[.//*[@name='%s CALLED']]/UIAButton[@name='ConversationMissedCallButton']",
            name.toUpperCase());

    private static final By xpathUserAvatarNextToInput = By.xpath(
            "//UIAImage[following-sibling::UIATextView[@name='ConversationTextInputField'] and @visible='true']");

    public static final Function<String, String> xpathStrConnectingToUserLabelByName = name -> String.format(
            "//UIAStaticText[contains(@name, 'CONNECTING TO %s.')]", name.toUpperCase());

    protected static final By nameCameraLibraryButton = MobileBy.AccessibilityId("cameraLibraryButton");

    public static final Function<String, String> xpathStrConnectedToUserLabelByName = name -> String.format(
            "//UIAStaticText[contains(@name, 'CONNECTED TO %s')]", name.toUpperCase());

    private static final By nameShieldIconNextToInput = MobileBy.AccessibilityId("verifiedConversationIndicator");

    public static final String MEDIA_STATE_PLAYING = "playing";

    public static final String MEDIA_STATE_PAUSED = "paused";

    public static final String MEDIA_STATE_STOPPED = "ended";

    private static final By nameCursorSketchButton = MobileBy.AccessibilityId("ComposeControllerSketchButton");
    protected static final By nameAddPictureButton = MobileBy.AccessibilityId("ComposeControllerPictureButton");
    private static final By namePingButton = MobileBy.AccessibilityId("ComposeControllerPingButton");

    //FIXME: Add accessibility locator
    private static final By xpathCallButton =
            By.xpath("//UIANavigationBar[@name='ConversationView']/UIAButton[last()]");
    //FIXME: Add accessibility locator
    private static final By xpathVideoCallButton =
            By.xpath("//UIANavigationBar[@name='ConversationView']/UIAButton[last() - 1]");

    private final By[] inputTools = new By[]{namePingButton, nameCursorSketchButton, nameAddPictureButton};

    private static final Logger log = ZetaLogger.getLog(DialogPage.class.getSimpleName());

    public DialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
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

    public void tapPingButton() throws Exception {
        getElement(namePingButton).click();
    }

    public void tapVideoCallButton() throws Exception {
        getElement(xpathVideoCallButton).click();
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
        getElement(xpathCallButton).click();
    }

    public int getNumberOfMessageEntries() throws Exception {
        return selectVisibleElements(xpathAllEntries).size();
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationCursorInput, 10);
    }

    public boolean isCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationCursorInput);
    }

    public void clickOnCallButtonForContact(String contact) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(contact));
        getElement(locator).click();
    }

    public void tapOnCursorInput() throws Exception {
        getElement(nameConversationCursorInput).click();
    }

    public void clearTextInput() throws Exception {
        getElement(nameConversationCursorInput).clear();
    }

    public String getStringFromInput() throws Exception {
        return getElement(nameConversationCursorInput).getText();
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

    public void swipeRightToShowConversationTools() throws Exception {
        final WebElement convoInput = getElement(nameConversationCursorInput);
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            final Point inputLocation = convoInput.getLocation();
            final Dimension inputSize = convoInput.getSize();
            final Dimension windowSize = getDriver().manage().window().getSize();
            IOSSimulatorHelper.swipe((inputLocation.x + inputSize.width / 10.0) / windowSize.width,
                    (inputLocation.y + inputSize.height / 2.0) / windowSize.height, (inputLocation.x + inputSize.width) * 1.0
                            / windowSize.width, (inputLocation.y + inputSize.height / 2.0) / windowSize.height);
        } else {
            DriverUtils.swipeRight(this.getDriver(), convoInput, 1000);
        }
    }

    public void swipeLeftToShowInputCursor() throws Exception {
        final WebElement closeButton = getElement(nameInputOptionsCloseButton);
        final Point btnLocation = closeButton.getLocation();
        final Dimension btnSize = closeButton.getSize();
        final Dimension windowSize = getDriver().manage().window().getSize();
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.swipe(btnLocation.x * 1.0 / windowSize.width, (btnLocation.y + btnSize.height / 2.0)
                            / windowSize.height, (btnLocation.x - btnSize.width * 7.0) / windowSize.width,
                    (btnLocation.y + btnSize.height / 2.0) / windowSize.height);
        } else {
            DriverUtils.swipeByCoordinates(this.getDriver(), 1000, btnLocation.x * 100 / windowSize.width,
                    (btnLocation.y + btnSize.height / 2) * 100 / windowSize.height, (btnLocation.x - btnSize.width * 7) * 100
                            / windowSize.width, (btnLocation.y + btnSize.height / 2) * 100 / windowSize.height);
        }
    }

    public void pressAddPictureButton() throws Exception {
        getElement(nameAddPictureButton).click();
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

    public boolean isChatMessageContainsStringsExist(List<String> values) throws Exception {
        final String xpathExpr = String.join(" and ",
                values.stream().map(x -> String.format("contains(@name, '%s')", x.toUpperCase())).collect(Collectors.toList()));
        final By locator = By.xpath(xpathConversationEntryTemplate.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 10);
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

    private static final int TEXT_INPUT_HEIGHT = 150;
    private static final int TOP_BORDER_WIDTH = 40;

    public void openConversationDetails() throws Exception {
        getElementIfDisplayed(namePlusButton, 3).orElseGet(DummyElement::new).click();
        getElement(nameOpenConversationDetails).click();
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
        final WebElement textInput = getElement(nameConversationCursorInput);
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

    public void typeAndSendConversationMessage(String message) throws Exception {
        final WebElement convoInput = getElement(nameConversationCursorInput,
                "Conversation input is not visible after the timeout");
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            inputStringFromKeyboard(convoInput, message, true, true);
        } else {
            convoInput.click();
            // Wait for animation
            Thread.sleep(1000);
            convoInput.sendKeys(message);
            this.clickKeyboardCommitButton();
        }
    }

    public void typeMessage(String message) throws Exception {
        final WebElement convoInput = getElement(nameConversationCursorInput,
                "Conversation input is not visible after the timeout");
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            inputStringFromKeyboard(convoInput, message, true, false);
        } else {
            convoInput.click();
            // Wait for animation
            Thread.sleep(1000);
            convoInput.sendKeys(message);
        }
    }

    public boolean isTypeOrSlideExists(String msg) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), MobileBy.AccessibilityId(msg), 5);
    }

    public void clickOnPlayVideoButton() throws Exception {
        getElement(xpathMediaContainerCell).click();
    }

    public void openGifPreviewPage() throws Exception {
        getElement(nameGifButton).click();
    }

    public void openSketch() throws Exception {
        getElement(nameCursorSketchButton).click();
    }

    public boolean isMyNameInDialogDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), MobileBy.AccessibilityId(name.toUpperCase()));
    }

    public boolean isConnectedToUserStartedConversationLabelVisible(String username) throws Exception {
        final By locator = By.xpath(xpathStrConnectedToUserLabelByName.apply(username));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isConnectingToUserConversationLabelVisible(String username) throws Exception {
        final By locator = By.xpath(xpathStrConnectingToUserLabelByName.apply(username));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void navigateBack(int timeMilliseconds) throws Exception {
        swipeRight(timeMilliseconds, DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 30);
    }

    public void clickPlusButton() throws Exception {
        getElement(namePlusButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), namePlusButton)) {
            throw new IllegalStateException("The Details plus button is still visible");
        }
    }

    public boolean isPlusButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), namePlusButton);
    }

    public boolean waitPlusButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), namePlusButton);
    }

    public boolean isOpenConversationDetailsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameOpenConversationDetails);
    }

    public boolean verifyInputOptionsCloseButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameInputOptionsCloseButton);
    }

    public void clickInputOptionsCloseButton() throws Exception {
        getElement(nameInputOptionsCloseButton, "Close input options button is not visible").click();
    }

    public boolean isGiphyImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGiphyImage);
    }

    public void tapHoldImage() {
        try {
            this.getDriver().tap(1, getElement(xpathLastImageCell), 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserAvatarNextToInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathUserAvatarNextToInput);
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
        this.clickPopupPasteButton();
        final WebElement convoInput = getElement(nameConversationCursorInput,
                "Conversation input is not visible after the timeout");
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            inputStringFromKeyboard(convoInput, "", false, true);
        } else {
            convoInput.click();
            // Wait for animation
            Thread.sleep(1000);
            this.clickKeyboardCommitButton();
        }
    }

    public boolean areInputToolsVisible() throws Exception {
        for (By inputTool : inputTools) {
            if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), inputTool, 2)) {
                return false;
            }
        }
        return true;
    }

    public boolean areInputToolsInvisible() throws Exception {
        for (By inputTool : inputTools) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), inputTool, 2)) {
                return false;
            }
        }
        return true;
    }

    public boolean isMissedCallButtonVisibleFor(String username) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallButtonByContact.apply(username));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSystemMessageVisible(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrSystemMessageByText.apply(expectedMsg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
