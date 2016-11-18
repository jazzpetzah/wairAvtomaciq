package com.wearezeta.auto.web.pages;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

import cucumber.api.PendingException;

public class ContactListPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(ContactListPage.class.getSimpleName());

    private static final String DEFAULT_GROUP_CONVO_NAMES_SEPARATOR = ",";
    private static final int CONVO_LIST_ENTRY_VISIBILITY_TIMEOUT = 15; // seconds

    @FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssBackground)
    private WebElement background;

    @FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssGearButton)
    private WebElement gearButton;

    @FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
    private List<WebElement> contactListEntries;

    @FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathActiveConversationEntry)
    private WebElement activeConversationEntry;

    @FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathArchivedContactListEntries)
    private List<WebElement> archivedContactListEntries;

    @FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton)
    private WebElement openArchivedConvosButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssCloseArchivedConvosButton)
    private WebElement closeArchivedConvosButton;

    @FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssOpenStartUIButton)
    private WebElement openStartUIButton;

    @FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
    private WebElement conversationInput;

    // options popover bubble
    @FindBy(css = WebAppLocators.ContactListPage.cssArchiveButton)
    private WebElement archiveButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssMuteButton)
    private WebElement muteButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssUnmuteButton)
    private WebElement unmuteButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssLeaveButton)
    private WebElement leaveButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssBlockButton)
    private WebElement blockButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteButton)
    private WebElement deleteButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssCancelRequestButton)
    private WebElement cancelRequestButton;

    // leave warning
    @FindBy(css = WebAppLocators.ContactListPage.cssLeaveModalCancelButton)
    private WebElement leaveModalCancelButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssLeaveModalActionButton)
    private WebElement leaveModalActionButton;

    // block warning
    @FindBy(css = WebAppLocators.ContactListPage.cssBlockModalCancelButton)
    private WebElement blockModalCancelButton;

    @FindBy(css = WebAppLocators.ContactListPage.cssBlockModalActionButton)
    private WebElement blockModalActionButton;

    // delete warning
    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalCancelButtonGroup)
    private WebElement deleteModalCancelButtonGroup;

    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalActionButtonGroup)
    private WebElement deleteModalActionButtonGroup;

    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalLeaveCheckboxGroup)
    private WebElement deleteModalLeaveCheckboxGroup;

    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalActionButtonSingle)
    private WebElement deleteModalActionButtonSingle;

    @FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalCancelButtonSingle)
    private WebElement deleteModalCancelButtonSingle;

    public ContactListPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    /**
     * Fixes default group conversation name, because we never know the original order of participant names in group convo name
     *
     * @param conversationName the initial name
     * @return fixed group convo name, as it is displayed in conversation list. 'conversationName' is not going to be changed if
     * there are no comma character(s)
     * @throws Exception
     */
    protected String fixDefaultGroupConvoName(String conversationName,
            boolean includeArchived, boolean throwOnError) throws Exception {
        if (conversationName.contains(DEFAULT_GROUP_CONVO_NAMES_SEPARATOR)) {
            final Set<String> initialNamesSet = new HashSet<String>(
                    Arrays.asList(conversationName.split(","))).stream()
                    .map(x -> x.trim()).collect(Collectors.toSet());
            List<WebElement> convoNamesToCheck = new ArrayList<WebElement>();
            if (DriverUtils
                    .waitUntilLocatorIsDisplayed(
                            this.getDriver(),
                            By.xpath(WebAppLocators.ContactListPage.xpathContactListEntries),
                            3)) {
                convoNamesToCheck.addAll(contactListEntries);
            }
            if (includeArchived) {
                if (DriverUtils
                        .waitUntilLocatorIsDisplayed(
                                this.getDriver(),
                                By.xpath(WebAppLocators.ContactListPage.xpathArchivedContactListEntries))) {
                    convoNamesToCheck.addAll(archivedContactListEntries);
                }
            }
            for (WebElement convoItem : convoNamesToCheck) {
                final String convoName = convoItem.getText();
                final Set<String> convoItemNamesSet = new HashSet<String>(
                        Arrays.asList(convoName.split(","))).stream()
                        .map(x -> x.trim()).collect(Collectors.toSet());
                if (convoItemNamesSet.equals(initialNamesSet)) {
                    return convoName;
                }
            }
            if (throwOnError) {
                throw new RuntimeException(String.format(
                        "Group conversation '%s' does not exists in the list",
                        conversationName));
            } else {
                return null;
            }
        } else {
            return conversationName;
        }
    }

    protected String fixDefaultGroupConvoName(String conversationName,
            boolean includeArchived) throws Exception {
        return fixDefaultGroupConvoName(conversationName, includeArchived, true);
    }

    public boolean waitForContactListVisible() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        this.getDriver(),
                        By.cssSelector(WebAppLocators.ContactListPage.cssOpenStartUIButton));

    }

    public void waitForSelfProfileButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(this.getDriver(),
                gearButton);
    }

    public boolean isConvoListEntryWithNameExist(String name) throws Exception {
        log.debug("Looking for contact with name '" + name + "'");
        name = fixDefaultGroupConvoName(name, false, false);
        if (name == null) {
            return false;
        }
        final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
                .apply(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(locator));
    }

    public boolean isArchiveListEntryWithNameExist(String name)
            throws Exception {
        log.debug("Looking for contact with name '" + name + "'");
        name = fixDefaultGroupConvoName(name, true, false);
        if (name == null) {
            return false;
        }
        final String locator = WebAppLocators.ContactListPage.cssArchiveListEntryByName
                .apply(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(locator), 5);
    }

    public boolean isConvoListEntryNotVisible(String name) throws Exception {
        log.debug("Looking for contact with name '" + name + "'");
        name = fixDefaultGroupConvoName(name, false, false);
        if (name == null) {
            return true;
        }
        final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
                .apply(name);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.cssSelector(locator));
    }

    public WebElement getListElementByName(String name, boolean includeArchived)
            throws Exception {
        name = fixDefaultGroupConvoName(name, includeArchived);
        final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
                .apply(name);
        return getDriver().findElement(By.cssSelector(locator));
    }

    public String getActiveConversationId() throws Exception {
        return activeConversationEntry.getAttribute("data-uie-uid");
    }

    public String getActiveConversationName() throws Exception {
        return activeConversationEntry.getText();
    }

    public int getActiveConversationIndex() throws Exception {
        return getItemIndex(activeConversationEntry.getText());
    }

    public boolean isMissedCallVisibleForContact(String conversationName)
            throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.ContactListPage.xpathMissedCallNotificationByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isMissedCallInvisibleForContact(String conversationName)
            throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.ContactListPage.xpathMissedCallNotificationByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public void openArchive() throws Exception {
        this.getWait().until(
                ExpectedConditions
                .elementToBeClickable(openArchivedConvosButton));
        openArchivedConvosButton.click();
    }

    public void closeArchive() throws Exception {
        this.getWait().until(
                ExpectedConditions
                .elementToBeClickable(closeArchivedConvosButton));
        closeArchivedConvosButton.click();
    }

    public void clickArchiveConversation() throws Exception {
        archiveButton.click();
    }

    public void clickMuteConversation() throws Exception {
        muteButton.click();
    }

    public boolean isMuteButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), muteButton);
    }

    public boolean isUnmuteButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), unmuteButton);
    }

    public boolean isDeleteButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), deleteButton);
    }

    public boolean isLeaveButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), leaveButton);
    }

    public boolean isArchiveButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(),
                archiveButton);
    }

    public boolean isBlockButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(),
                blockButton);
    }

    public boolean isCancelRequestButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(),
                cancelRequestButton);
    }

    public boolean isConversationMuted(String conversationName)
            throws Exception {
        // moving focus from contact - to now show ... button
        // do nothing (safari workaround)
        if (WebAppExecutionContext.getBrowser()
                .isSupportingNativeMouseActions()) {
            DriverUtils.moveMouserOver(this.getDriver(), gearButton);
        }
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        this.getDriver(),
                        By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
                                .apply(conversationName)));
    }

    public boolean isConversationNotMuted(String conversationName)
            throws Exception {
        // moving focus from contact - to now show ... button
        // do nothing (safari workaround)
        if (WebAppExecutionContext.getBrowser()
                .isSupportingNativeMouseActions()) {
            DriverUtils.moveMouserOver(this.getDriver(), gearButton);
        }
        return DriverUtils
                .waitUntilLocatorDissapears(
                        this.getDriver(),
                        By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
                                .apply(conversationName)));
    }

    public void clickOptionsButtonForContact(String conversationName)
            throws Exception {
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingNativeMouseActions()) {
            DriverUtils.addClassToParent(this.getDriver(),
                    getListElementByName(conversationName, false), "hover");
        } else {
            DriverUtils.moveMouserOver(this.getDriver(),
                    getListElementByName(conversationName, false));
        }
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String cssOptionsButtonLocator = WebAppLocators.ContactListPage.cssOptionsButtonByContactName
                .apply(conversationName);
        final By locator = By.cssSelector(cssOptionsButtonLocator);
        final WebElement optionsButton = getDriver().findElement(locator);
        DriverUtils.waitUntilElementClickable(getDriver(), optionsButton);
        optionsButton.click();
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingNativeMouseActions()) {
            DriverUtils.removeClassFromParent(this.getDriver(),
                    getListElementByName(conversationName, false), "hover");
        }
    }

    public List<String> getConvOptions() throws Exception {
        List<WebElement> option = getDriver().findElements(By.cssSelector("li"));
        List<String> convOptions = new ArrayList<>();
        for (WebElement element : option) {
            if (element.isDisplayed()) {
                convOptions.add(element.getAttribute("title"));
            }
        }
        return convOptions;
    }

    public void clickWithJS(String cssSelector) throws Exception {
        this.getDriver()
                .executeScript(
                        String.format("$(document).find(\"%s\").click();",
                                cssSelector));
    }

    public void openConversation(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        By locator = By.cssSelector(WebAppLocators.ContactListPage.cssContactListEntryByName.apply(conversationName));
        WebElement conversation = getDriver().findElement(locator);
        DriverUtils.waitUntilElementClickable(this.getDriver(), conversation);
        conversation.click();
    }

    public boolean isConversationVisible(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        By locator = By.cssSelector(WebAppLocators.ContactListPage.cssContactListEntryByName.apply(conversationName));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator, CONVO_LIST_ENTRY_VISIBILITY_TIMEOUT);
    }

    public boolean isConversationSelected(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        By selected = By.cssSelector(WebAppLocators.ContactListPage.cssSelectedContactListEntryByName.apply(conversationName));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), selected);
    }

    public boolean isConnectionRequestsListVisible() throws Exception{
        By locator = By.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator, CONVO_LIST_ENTRY_VISIBILITY_TIMEOUT);
    }

    public void openConnectionRequestsList() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
        this.getDriver().findElement(locator).click();
    }

    public boolean isConnectionRequestsListSelected() throws Exception {
        By selected = By.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItemSelected);
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), selected);
    }

    public boolean isPreferencesButtonClickable() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), gearButton);
    }

    public void openPreferences() throws Exception {
        gearButton.click();
    }

    public void openStartUI() throws Exception {
        DriverUtils
                .waitUntilLocatorAppears(
                        this.getDriver(),
                        By.cssSelector(WebAppLocators.ContactListPage.cssOpenStartUIButton));
        if (WebAppExecutionContext.getBrowser() == Browser.InternetExplorer) {
            clickWithJS(WebAppLocators.ContactListPage.cssOpenStartUIButton);
        } else {
            openStartUIButton.click();
        }
    }

    public void clickUnmuteConversation() throws Exception {
        unmuteButton.click();
    }

    public void clickLeaveConversation() throws Exception {
        leaveButton.click();
    }

    public void unarchiveConversation(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, true);
        final By archivedEntryLocator = By
                .xpath(WebAppLocators.ContactListPage.xpathArchivedContactListEntryByName
                        .apply(conversationName));
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                archivedEntryLocator, 3);
        final WebElement archivedEntry = this.getDriver().findElement(
                archivedEntryLocator);
        archivedEntry.click();

        assert isConversationSelected(conversationName) : "Conversation not selected after unarchiving";
    }

    public String getIncomingPendingItemText() throws Exception {
        final By entryLocator = By
                .cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                entryLocator) : "There are no visible incoming pending connections in the conversations list";
        return getDriver().findElement(entryLocator).getText();
    }

    public int getItemIndex(String convoName) throws Exception {
        convoName = fixDefaultGroupConvoName(convoName, false);
        final int entriesCount = this
                .getDriver()
                .findElements(
                        By.xpath(WebAppLocators.ContactListPage.xpathContactListEntries))
                .size();
        for (int entryIdx = 1; entryIdx <= entriesCount; entryIdx++) {
            final WebElement entryElement = this
                    .getDriver()
                    .findElement(
                            By.xpath(WebAppLocators.ContactListPage.xpathContactListEntryByIndex
                                    .apply(entryIdx)));
            if (entryElement.getAttribute("data-uie-value").equals(convoName)) {
                return entryIdx;
            }
        }
        throw new AssertionError(String.format(
                "There is no '%s' conversation in the list", convoName));
    }

    public void waitUntilArchiveButtonIsNotVisible(int archiveBtnVisilityTimeout)
            throws Exception {
        assert DriverUtils
                .waitUntilLocatorDissapears(
                        this.getDriver(),
                        By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
                        archiveBtnVisilityTimeout) : "Open Archive button is still visible after "
                + archiveBtnVisilityTimeout + " second(s)";
    }

    public void waitUntilArchiveButtonIsVisible(int archiveBtnVisilityTimeout)
            throws Exception {
        assert DriverUtils
                .waitUntilLocatorIsDisplayed(
                        this.getDriver(),
                        By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
                        archiveBtnVisilityTimeout) : "Open Archive button is not visible after "
                + archiveBtnVisilityTimeout + " second(s)";
    }

    public AccentColor getCurrentPingIconAccentColor(String name)
            throws Exception {
        final By locator = By
                .xpath(WebAppLocators.ContactListPage.xpathPingIconByContactName
                        .apply(name));
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 3);
        final WebElement entry = getDriver().findElement(locator);
        return AccentColor.getByRgba(entry.getCssValue("color"));
    }

    public boolean isUnreadDotVisibleForConversation(String name) throws Exception {
        final By locator = By.xpath(WebAppLocators.ContactListPage.xpathUnreadDotByContactName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean isUnreadDotInvisibleForConversation(String name) throws Exception {
        final By locator = By.xpath(WebAppLocators.ContactListPage.xpathUnreadDotByContactName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public AccentColor getCurrentUnreadDotAccentColor(String name) throws Exception {
        final By locator = By.xpath(WebAppLocators.ContactListPage.xpathUnreadDotByContactName.apply(name));
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
        final WebElement entry = getDriver().findElement(locator);
        return AccentColor.getByRgba(entry.getCssValue("background-color"));
    }

    public boolean isPingIconVisibleForConversation(String conversationName)
            throws Exception {
        final By locator = By
                .xpath(WebAppLocators.ContactListPage.xpathPingIconByContactName
                        .apply(conversationName));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator, 3);
    }

    public String getMuteButtonToolTip() throws Exception {
        return muteButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
    }

    public void pressShortCutToMuteOrUnmute()
            throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ALT, "s"));
        } else {
            throw new PendingException(
                    "Webdriver does not support shortcuts for Mac browsers");
        }
    }

    public void pressShortcutForArchive() throws Exception {
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            conversationInput.sendKeys(Keys.chord(Keys.ALT, Keys.CONTROL, "D"));
        } else {
            conversationInput.sendKeys(Keys.chord(Keys.META, Keys.ALT, Keys.SHIFT, "d"));
        }
    }

    public boolean isLeaveWarningModalVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.ContactListPage.cssLeaveModal));
    }

    public void clickCancelOnLeaveWarning() {
        leaveModalCancelButton.click();
    }

    public boolean isBlockWarningModalVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.ContactListPage.cssBlockModal));
    }

    public void clickCancelOnBlockWarning() {
        blockModalCancelButton.click();
    }

    public void clickBlockOnBlockWarning() {
        blockModalActionButton.click();
    }

    public void clickDeleteConversation() throws Exception {
        deleteButton.click();
    }

    public boolean isDeleteWarningModalForGroupVisible() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.ContactListPage.cssDeleteModalGroup));
    }

    public void clickDeleteOnDeleteWarning() {
        deleteModalActionButtonGroup.click();
    }

    public void clickLeaveOnLeaveWarning() {
        leaveModalActionButton.click();
    }

    public void clickLeaveCheckboxOnDeleteWarning() {
        deleteModalLeaveCheckboxGroup.click();
    }

    public void clickCancelOnDeleteWarning() {
        deleteModalCancelButtonGroup.click();
    }

    public boolean isDeleteWarningModalSingleVisible() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(WebAppLocators.ContactListPage.cssDeleteModalSingle));
    }

    public void clickDeleteOnDeleteWarningSingle() {
        deleteModalActionButtonSingle.click();
    }

    public void clickCancelOnDeleteWarningSingle() {
        deleteModalCancelButtonSingle.click();
    }

    public void clickCancelRequest() throws Exception {
        cancelRequestButton.click();
    }

    public void clickBlockButton() throws Exception {
        blockButton.click();
    }

    public void openContextMenuWithContextClickForConversation(String name) throws Exception {
        if (name == null) {
            throw new Exception("The name to look for in the conversation list was null");
        }
        name = fixDefaultGroupConvoName(name, false, false);
        final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName.apply(name);
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.cssSelector(locator));
        WebElement entry = getDriver().findElement(By.cssSelector(locator));
        new Actions(getDriver()).contextClick(entry).perform();
    }

    public boolean waitForBadgeVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(WebAppLocators.ContactListPage.cssBadge));
    }

    public BufferedImage getBackgroundPicture() throws Exception {
        Optional<BufferedImage> screenshot = getElementScreenshot(background);
        if (!screenshot.isPresent()) {
            throw new Exception("Could not get screenshot of contact list with background");
        }
        return screenshot.get();
    }
}
