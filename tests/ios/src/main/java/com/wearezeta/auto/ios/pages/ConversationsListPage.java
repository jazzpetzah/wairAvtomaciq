package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDragArguments;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConversationsListPage extends IOSPage {
    private static final By nameSettingsGearButton = MobileBy.AccessibilityId("bottomBarSettingsButton");

    private static final By nameOpenArchiveButton = MobileBy.AccessibilityId("bottomBarArchivedButton");

    private static final String xpathStrContactListRoot = "//XCUIElementTypeCollectionView";

    protected static final String xpathStrContactListItems = xpathStrContactListRoot + "/XCUIElementTypeCell";
    private static final Function<String, String> xpathStrContactListItemByExpr = xpathExpr ->
            String.format("%s//XCUIElementTypeStaticText[%s]", xpathStrContactListItems, xpathExpr);
    protected static final Function<String, String> xpathStrConvoListEntryByName = name ->
            String.format("%s[ .//XCUIElementTypeStaticText[@value='%s'] ]", xpathStrContactListItems, name);
    private static final Function<Integer, String> xpathStrConvoListEntryByIdx = idx ->
            String.format("%s[%s]", xpathStrContactListItems, idx);
    private static final Function<String, String> xpathStrFirstConversationEntryByName = name ->
            String.format("%s[1][ .//XCUIElementTypeStaticText[@value='%s'] ]", xpathStrContactListItems, name);

    private static final String strNameContactsButton = "bottomBarContactsButton";

    public static final By nameContactsButton = MobileBy.AccessibilityId(strNameContactsButton);

    protected static final By xpathContactsLabel = By.xpath(
            String.format("//XCUIElementTypeButton[@name='%s' and @label='CONTACTS']", strNameContactsButton));

    private static final By xpathPendingRequest =
            By.xpath("//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[contains(@name,' waiting')] ]");

    private static final By nameMuteCallButton = MobileBy.AccessibilityId("MuteVoiceButton");

    // public static final By nameCancelActionButton = MobileBy.AccessibilityId("CANCEL");

    private static final Function<String, String> xpathStrContactListPlayPauseButtonByConvoName = name ->
            String.format("//XCUIElementTypeCell[ .//*[@name='%s'] ]" +
                    "//XCUIElementTypeButton[@name='mediaCellButton']", name);

    private static final Function<String, String> xpathStrActionMenuByConversationName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name.toUpperCase());

    private static final By nameEmptyConversationsListMessage = MobileBy.AccessibilityId(
            "NO ACTIVE CONVERSATIONS TAP CONTACTS TO START A CONVERSATION");

    private static final By nameCloseArchiveButton = MobileBy.AccessibilityId("archiveCloseButton");

    private static final By nameConversationsHintTextLabel = MobileBy.AccessibilityId("Conversations start here");

    public ConversationsListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapContactsButton() throws Exception {
        getElement(nameContactsButton).click();
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
        Thread.sleep(1500);
    }

    protected Optional<WebElement> findNameInContactList(String name, int timeoutSeconds) throws Exception {
        final By locator = FBBy.xpath(xpathStrConvoListEntryByName.apply(name));
        return getElementIfDisplayed(locator, timeoutSeconds);
    }

    protected Optional<WebElement> findNameInContactList(String name) throws Exception {
        return findNameInContactList(name,
                Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass())));
    }

    public boolean isConversationInList(String name) throws Exception {
        return findNameInContactList(name).isPresent();
    }

    public boolean isConversationInList(String name, int timeoutSeconds) throws Exception {
        return findNameInContactList(name, timeoutSeconds).isPresent();
    }

    private void swipeRightOnContact(String name) throws Exception {
        final FBElement dstElement = (FBElement) findNameInContactList(name).orElseThrow(
                () -> new IllegalStateException(String.format("Cannot find a conversation named '%s'", name))
        );
        final Dimension elSize = dstElement.getSize();
        final double y = elSize.getHeight() * 8 / 9;
        dstElement.dragFromToForDuration(
                new FBDragArguments(elSize.getWidth() / 10, y, elSize.getWidth() * 3 / 4, y, 1)
        );
    }

    public void swipeRightConversationToRevealActionButtons(String conversation) throws Exception {
        swipeRightOnContact(conversation);
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isPendingRequestInContactList() throws Exception {
        return isLocatorDisplayed(xpathPendingRequest, 5);
    }

    public boolean pendingRequestInContactListIsNotShown() throws Exception {
        return isLocatorInvisible(xpathPendingRequest);
    }

    public void tapPendingRequest() throws Exception {
        getElement(xpathPendingRequest).click();
    }

    public boolean isConversationNotInList(String name, int timeoutSeconds) throws Exception {
        return isLocatorInvisible(MobileBy.AccessibilityId(name), timeoutSeconds);
    }

    public boolean isConversationNotInList(String name) throws Exception {
        return isConversationNotInList(name, 5);
    }

    public boolean isConversationWithUsersExist(List<String> names, int timeoutSeconds) throws Exception {
        final String xpathExpr = String.join(" and ", names.stream().
                map(x -> String.format("contains(@name, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrContactListItemByExpr.apply(xpathExpr));
        return isLocatorDisplayed(locator, timeoutSeconds);
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
        return isLocatorDisplayed(nameMuteCallButton);
    }

    public void clickMuteCallButton() throws Exception {
        getElement(nameMuteCallButton).click();
    }

    public boolean isActionMenuVisibleForConversation(String conversation) throws Exception {
        final By locator = By.xpath(xpathStrActionMenuByConversationName.apply(conversation));
        return isLocatorDisplayed(locator);
    }

    private static By getActionButtonByName(String buttonTitle) {
        return MobileBy.AccessibilityId(buttonTitle.toUpperCase());
    }

    public boolean isButtonVisibleInActionMenu(String buttonTitle) throws Exception {
        return isLocatorDisplayed(getActionButtonByName(buttonTitle));
    }

    public void tapButtonInActionMenu(String buttonTitle) throws Exception {
        getElement(getActionButtonByName(buttonTitle)).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public BufferedImage getSettingsGearStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(nameSettingsGearButton)).orElseThrow(() ->
                new IllegalStateException("Settings gear button is not visible"));
    }

    public void tapConvoItemByIdx(int idx) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(idx));
        getElement(locator, String.format("Conversation list entry number '%s' is not visible", idx)).click();
    }

    public boolean isFirstConversationName(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrFirstConversationEntryByName.apply(convoName));
        return isLocatorDisplayed(locator);
    }

    public void openArchivedConversations() throws Exception {
        tapElementWithRetryIfStillDisplayed(nameOpenArchiveButton);
    }

    public boolean isArchiveButtonVisible() throws Exception {
        return isLocatorDisplayed(nameOpenArchiveButton);
    }

    public boolean isArchiveButtonInvisible() throws Exception {
        return isLocatorInvisible(nameOpenArchiveButton);
    }

    public boolean contactsLabelIsVisible() throws Exception {
        return isLocatorDisplayed(xpathContactsLabel);
    }

    public boolean contactLabelIsNotVisible() throws Exception {
        return isLocatorInvisible(xpathContactsLabel);
    }

    public boolean noConversationsMessageIsVisible() throws Exception {
        return isLocatorDisplayed(nameEmptyConversationsListMessage);
    }

    public void clickCloseArchivePageButton() throws Exception {
        getElement(nameCloseArchiveButton).click();
    }

    public boolean hintTextIsVisible() throws Exception {
        return isLocatorDisplayed(nameConversationsHintTextLabel);
    }

    public boolean hintTextIsNotVisible() throws Exception {
        return isLocatorInvisible(nameConversationsHintTextLabel);
    }

    public void tapHintText() throws Exception {
        getElement(nameConversationsHintTextLabel).click();
    }
}
