package com.wearezeta.auto.ios.pages;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDragArguments;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.misc.Timedelta;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.Point;

public class ConversationsListPage extends IOSPage {
    private static final By nameSettingsGearButton = MobileBy.AccessibilityId("bottomBarSettingsButton");

    private static final By nameOpenArchiveButton = MobileBy.AccessibilityId("bottomBarArchivedButton");

    protected static final By fbClassConversationsListRoot = FBBy.className("XCUIElementTypeCollectionView");

    // TODO: change these locators to relative after WDA update
    private static final Function<String, String> xpathStrConvoListItemRelativeByExpr = xpathExpr ->
            String.format("//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[%s] ]", xpathExpr);
    protected static final Function<String, String> xpathStrConvoListRelativeEntryByName = name ->
            xpathStrConvoListItemRelativeByExpr.apply("@value='" + name + "'");
    private static final Function<Integer, String> xpathStrConvoListEntryByIdx = idx ->
            String.format("//XCUIElementTypeCollectionView/XCUIElementTypeCell[%s]", idx);
    private static final Function<String, String> xpathStrFirstConversationEntryByName = name ->
            String.format("%s[ .//XCUIElementTypeStaticText[@value='%s'] ]",
                    xpathStrConvoListEntryByIdx.apply(1), name);

    private static final String strNameContactsButton = "bottomBarContactsButton";

    public static final By nameContactsButton = MobileBy.AccessibilityId(strNameContactsButton);

    protected static final By xpathContactsLabel = By.xpath(
            String.format("//XCUIElementTypeButton[@name='%s' and @label='CONTACTS']", strNameContactsButton));

    private static final By xpathPendingRequest =
            By.xpath("//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[contains(@name,' waiting')] ]");

    // public static final By nameCancelActionButton = MobileBy.AccessibilityId("CANCEL");

    private static final Function<String, String> xpathStrContactListPlayPauseButtonByConvoName = name ->
            String.format("//XCUIElementTypeCell[ .//*[@name='%s'] ]" +
                    "//XCUIElementTypeButton[@name='mediaCellButton']", name);

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

    public boolean isPlayPauseButtonVisibleNextTo(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactListPlayPauseButtonByConvoName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isPlayPauseButtonInvisibleNextTo(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactListPlayPauseButtonByConvoName.apply(name));
        return isLocatorInvisible(locator);
    }

    public void tapSettingsGearButton() throws Exception {
        getElement(nameSettingsGearButton).click();
        // Wait for transition animation
        Thread.sleep(1000);
    }

    public void tapOnName(String name) throws Exception {
        getConversationsListItem(name).click();
        // Wait for transition animation
        Thread.sleep(1500);
    }

    private WebElement getConversationsListItem(String name, Timedelta timeout) throws Exception {
        final WebElement convoListRoot = getElement(fbClassConversationsListRoot);
        final By locator = FBBy.xpath(xpathStrConvoListRelativeEntryByName.apply(name));
        return getElement(convoListRoot, locator,
                String.format("The conversation '%s' is not visible in the list after %s", name, timeout.toString()),
                timeout);
    }

    protected WebElement getConversationsListItem(String name) throws Exception {
        return getConversationsListItem(name,
                Timedelta.fromSeconds(Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass()))));
    }

    public boolean isConversationInList(String name) throws Exception {
        return this.isConversationInList(name,
                Timedelta.fromSeconds(Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass()))));
    }

    public boolean isConversationInList(String name, Timedelta timeout) throws Exception {
        final WebElement convoListRoot = getElement(fbClassConversationsListRoot);
        final By locator = FBBy.xpath(xpathStrConvoListRelativeEntryByName.apply(name));
        return isLocatorDisplayed(convoListRoot, locator, timeout);
    }

    private void swipeRightOnContact(String name) throws Exception {
        final FBElement dstElement = (FBElement) getConversationsListItem(name);
        final Rectangle elRect = dstElement.getRect();
        final Point startPoint = getDriver().fixCoordinates(
                new Point(elRect.x + elRect.width / 10, elRect.y + elRect.height * 8 / 9)
        );
        final Point endPoint = getDriver().fixCoordinates(
                new Point(elRect.x + elRect.width * 3 / 4, elRect.y + elRect.height * 8 / 9)
        );
        getDriver().dragFromToForDuration(
                new FBDragArguments(startPoint.x, startPoint.y, endPoint.x, endPoint.y, Timedelta.fromSeconds(1))
        );
    }

    public void swipeRightConversationToRevealActionButtons(String conversation) throws Exception {
        swipeRightOnContact(conversation);
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isPendingRequestInContactList() throws Exception {
        return isLocatorDisplayed(xpathPendingRequest, Timedelta.fromSeconds(5));
    }

    public boolean pendingRequestInContactListIsNotShown() throws Exception {
        return isLocatorInvisible(xpathPendingRequest);
    }

    public void tapPendingRequest() throws Exception {
        getElement(xpathPendingRequest).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public boolean isConversationNotInList(String name, Timedelta timeout) throws Exception {
        final WebElement convoListRoot = getElement(fbClassConversationsListRoot);
        final By locator = FBBy.xpath(xpathStrConvoListRelativeEntryByName.apply(name));
        return isLocatorInvisible(convoListRoot, locator, timeout);
    }

    public boolean isConversationNotInList(String name) throws Exception {
        return isConversationNotInList(name, Timedelta.fromSeconds(5));
    }

    public boolean isConversationWithUsersExist(List<String> names, Timedelta timeout) throws Exception {
        final WebElement root = getElement(fbClassConversationsListRoot);
        final String xpathExpr = String.join(" and ", names.stream().
                map(x -> String.format("contains(@name, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = FBBy.xpath(xpathStrConvoListItemRelativeByExpr.apply(xpathExpr));
        return isLocatorDisplayed(root, locator, timeout);
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
        final WebElement convoEntry = getConversationsListItem(name);
        return this.getElementScreenshot(convoEntry).orElseThrow(IllegalStateException::new);
        // ImageIO.write(scr, "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
        // return scr;
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
        // Wait for animation
        Thread.sleep(1000);
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
