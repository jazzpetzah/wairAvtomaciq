package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactListPage extends IOSPage {
    private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.70;
    private final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.80;
    private final int CONV_SWIPE_TIME = 500;

    public static final String nameSelfButton = "SelfButton";
    @FindBy(name = nameSelfButton)
    private WebElement selfUserButton;

    private static final String xpathContactListRoot = xpathMainWindow + "/UIACollectionView[1]";
    @FindBy(xpath = xpathContactListRoot)
    private WebElement contactListContainer;

    private static final String xpathNameContactListItems = xpathContactListRoot + "//UIACollectionCell";

    private static final Function<String, String> convoListEntryByName = name ->
            String.format("%s[ .//*[@value='%s'] ]", xpathNameContactListItems, name);
    private static final Function<Integer, String> convoListEntryByIdx = idx ->
            String.format("%s[%s]", xpathNameContactListItems, idx);

    private static final String nameProfileName = "ProfileSelfNameField";
    @FindBy(name = nameProfileName)
    private WebElement profileName;

    private static final String nameOpenStartUI = "START A CONVERSATION";
    @FindBy(name = nameOpenStartUI)
    private WebElement openStartUI;

    private static final String nameMuteButton = "ConvCellMuteButton";
    @FindBy(name = nameMuteButton)
    private List<WebElement> muteButtons;

    private static final String xpathFirstChatInChatListTextField =
            xpathContactListRoot + "/UIACollectionCell[1]/UIAStaticText[1]";
    @FindBy(xpath = xpathFirstChatInChatListTextField)
    private WebElement firstChatInChatListTextField;

    private static final String nameContactListLoadBar = "LoadBar";
    @FindBy(name = nameContactListLoadBar)
    private WebElement loadBar;

    private static final String nameMediaCellPlayButton = "mediaCellButton";
    @FindBy(name = nameMediaCellPlayButton)
    private WebElement playPauseButton;

    private static final String xpathPendingRequest = "//UIACollectionCell[contains(@name,' waiting')]/UIAStaticText[1]";
    @FindBy(xpath = xpathPendingRequest)
    private WebElement pendingRequest;

    private static final String nameTutorialView = "ZClientNotificationWindow";
    @FindBy(name = nameTutorialView)
    private WebElement tutorialView;

    private static final String nameMuteCallButton = "MuteVoiceButton";
    @FindBy(name = nameMuteCallButton)
    private WebElement muteCallButton;

    private static final String nameLeaveConversationButton = "LEAVE";
    @FindBy(name = nameLeaveConversationButton)
    private WebElement leaveActionMenuButton;

    public static final String nameCancelButton = "CANCEL";
    @FindBy(name = nameCancelButton)
    private WebElement cancelActionMenuButton;

    private static final String nameSendAnInviteButton = "INVITE MORE PEOPLE";
    @FindBy(name = nameSendAnInviteButton)
    private WebElement inviteMorePeopleButton;

    private static final String xpathContactListPlayPauseButton =
            "//UIACollectionCell[@name='%s']/UIAButton[@name='mediaCellButton']";
    private static final String xpathFirstInContactList =
            xpathMainWindow + "/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";
    private static final String xpathMyUserInContactList =
            xpathMainWindow+ "/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText[1]";

    private static final String xpathArchiveConversationButton =
            "//UIAButton[@name='ARCHIVE' and @visible='true']";

    private static final String classNameContactListNames = "UIACollectionCell";

    private static final String xpathSpecificContactListCell =
            xpathMainWindow + "/UIACollectionView[1]/UIACollectionCell[@name='%s']";

    private static final String xpathFormatActionMenuXButton =
            "//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAButton[@name='%s']";

    private static final String xpathFormatActionMenuConversationName =
            "//UIAStaticText[@name='ARCHIVE']/following-sibling::UIAStaticText[@name='%s']";


    public ContactListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isMyUserNameDisplayedFirstInContactList(String name) throws Exception {
        final By locator = By.xpath(convoListEntryByIdx.apply(1));
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
            return getDriver().findElement(locator).getText().equals(name);
        } else {
            return false;
        }
    }

    public PeoplePickerPage openSearch() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), openStartUI);
        openStartUI.click();
        return new PeoplePickerPage(getLazyDriver());
    }

    public boolean isPlayPauseButtonVisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By
                .xpath(String.format(
                        xpathContactListPlayPauseButton, contact)));
    }

    public void tapPlayPauseButton() {
        playPauseButton.click();
    }

    public void tapPlayPauseButtonNextTo(String name) throws Exception {
        WebElement element = this.getDriver().findElement(
                By.xpath(String.format(xpathContactListPlayPauseButton, name)));
        element.click();
    }

    public PersonalInfoPage tapOnMyName() throws Exception {
        selfUserButton.click();
        return new PersonalInfoPage(this.getLazyDriver());
    }

    public String getSelfButtonLabel() {
        return selfUserButton.getAttribute("label").toUpperCase();
    }

    public boolean isSelfButtonContainingFirstNameLetter(String name) {
        String sub = name.substring(0, 1).toUpperCase();
        return sub.equals(getSelfButtonLabel());
    }

    public void tapOnName(String name) throws Exception {
        findNameInContactList(name).orElseThrow(IllegalStateException::new).click();
    }

    public String getFirstDialogName() throws Exception {
        DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathFirstInContactList));
        WebElement contact = this.getDriver().findElement(
                By.xpath(xpathFirstInContactList));
        return contact.getText();
    }

    public String getDialogNameByIndex(int index) throws Exception {
        final By locator = By.xpath(convoListEntryByIdx.apply(index));
        assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator) :
            String.format("Conversation # %s is not visible", index);
        return this.getDriver().findElement(locator).getText();
    }

    private Optional<WebElement> findNameInContactList(String name) throws Exception {
        final By locator = By.xpath(convoListEntryByName.apply(name));
        if (DriverUtils.waitUntilLocatorAppears(getDriver(), locator)) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return Optional.of(getDriver().findElement(locator));
            } else {
                return Optional.of(((IOSElement) getDriver().findElementByXPath(xpathContactListRoot)).
                        scrollToExact(name));
            }
        }
        return Optional.empty();
    }

    public boolean isChatInContactList(String name) throws Exception {
        return findNameInContactList(name).isPresent();
    }

    public void swipeRightOnContact(String contact) throws Exception {
        DriverUtils.swipeRight(this.getDriver(),
                findNameInContactList(contact).orElseThrow(IllegalStateException::new), CONV_SWIPE_TIME, 90, 50);
    }

    public void swipeRightConversationToRevealActionButtons(String conversation)
            throws Exception {
        int count = 0;

        do {
            swipeRightOnContact(conversation);
            count++;
        } while ((count < 5) && !isCancelActionButtonVisible());
    }

    public String getFirstConversationName() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.xpath(xpathFirstChatInChatListTextField))) {
            return firstChatInChatListTextField.getText();
        } else
            return null;
    }

    public IOSPage tapOnGroupChat(String chatName) throws Exception {
        findNameInContactList(chatName).orElseThrow(IllegalStateException::new).click();
        return new GroupChatPage(this.getLazyDriver());
    }

    public boolean waitForContactListToLoad() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathMyUserInContactList));
    }

    public boolean isPendingRequestInContactList() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathPendingRequest), 5);
    }

    public boolean pendingRequestInContactListIsNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathPendingRequest));
    }

    public PendingRequestsPage clickPendingRequest() throws Exception {
        pendingRequest.click();
        return new PendingRequestsPage(this.getLazyDriver());
    }

    public boolean isDisplayedInContactList(String name) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.name(name), 5);
    }

    public boolean contactIsNotDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.name(name), 5);
    }

    public List<WebElement> GetVisibleContacts() throws Exception {
        return this.getDriver().findElements(
                By.className(classNameContactListNames));
    }

    @Override
    public void swipeDown(int time) throws Exception {
        Point coords = mainWindow.getLocation();
        Dimension elementSize = mainWindow.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + 150, coords.x + elementSize.width / 2,
                coords.y + elementSize.height - 150, time);
    }

    public boolean conversationWithUsersPresented(String name1, String name2,
                                                  String name3) throws Exception {
        String firstChat = getFirstConversationName();
        return firstChat.contains(name1)
                && firstChat.contains(name2) && firstChat.contains(name3);
    }

    public boolean isConversationSilenced(String conversation,
                                          boolean isSilenced) throws Exception {
        String deviceType = CommonUtils.getDeviceName(this.getClass());
        BufferedImage referenceImage = null;
        WebElement element = findNameInContactList(conversation).orElseThrow(IllegalStateException::new);
        BufferedImage silencedConversation = CommonUtils.getElementScreenshot(element,
                this.getDriver(), CommonUtils.getDeviceName(this.getClass()))
                .orElseThrow(IllegalStateException::new);
        switch (deviceType) {
            case "iPhone 6 Plus":
                referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath() +
                        (isSilenced ? "silenceiPhone6plus.png" : "verifyUnsilenceIphone6plus.png"));
                break;
            case "iPhone 6":
                referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath() +
                        (isSilenced ? "silenceiPhone6.png" : "verifyUnsilenceTestIphone6"));
                break;
            case "iPad Air":
                if (isSilenced) {
                    referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                            + "verifySilenceiPadAir_" + getOrientation().toString() + ".png");
                } else {
                    referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                            + "verifyUnsilenceTestiPadAir_" + getOrientation().toString() + ".png");
                }
                break;
        }

        double score = ImageUtil.getOverlapScore(silencedConversation,
                referenceImage, 0);
        return score > MIN_ACCEPTABLE_IMAGE_VALUE;
    }

    public boolean isConversationSilencedBefore(String conversation)
            throws Exception {
        String deviceType = CommonUtils.getDeviceName(this.getClass());
        BufferedImage referenceImage;
        WebElement element = findNameInContactList(conversation).orElseThrow(IllegalStateException::new);
        BufferedImage silencedConversation = CommonUtils.getElementScreenshot(element,
                this.getDriver(), CommonUtils.getDeviceName(this.getClass()))
                .orElseThrow(IllegalStateException::new);
        switch (deviceType) {
            case "iPhone 6 Plus":
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unsilenceTestiPhone6plus.png");
                break;
            case "iPhone 6":
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unsilenceTestiPhone6.png");
                break;
            case "iPad Air":
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unsilenceTestiPadAir.png");
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown device type '%s'",
                        deviceType));
        }
        double score = ImageUtil.getOverlapScore(silencedConversation,
                referenceImage, 0);
        return score > MIN_ACCEPTABLE_IMAGE_VALUE;
    }

    private boolean isCancelActionButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                cancelActionMenuButton);
    }

    public void clickArchiveConversationButton() throws Exception {
        WebElement archiveButton = this
                .getDriver()
                .findElement(
                        By.xpath(xpathArchiveConversationButton));
        DriverUtils.tapByCoordinates(getDriver(), archiveButton);
    }

    public BufferedImage getScreenshotFirstContact() throws Exception {
        // This takes a screenshot of the area to the left of a contact where
        // ping and unread dot notifications are visible
        final By locator = By.xpath(convoListEntryByIdx.apply(1));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                "No contacts are visible in the list";
        WebElement contact = getDriver().findElement(locator);
        return getScreenshotByCoordinates(contact.getLocation().x,
                contact.getLocation().y + contactListContainer.getLocation().y,
                contact.getSize().width / 4, contact.getSize().height * 2)
                .orElseThrow(IllegalStateException::new);
    }

    public boolean missedCallIndicatorIsVisible(String conversation)
            throws Exception {
        WebElement contact = findNameInContactList(conversation).orElseThrow(IllegalStateException::new);

        BufferedImage missedCallIndicator = getElementScreenshot(contact).orElseThrow(
                IllegalStateException::new).getSubimage(0, 0,
                2 * contact.getSize().height, 2 * contact.getSize().height);

        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                + "missedCallIndicator.png");

        double score = ImageUtil.getOverlapScore(referenceImage, missedCallIndicator,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_IMAGE_SCORE;
    }

    public boolean unreadMessageIndicatorIsVisible(int numberOfMessages,
                                                   String conversation) throws Exception {
        BufferedImage referenceImage = null;
        WebElement contact = findNameInContactList(conversation).orElseThrow(IllegalStateException::new);

        BufferedImage unreadMessageIndicator = getElementScreenshot(contact).orElseThrow(
                IllegalStateException::new).getSubimage(0, 0,
                2 * contact.getSize().height, 2 * contact.getSize().height);

        if (numberOfMessages == 0) {
            if (CommonUtils.getDeviceName(this.getClass()).equals("iPad Air")) {
                if (getOrientation() == ScreenOrientation.LANDSCAPE) {
                    referenceImage = ImageUtil.readImageFromFile(IOSPage
                            .getImagesPath()
                            + "unreadMessageIndicator0_iPad_landscape.png");
                } else {
                    referenceImage = ImageUtil.readImageFromFile(IOSPage
                            .getImagesPath()
                            + "unreadMessageIndicator0_iPad.png");
                }

            } else if (CommonUtils.getDeviceName(this.getClass()).equals(
                    "iPhone 6")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator0_iPhone6.png");
            } else if (CommonUtils.getDeviceName(this.getClass()).equals(
                    "iPhone 6 Plus")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator0_iPhone6Plus.png");
            }
        } else if (numberOfMessages == 1) {
            if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator1_iPhone6.png");
            } else if (CommonUtils.getDeviceName(this.getClass()).equals(
                    "iPhone 6 Plus")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator1_iPhone6Plus.png");
            } else {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unreadMessageIndicator1.png");
            }
        } else if (numberOfMessages > 1 && numberOfMessages < 10) {
            if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator5_iPhone6.png");
            } else if (CommonUtils.getDeviceName(this.getClass()).equals(
                    "iPhone 6 Plus")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator5_iPhone6Plus.png");
            } else {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unreadMessageIndicator5.png");
            }
        } else if (numberOfMessages >= 10) {
            if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator10_iPhone6.png");
            } else if (CommonUtils.getDeviceName(this.getClass()).equals(
                    "iPhone 6 Plus")) {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath()
                        + "unreadMessageIndicator10_iPhone6Plus.png");
            } else {
                referenceImage = ImageUtil.readImageFromFile(IOSPage
                        .getImagesPath() + "unreadMessageIndicator10.png");
            }
        }

        double score = ImageUtil.getOverlapScore(referenceImage,
                unreadMessageIndicator,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_IMAGE_VALUE;
    }

    public boolean isMuteCallButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                muteCallButton);
    }

    public void clickMuteCallButton() {
        muteCallButton.click();
    }

    public boolean isPauseButtonVisible() throws Exception {
        BufferedImage pauseMediaButtonIcon = getElementScreenshot(playPauseButton)
                .orElseThrow(IllegalStateException::new);
        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                + "pauseMediaButtonIcon.png");

        double score = ImageUtil.getOverlapScore(referenceImage, pauseMediaButtonIcon,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_IMAGE_SCORE;
    }

    public boolean isPlayButtonVisible() throws Exception {
        BufferedImage playMediaButtonIcon = getElementScreenshot(playPauseButton)
                .orElseThrow(IllegalStateException::new);
        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                + "playMediaButtonIcon.png");

        double score = ImageUtil.getOverlapScore(referenceImage, playMediaButtonIcon,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_IMAGE_SCORE;
    }

    public boolean isActionMenuVisibleForConversation(String conversation)
            throws Exception {
        String xpath = String
                .format(xpathFormatActionMenuConversationName, conversation.toUpperCase());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpath));
    }

    public boolean isButtonVisibleInActionMenu(String buttonTitle)
            throws Exception {
        String xpath = String.format(xpathFormatActionMenuXButton,
                buttonTitle.toUpperCase());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpath));
    }

    public void clickArchiveButtonInActionMenu() throws Exception {
        WebElement archiveButton = this
                .getDriver()
                .findElement(
                        By.xpath(xpathArchiveConversationButton));
        DriverUtils.tapByCoordinates(getDriver(), archiveButton);
    }

    public void clickLeaveButtonInActionMenu() {
        leaveActionMenuButton.click();
    }

    public void clickCancelButtonInActionMenu() {
        cancelActionMenuButton.click();
    }

    public String getSelectedConversationCellValue(String conversation)
            throws Exception {
        WebElement conversationCell = this
                .getDriver()
                .findElement(
                        By.xpath(String.format(xpathSpecificContactListCell, conversation)));
        return conversationCell.getAttribute("value");
    }

    public boolean isInviteMorePeopleButtonVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                inviteMorePeopleButton);
    }

    public boolean isInviteMorePeopleButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.name(nameSendAnInviteButton));
    }

    public boolean waitUntilSelfButtonIsDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameSelfButton));
    }

}
