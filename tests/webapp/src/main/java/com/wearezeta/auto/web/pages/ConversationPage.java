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

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import cucumber.api.PendingException;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConversationPage extends WebPage {

    private static final int TIMEOUT_IMAGE_MESSAGE_UPLOAD = 40; // seconds
    private static final int TIMEOUT_FILE_UPLOAD = 100; // seconds
    private static final int TIMEOUT_VIDEO_UPLOAD = 100; // seconds

    private static final Logger log = ZetaLogger.getLog(ConversationPage.class
            .getSimpleName());

    private static final String TOOLTIP_PEOPLE = "People";

    private static final String CALLING_IN_LABEL = "IN ";

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssMessageAmount)
    private List<WebElement> messageAmount;

    @FindBy(id = WebAppLocators.ConversationPage.idConversation)
    private WebElement conversation;

    @FindBy(css = WebAppLocators.ConversationPage.cssTitlebarLabel)
    private WebElement titlebarLabel;

    @FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
    private WebElement conversationInput;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssShowParticipantsButton)
    private WebElement showParticipants;

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

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssNobodyLeftModal)
    private WebElement nobodyLeftModal;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssVideoCallButton)
    private WebElement videoCallButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssPingMessage)
    private WebElement pingMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssLastTextMessage)
    private WebElement lastTextMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssSecondLastTextMessage)
    private WebElement secondLastTextMessage;

    @FindBy(css = WebAppLocators.ConversationPage.cssMessages)
    private List<WebElement> messages;

    @FindBy(css = WebAppLocators.ConversationPage.cssImageEntries)
    private List<WebElement> pictures;

    @FindBy(css = WebAppLocators.ConversationPage.cssLoadingImageEntries)
    private List<WebElement> loadingPictures;

    @FindBy(css = WebAppLocators.ConversationPage.cssFirstImage)
    private WebElement firstPicture;

    @FindBy(css = WebAppLocators.ConversationPage.cssFullscreenImage)
    private WebElement fullscreenImage;

    @FindBy(xpath = WebAppLocators.ConversationPage.xpathXButton)
    private WebElement xButton;

    @FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idBlackBorder)
    private WebElement blackBorder;

    @FindBy(css = WebAppLocators.ConversationPage.cssUserAvatar)
    private WebElement userAvatar;

    @FindBy(css = WebAppLocators.ConversationPage.cssLabelOnOutgoingCall)
    private WebElement labelOnOutgoingCall;

    @FindBy(css = WebAppLocators.ConversationPage.cssConnectedMessageUser)
    private WebElement connectedMessageUser;

    @FindBy(css = WebAppLocators.ConversationPage.cssConnectedMessageLabel)
    private WebElement connectedMessageLabel;

    @FindBy(css = WebAppLocators.ConversationPage.cssDoDelete)
    private WebElement doDeleteButton;

    public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
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

    public void writeNewMessage(String message) throws Exception {
        if (WebAppExecutionContext.getBrowser()
                .equals(Browser.InternetExplorer)) {
            // IE11 has a bug that sends the form when pressing SHIFT+ENTER
            message = message
                    .replace(Keys.chord(Keys.SHIFT, Keys.ENTER), "\\n");
            String addMessageToInput = "var a=arguments[0];a.value=a.value+'"
                    + message + "';";
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript(addMessageToInput, conversationInput);
            // since we did not press any keys, we fake input by sending a space
            // and then removing it again
            conversationInput.sendKeys(" " + Keys.BACK_SPACE);
        } else {
            conversationInput.sendKeys(message);
        }
    }

    public void sendNewMessage() {
        conversationInput.sendKeys(Keys.ENTER);
    }

    public String getLastActionMessage() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssFirstAction);
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
        final List<WebElement> actionElements = this.getDriver().findElements(
                locator);
        return actionElements.get(actionElements.size() - 1).getText();
    }

    private static List<String> getTextOfPresentElements(By locator,
            WebDriver driver) throws Exception {
        final List<WebElement> headers = driver.findElements(locator);
        return headers.stream().filter(a -> a.isDisplayed())
                .map(a -> a.getText().replace("\n", ""))
                .collect(Collectors.toList());
    }

    private static List<String> getTextOfDisplayedElements(By locator,
            WebDriver driver) throws Exception {
        final List<WebElement> headers = driver.findElements(locator);
        return headers.stream().filter(a -> DriverUtils.isElementPresentAndDisplayed((RemoteWebDriver) driver, a))
                .map(a -> a.getText().replace("\n", ""))
                .collect(Collectors.toList());
    }

    private static boolean containsAllCaseInsensitive(String text,
            Set<String> parts) {
        for (String part : parts) {
            if (!text.replaceAll(" +", " ").toLowerCase()
                    .contains(part.toLowerCase())) {
                return false;
            }
        }
        return true;
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
        wait.until(visibilityOfTextInElementsLocated(locator, parts));
    }

    public boolean waitForPresentMessageContains(String text) throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ConversationPage.cssTextMessage);
        WebDriverWait wait = new WebDriverWait(getDriver(), DriverUtils.getDefaultLookupTimeoutSeconds());
        return wait.until(presenceOfTextInElementsLocated(locator, new HashSet<String>(Arrays.asList(text))));
    }

    public boolean waitForDisplayedMessageContains(String text, int timeout) throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ConversationPage.cssTextMessage);
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        return wait.until(visibilityOfTextInElementsLocated(locator, new HashSet<String>(Arrays.asList(text))));
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

    public boolean isYoutubeVideoEmbedded(String url) throws Exception {
        String pattern = "[\\w\\-\\_]{10,12}";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        assert matcher.find() : "Could not find Youtube id in URL: " + url;
        final String id = matcher.group();

        final By locator = By
                .xpath(WebAppLocators.ConversationPage.xpathEmbeddedYoutubeVideoById
                        .apply(id));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 5);
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
        moveCssSelectorIntoViewport(WebAppLocators.ConversationPage.cssSendImageInput);
        if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
            WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver().getNodeIp());
        } else {
            imagePathInput.sendKeys(picturePath);
        }
        moveCssSelectorOutOfViewport(WebAppLocators.ConversationPage.cssSendImageInput);
    }

    private void hoverOverConversation() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            // native mouse over
            DriverUtils.moveMouserOver(this.getDriver(), conversation);
        } else {
            // safari workaround
            DriverUtils.addClass(this.getDriver(), conversation, "hover");
        }
    }

    public void moveCssSelectorIntoViewport(String selector) throws Exception {
        final String showPathInputJScript = "$(\"" + selector + "\").css({'left': -200});";
        getDriver().executeScript(showPathInputJScript);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.cssSelector(selector)) : "Could not move element "
                + "with selector " + selector + " into viewport";
    }

    public void moveCssSelectorOutOfViewport(String selector) throws Exception {
        final String showPathInputJScript = "$(\"" + selector + "\").css({'left': 200});";
        getDriver().executeScript(showPathInputJScript);
    }

    public void sendFile(String fileName) throws Exception {
        final String filePath = WebCommonUtils.getFullFilePath("filetransfer/" + fileName);
        moveCssSelectorIntoViewport(WebAppLocators.ConversationPage.cssSendFileInput);
        filePathInput.sendKeys(filePath);
        moveCssSelectorOutOfViewport(WebAppLocators.ConversationPage.cssSendFileInput);
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

    public void clickPingButton() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssPingButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 2) : "Ping button has not been shown after 2 seconds";
        pingButton.click();
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

    private static final int MISSED_CALL_MSG_TIMOEUT = 15;

    public String getMissedCallMessage() throws Exception {
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssLastAction);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, MISSED_CALL_MSG_TIMOEUT) : "Missed call message is not visible after "
                + MISSED_CALL_MSG_TIMOEUT + " second(s) timeout";
        return getDriver().findElement(locator).getText();
    }

    private static final int MAX_CALLING_BAR_VISIBILITY_TIMEOUT = 5; // seconds

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
        assert DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.ConversationPage.cssLastTextMessage));
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
        Actions builder = new Actions(getDriver());
        builder.moveToElement(fullscreenImage, -10, -10).click().build()
                .perform();
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

    public void hoverPingButton() throws Exception {
        hoverOverConversation();
        final By locator = By
                .cssSelector(WebAppLocators.ConversationPage.cssPingButton);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 2) : "Ping button has not been shown after 2 seconds";
        assert DriverUtils.waitUntilElementClickable(this.getDriver(),
                pingButton) : "Ping button has to be clickable";

    }

    public void pressShortCutForPing() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ALT, "k"));
        } else {
            throw new PendingException(
                    "Webdriver does not support shortcuts for Mac browsers");
        }
    }

    public String getPingButtonToolTip() {
        return pingButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public void hoverCallButton() throws Exception {
        hoverOverConversation();
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssCallButton));
    }

    public String getCallButtonToolTip() {
        return callButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public void pressShortCutForCall() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ALT, "r"));
        } else {
            conversationInput.sendKeys(Keys.chord(Keys.META, Keys.ALT, "r"));
        }
    }

    public void clickUserAvatar() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), userAvatar);
        userAvatar.click();
    }

    public void clickUserAvatar(String userId) throws Exception {
        String css = WebAppLocators.ConversationPage.cssUserAvatarById
                .apply(userId);
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

    public boolean isFileTransferDisplayed(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isFileTransferInvisible(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFile, fileName));
        return !DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
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

    public boolean waitUntilFileUploaded(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileStatus, fileName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, TIMEOUT_FILE_UPLOAD);
    }

    public void downloadFile(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssFileDownload, fileName));
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilVideoUploaded(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator, TIMEOUT_VIDEO_UPLOAD);
    }

    public void playVideo(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoPlay, fileName));
        getDriver().findElement(locator).click();
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

    public boolean isSeekbarVisible(String fileName) throws Exception {
        hoverOverVideo(fileName);
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoSeekbar, fileName));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public String getTimeOfVideo(String fileName) throws Exception {
        hoverOverVideo(fileName);
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideoTime, fileName));
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : "No time element found for locator " + locator;
        return getDriver().findElement(locator).getText();
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
            if(!message.findElements(By.cssSelector(".message-body")).isEmpty()) {
                String text = message.findElement(By.cssSelector(".text")).getText();
                String time = message.findElement(By.cssSelector(".time")).getAttribute("data-timestamp");
                String senderId = message.findElement(By.cssSelector("user-avatar")).getAttribute("user-id");
                mappedMessages.add(new Message(text, senderId, Instant.ofEpochMilli(Long.parseLong(time))));
            }
        }
        return mappedMessages;
    }

    public void clickToDeleteLatestMessage() throws Exception {
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessage);
        String id = getDriver().findElement(lastMessageLocator).getAttribute("data-uie-uid");
        hoverOverMessage(id);
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteButtonByMessageId.apply(id));
        getDriver().findElement(locator).click();
    }

    public void hoverOverLatestMessage() throws Exception {
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessage);
        String id = getDriver().findElement(lastMessageLocator).getAttribute("data-uie-uid");
        hoverOverMessage(id);
    }

    private void hoverOverMessage(String id) throws Exception {
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssMessagesById.apply(id));
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            // native mouse over
            DriverUtils.moveMouserOver(this.getDriver(), getDriver().findElement(locator));
        } else {
            throw new Exception("hovering over a message is not implemented for this browser");
        }
    }

    private void hoverOverVideo(String fileName) throws Exception {
        By locator = By.cssSelector(String.format(WebAppLocators.ConversationPage.cssVideo, fileName));
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            // native mouse over
            DriverUtils.moveMouserOver(this.getDriver(), getDriver().findElement(locator));
        } else {
            throw new Exception("hovering over a video is not implemented for this browser");
        }
    }

    public void confirmDelete() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), doDeleteButton);
        doDeleteButton.click();
    }

    public boolean isDeleteButtonVisibleForLatestMessage() throws Exception {
        By lastMessageLocator = By.cssSelector(WebAppLocators.ConversationPage.cssLastMessage);
        String id = getDriver().findElement(lastMessageLocator).getAttribute("data-uie-uid");
        By locator = By.cssSelector(WebAppLocators.ConversationPage.cssDeleteButtonByMessageId.apply(id));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 3);
    }

    public String getTitlebarLabel() {
        return titlebarLabel.getText();
    }
}
