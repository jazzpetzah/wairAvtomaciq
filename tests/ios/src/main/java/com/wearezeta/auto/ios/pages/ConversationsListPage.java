package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.ios.tools.FastLoginContainer;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConversationsListPage extends IOSPage {
    private static final int CONV_SWIPE_TIME = 500;

    private static final By nameSettingsGearButton = MobileBy.AccessibilityId("bottomBarSettingsButton");

    private static final By nameOpenArchiveButton = MobileBy.AccessibilityId("bottomBarArchivedButton");

    private static final String xpathStrContactListRoot = String.format("(%s//XCUIElementTypeCollectionView)[1]",
            xpathStrMainWindow);

    protected static final String xpathStrContactListItems = xpathStrContactListRoot + "/XCUIElementTypeCell";
    private static final Function<String, String> xpathStrContactListItemByExpr = xpathExpr ->
            String.format("%s/XCUIElementTypeStaticText[%s]", xpathStrContactListItems, xpathExpr);
    protected static final Function<String, String> xpathStrConvoListEntryByName = name ->
            String.format("%s/XCUIElementTypeStaticText[@value='%s']/parent::*", xpathStrContactListItems, name);
    private static final Function<Integer, String> xpathStrConvoListEntryByIdx = idx ->
            String.format("%s[%s]", xpathStrContactListItems, idx);
    private static final Function<String, String> xpathStrFirstConversationEntryByName = name ->
            String.format("%s[1]/XCUIElementTypeStaticText[@value='%s']", xpathStrContactListItems, name);

    private static final String strNameContactsButton = "bottomBarContactsButton";

    public static final By nameContactsButton = MobileBy.AccessibilityId(strNameContactsButton);

    protected static final By xpathContactsLabel = By.xpath(
            String.format("//XCUIElementTypeButton[@name='%s' and @label='CONTACTS']", strNameContactsButton));

    private static final By xpathPendingRequest =
            By.xpath("//XCUIElementTypeCell[contains(@name,' waiting')]/XCUIElementTypeStaticText[1]");

    private static final By nameMuteCallButton = MobileBy.AccessibilityId("MuteVoiceButton");

    public static final By nameCancelActionButton = MobileBy.AccessibilityId("CANCEL");

    private static final By nameSendAnInviteButton = MobileBy.AccessibilityId("INVITE MORE PEOPLE");


    private static final Function<String, String> xpathStrContactListPlayPauseButtonByConvoName = name ->
            String.format("//XCUIElementTypeCell[@name='%s']///XCUIElementTypeButton[@name='mediaCellButton']", name);

    private static final Function<String, String> xpathStrSelectedConversationEntryByName = name ->
            String.format("%s/XCUIElementTypeCell[@name='%s']", xpathStrContactListRoot, name);

    private static final Function<String, String> xpathStrActionMenuByConversationName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s' and @visible='true']", name.toUpperCase());

    private static final By nameEmptyConversationsListMessage = MobileBy.AccessibilityId(
            "NO ACTIVE CONVERSATIONS TAP CONTACTS TO START A CONVERSATION");

    private static final By nameCloseArchiveButton = MobileBy.AccessibilityId("archiveCloseButton");

    private static final By nameConversationsHintTextLabel = MobileBy.AccessibilityId("Conversations start here");

    private static final By shareContactsAlertOKButton = By.xpath(xpathStrAlertButtonByCaption.apply("OK"));

    public ConversationsListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapContactsButton() throws Exception {
        getElement(nameContactsButton).click();
        if (FastLoginContainer.getInstance().isEnabled()) {
            getElementIfDisplayed(shareContactsAlertOKButton, 3).orElseGet(DummyElement::new).click();
        } else {
            // Wait until animation is completed
            isElementDisplayed(PeoplePickerPage.xpathPickerClearButton, 3);
        }
    }

    public void tapPlayPauseButtonNextTo(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactListPlayPauseButtonByConvoName.apply(name));
        getElement(locator).click();
    }

    public void tapSettingsGearButton() throws Exception {
        getElement(nameSettingsGearButton).click();
        // Wait for transition animation
        Thread.sleep(1000);
    }

    public void tapOnName(String name) throws Exception {
        findNameInContactList(name).orElseThrow(
                () -> new IllegalStateException(String.format("The conversation '%s' is not visible in the list", name))
        ).click();
        // Wait for transition animation
        Thread.sleep(1000);
    }

    private Optional<WebElement> findNameInContactList(String name, int timeoutSeconds) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByName.apply(name));
        return getElementIfDisplayed(locator, timeoutSeconds);
    }

    private Optional<WebElement> findNameInContactList(String name) throws Exception {
        return findNameInContactList(name,
                Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass())));
    }

    public boolean isConversationInList(String name) throws Exception {
        return findNameInContactList(name).isPresent();
    }

    public boolean isConversationInList(String name, int timeoutSeconds) throws Exception {
        return findNameInContactList(name, timeoutSeconds).isPresent();
    }

    public void swipeRightOnContact(String contact) throws Exception {
        DriverUtils.swipeRight(this.getDriver(),
                findNameInContactList(contact).orElseThrow(IllegalStateException::new), CONV_SWIPE_TIME, 90, 50);
    }

    public void swipeRightConversationToRevealActionButtons(String conversation) throws Exception {
        int count = 0;
        do {
            swipeRightOnContact(conversation);
            count++;
        } while ((count < 5) && !isCancelActionButtonVisible());
    }

    public boolean isPendingRequestInContactList() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), xpathPendingRequest, 5);
    }

    public boolean pendingRequestInContactListIsNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathPendingRequest);
    }

    public void clickPendingRequest() throws Exception {
        getElement(xpathPendingRequest).click();
    }

    public boolean isConversationNotInList(String name, int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), MobileBy.AccessibilityId(name), timeoutSeconds);
    }

    public boolean isConversationNotInList(String name) throws Exception {
        return isConversationNotInList(name, 5);
    }

    @Override
    public void swipeDown(int time) throws Exception {
        Point coords = getElement(nameMainWindow).getLocation();
        Dimension elementSize = getElement(nameMainWindow).getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + 150, coords.x + elementSize.width / 2,
                coords.y + elementSize.height - 150, time);
    }

    public boolean isConversationWithUsersExist(List<String> names, int timeoutSeconds) throws Exception {
        final String xpathExpr = String.join(" and ", names.stream().
                map(x -> String.format("contains(@name, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrContactListItemByExpr.apply(xpathExpr));
        return isElementDisplayed(locator, timeoutSeconds);
    }

    private boolean isCancelActionButtonVisible() throws Exception {
        return isElementDisplayed(nameCancelActionButton);
    }

    public BufferedImage getConversationEntryScreenshot(int idx) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(idx));
        final WebElement el = getElement(locator,
                String.format("Conversation list entry number '%s' is not visible", idx));
        // ImageIO.write(takeScreenshot().get(), "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() +
        // ".png"));
        return this.getElementScreenshot(el).orElseThrow(IllegalStateException::new);
        // ImageIO.write(scr, "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
        // return scr;
    }

    public BufferedImage getConversationEntryScreenshot(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByName.apply(name));
        final WebElement el = getElement(locator, String.format("Conversation list entry '%s' is not visible", name));
        return this.getElementScreenshot(el).orElseThrow(IllegalStateException::new);
        // ImageIO.write(scr, "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
        // return scr;
    }

    public boolean isMuteCallButtonVisible() throws Exception {
        return isElementDisplayed(nameMuteCallButton);
    }

    public void clickMuteCallButton() throws Exception {
        getElement(nameMuteCallButton).click();
    }

    public boolean isActionMenuVisibleForConversation(String conversation) throws Exception {
        final By locator = By.xpath(xpathStrActionMenuByConversationName.apply(conversation));
        return isElementDisplayed(locator);
    }

    private By getActionButtonByName(String buttonTitle) {
        return MobileBy.AccessibilityId(buttonTitle.toUpperCase());
    }

    public boolean isButtonVisibleInActionMenu(String buttonTitle) throws Exception {
        return isElementDisplayed(getActionButtonByName(buttonTitle));
    }

    public void tapButtonInActionMenu(String buttonTitle) throws Exception {
        getElement(getActionButtonByName(buttonTitle)).click();
    }

    public Optional<String> getSelectedConversationCellValue(String conversation) throws Exception {
        final By locator = By.xpath(xpathStrSelectedConversationEntryByName.apply(conversation));
        final Optional<WebElement> cell = getElementIfDisplayed(locator);
        if (cell.isPresent()) {
            return Optional.of(cell.get().getAttribute("value"));
        }
        return Optional.empty();
    }

    public boolean isInviteMorePeopleButtonVisible() throws Exception {
        return isElementDisplayed(nameSendAnInviteButton);
    }

    public boolean isInviteMorePeopleButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameSendAnInviteButton);
    }

    public BufferedImage getSettingsGearStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(nameSettingsGearButton)).orElseThrow(() ->
                new IllegalStateException("Settings gear button is not visible"));
    }

    public void tapConvoItemByIdx(int idx) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(idx));
        getElement(locator, String.format("Conversation list entry number '%s' is not visible", idx)).click();
    }

    public void tapOnNameYourInCallWith(String name) throws Exception {
        findNameIamCallingInContactList(name).orElseThrow(
                () -> new IllegalStateException(
                        String.format("The conversation '%s' you are in a call with is not shown on top", name))
        ).click();
    }

    private Optional<WebElement> findNameIamCallingInContactList(String name) throws Exception {
        return getElementIfDisplayed(MobileBy.AccessibilityId(name));
    }

    public boolean isFirstConversationName(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrFirstConversationEntryByName.apply(convoName));
        return isElementDisplayed(locator);
    }

    public void openArchivedConversations() throws Exception {
        clickElementWithRetryIfStillDisplayed(nameOpenArchiveButton);
    }

    public boolean isArchiveButtonVisible() throws Exception {
        return isElementDisplayed(nameOpenArchiveButton);
    }

    public boolean isArchiveButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameOpenArchiveButton);
    }

    public boolean contactsLabelIsVisible() throws Exception {
        return isElementDisplayed(xpathContactsLabel);
    }

    public boolean contactLabelIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathContactsLabel);
    }

    public boolean noConversationsMessageIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameEmptyConversationsListMessage);
    }

    public void clickCloseArchivePageButton() throws Exception {
        getElement(nameCloseArchiveButton).click();
    }

    public boolean hintTextIsVisible() throws Exception {
        return isElementDisplayed(nameConversationsHintTextLabel);
    }

    public boolean hintTextIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConversationsHintTextLabel);
    }

    public void tapHintText() throws Exception {
        getElement(nameConversationsHintTextLabel).click();
    }
}
