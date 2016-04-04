package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConversationsListPage extends IOSPage {
    private static final int CONV_SWIPE_TIME = 500;

    private static final By nameSelfButton = MobileBy.AccessibilityId("SelfButton");

    private static final String xpathStrContactListRoot = xpathStrMainWindow + "/UIACollectionView[1]";
    private static final By xpathContactListRoot = By.xpath(xpathStrContactListRoot);

    protected static final String xpathStrContactListItems = xpathStrContactListRoot + "/UIACollectionCell";
    private static final Function<String, String> xpathStrContactListItemByExpr = xpathExpr ->
            String.format("%s/UIAStaticText[%s]", xpathStrContactListItems, xpathExpr);
    protected static final Function<String, String> xpathStrConvoListEntryByName = name ->
            String.format("%s/UIAStaticText[@value='%s']/parent::*", xpathStrContactListItems, name);
    private static final Function<Integer, String> xpathStrConvoListEntryByIdx = idx ->
            String.format("%s[%s]", xpathStrContactListItems, idx);
    private static final Function<String, String> xpathStrFirstConversationEntryByName = name ->
            String.format("%s[1]/UIAStaticText[@value='%s']", xpathStrContactListItems, name);

    private static final By nameOpenStartUI = MobileBy.AccessibilityId("START A CONVERSATION");

    private static final By nameMediaCellPlayButton = MobileBy.AccessibilityId("mediaCellButton");

    private static final By xpathPendingRequest =
            By.xpath("//UIACollectionCell[contains(@name,' waiting')]/UIAStaticText[1]");

    private static final By nameMuteCallButton = MobileBy.AccessibilityId("MuteVoiceButton");

    private static final By nameLeaveConversationButton = MobileBy.AccessibilityId("LEAVE");

    public static final By nameCancelButton = MobileBy.AccessibilityId("CANCEL");

    private static final By nameSendAnInviteButton = MobileBy.AccessibilityId("INVITE MORE PEOPLE");

    private static final Function<String, String> xpathStrContactListPlayPauseButtonByConvoName = name ->
            String.format("//UIACollectionCell[@name='%s']/UIAButton[@name='mediaCellButton']", name);

    private static final By xpathArchiveConversationButton =
            By.xpath("//UIAButton[@name='ARCHIVE' and @visible='true']");

    private static final Function<String, String> xpathStrSelectedConversationEntryByName = name ->
            String.format("%s/UIACollectionView[1]/UIACollectionCell[@name='%s']", xpathStrMainWindow, name);

    private static final Function<String, String> xpathStrActionMenuXButtonByName = name ->
            String.format("//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAButton[@name='%s']",
                    name.toUpperCase());

    private static final Function<String, String> xpathStrActionMenuByConversationName = name ->
            String.format("//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAStaticText[@name='%s']",
                    name.toUpperCase());


    public ConversationsListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void openSearch() throws Exception {
        getElement(nameOpenStartUI).click();
        // Wait until animation is completed
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), PeoplePickerPage.xpathPickerClearButton, 3);
    }

    public boolean isPlayPauseButtonVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrContactListPlayPauseButtonByConvoName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public void tapPlayPauseButton() throws Exception {
        getElement(nameMediaCellPlayButton).click();
    }

    public void tapPlayPauseButtonNextTo(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactListPlayPauseButtonByConvoName.apply(name));
        getElement(locator).click();
    }

    public void tapMyAvatar() throws Exception {
        getElement(nameSelfButton).click();
    }

    public void tapOnName(String name) throws Exception {
        findNameInContactList(name).orElseThrow(
                () -> new IllegalStateException(String.format("The conversation '%s' is not visible in the list", name))
        ).click();
        // Wait for transition animation
        Thread.sleep(1000);
    }

    private Optional<WebElement> findNameInContactList(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByName.apply(name));
        final Optional<WebElement> contactCell = getElementIfDisplayed(locator);
        if (contactCell.isPresent()) {
            return contactCell;
        } else {
            try {
                return Optional.of(((IOSElement) getElement(xpathContactListRoot)).scrollToExact(name));
            } catch (WebDriverException e) {
                return Optional.empty();
            }
        }
    }

    public boolean isChatInContactList(String name) throws Exception {
        return findNameInContactList(name).isPresent();
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

    public boolean waitForContactListToLoad() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(xpathStrConvoListEntryByIdx.apply(1)));
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

    public boolean contactIsNotDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), MobileBy.AccessibilityId(name), 5);
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, timeoutSeconds);
    }

    private boolean isCancelActionButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCancelButton);
    }

    public void clickArchiveConversationButton() throws Exception {
        WebElement archiveButton = this.getDriver().findElement(xpathArchiveConversationButton);
        DriverUtils.tapByCoordinates(getDriver(), archiveButton);
    }

    public BufferedImage getConversationEntryScreenshot(int idx) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(idx));
        final WebElement el = getElement(locator,
                String.format("Conversation list entry number '%s' is not visible", idx));
        // ImageIO.write(takeScreenshot().get(), "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameMuteCallButton);
    }

    public void clickMuteCallButton() throws Exception {
        getElement(nameMuteCallButton).click();
    }

    public boolean isActionMenuVisibleForConversation(String conversation) throws Exception {
        final By locator = By.xpath(xpathStrActionMenuByConversationName.apply(conversation));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isButtonVisibleInActionMenu(String buttonTitle) throws Exception {
        final By locator = By.xpath(xpathStrActionMenuXButtonByName.apply(buttonTitle));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickArchiveButtonInActionMenu() throws Exception {
        WebElement archiveButton = this.getDriver().findElement(xpathArchiveConversationButton);
        DriverUtils.tapByCoordinates(getDriver(), archiveButton);
    }

    public void clickLeaveButtonInActionMenu() throws Exception {
        getElement(nameLeaveConversationButton).click();
    }

    public void clickCancelButtonInActionMenu() throws Exception {
        getElement(nameCancelButton).click();
    }

    public Optional<String> getSelectedConversationCellValue(String conversation)
            throws Exception {
        final By locator = By.xpath(xpathStrSelectedConversationEntryByName.apply(conversation));
        final Optional<WebElement> cell = getElementIfDisplayed(locator);
        if (cell.isPresent()) {
            return Optional.of(cell.get().getAttribute("value"));
        }
        return Optional.empty();
    }

    public boolean isInviteMorePeopleButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSendAnInviteButton);
    }

    public boolean isInviteMorePeopleButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameSendAnInviteButton);
    }

    public boolean waitUntilSelfButtonIsDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSelfButton);
    }

    public BufferedImage getAvatarStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(nameSelfButton)).orElseThrow(() ->
                new IllegalStateException("Self avatar is not visible"));
    }

    public void tapConvoItemByIdx(int idx) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(idx));
        getElement(locator, String.format("Conversation list entry number '%s' is not visible", idx)).click();
    }

    public void tapOnNameYourInCallWith(String name) throws Exception {
        findNameIamCallingInContactList(name).orElseThrow(
                () -> new IllegalStateException(String.format("The conversation '%s' you are in a call with is not" +
                        " shown on top", name))
        ).click();
    }

    private Optional<WebElement> findNameIamCallingInContactList(String name) throws Exception {
        return getElementIfDisplayed(MobileBy.AccessibilityId(name));
    }

    public boolean isFirstConversationName(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrFirstConversationEntryByName.apply(convoName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void openArchivedConversations() throws Exception {
        // This is to make sure that we are not in some transition state from the previous step
        Thread.sleep(3000);
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 4s")) {
                IOSSimulatorHelper.swipe(0.2, 0.6, 0.2, 0.1);
            } else {
                IOSSimulatorHelper.swipe(0.2, 0.7, 0.2, 0.1);
            }
        } else {
            swipeUp(1000);
        }
    }
}
