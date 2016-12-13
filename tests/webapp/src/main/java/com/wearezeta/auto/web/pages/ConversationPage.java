package com.wearezeta.auto.web.pages;

import com.google.common.base.Function;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.Message;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import cucumber.api.PendingException;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

public class ConversationPage extends WebPage {

    private static final int TIMEOUT_I_SEE_MESSAGE = 20; // seconds
    private static final int TIMEOUT_LONG_MESSAGE = 30; // seconds
    private static final int TIMEOUT_IMAGE_MESSAGE_UPLOAD = 40; // seconds
    private static final int TIMEOUT_FILE_UPLOAD = 100; // seconds
    private static final int TIMEOUT_VIDEO_UPLOAD = 120; // seconds
    private static final int TIMEOUT_AUDIO_UPLOAD = 100; // seconds
    private static final int MISSED_CALL_MSG_TIMOEUT = 15;
    private static final int MAX_CALLING_BAR_VISIBILITY_TIMEOUT = 5; // seconds

    private static final Logger log = ZetaLogger.getLog(ConversationPage.class.getSimpleName());

    private static final String TOOLTIP_PEOPLE = "People";

    @FindBy(css = WebAppLocators.ConversationPage.cssFirstTimeExperienceMessage)
    private WebElement firstTimeExperienceMessage;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssMessageAmount)
    private List<WebElement> messageAmount;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssDeletedMessageAmount)
    private List<WebElement> deletedMessageAmount;

    @FindBy(css = WebAppLocators.ConversationPage.cssTitlebarLabel)
    private WebElement titlebarLabel;

    @FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
    private WebElement conversationInput;

    @FindBy(css = WebAppLocators.ConversationPage.cssCancelRequestButton)
    private WebElement cancelRequestButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssShowParticipantsButton)
    private WebElement showParticipants;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssEphemeralButton)
    private WebElement ephemeralButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssEphemeralTimers)
    private List<WebElement> ephemeralTimers;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssSendImageInput)
    private WebElement imagePathInput;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssSendFileInput)
    private WebElement filePathInput;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssPingButton)
    private WebElement pingButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssGIFButton)
    private WebElement gifButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssCallButton)
    private WebElement callButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssVideoCallButton)
    private WebElement videoCallButton;

    @FindBy(css = WebAppLocators.ConversationPage.cssLastMessage)
    private WebElement lastGenericMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssSecondLastMessage)
    private WebElement secondLastGenericMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssLastTextMessage)
    private WebElement lastTextMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssSecondLastTextMessage)
    private WebElement secondLastTextMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssMessages)
    private List<WebElement> messages;

    @FindBy(css = WebAppLocators.ConversationPage.cssImageEntries)
    private List<WebElement> pictures;

    @FindBy(css = WebAppLocators.ConversationPage.cssFirstImage)
    private WebElement firstPicture;

    @FindBy(css = WebAppLocators.ConversationPage.cssFullscreenImage)
    private WebElement fullscreenImage;

    @FindBy(xpath = WebAppLocators.ConversationPage.xpathXButton)
    private WebElement xButton;

    @FindBy(css = WebAppLocators.ConversationPage.cssUserAvatar)
    private WebElement userAvatar;

    @FindBy(css = WebAppLocators.ConversationPage.cssConnectedMessageUser)
    private WebElement connectedMessageUser;

    @FindBy(css = WebAppLocators.ConversationPage.cssConnectedMessageLabel)
    private WebElement connectedMessageLabel;

    @FindBy(css = WebAppLocators.ConversationPage.cssDoDelete)
    private WebElement doDeleteButton;

    @FindBy(css = WebAppLocators.ConversationPage.cssDoDeleteForEveryone)
    private WebElement doDeleteForEveryoneButton;

    @FindBy(css = WebAppLocators.ConversationPage.cssDoEditMessage)
    private WebElement doEditMessageButton;

    @FindBy(css = WebAppLocators.ConversationPage.cssLastEditTimestamp)
    private WebElement lastEditTimestamp;

    @FindBy(css = WebAppLocators.ConversationPage.cssSecondLastEditTimestamp)
    private WebElement secondLastEditTimestamp;

    @FindBy(css = WebAppLocators.ConversationPage.cssLastMsgHeader)
    private WebElement lastMsgHeader;

    @FindBy(css = WebAppLocators.ConversationPage.cssSecondLastMsgHeader)
    private WebElement secondLastMsgHeader;

    @FindBy(css = WebAppLocators.ConversationPage.cssLongMessageDialog)
    private WebElement longMessageDialog;

    @FindBy(css = WebAppLocators.ConversationPage.cssCloseResetSessionDialog)
    private WebElement closeResetSessionDialogButton;

    @FindBy(xpath = WebAppLocators.ConversationPage.xpathOKButtonOnLongMWarning)
    private WebElement oKButtonOnLongMWarning;

    @FindBy(xpath = WebAppLocators.ConversationPage.xpathXButtonOnLongMWarning)
    private WebElement xButtonOnLongMWarning;

    @FindBy(css = WebAppLocators.ConversationPage.cssXbuttonEdit)
    private WebElement xButtonEdit;

    @FindBy(css = WebAppLocators.ConversationPage.cssSharedLocation)
    private WebElement locationName;

    @FindBy(xpath = WebAppLocators.ConversationPage.xpathSharedLocationLink)
    private WebElement locationLink;

    @FindBy(css = WebAppLocators.ConversationPage.cssLinkTitle)
    private WebElement linkTitle;

    @FindBy(css = WebAppLocators.ConversationPage.cssLinkPreviewImage)
    private WebElement previewImage;

    @FindBy(css = WebAppLocators.ConversationPage.cssUsername)
    private WebElement uniqueUsername;

    public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void writeNewMessage(String message) throws Exception {
        conversationInput.sendKeys(message);
    }

    public void sendNewMessage() {
        conversationInput.sendKeys(Keys.ENTER);
    }

    /**
        Gets message id for messages started from the last (messageIndex = 1), to the top - third last (messageIndex = 3)
     */

    private String getMessageId(int messageIndex) throws Exception{
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessageByIndex.apply(messageIndex));
        return getDriver().findElement(lastMessageLocator).getAttribute("data-uie-uid");
    }

    public String getLastActionMessage() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssFirstAction);
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
        final List<WebElement> actionElements = this.getDriver().findElements(
                locator);
        return actionElements.get(actionElements.size() - 1).getText();
    }

    public void waitForTextMessageContains(String text) throws Exception {
        waitForTextMessageContains(new HashSet<String>(Arrays.asList(text)));
    }

    public void waitForTextMessageContains(Set<String> parts)
            throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssTextMessage);
        WebDriverWait wait = new WebDriverWait(getDriver(),
                DriverUtils.getDefaultLookupTimeoutSeconds());
        wait
                .withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .until(visibilityOfTextInElementsLocated(locator, parts));
    }

    public boolean waitForPresentMessageContains(String text) throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ConversationPage.cssTextMessage);
        WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_I_SEE_MESSAGE);
        return wait.withTimeout(TIMEOUT_I_SEE_MESSAGE, TimeUnit.SECONDS)
                .until(presenceOfTextInElementsLocated(locator, new HashSet<String>(Arrays.asList(text))));
    }

    public boolean waitForDisplayedMessageContains(String text, int timeout) throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ConversationPage.cssTextMessage);
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        return wait
                .withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .until(visibilityOfTextInElementsLocated(locator, new HashSet<String>(Arrays.asList(text))));
    }

    public boolean waitForDisplayedMessageContains(String text) throws Exception {
        return waitForDisplayedMessageContains(text, DriverUtils.getDefaultLookupTimeoutSeconds());
    }

    public int waitForNumberOfMessageHeadersContain(String text)
            throws Exception {
        return waitForNumberOfMessageHeadersContain(new HashSet<String>(Arrays.asList(text)));
    }

    public int waitForNumberOfMessageHeadersContain(Set<String> parts)
            throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssMessageHeader);
        Thread.sleep(DriverUtils.getDefaultLookupTimeoutSeconds() * 1000);
        return getNumberOfElementsContainingText(locator, parts);
    }

    public String getMessageIdFromMessageText(String messageText) throws Exception {
        By locator = By.xpath(WebAppLocators.ConversationPage.xpathMessageHeaderByText.apply(messageText));
        return getDriver().findElement(locator).getAttribute("data-uie-uid");
    }

    public String getUniqueUsername() throws Exception{
        return uniqueUsername.getText();
    }

    /**
     * An expectation for checking that a system message is visible that contains all strings of the expected strings.
     *
     * @param locator used to find the element
     * @param expectedTexts the strings that should be found in a certain system message
     * @return returns true if found
     */
    public static ExpectedCondition<Boolean> visibilityOfTextInElementsLocated(
            final By locator, final Set<String> expectedTexts) {
        return new ExpectedCondition<Boolean>() {
            List<String> lastElements = new ArrayList<String>();

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    lastElements = getTextOfDisplayedElements(locator, driver);
                    for (String element : lastElements) {
                        if (containsAllCaseInsensitive(element, expectedTexts)) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "visibility of text '" + expectedTexts
                        + "' in elements " + lastElements + " located by "
                        + locator;
            }
        };
    }

    /**
     * An expectation for checking that a system message is present in the dom that contains all strings of the expected
     * strings.
     *
     * @param locator used to find the element
     * @param expectedTexts the strings that should be found in a certain system message
     * @return returns true if found
     */
    public static ExpectedCondition<Boolean> presenceOfTextInElementsLocated(
            final By locator, final Set<String> expectedTexts) {
        return new ExpectedCondition<Boolean>() {
            List<String> lastElements = new ArrayList<String>();

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    lastElements = getTextOfPresentElements(locator, driver);
                    for (String element : lastElements) {
                        if (containsAllCaseInsensitive(element, expectedTexts)) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "visibility of text '" + expectedTexts
                        + "' in elements " + lastElements + " located by "
                        + locator;
            }
        };
    }

    public int getNumberOfElementsContainingText(final By locator,
            final Set<String> expectedTexts) throws Exception {
        int count = 0;
        List<String> elements = getTextOfDisplayedElements(locator, getDriver());
        for (String element : elements) {
            if (containsAllCaseInsensitive(element, expectedTexts)) {
                count++;
            }
        }
        return count;
    }

    public boolean isActionMessageSent(final Set<String> parts)
            throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssFirstAction);
        assert DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
        final List<WebElement> actionMessages = this.getDriver().findElements(
                locator);
        // Get the most recent action message only
        final String actionMessageInUI = actionMessages.get(
                actionMessages.size() - 1).getText();
        for (String part : parts) {
            if (!actionMessageInUI.toUpperCase().contains(part.toUpperCase())) {
                log.error(String
                        .format("'%s' substring has not been found in '%s' action message",
                                part, actionMessageInUI));
                return false;
            }
        }
        return true;
    }

    public boolean isActionMessageSent(String message) throws Exception {
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        return isActionMessageSent(parts);
    }

    public boolean isActionMessageNotSent(String message) throws Exception {
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        return isActionMessageNotSent(parts);
    }

    public boolean isMessageSent(String message) throws Exception {
        final By locator = By
                .xpath(WebAppLocators.ConversationPage.xpathMessageEntryByText
                        .apply(message));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 5);
    }

    public boolean isMessageEmbedded(boolean embedded, String typeOfMessage, String url) throws Exception {
        String pattern = "[\\w\\-\\_]{7,12}";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        assert matcher.find() : "Could not find " + typeOfMessage + " id in URL: " + url;
        final String id = matcher.group();
        By locator = null;
        if (typeOfMessage.contains("youtube")) {
            locator = By.xpath(WebAppLocators.ConversationPage.xpathEmbeddedYoutubeVideoById.apply(id));
        } else if (typeOfMessage.contains("soundcloud")) {
            locator = By.xpath(WebAppLocators.ConversationPage.xpathEmbeddedSoundcloudById.apply(id));
        } else if (typeOfMessage.contains("vimeo")) {
            locator = By.xpath(WebAppLocators.ConversationPage.xpathEmbeddedVimeoById.apply(id));
        } else if (typeOfMessage.contains("spotify")) {
            locator = By.xpath(WebAppLocators.ConversationPage.xpathEmbeddedSpotifyById.apply(id));
        }
        if (embedded) {
            return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
        } else {
            return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
        }
    }

    public void clickPeopleButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(),
                showParticipants);
        showParticipants.click();
    }

    public void clickShowParticipantsButton() throws Exception {
        showParticipants.click();
    }

    public boolean isPeopleButtonToolTipCorrect() {
        return TOOLTIP_PEOPLE.equals(showParticipants
                .getAttribute(TITLE_ATTRIBUTE_LOCATOR));
    }

    public void sendPicture(String pictureName) throws Exception {
        final String picturePath = WebCommonUtils.getFullPicturePath(pictureName);
        if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
            WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver().getNodeIp());
        } else {
            imagePathInput.sendKeys(picturePath);
        }
    }

    public void sendFile(String fileName) throws Exception {
        final String filePath = WebCommonUtils.getFullFilePath("filetransfer/" + fileName);
        filePathInput.sendKeys(filePath);
    }

    public double getOverlapScoreOfLastImage(String pictureName)
            throws Exception {
        final String picturePath = WebCommonUtils
                .getFullPicturePath(pictureName);
        if (!isImageMessageFound()) {
            return 0.0;
        }
        // try to get the latest image
        BufferedImage actualImage;
        List<WebElement> images = waitUntilAllImagesAreFullyLoaded();
        actualImage = this.getElementScreenshot(images.get(images.size() - 1)).orElseThrow(IllegalStateException::new);
        // comparison of the original and sent pictures
        BufferedImage expectedImage = ImageUtil.readImageFromFile(picturePath);
        return ImageUtil.getOverlapScore(actualImage, expectedImage,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
    }

    public double getOverlapScoreOfFullscreenImage(String pictureName)
            throws Exception {
        final String picturePath = WebCommonUtils
                .getFullPicturePath(pictureName);
        if (!isImageMessageFound()) {
            return 0.0;
        }
        // comparison of the fullscreen image and sent picture
        BufferedImage actualImage = this.getElementScreenshot(fullscreenImage).orElseThrow(IllegalStateException::new);
        BufferedImage expectedImage = ImageUtil.readImageFromFile(picturePath);
        return ImageUtil.getOverlapScore(actualImage, expectedImage,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
    }

    public boolean isImageMessageFound() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssFirstImage),
                TIMEOUT_IMAGE_MESSAGE_UPLOAD);
    }

    public int getNumberOfImagesInCurrentConversation() throws Exception {
        return waitUntilAllImagesAreFullyLoaded().size();
    }

    public int getNumberOfMessagesInCurrentConversation() {
        return messageAmount.size();
    }

    public boolean isEphemeralButtonNotVisible() throws Exception {
        return !DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssEphemeralButton));
    }

    public void clickEphemeralButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), ephemeralButton);
        ephemeralButton.click();
    }

    public List<String> getEphemeralTimers() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), ephemeralTimers.get(0));
        return ephemeralTimers.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void setEphemeralTimer(String label) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), ephemeralTimers.get(0));
        for(WebElement element: ephemeralTimers) {
            if(element.getText().equals(label)) {
                element.click();
                return;
            }
        }
    }

    public boolean isTimeShortOnEphemeralButtonVisible(String time) throws Exception {
        final By locator = By.xpath(WebAppLocators.ConversationPage.xpathEphemeralButtonByTime.apply(time));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public boolean isTimeUnitOnEphemeralButtonVisible(String unit) throws Exception {
        final By locator = By.xpath(WebAppLocators.ConversationPage.xpathEphemeralButtonByUnit.apply(unit));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public String getPlaceholderOfConversationInput() throws Exception {
        return conversationInput.getAttribute("placeholder");
    }

    public void clickPingButton() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssPingButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 2) : "Ping button has not been shown after 2 seconds";
        pingButton.click();
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssCancelRequestButton));
    }

    public boolean isConversationInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(WebAppLocators.ConversationPage.idConversationInput));
    }

    public boolean isImageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssSendImageInput));
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.
                cssSelector(WebAppLocators.ConversationPage.cssCallButton));
    }

    public boolean isCallButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.
                cssSelector(WebAppLocators.ConversationPage.cssCallButton));
    }

    public boolean isVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.
                cssSelector(WebAppLocators.ConversationPage.cssVideoCallButton));
    }

    public boolean isVideoCallButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.
                cssSelector(WebAppLocators.ConversationPage.cssVideoCallButton));
    }

    public boolean isNobodyToCallMsgVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssNobodyLeftModal));
    }

    public boolean isFileButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssSendFileButton));
    }

    public boolean isPingButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.
                cssSelector(WebAppLocators.ConversationPage.cssPingButton));
    }

    public void clickCallButton() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssCallButton));
        callButton.click();
    }

    public void clickVideoCallButton() throws Exception {
        DriverUtils
                .waitUntilLocatorIsDisplayed(
                        this.getDriver(),
                        By.cssSelector(WebAppLocators.ConversationPage.cssVideoCallButton));
        videoCallButton.click();
    }

    public boolean isTextMessageVisible(String message) throws Exception {
        final By locator = By.xpath(WebAppLocators.ConversationPage.textMessageByText.apply(message));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean isTextMessageInvisible(String message) throws Exception {
        final By locator = By.xpath(WebAppLocators.ConversationPage.textMessageByText.apply(message));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickAcceptCallButton() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssAcceptCallButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Accept call button has not been shown after "
                + MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
        getDriver().findElement(locator).click();
    }

    public void clickAcceptVideoCallButton() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssAcceptVideoCallButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Accept video call button has not been shown after "
                + MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
        getDriver().findElement(locator).click();
    }

    public void clickSilenceCallButton() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssSilenceIncomingCallButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Silence call button has not been shown after "
                + MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
        getDriver().findElement(locator).click();
    }

    public String getLastTextMessage() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(WebAppLocators.ConversationPage.cssLastTextMessage),
                TIMEOUT_LONG_MESSAGE);
        return lastTextMessage.getText();
    }

    public String getSecondLastTextMessage() throws Exception {
        DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.ConversationPage.cssSecondLastTextMessage));
        return secondLastTextMessage.getText();
    }

    public void clickOnPicture() throws Exception {
        assert DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.ConversationPage.cssFirstImage));
        firstPicture.click();
    }

    public boolean isPictureInModalDialog() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssModalDialog));
    }

    public boolean isPictureInFullscreen() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        this.getDriver(),
                        By.cssSelector(WebAppLocators.ConversationPage.cssFullscreenImage));
    }

    public boolean isPictureNotInModalDialog() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssModalDialog));
    }

    public void clickXButton() throws Exception {
        xButton.click();
    }

    public void clickOnBlackBorder() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            Actions builder = new Actions(getDriver());
            builder.moveToElement(fullscreenImage, -10, -10).click().build().perform();
        } else {
            WebElement blackBorder = getDriver().findElement(By.cssSelector("#detail-view.modal"));
            getDriver().executeScript("var evt = new MouseEvent('click', {view: window});arguments[0].dispatchEvent(evt);",
                    blackBorder);
        }
    }

    public void clickGIFButton() throws Exception {
        gifButton.click();
    }

    public String getMessageFromInputField() {
        return conversationInput.getAttribute("value");
    }

    public void pressShortCutForSearch() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ALT, "n"));
        } else {
            throw new PendingException(
                    "Webdriver does not support shortcuts for Mac browsers");
        }
    }

    public void pressShortCutForPing() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ALT, "k"));
        } else {
            conversationInput.sendKeys(Keys.chord(Keys.META, Keys.ALT, "k"));
        }
    }

    public String getPingButtonToolTip() {
        return pingButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public String getCallButtonToolTip() {
        return callButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public String getVideoCallButtonToolTip() {
        return videoCallButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public void clickUserAvatar() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), userAvatar);
        userAvatar.click();
    }

    public void clickUserAvatar(String userId) throws Exception {
        String css = WebAppLocators.ConversationPage.cssUserAvatarById.apply(userId);
        final WebElement avatar = getDriver().findElement(By.cssSelector(css));
        DriverUtils.waitUntilElementClickable(this.getDriver(), avatar);
        avatar.click();
    }

    public boolean isActionMessageNotSent(final Set<String> parts)
            throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssFirstAction);

        if (DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator)) {
            return true;
        } else {
            final List<WebElement> actionMessages = this.getDriver()
                    .findElements(locator);
            // Get the most recent action message only
            final String actionMessageInUI = actionMessages.get(
                    actionMessages.size() - 1).getText();
            for (String part : parts) {
                if (!actionMessageInUI.toUpperCase().contains(
                        part.toUpperCase())) {
                    log.error(String
                            .format("'%s' substring has not been found in '%s' action message",
                                    part, actionMessageInUI));
                    return true;
                }
            }
            return false;
        }
    }

    public Object getConnectedMessageUser() {
        return connectedMessageUser.getText();
    }

    public Object getConnectedMessageLabel() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssConnectedMessageLabel));
        return connectedMessageLabel.getText();
    }

    public boolean isConversationVerified() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssConversationVerifiedIcon));
    }

    //file transfer

    public boolean isFileTransferDisplayed(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isFileTransferInvisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator) && !DriverUtils.waitUntilLocatorIsDisplayed(
                getDriver(), locator);
    }

    public boolean getFileIcon(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileIcon, fileName));
        return getDriver().findElement(locator).isDisplayed();
    }

    public String getFileNameOf(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileName, fileName));
        return getDriver().findElement(locator).getText();
    }

    public String getFileSizeOf(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileSize, fileName));
        return getDriver().findElement(locator).getText();
    }

    public String getFileStatusOf(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileStatus, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No file status element found for locator "
                + locator;
        return getDriver().findElement(locator).getText();
    }

    public Optional<String> getOptionalFileStatusOf(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileStatus, fileName));
        Optional<WebElement> element = DriverUtils.getElementIfDisplayed(getDriver(), locator);
        if (element.isPresent()) {
            try {
                return Optional.of(element.get().getText());
            } catch (StaleElementReferenceException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public String getFileTypeOf(String fileType) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileType, fileType));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No file type element found for locator " + locator;
        return getDriver().findElement(locator).getText();
    }

    public void cancelFileUpload(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileCancelUpload, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No cancel element found for locator " + locator;
        getDriver().findElement(locator).click();
    }

    public void cancelVideoUpload(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoCancelUpload, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No cancel element found for locator " + locator;
        getDriver().findElement(locator).click();
    }

    public void cancelVideoDownload(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoCancelDownload, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No cancel element found for locator " + locator;
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilFileUploaded(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        DriverUtils.waitUntilLocatorAppears(getDriver(), locator, TIMEOUT_FILE_UPLOAD);
        By locatorPlaceholder = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileCancelUpload, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locatorPlaceholder, TIMEOUT_FILE_UPLOAD);
    }

    public boolean waitUntilFilePlaceholderDisappears(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        DriverUtils.waitUntilLocatorAppears(getDriver(), locator, TIMEOUT_FILE_UPLOAD);
        By locatorPlaceholder = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFilePlaceholder, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locatorPlaceholder, TIMEOUT_FILE_UPLOAD);
    }

    public void clickFileIcon(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileIcon, fileName));
        getDriver().findElement(locator).click();
    }

    //video

    public boolean waitUntilVideoUploaded(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator, TIMEOUT_VIDEO_UPLOAD);
    }

    public void playVideo(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        getDriver().findElement(locator).click();
    }

    public boolean isCancelButtonVisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoCancelUpload, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean isPlayButtonVisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public void pauseVideo(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPause, fileName));
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilVideoPlays(String fileName) throws Exception {
        By locator1 = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        DriverUtils.waitUntilLocatorDissapears(getDriver(), locator1);
        By locator2 = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoLoading, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator2, TIMEOUT_VIDEO_UPLOAD);
    }

    public boolean isVideoSeekbarVisible(String fileName) throws Exception {
        hoverOverVideo(fileName);
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoSeekbar, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean waitUntilVideoTimeChanges(String fileName) throws Exception {
        hoverOverVideo(fileName);
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoTime, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No time element found for locator " + locator;
        final String time = getDriver().findElement(locator).getText();
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Waited for time " + time + " to change, but is still " + getDriver().findElement(locator)
                        .getText() + " on locator " + locator);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    hoverOverVideo(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return !driver.findElement(locator).getText().equals(time);
            }
        });
        return true;
    }

    public boolean isVideoMessageVisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideo, fileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isVideoMessageInvisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideo, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public String getTimeOfVideo(String fileName) throws Exception {
        hoverOverVideo(fileName);
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoTime, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No time element found for locator " + locator;
        return getDriver().findElement(locator).getText();
    }

    //audio

    public boolean waitUntilAudioUploaded(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioPlay, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator, TIMEOUT_AUDIO_UPLOAD);
    }

    public void playAudio(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioPlay, fileName));
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilAudioPlays(String fileName) throws Exception {
        By locator1 = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioPlay, fileName));
        DriverUtils.waitUntilLocatorDissapears(getDriver(), locator1);
        By locator2 = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioLoading, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator2, TIMEOUT_AUDIO_UPLOAD);
    }

    public boolean isAudioSeekbarVisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioSeekbar, fileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public String getAudioTime(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioTime, fileName));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "No time element found for locator " + locator;
        return getDriver().findElement(locator).getText();
    }

    public boolean waitUntilAudioTimeChanges(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudioTime, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No time element found for locator " + locator;
        final String time = getDriver().findElement(locator).getText();
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Waited for time " + time + " to change, but is still " + getDriver().findElement(locator)
                        .getText() + " on locator " + locator);
        wait.until(d -> !d.findElement(locator).getText().equals(time));
        return true;
    }

    public boolean isAudioMessageVisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudio, fileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isAudioMessageInvisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssAudio, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void scrollUp() throws Exception {
        getDriver().executeScript("$(\"" + WebAppLocators.ConversationPage.cssConversation + "\").animate({\n"
                + "scrollTop: 0\n"
                + "},100);", "");
    }

    public SortedSet<Message> getAllLoadedMessages() throws InterruptedException {
        Thread.sleep(1000);
        SortedSet<Message> mappedMessages = new TreeSet<>();
        for (WebElement message : messages) {
            log.debug("message: " + message.getText());
            // Ignores system messages
            if (!message.findElements(By.cssSelector(".text")).isEmpty()) {
                String text = message.findElement(By.cssSelector(".text")).getText();
                String time = message.findElement(By.cssSelector(".time")).getAttribute("data-timestamp");
                String senderId = "";
                if(message.findElements(By.cssSelector("user-avatar")).size() > 0 ) {
                    senderId = message.findElement(By.cssSelector("user-avatar")).getAttribute("user-id");
                }
                mappedMessages.add(new Message(text, senderId, Instant.ofEpochMilli(Long.parseLong(time))));
            }
        }
        return mappedMessages;
    }

    public boolean isLatestMessageWithTextVisible(String text) throws Exception {
        By locator = By.xpath(WebAppLocators.ConversationPage.xpathMessageTextByMessageId.apply(getMessageId(1), text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    //likes

    public boolean isLikeSymbolVisibleForLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeSymbol.apply(getMessageId(1)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeSymbolInvisibleForLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeSymbol.apply(getMessageId(1)));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isLikeSymbolVisibleForSecondLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeSymbol.apply(getMessageId(2)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeSymbolInvisibleForSecondLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeSymbol.apply(getMessageId(2)));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void clickLatestLikeLine() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLastLikeLine);
        getDriver().findElement(locator).click();
    }

    public boolean isLikeListOfLatestMsgVisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLastLikeList);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickXLatestLikeList() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssXLastLikeList);
        getDriver().findElement(locator).click();
    }

    public int getAvatarsInLatestLikeList() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeListAvatars);
        return this.getDriver().findElements(locator).size();
    }

    public void clickLikeMessageWithoutOtherLikes(int messageIndex) throws Exception {
        String messageId = getMessageId(messageIndex);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeWithoutOtherLikesByMessageId.apply(messageId));
        getDriver().findElement(locator).click();
    }

    public void clickUnlikeMessageWithoutOtherLikes(int messageIndex) throws Exception {
        String messageId = getMessageId(messageIndex);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssUnlikeWithoutOtherLikesByMessageId.apply(messageId));
        getDriver().findElement(locator).click();
    }

    public void clickLikeMessageWithOtherLikes(int messageIndex) throws Exception {
        String messageId = getMessageId(messageIndex);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeWithoutOtherLikesByMessageId.apply(messageId));
        getDriver().findElement(locator).click();
    }

    public void clickUnlikeMessageWithOtherLikes(int messageIndex) throws Exception {
        String messageId = getMessageId(messageIndex);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssUnlikeWithOtherLikesByMessageId.apply(messageId));
        getDriver().findElement(locator).click();
    }

    public List<String> getUsersThatLikeTheLastMessage() throws Exception {
        String latestMessageId = getMessageId(1);
        hoverOverMessage(latestMessageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssUserThatLikeByMessageId.apply(latestMessageId));
        String text = getDriver().findElement(locator).getAttribute("data-uie-value");
        return Arrays.asList(text.split("\\s*,\\s*"));
    }

    public String getLastLikeString() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLastLikeLine);
        return getDriver().findElement(locator).getText();
    }

    public boolean isUnlikeWithOtherLikesVisibleForLastMessage() throws Exception {
        String latestMessageId = getMessageId(1);
        hoverOverMessage(latestMessageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssUnlikeWithOtherLikesByMessageId.apply(latestMessageId));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeWithOtherLikesVisibleForLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLikeWithOtherLikesByMessageId.apply(getMessageId(1)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUnlikeWithoutOtherLikesVisibleForMessage(int messageIndex) throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssUnlikeWithoutOtherLikesByMessageId.apply(getMessageId(messageIndex)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeLineVisibleForMessage(int messageIndex) throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssFooterByMessageId.apply(getMessageId(messageIndex)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeLineInvisibleForMessage(int messageIndex) throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssFooterByMessageId.apply(getMessageId(messageIndex)));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void hoverOverLastMessage() throws Exception {
        hoverOverMessage(getMessageId(1));
    }

    public void clickEditInMessageContextMenu() throws Exception {
        doEditMessageButton.click();
    }

    public String getLastEditTimestamp() throws Exception {
        return lastEditTimestamp.getAttribute("title");
    }

    public String getSecondLastEditTimestamp() throws Exception {
        return secondLastEditTimestamp.getAttribute("title");
    }

    public boolean isLastMsgHeaderVisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMsgHeader);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSecondLastMsgHeaderVisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssSecondLastMsgHeader);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickToResetSessionOnLatestError() throws Exception {
        String latestMessageId = getMessageId(1);
        hoverOverMessage(latestMessageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssResetSessionByMessageId.apply(latestMessageId));
        getDriver().findElement(locator).click();
    }

    public void confirmDelete() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), doDeleteButton);
        doDeleteButton.click();
    }

    public void confirmDeleteForEveryone() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), doDeleteForEveryoneButton);
        doDeleteForEveryoneButton.click();
    }

    public boolean isDeleteButtonVisibleForLastMessage() throws Exception {
        String messageId = getMessageId(1);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteButtonByMessageId.apply(messageId));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isEditButtonInvisibleForLastMessage() throws Exception {
        String messageId = getMessageId(1);
        hoverOverMessage(messageId);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssEditButtonByMessageId.apply(messageId));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    //context menu

    public void clickContextMenuOnMessage(int indexNumber) throws Exception {
        String id = getMessageId(indexNumber);
        hoverOverMessage(id);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssContextMenuButtonByMessageId.apply(id));
        getDriver().findElement(locator).click();
    }

    public boolean isDeleteForEveryoneButtonInContextMenuInvisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteForEveryoneInContext);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isLikeButtonInContextMenuVisible() throws Exception {
        By locator = By.xpath(WebAppLocators.ConversationPage.xpathLikeInContext);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLikeButtonInContextMenuInvisible() throws Exception {
        By locator = By.xpath(WebAppLocators.ConversationPage.xpathLikeInContext);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isDeleteForMeButtonInContextMenuVisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteForMeInContext);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUnlikeButtonInContextMenuVisible() throws Exception {
        By locator = By.xpath(WebAppLocators.ConversationPage.xpathUnlikeInContext);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public boolean isDownloadButtonInContextMenuInvisible() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDownloadInContext);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void clickDeleteEverywhereInContextMenuOfLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteEverywhereByMessageId.apply(getMessageId(1)));
        getDriver().findElement(locator).click();
    }

    public void clickDeleteForMeInContextMenuOfLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteForMeByMessageId.apply(getMessageId(1)));
        getDriver().findElement(locator).click();
    }

    public void clickReactInContextMenuOfLastMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssReactByMessageId.apply(getMessageId(1)));
        getDriver().findElement(locator).click();
    }

    public void setCloseResetSessionDialog() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), closeResetSessionDialogButton);
        closeResetSessionDialogButton.click();
    }

    public String getTitlebarLabel() {
        return titlebarLabel.getText();
    }

    public void clickCancelPendingRequestButton() throws Exception {
        cancelRequestButton.click();
    }

    public boolean isImageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.cssSelector(WebAppLocators.ConversationPage.cssFirstImage));
    }

    public boolean isLongMessageWarnDialogShown() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), longMessageDialog);
    }

    public boolean isLongMessageWarnDialogNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.cssSelector(WebAppLocators.ConversationPage.cssLongMessageDialog));
    }

    public void clickOKButtonOnLongMWarning() throws Exception {
        oKButtonOnLongMWarning.click();
    }

    public void clickXButtonOnLongMWarning() throws Exception {
        xButtonOnLongMWarning.click();
    }

    public void clickXButtonToCloseEdit() throws Exception {
        xButtonEdit.click();
    }

    public void clearConversationInput() throws Exception {
        conversationInput.sendKeys(Keys.BACK_SPACE);
    }

    public void pressUpArrow() throws Exception {
        conversationInput.sendKeys(Keys.ARROW_UP);
    }

    //location sharing

    public String getLocationName() {
        return locationName.getText();
    }

    public String getLocationNameFromLink() {
        return locationLink.getAttribute("href");
    }

    public boolean isLocationNotShownInConversationView() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssSharedLocation));
    }

    public boolean isBroadcastIndicatorVideoShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssBroadcastIndicatorVideo));
    }

    public boolean isBroadcastIndicatorVideoNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssBroadcastIndicatorVideo));
    }

    //link preview

    public String getLinkTitle() {
        return linkTitle.getText();
    }

    public boolean isLinkPreviewLinkVisibleForLatestMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLinkPreviewLinkByMessageId.apply(getMessageId(1)));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLinkPreviewLinkInvisibleForLatestMessage() throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLinkPreviewLinkByMessageId.apply(getMessageId(1)));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isLinkTitleNotShownInConversationView() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.cssSelector(
                WebAppLocators.ConversationPage.cssLinkTitle));
    }

    public boolean isImageFromLinkPreviewVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), previewImage);
    }

    public boolean isImageFromLinkPreviewNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssLinkPreviewImage));
    }

    public BufferedImage getImageFromLastLinkPreview() throws Exception {
        List<WebElement> images = getPreviewImages();
        return this.getElementScreenshot(images.get(images.size() - 1)).orElseThrow(IllegalStateException::new);
    }

    public boolean waitForLinkPreviewContains(String link) throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ConversationPage.cssLinkPreviewLink);
        WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_I_SEE_MESSAGE);
        return wait.withTimeout(TIMEOUT_I_SEE_MESSAGE, TimeUnit.SECONDS)
                .until(presenceOfTextInElementsLocated(locator, new HashSet<String>(Arrays.asList(link))));
    }

    public int getNumberOfDeletedMessagesInCurrentConversation() {
        return deletedMessageAmount.size();
    }

    private List<WebElement> waitUntilAllImagesAreFullyLoaded() throws Exception {
        By picturesLocator = By.cssSelector(WebAppLocators.ConversationPage.cssImageEntries);
        By loadingPicturesLocator = By.cssSelector(WebAppLocators.ConversationPage.cssImageEntries);

        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                .withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(InvalidElementStateException.class);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                List<WebElement> all = driver.findElements(picturesLocator);
                List<WebElement> loading = driver.findElements(loadingPicturesLocator);
                return all.size() == loading.size();
            }
        });
        return pictures;
    }

    protected List<WebElement> getPreviewImages() throws Exception {
        By picturesLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLinkPreviewImage);
        List<WebElement> all = getDriver().findElements(picturesLocator);
        return all;
    }

    private void hoverOverMessage(String id) throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssMessagesById.apply(id));
        WebElement element = getDriver().findElement(locator);
        WebCommonUtils.hoverOverElement(getDriver(), element);
    }

    private void hoverOverVideo(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideo, fileName));
        WebElement element = getDriver().findElement(locator);
        WebCommonUtils.hoverOverElement(getDriver(), element);
    }

    private static List<String> getTextOfPresentElements(By locator, WebDriver driver) throws Exception {
        final List<WebElement> headers = driver.findElements(locator);
        return headers.stream().filter(a -> a.isDisplayed())
                .map(a -> a.getText().replace("\n", ""))
                .collect(Collectors.toList());
    }

    private static List<String> getTextOfDisplayedElements(By locator, WebDriver driver) throws Exception {
        final List<WebElement> headers = driver.findElements(locator);
        return headers.stream().filter(a -> DriverUtils.isElementPresentAndDisplayed((RemoteWebDriver) driver, a))
                .map(a -> a.getText().replace("\n", ""))
                .collect(Collectors.toList());
    }

    private static boolean containsAllCaseInsensitive(String text, Set<String> parts) {
        for (String part : parts) {
            if (!text.replaceAll(" +", " ").toLowerCase()
                    .contains(part.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean isWatermarkVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssWatermark));
    }

    public boolean isWatermarkNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssWatermark));
    }

    public String getFirstTimeExperienceMessage() {
        return firstTimeExperienceMessage.getText();
    }

    public String getDeliveryStatusOfLastMessage() throws Exception {
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessageByIndex.apply(1) + " " +
                ".message-status");
        return getDriver().findElement(lastMessageLocator).getText();
    }

    public boolean isDeliveryStatusOfLastMessageVisible() throws Exception {
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessageByIndex.apply(1) + " " +
                ".message-status");
        return DriverUtils.waitUntilLocatorAppears(getDriver(), lastMessageLocator);
    }

    public boolean isLastMessageObfuscated() {
        return lastGenericMessage.findElement(By.cssSelector(".ephemeral-message-obfuscated")).isDisplayed();
    }

    public boolean isLastMessageNotObfuscated() {
        return !lastTextMessage.getAttribute("class").contains("ephemeral-message-obfuscated");
    }

    public boolean isSecondLastMessageObfuscated() {
        return secondLastGenericMessage.findElement(By.cssSelector(".ephemeral-message-obfuscated")).isDisplayed();
    }

    public boolean isLastMessageReplaced() {
        return lastGenericMessage.findElement(By.cssSelector(".bg-color-ephemeral")).isDisplayed();
    }

    public boolean isOrangeBlockInLastMessageNotVisible() throws Exception {
        return !lastGenericMessage.getAttribute("class").contains("bg-color-ephemeral");
    }

    /**
     * Use selenium's executeAsyncScript method to grab message of a specific user from a specific conversation. Because the
     * framework Dexie.js (the Javascript framework of the webapp that saves data in the indexedDB) is using Promises which are
     * asyncron, we call the method callback() which allows us to return a value at any time and therefor tell selenium that we
     * have finished our script.
     *
     * @param conversationId id of the conversation
     * @param userId         id of the user
     * @return a list of text messages
     * @throws Exception
     */
    public List<String> getMessagesFromDb(String conversationId, String userId) throws Exception {
        getDriver().manage().timeouts().setScriptTimeout(7, TimeUnit.SECONDS);
        String backendType = WebCommonUtils.getBackendType(ConversationPage.class);
        return (List<String>) getDriver().executeAsyncScript("var conversationId = arguments[0];\n" +
                        "var userId = arguments[1];\n" +
                        "var backendType = arguments[2];\n" +
                        "var callback = arguments[arguments.length - 1];\n" +
                        "var messages = [];\n" +
                        "db = new Dexie('wire@'+backendType+'@'+userId+'@permanent');\n" +
                        "    db.open().then(function () {\n" +
                        "      table=db.table(\"conversation_events\");\n" +
                        "      table.where(':id').startsWith(conversationId+'@'+userId).each(\n" +
                        "        function(row){console.log(row.data.content);messages.push(row.data.content)}\n" +
                        "      ).then(function() {callback(messages)});\n" +
                        "    });\n",
                conversationId,
                userId,
                backendType);
    }
}
