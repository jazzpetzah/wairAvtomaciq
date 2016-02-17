package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class DialogPage extends IOSPage {
    private static final By nameConversationBackButton = By.name("ConversationBackButton");

    private static final By nameConversationCursorInput = By.name("ConversationTextInputField");

    private static final By namePlusButton = By.name("plusButton");

    private static final By nameOpenConversationDetails = By.name("ComposeControllerConversationDetailButton");

    protected static final By nameYouRenamedConversation = By.name("YOU RENAMED THE CONVERSATION");

    private static final By xpathLastChatMessage = By.xpath(xpathStrMainWindow
            + "/UIATableView[1]/UIATableCell[last()]/*[last()]");

    private static final By xpathMessageEntries = By.xpath(xpathStrMainWindow + "/UIATableView/UIATableCell");

    private static final String xpathStrImageCells = "//UIATableCell[@name='ImageCell']";
    private static final By xpathImageCell = By.xpath(xpathStrImageCells);
    private static final By xpathLastImageCell = By.xpath(String.format("(%s)[last()]", xpathStrImageCells));

    private static final By xpathMediaContainerCell =
            By.xpath("//UIATextView[contains(@value, '://')]/following-sibling::UIAButton");

    private static final By namePlayButton = By.name("mediaBarPlayButton");

    private static final By namePauseButton = By.name("mediaBarPauseButton");

    private static final By xpathConversationPage = By.xpath(xpathStrMainWindow + "/UIATableView[1]");

    private static final By nameMediaBarCloseButton = By.name("mediabarCloseButton");

    private static final By nameInputOptionsCloseButton = By.name("closeButton");

    private static final By nameTitle = By.name("playingMediaTitle");

    private static final Function<String, String> xpathStrDialogTitleBar = title -> String.format(
            "//UIAStaticText[@name='%s']", title);

    private static final By nameGifButton = By.name("rightMenuButton");

    private static final By xpathGiphyImage = By
            .xpath("//UIATextView[@name='via giphy.com']/following::UIATableCell[@name='ImageCell']");

    private static final By nameSoundCloudButton = By.name("soundcloud");

    private static final By xpathUserAvatarNextToInput = By
            .xpath("//UIAImage[following-sibling::UIATextView[@name='ConversationTextInputField'] and @visible='true']");

    private static final By xpathAllMessages = By.xpath(xpathStrMainWindow + "/UIATableView[1]/UIATableCell/UIATextView");

    private static final String xpathStrAllMessages = xpathStrMainWindow + "/UIATableView[1]/UIATableCell/UIATextView";

    private static final Function<String, String> xpathMessagesByText =
            text -> String.format("%s[starts-with(@value, '%s')]", xpathStrAllMessages, text);

    public static final Function<String, String> xpathFormatMissedCallButtonByContact = name -> String.format(
            "//UIATableCell[UIAStaticText[@name='%s CALLED']]/UIAButton[@name='ConversationMissedCallButton']", name.toUpperCase());

    private static final By xpathLastMessage = By.xpath(String.format("%s/UIATableView[1]/UIATableCell[last()]/UIATextView[1]",
            xpathStrMainWindow));

    public static final Function<String, String> xpathStrConnectingToUserLabelByName = name -> String.format(
            "//UIAStaticText[contains(@name, 'CONNECTING TO %s.')]", name.toUpperCase());

    protected static final By nameCameraLibraryButton = By.name("cameraLibraryButton");

    private static final Function<String, String> xpathStrMessageViewByText = text -> String.format(
            "//UIATextView[contains(@value, '%s')]", text);

    private static final Function<String, String> xpathStrLastItemByNameInDialog = name -> String.format(
            "//UIAStaticText[@name='%s'][last()]", name.toUpperCase());

    public static final Function<String, String> xpathStrConnectedToUserLabelByName = name -> String.format(
            "//UIAStaticText[contains(@name, 'CONNECTED TO %s')]", name.toUpperCase());

    private static final Function<String, String> xpathStartConversationEntryTemplate = xpathExpr -> String.format(
            "//UIAStaticText[%s]", xpathExpr);

    private static final By nameShieldIconNextToInput = By.name("verifiedConversationIndicator");

    private static final Function<String, String> xpathStrConvoMessageByText = text -> String.format(
            "%s//UIATableView//*[contains(@name, '%s')]", xpathStrMainWindow, text);

    private static final By xpathYouStartedUsingThisDeviceSystemMesssage = By
            .xpath("//UIATextView[@name='YOU STARTED USING THIS DEVICE']");

    private static final By xpathResendMessageButton = By.xpath("//UIATableView[1]/UIATableCell[last()]/UIAButton[1]");

    public static final String MEDIA_STATE_PLAYING = "playing";

    public static final String MEDIA_STATE_PAUSED = "paused";

    public static final String MEDIA_STATE_STOPPED = "ended";

    private static final By nameCursorSketchButton = By.name("ComposeControllerSketchButton");
    protected static final By nameAddPictureButton = By.name("ComposeControllerPictureButton");
    private static final By nameVideoCallButton = By.name("ComposeControllerVideoButton");
    private static final By nameCallButton = By.name("ComposeControllerVoiceButton");

    private final By[] inputTools = new By[]{nameCallButton, nameCursorSketchButton, nameAddPictureButton};

    public DialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isMessageVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConvoMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean waitUntilMessageIsNotVisible(String msg) throws Exception {
        final By locator = By.xpath(xpathStrConvoMessageByText.apply(msg));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void pressPingButton() throws Exception {
        getDriver().tap(1, getElement(nameCallButton), DriverUtils.LONG_TAP_DURATION);
    }

    public void pressVideoCallButton() throws Exception {
        getElement(nameVideoCallButton).click();
    }

    public void returnToContactList() throws Exception {
        getElement(nameConversationBackButton, "Back to list button is not visible").click();
    }

    public void pressCallButton() throws Exception {
        getElement(nameCallButton).click();
    }

    public int getNumberOfMessageEntries() throws Exception {
        return getElements(xpathMessageEntries).size();
    }

    public boolean waitForCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationCursorInput, 10);
    }

    public boolean isCursorInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConversationCursorInput);
    }

    public void clickOnCallButtonForContact(String contact) throws Exception {
        final By locator = By.xpath(xpathFormatMissedCallButtonByContact.apply(contact));
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

    public Optional<String> getLastMessageFromDialog() throws Exception {
        final Optional<WebElement> el = getElementIfDisplayed(xpathLastMessage);
        if (el.isPresent()) {
            return Optional.of(el.get().getText());
        }
        return Optional.empty();
    }

    public int getMessagesCount() throws Exception {
        return getMessagesCount(null);
    }

    public int getMessagesCount(String expectedMessage) throws Exception {
        By locator;
        if (expectedMessage == null) {
            locator = xpathAllMessages;
        } else {
            locator = By.xpath(xpathMessagesByText.apply(expectedMessage));
        }
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
            return getDriver().findElements(locator).size();
        }
        return 0;
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
        final By locator = By.xpath(xpathStartConversationEntryTemplate.apply(xpathExpr));
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
        Point coords = getElement(nameMainWindow).getLocation();
        Dimension elementSize = getElement(nameMainWindow).getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y + elementSize.height - TEXT_INPUT_HEIGHT,
                coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH, time);
    }

    public void swipeDialogPageDown() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.swipeDown();
        } else {
            DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationPage), 1000, 50, 30, 50, 95);
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

    public void scrollToEndOfConversation() throws Exception {
        try {
            this.getDriver().scrollToExact(getElement(xpathLastChatMessage).getText());
        } catch (WebDriverException e) {
            // Simply ignore
        }
    }

    public boolean isTitleBarDisplayed(String name) throws Exception {
        final By locator = By.xpath(xpathStrDialogTitleBar.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isTypeOrSlideExists(String msg) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(msg), 5);
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
        final By locator = By.xpath(xpathStrLastItemByNameInDialog.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
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

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCallButton);
    }

    public boolean isCameraButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameAddPictureButton);
    }

    public boolean isOpenSketchButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCursorSketchButton);
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
        final By locator = By.xpath(xpathStrMessageViewByText.apply(expectedLink));
        final WebElement el = getElement(locator);
        DriverUtils.tapByCoordinates(getDriver(), el, -el.getSize().width / 4, 0);
    }

    public boolean isShieldIconVisibleNextToInputField() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameShieldIconNextToInput);
    }

    public boolean isShieldIconInvisibleNextToInputField() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameShieldIconNextToInput);
    }

    public void clickThisDeviceLink() throws Exception {
        DriverUtils.tapByCoordinatesWithPercentOffcet(getDriver(),
                getElement(xpathYouStartedUsingThisDeviceSystemMesssage), 90, 50);
    }

    public void resendLastMessageInDialogToUser() throws Exception {
        getElement(xpathResendMessageButton).click();
    }

    public BufferedImage getMediaContainerStateGlyphScreenshot() throws Exception {
        final BufferedImage containerScreen =
                this.getElementScreenshot(getElement(xpathMediaContainerCell)).orElseThrow(() ->
                        new IllegalStateException("Cannot take a screenshot of media container"));
        final int stateGlyphWidth = containerScreen.getWidth() / 7;
        final int stateGlyphHeight = containerScreen.getHeight() / 7;
        final int stateGlyphX = (containerScreen.getWidth() - stateGlyphWidth) / 2;
        final int stateGlyphY = (containerScreen.getHeight() - stateGlyphHeight) / 2;
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
}
