package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactListPage extends IOSPage {
    private static final int CONV_SWIPE_TIME = 500;

    private static final By nameSelfButton = By.name("SelfButton");

    private static final String xpathStrContactListRoot = xpathStrMainWindow + "/UIACollectionView[1]";
    private static final By xpathContactListRoot = By.xpath(xpathStrContactListRoot);

    private static final String xpathStrContactListItems = xpathStrContactListRoot + "//UIACollectionCell";
    private static final Function<String, String> xpathStrContactListItemByExpr = xpathExpr ->
            String.format("%s/UIAStaticText[%s]", xpathStrContactListItems, xpathExpr);

    private static final Function<String, String> xpathStrConvoListEntryByName = name ->
            String.format("%s[ .//*[@value='%s'] ]", xpathStrContactListItems, name);
    private static final Function<Integer, String> xpathStrConvoListEntryByIdx = idx ->
            String.format("(%s)[%s]", xpathStrContactListItems, idx);
    private static final Function<Integer, String> xpathStrConvoListEntryNameByIdx = idx ->
            String.format("(%s)[%s]/UIAStaticText", xpathStrContactListItems, idx);

    private static final By nameOpenStartUI = By.name("START A CONVERSATION");

    private static final By nameMediaCellPlayButton = By.name("mediaCellButton");

    private static final By xpathPendingRequest =
            By.xpath("//UIACollectionCell[contains(@name,' waiting')]/UIAStaticText[1]");

    private static final By nameMuteCallButton = By.name("MuteVoiceButton");

    private static final By nameLeaveConversationButton = By.name("LEAVE");

    public static final By nameCancelButton = By.name("CANCEL");

    private static final By nameSendAnInviteButton = By.name("INVITE MORE PEOPLE");

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


    public ContactListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isMyUserNameDisplayedFirstInContactList(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(1));
        final Optional<WebElement> el = getElementIfDisplayed(locator);
        return el.isPresent() && el.get().getText().equalsIgnoreCase(name);
    }

    public void openSearch() throws Exception {
        getElement(nameOpenStartUI).click();
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

    public void tapOnMyName() throws Exception {
        getElement(nameSelfButton).click();
    }

    public String getSelfButtonLabel() throws Exception {
        return getElement(nameSelfButton).getAttribute("label").toUpperCase();
    }

    public boolean isSelfButtonContainingFirstNameLetter(String name) throws Exception {
        String sub = name.substring(0, 1).toUpperCase();
        return sub.equals(getSelfButtonLabel());
    }

    public void tapOnName(String name) throws Exception {
        findNameInContactList(name).orElseThrow(IllegalStateException::new).click();
    }

    public String getFirstDialogName() throws Exception {
        return getDialogNameByIndex(1);
    }

    public String getDialogNameByIndex(int index) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryNameByIdx.apply(index));
        return getElement(locator, String.format("Conversation # %s is not visible", index)).getText();
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

    public String getFirstConversationName() throws Exception {
        return getDialogNameByIndex(1);
    }

    public void tapOnGroupChat(String chatName) throws Exception {
        findNameInContactList(chatName).orElseThrow(IllegalStateException::new).click();
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

    public boolean isDisplayedInContactList(String name) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(name), 5);
    }

    public boolean contactIsNotDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(name), 5);
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

    public BufferedImage getConversationEntryScreenshot(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoListEntryByName.apply(name));
        final WebElement el = getElement(locator,
                String.format("Conversation list entry '%s' is not visible", name));
        return this.getElementScreenshot(el).orElseThrow(IllegalStateException::new);
        // ImageIO.write(scr, "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
    }

    public BufferedImage getScreenshotFirstContact() throws Exception {
        // This takes a screenshot of the area to the left of a contact where
        // ping and unread dot notifications are visible
        final By locator = By.xpath(xpathStrConvoListEntryByIdx.apply(1));
        final WebElement contact = getElement(locator, "No contacts are visible in the list");
        return getScreenshotByCoordinates(contact.getLocation().x,
                contact.getLocation().y + getElement(xpathContactListRoot).getLocation().y,
                contact.getSize().width / 4, contact.getSize().height * 2)
                .orElseThrow(IllegalStateException::new);
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
        final Optional<WebElement> cell =  getElementIfDisplayed(locator);
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

}
